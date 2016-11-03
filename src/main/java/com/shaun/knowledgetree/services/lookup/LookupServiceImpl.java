package com.shaun.knowledgetree.services.lookup;

import com.shaun.knowledgetree.article.SingularWikiEntity;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.services.relevance.RelevanceServiceImpl;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LookupServiceImpl implements LookupService {


    private final PageContentService pageContentService;
    private final RelevanceServiceImpl relevanceServiceImpl;
    private User user;

    public LookupServiceImpl() {
        this.user = new User("", "", "https://en.wikipedia.org/w/api.php");
        user.login();
        this.pageContentService = new PageContentService();
        this.relevanceServiceImpl = new RelevanceServiceImpl();
    }

    @Override
    public SingularWikiEntity findRoot(String searchPhrase) throws InterruptedException {
        SingularWikiEntity singularWikiEntity = new SingularWikiEntity();
        String[] listOfTitleStrings = {searchPhrase};

        List<Page> listOfPages = user.queryContent(listOfTitleStrings);
        listOfPages.parallelStream().forEach(page -> {
            WikiModel wikiModel = new WikiModel("${image}", "${title}");
            String html = null;
            html = wikiModel.render(page.toString());
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
        });
        return singularWikiEntity;
    }

    @Override
    public Set<SingularWikiEntity> findPages(List<String> titles, SingularWikiEntity rootEntity) throws InterruptedException {

        List<String> titlesSubSet;
        List<Page> listOfPages;

        Set<SingularWikiEntity> toReturn = new HashSet<>();
        if (titles.size() > 10) {
            titlesSubSet = titles.subList(0, 10);
            listOfPages = user.queryContent(titlesSubSet);
        } else {
            listOfPages = user.queryContent(titles);
        }
        for (Page page : listOfPages) {
            SingularWikiEntity singularWikiEntity = new SingularWikiEntity();
            singularWikiEntity.setRootEntity(rootEntity);
            WikiModel wikiModel = new WikiModel("${image}", "${title}");
            String html = wikiModel.render(page.toString());
            String wikiPageText = page.getCurrentContent();
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
            toReturn.add(singularWikiEntity);
        }
        return toReturn;
    }

    /**
     * Pass in parent and root, get links from parent add them to titles to search.
     * Carry out search on these titles, setting depth from root, aggregate and return found entities.
     *
     * @param parent     - Parent, we use this as starting point.
     * @param rootEntity - root to set root to new entities.
     * @return - Aggregated group of entities we find.
     */
    @Override
    public Set<SingularWikiEntity> findEntities(SingularWikiEntity parent, SingularWikiEntity rootEntity) throws InterruptedException {

        List<String> titles = new ArrayList<>();

        parent.getPageContent().getLinks().forEach(titles::add);

        //Find the set of entities from this object, then set its parent,root and depth
        Set<SingularWikiEntity> wikiEntitySet = findPages(titles,rootEntity);

        wikiEntitySet.forEach(wikiEntity -> {
            wikiEntity.setParent(parent);
            wikiEntity.setRootEntity(rootEntity);
            wikiEntity.setDepthFromRoot(wikiEntity.getParent().getDepthFromRoot() + 1);
        });

        return wikiEntitySet;
    }

    @Override
    public Set<String> extractExternalLinksFromHtml(String html) {

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
