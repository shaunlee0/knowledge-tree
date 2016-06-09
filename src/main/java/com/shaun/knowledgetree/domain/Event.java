package com.shaun.knowledgetree.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by Shaun on 29/05/2016.
 */
@NodeEntity
public class Event {

    @GraphId
    private Long id;
    private String date;
    private String title;
    private boolean titleIsALink;

    public Event(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void checkIfTitleIsALink() {
       titleIsALink = title.contains("[[");
    }

    public void setTitleIsALink(boolean titleIsALink) {
        this.titleIsALink = titleIsALink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasTitle() {
        return title!=null;
    }
}
