package com.shaun.knowledgetree.services.lookup;

import com.shaun.knowledgetree.model.SingularWikiEntity;
import com.shaun.knowledgetree.services.pageContent.PageContentServiceImpl;
import com.shaun.knowledgetree.services.relevance.RelevanceServiceImpl;
import info.bliki.api.Connector;
import info.bliki.api.Page;
import info.bliki.api.PageInfo;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LookupServiceImpl implements LookupService {


    private final PageContentServiceImpl pageContentServiceImpl;
    private final RelevanceServiceImpl relevanceServiceImpl;
    private User user;

    public LookupServiceImpl() {
        this.user = new User("", "", "https://en.wikipedia.org/w/api.php");
        user.login();
        this.pageContentServiceImpl = new PageContentServiceImpl();
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
            String content = page.getCurrentContent();
            String title = page.getTitle();
            singularWikiEntity.setTitle(title);
            singularWikiEntity.getPageContent().setTitle(title);
            singularWikiEntity.getPageContent().setPageText(content);
            singularWikiEntity.getPageContent().setCategories(pageContentServiceImpl.extractCategories(content));
            singularWikiEntity.getPageContent().setKeyValuesPairs(pageContentServiceImpl.extractKeyValuePairs(content));
            singularWikiEntity.getPageContent().extractKeyValuePairsToContent();
            singularWikiEntity.getPageContent().extractSeeAlsoSet();
            singularWikiEntity.getPageContent().setHtml(html);
            singularWikiEntity.setExternalLinks(extractExternalLinksFromHtml(html));
            singularWikiEntity.getPageContent().setLinks(relevanceServiceImpl.assignScoreToEntity(singularWikiEntity, singularWikiEntity, wikiModel.getLinks()));
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
            String html = null;
            html = wikiModel.render(page.toString());
            String content = page.getCurrentContent();
            String title = page.getTitle();
            singularWikiEntity.setTitle(title);
            singularWikiEntity.getPageContent().setTitle(title);
            singularWikiEntity.getPageContent().setPageText(content);
            singularWikiEntity.getPageContent().setHtml(html);
            singularWikiEntity.setExternalLinks(extractExternalLinksFromHtml(html));
            singularWikiEntity.getPageContent().setCategories(pageContentServiceImpl.extractCategories(content));
            singularWikiEntity.getPageContent().setKeyValuesPairs(pageContentServiceImpl.extractKeyValuePairs(content));
            singularWikiEntity.getPageContent().extractKeyValuePairsToContent();
            singularWikiEntity.getPageContent().extractSeeAlsoSet();
            singularWikiEntity.getPageContent().setLinks(relevanceServiceImpl.assignScoreToEntity(singularWikiEntity, rootEntity, wikiModel.getLinks()));
            toReturn.add(singularWikiEntity);
        }
        return toReturn;
    }

    @Override
    public Set<SingularWikiEntity> findEntities(SingularWikiEntity parent, SingularWikiEntity rootEntity) throws InterruptedException {

        List<String> titles = new ArrayList<>();

        //We sort the hash map by score before passing to find pages to get the top ten
        parent.getPageContent().sortLinksByScore();

        parent.getPageContent().getLinks().stream().forEach(link -> titles.add(link.getLinkText()));

        //Find the set of entities from this object, then set its parent,root and depth
        Set<SingularWikiEntity> wikiEntitySet = findPages(titles,rootEntity);
        wikiEntitySet.stream().forEach(wikiEntity ->{
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
