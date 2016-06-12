package com.shaun.knowledgetree.domain;

import com.shaun.knowledgetree.util.WikiEntityUtil;
import info.bliki.api.Page;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Set;

/**
 * Neo4j compatible single wiki entity object, using a page content Dto object as page content.
 */
@NodeEntity
public class SingularWikiEntityDto {


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

    @Relationship(type = "LINKED_PAGES", direction = Relationship.INCOMING)
    private List<Link> links;

    private Set<String> externalLinks;
    private int depthFromRoot;

    @Relationship(type = "ROOT_ENTITY", direction = Relationship.OUTGOING)
    private SingularWikiEntityDto rootEntity;

    //    @Relationship(type = "PARENT_ENTITY")
    private SingularWikiEntityDto parent;

    private PageContentDto pageContent;

    public SingularWikiEntityDto() {
        this.pageContent = new PageContentDto();
    }

    public SingularWikiEntityDto(SingularWikiEntityDto rootEntity, Page page, String title, List<Link> links, Set<String> externalLinks) {
        this.page = page;
        this.title = title;
        this.links = links;
        this.externalLinks = externalLinks;
    }

    //Parent
    public SingularWikiEntityDto getParent() {
        return parent;
    }

    public void setParent(SingularWikiEntityDto parent) {
        this.parent = parent;
    }

    //Root
    public SingularWikiEntityDto getRootEntity() {
        return rootEntity;
    }

    public void setRootEntity(SingularWikiEntityDto rootEntity) {
        this.rootEntity = rootEntity;
    }

    public Integer getDepthFromRoot() {
        return depthFromRoot;
    }

    public void setDepthFromRoot(int depthFromRoot) {
        this.depthFromRoot = depthFromRoot;
    }

    //Content
    public PageContentDto getPageContent() {
        return pageContent;
    }

    public void setPageContentDto(PageContentDto pageContent) {
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
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void sortLinksByScore() {
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