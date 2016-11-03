package com.shaun.knowledgetree.domain;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class PageContentDto {

    String pageWikiText;
    String pagePlainText;



    private String title;
    String html;
    String lifeSpan; //  life_span = 754–1870

    @Relationship(type = "HAPPENED_IN", direction = "INCOMING")
    Set<Event> events;// within Infobox : event2 = [[Treaty of Venice]] (Independence from the Holy Roman Empire)
    Set<String> seeAlsoSet;// ==See also== section

    @Relationship(type = "IN_CATEGORY")
    Set<Category> categories; // [[Category:Religion and government]]

    Set<String> keyValuesPairs;
    Event startEvent;
    Event endEvent;

    @GraphId
    private Long id;

    private Set<String> links;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PageContentDto() {

    }

    //Page content
    public String getPageWikiText() {
        return pageWikiText;
    }

    public void setPageWikiText(String pageWikiText) {
        this.pageWikiText = pageWikiText;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getKeyValuesPairs() {
        return keyValuesPairs;
    }

    public void setKeyValuesPairs(Set<String> keyValuesPairs) {
        this.keyValuesPairs = keyValuesPairs;
    }

    //Timeline/Events
    public String getLifeSpan() {
        return lifeSpan;
    }
    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }
    public Event getStartEvent() {
        return startEvent;
    }
    public void setStartEvent(Event startEvent) {
        this.startEvent = startEvent;
    }
    public Event getEndEvent() {
        return endEvent;
    }
    public void setEndEvent(Event endEvent) {
        this.endEvent = endEvent;
    }
    public Set<Event> getEvents() {
        return events;
    }
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    //See also set
    public Set<String> getSeeAlsoSet() {
        return seeAlsoSet;
    }
    public void setSeeAlsoSet(Set<String> seeAlsoSet) {
        this.seeAlsoSet = seeAlsoSet;
    }

    //Categories
    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    //Links
    public Set<String> getLinks() {
        return links;
    }
    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public String getPagePlainText() {
        return pagePlainText;
    }

    public void setPagePlainText(String pagePlainText) {
        this.pagePlainText = pagePlainText;
    }

}
