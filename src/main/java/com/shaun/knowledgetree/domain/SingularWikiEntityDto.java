package com.shaun.knowledgetree.domain;

import com.shaun.knowledgetree.util.Common;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
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

    private String title;

    private Set<String> externalLinks;
    private int depthFromRoot;

    @Relationship(type = "ROOT_ENTITY", direction = Relationship.OUTGOING)
    private SingularWikiEntityDto rootEntity;

    //    @Relationship(type = "PARENT_ENTITY")
    private SingularWikiEntityDto parent;

    private PageContentDto pageContent;

    private List<com.shaun.knowledgetree.domain.Relationship> relatedEntities;

    private boolean isRoot;

    public SingularWikiEntityDto() {
        this.pageContent = new PageContentDto();
        this.relatedEntities = new ArrayList<>();
    }

    public SingularWikiEntityDto(SingularWikiEntityDto rootEntity, String title, Set<String> externalLinks) {
        this.title = title;
        this.externalLinks = externalLinks;
    }

    //Parent
    public SingularWikiEntityDto getParent() {
        return parent;
    }
    public void setParent(SingularWikiEntityDto parent) {

        //Only add parent if it is not root otherwise we get redundant nodes in graph
        if (!parent.getTitle().equals(Common.getRootEntity().getTitle())) {
            this.parent = parent;
        } else {
            this.parent = Common.getRootEntity();
        }
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

    //External Links
    public Set<String> getExternalLinks() {
        return externalLinks;
    }
    public void setExternalLinks(Set<String> exterenalLinks) {
        this.externalLinks = exterenalLinks;
    }

    //Related Entities
    public List<com.shaun.knowledgetree.domain.Relationship> getRelatedEntities() {
        return relatedEntities;
    }

    public void setRelatedEntities(List<com.shaun.knowledgetree.domain.Relationship> relatedEntities) {
        this.relatedEntities = relatedEntities;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean root) {
        isRoot = root;
    }

}