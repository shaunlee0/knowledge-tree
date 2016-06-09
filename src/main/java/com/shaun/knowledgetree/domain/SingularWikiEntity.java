package com.shaun.knowledgetree.domain;

import com.shaun.knowledgetree.util.WikiEntityUtil;
import info.bliki.api.Page;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shaun on 23/04/2016.
 */
@NodeEntity
public class SingularWikiEntity  {


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @GraphId
    private Long id;

    private Page page;
    private String title;
    private Map<String,Integer> links;
    private Set<String> externalLinks;
    private int depthFromRoot;

    @Relationship(type="ROOT_ENTITY",direction = Relationship.OUTGOING)
    private SingularWikiEntity rootEntity;

//    @Relationship(type = "PARENT_ENTITY")
    private SingularWikiEntity parent;

    private PageContent pageContent;

    public SingularWikiEntity() {
        this.pageContent = new PageContent();
    }

    public SingularWikiEntity(SingularWikiEntity rootEntity, Page page, String title, HashMap<String, Integer> links, Set<String> externalLinks) {
        this.page = page;
        this.title = title;
        this.links = links;
        this.externalLinks = externalLinks;

    }

    //Parent
    public SingularWikiEntity getParent() {
        return parent;
    }
    public void setParent(SingularWikiEntity parent) {
        this.parent = parent;
    }

    //Root
    public SingularWikiEntity getRootEntity() {
        return rootEntity;
    }
    public void setRootEntity(SingularWikiEntity rootEntity) {
        this.rootEntity = rootEntity;
    }
    public Integer getDepthFromRoot() {
        return depthFromRoot;
    }
    public void setDepthFromRoot(int depthFromRoot) {
        this.depthFromRoot = depthFromRoot;
    }

    //Content
    public PageContent getPageContent() {
        return pageContent;
    }
    public void setPageContent(PageContent pageContent) {
        this.pageContent = pageContent;
    }

    //Title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //Links
    public Map<String, Integer> getLinks() {
        return links;
    }
    public void setLinks(Map<String, Integer> links) {
        this.links = links;
    }
    public void sortLinksByScore()
    {
        this.links = WikiEntityUtil.sortByComparator(links);
    }


    public Set<String> getExternalLinks() {
        return externalLinks;
    }
    public void setExternalLinks(Set<String> exterenalLinks) {
        this.externalLinks = exterenalLinks;
    }

    //Page
    public Page getPage() {
        return page;
    }
    public void setPage(Page page) {
        this.page = page;
    }

}
