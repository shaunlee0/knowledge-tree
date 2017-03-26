package com.shaun.knowledgetree.services.lookup;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.services.relationships.RelationshipService;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shaun.knowledgetree.util.SharedSearchStorage.*;
import static com.shaun.knowledgetree.util.SharedSearchStorage.getAllLinksAndOccurrences;
import static com.shaun.knowledgetree.util.SharedSearchStorage.getGraph;

@Component
public class LookupService {

    @Autowired
    private PageContentService pageContentService;

    @Autowired
    private Neo4jServices neo4jServices;

    @Autowired
    SingularWikiEntityDtoBuilder singularWikiEntityDtoBuilder;

    @Autowired
    RelationshipService relationshipService;

    @Autowired
    GraphService graphService;

    private User user;

    public LookupService() {
        this.user = new User("", "", "https://en.wikipedia.org/w/api.php");
        user.login();
    }

    /**
     * Method to encapsulate all actions to perform a search.
     * @param maxGenerations - Generations to get entities for.
     * @param rootNodeTitle - Search title.
     * @param linkDepthLimit - Depth of links to add for each entity.
     * @return result of the search.
     */
    public boolean performSearch(int maxGenerations, String rootNodeTitle, int linkDepthLimit){

        boolean result = true;

        try {
            SharedSearchStorage.clearContent();
            System.out.println("Searching for " + rootNodeTitle);
            neo4jServices.clearGraph();
            setGraph(new Graph(rootNodeTitle));

            //Find root
            SingularWikiEntity rootEntity = findRoot(rootNodeTitle);
            rootEntity.setDepthFromRoot(0);

            SingularWikiEntityDto rootEntityDto = singularWikiEntityDtoBuilder.convertRoot(rootEntity);
            setRootEntity(rootEntityDto);
            getGraph().getEntities().add(rootEntityDto);


            //Our first layer is only a set of size 10
            Set<SingularWikiEntity> firstEntities = findEntities(rootEntity, rootEntity, linkDepthLimit);

            //For each wiki entity hanging off the root(first relationships) convert it and add it to the graph
            firstEntities.forEach(singularWikiEntity -> {
                getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
            });

            if (maxGenerations == 2) {
                //Second layer is a set size 100, converting all these and adding to graph
                Set<SingularWikiEntity> allSecondLayerEntities = aggregateAndReturnChildrenFromSetOfEntities(firstEntities, rootEntity, linkDepthLimit);
                allSecondLayerEntities.parallelStream().forEach(singularWikiEntity -> {
                    getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
                });

            }

            getGraph().getEntities().parallelStream().forEach(singularWikiEntityDto -> {
                if (singularWikiEntityDto.getParent() != null) {

                    //Establish parent to child relationship
                    List<Relationship> parentToChildRelationships = relationshipService.extractRelationshipContentFromPageContent(singularWikiEntityDto.getParent(), singularWikiEntityDto);
                    if (parentToChildRelationships.size() > 0) {
                        singularWikiEntityDto.getParent().getRelatedEntities().addAll(parentToChildRelationships);
                    }

                    //Establish child to parent relationship
                    List<Relationship> childToParentRelationships = relationshipService.extractRelationshipContentFromPageContent(singularWikiEntityDto, singularWikiEntityDto.getParent());
                    if (childToParentRelationships.size() > 0) {
                        singularWikiEntityDto.getRelatedEntities().addAll(childToParentRelationships);
                    }
                }
            });

            findLinksAndOccurrences();
            neo4jServices.saveGraph(getGraph());
            neo4jServices.removeVerboseRelationships();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    private SingularWikiEntity findRoot(String searchPhrase) throws InterruptedException {
        SingularWikiEntity singularWikiEntity = new SingularWikiEntity();
        String[] listOfTitleStrings = {searchPhrase};

        List<Page> listOfPages = user.queryContent(listOfTitleStrings);
        String pageWikiText = listOfPages.get(0).getCurrentContent();
        if (pageWikiText.contains("#REDIRECT")) {
            pageWikiText = pageWikiText.replace("#REDIRECT", "");
            String correctedSpelling = pageWikiText.substring(pageWikiText.indexOf("[[") + 2, pageWikiText.indexOf("]]"));
            listOfTitleStrings[0] = correctedSpelling;
            listOfPages = user.queryContent(listOfTitleStrings);
        }
        listOfPages.parallelStream().forEach(page -> {
            WikiModel wikiModel = new WikiModel("${image}", "${title}");
            String html = null;
            html = WikiModel.toHtml(page.getCurrentContent()).replaceAll("\\{\\{.*?}}", "");
            String wikiPageText = page.getCurrentContent();
            String title = page.getTitle();
            String pagePlainText = wikiModel.render(new PlainTextConverter(), wikiPageText);
            singularWikiEntity.setTitle(title);
            singularWikiEntity.getPageContent().setTitle(title);
            singularWikiEntity.getPageContent().setPageWikiText(wikiPageText);
            singularWikiEntity.getPageContent().setPagePlainText(pagePlainText);
            singularWikiEntity.getPageContent().setCategories(pageContentService.extractCategories(wikiPageText));
            singularWikiEntity.getPageContent().setKeyValuesPairs(pageContentService.extractKeyValuePairs(wikiPageText));
            singularWikiEntity.getPageContent().extractKeyValuePairsToContent();
            singularWikiEntity.getPageContent().extractSeeAlsoSet();
            singularWikiEntity.getPageContent().setHtml(html);
            singularWikiEntity.setExternalLinks(extractExternalLinksFromHtml(html));
            singularWikiEntity.getPageContent().setLinks(wikiModel.getLinks());
            singularWikiEntity.setIsRoot(true);
        });
        return singularWikiEntity;
    }

    public boolean checkArticleExists(String articleTitle) {
        String[] listOfTitleStrings = {articleTitle};

        List<Page> listOfPages = user.queryContent(listOfTitleStrings);
        String pageWikiText = listOfPages.get(0).getCurrentContent();
        if (pageWikiText.contains("#REDIRECT")) {
            pageWikiText = pageWikiText.replace("#REDIRECT", "");
            String correctedSpelling = pageWikiText.substring(pageWikiText.indexOf("[[") + 2, pageWikiText.indexOf("]]"));
            listOfTitleStrings[0] = correctedSpelling;
            listOfPages = user.queryContent(listOfTitleStrings);
        }

        return listOfPages.get(0).getPageid() != null;
    }

    private Set<SingularWikiEntity> findPages(List<String> titles, SingularWikiEntity rootEntity,
                                             int depthLimit) throws InterruptedException {

        List<String> titlesSubSet;
        List<Page> listOfPages;

        Set<SingularWikiEntity> toReturn = new HashSet<>();
        if (titles.size() > depthLimit) {
            titlesSubSet = titles.subList(0, depthLimit);
            listOfPages = user.queryContent(titlesSubSet);
        } else {
            listOfPages = user.queryContent(titles);
        }


        for (Page page : listOfPages) {
            SingularWikiEntity singularWikiEntity = new SingularWikiEntity();
            singularWikiEntity.setRootEntity(rootEntity);
            WikiModel wikiModel = new WikiModel("${image}", "${title}");
            String html = WikiModel.toHtml(page.getCurrentContent()).replaceAll("\\{\\{.*?}}", "");
            String wikiPageText = page.getCurrentContent();
            if (wikiPageText.contains("#REDIRECT")) {
                wikiPageText = wikiPageText.replace("#REDIRECT", "");
                String correctedSpelling = wikiPageText.substring(wikiPageText.indexOf("[[") + 2, wikiPageText.indexOf("]]"));
                singularWikiEntity = getSingularWikiEntityForSearchTerm(correctedSpelling);
            } else {
                String pagePlainText = wikiModel.render(new PlainTextConverter(), wikiPageText);
                String title = page.getTitle();
                singularWikiEntity.setTitle(title);
                singularWikiEntity.getPageContent().setTitle(title);
                singularWikiEntity.getPageContent().setPageWikiText(wikiPageText);
                singularWikiEntity.getPageContent().setPagePlainText(pagePlainText);
                singularWikiEntity.getPageContent().setHtml(html);
                singularWikiEntity.setExternalLinks(extractExternalLinksFromHtml(html));
                singularWikiEntity.getPageContent().setCategories(pageContentService.extractCategories(wikiPageText));
                singularWikiEntity.getPageContent().setKeyValuesPairs(pageContentService.extractKeyValuePairs(wikiPageText));
                singularWikiEntity.getPageContent().extractKeyValuePairsToContent();
                singularWikiEntity.getPageContent().extractSeeAlsoSet();
                singularWikiEntity.getPageContent().setLinks(wikiModel.getLinks());
                singularWikiEntity.setIsRoot(false);
            }
            toReturn.add(singularWikiEntity);
        }
        return toReturn;
    }

    private SingularWikiEntity getSingularWikiEntityForSearchTerm(String correctedSpelling) {
        SingularWikiEntity singularWikiEntity = new SingularWikiEntity();
        String[] listOfTitleStrings = {correctedSpelling};

        List<Page> listOfPages = user.queryContent(listOfTitleStrings);
        Page page = listOfPages.get(0);
        WikiModel wikiModel = new WikiModel("${image}", "${title}");
        String html = WikiModel.toHtml(page.getCurrentContent()).replaceAll("\\{\\{.*?}}", "");
        String wikiPageText = page.getCurrentContent();
        String title = page.getTitle();
        String pagePlainText = wikiModel.render(new PlainTextConverter(), wikiPageText);
        singularWikiEntity.setTitle(title);
        singularWikiEntity.getPageContent().setTitle(title);
        singularWikiEntity.getPageContent().setPageWikiText(wikiPageText);
        singularWikiEntity.getPageContent().setPagePlainText(pagePlainText);
        singularWikiEntity.getPageContent().setCategories(pageContentService.extractCategories(wikiPageText));
        singularWikiEntity.getPageContent().setKeyValuesPairs(pageContentService.extractKeyValuePairs(wikiPageText));
        singularWikiEntity.getPageContent().extractKeyValuePairsToContent();
        singularWikiEntity.getPageContent().extractSeeAlsoSet();
        singularWikiEntity.getPageContent().setHtml(html);
        singularWikiEntity.setExternalLinks(extractExternalLinksFromHtml(html));
        singularWikiEntity.getPageContent().setLinks(wikiModel.getLinks());
        singularWikiEntity.setIsRoot(true);

        return singularWikiEntity;

    }

    /**
     * Pass in parent and root, get links from parent add them to titles to search.
     * Carry out search on these titles, setting depth from root, aggregate and return found relationships.
     *
     * @param parent     - Parent, we use this as starting point.
     * @param rootEntity - root to set root to new relationships.
     * @return - Aggregated group of relationships we find.
     */
    private Set<SingularWikiEntity> findEntities(SingularWikiEntity parent, SingularWikiEntity rootEntity,
                                                int linkDepthLimit) throws InterruptedException {

        List<String> titles = new ArrayList<>();

        titles.addAll(parent.getPageContent().getLinks());

        //Find the set of relationships from this object, then set its parent,root and depth
        Set<SingularWikiEntity> wikiEntitySet = findPages(titles, rootEntity, linkDepthLimit);

        wikiEntitySet.parallelStream().forEach(wikiEntity -> {
            wikiEntity.setParent(parent);
            wikiEntity.setRootEntity(rootEntity);
            wikiEntity.setDepthFromRoot(wikiEntity.getParent().getDepthFromRoot() + 1);
        });

        return wikiEntitySet;
    }

    /**
     * For each entity in the set passed in we aggregate all the children we find and then return them.
     *
     * @param inputEntities : Set of relationships to find children for
     * @param rootEntity    : used to set root entity.
     * @return : Set of child relationships combined for every element in the input set.
     */
    private Set<SingularWikiEntity> aggregateAndReturnChildrenFromSetOfEntities(Set<SingularWikiEntity> inputEntities, SingularWikiEntity rootEntity, int linkDepthLimit) {
        Set<SingularWikiEntity> toReturn = new HashSet<>();

            inputEntities.parallelStream().forEach(firstLayerEntity ->{
                Set<SingularWikiEntity> childEntities = null;
                try {
                    childEntities = findEntities(firstLayerEntity, rootEntity, linkDepthLimit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toReturn.addAll(childEntities);
            });

        return toReturn;
    }

    private Set<String> extractExternalLinksFromHtml(String html) {

        Set<String> urls = new HashSet<>();
        Pattern p = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            urls.add(urlStr);
        }
        return urls;
    }
}
