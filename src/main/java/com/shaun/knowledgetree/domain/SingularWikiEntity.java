package com.shaun.knowledgetree.domain;

import info.bliki.api.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SingularWikiEntity  {

    private Page page;
    private String title;

    private Set<String> externalLinks;
    private int depthFromRoot;

    private SingularWikiEntity rootEntity;

    private SingularWikiEntity parent;

    private PageContent pageContent;

    private List<Relationship> relatedEntities;

    private boolean isRoot;

    public SingularWikiEntity() {
        this.pageContent = new PageContent();
        this.relatedEntities = new ArrayList<>();
    }

    public List<Relationship> getRelatedEntities() {
        return relatedEntities;
    }

    public void setRelatedEntities(List<Relationship> relatedEntities) {
        this.relatedEntities = relatedEntities;
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

    //External Links
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

    //isRoot
    public boolean isRoot() {
        return isRoot;
    }
    public void setIsRoot(boolean bool) {
        isRoot = bool;
    }
}
