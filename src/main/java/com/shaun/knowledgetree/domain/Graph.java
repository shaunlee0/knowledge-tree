package com.shaun.knowledgetree.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shaun on 14/06/2016.
 */
@NodeEntity
public class Graph {

    @GraphId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String searchTerm;

    public Graph(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    private Set<Category> allCategories = new HashSet<>();

    private Set<SingularWikiEntityDto> entities = new HashSet<>();
    public Set<SingularWikiEntityDto> getEntities() {
        return entities;
    }

    public void setEntities(Set<SingularWikiEntityDto> entities) {
        this.entities = entities;
    }

    public Set<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(Set<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
