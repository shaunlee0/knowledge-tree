package com.shaun.knowledgetree.article;

import com.shaun.knowledgetree.domain.Category;
import com.shaun.knowledgetree.domain.Event;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class to encapsulate all page content on a wikipedia article, including events, categories and html.
 */
public class PageContent {

    String pageWikiText;
    String summarySentance;
    String pagePlainText;
    private String title;
    String html;
    String lifeSpan; //  life_span = 754–1870
    Set<Event> events;// within Infobox : event2 = [[Treaty of Venice]] (Independence from the Holy Roman Empire)
    Set<String> seeAlsoSet;// ==See also== section
    Set<Category> categories; // [[Category:Religion and government]]
    Map<String, String> keyValuesPairs;
    Event startEvent;
    Event endEvent;

    public PageContent() {

    }

    //Page content
    public String getPageWikiText() {
        return pageWikiText;
    }

    public void setPageWikiText(String pageWikiText) {
        this.pageWikiText = pageWikiText;
    }

    public String getPagePlainText() {
        return pagePlainText;
    }

    public void setPagePlainText(String pagePlainText) {
        Pattern p = Pattern.compile("\\{\\{(.*?)}}");
        Matcher m = p.matcher(pagePlainText);
        while (m.find()) {
            String found = m.group();
            pagePlainText = pagePlainText.replace(found, "");
        }

        this.pagePlainText = pagePlainText;

        extractSummarySentence();
    }

    private void extractSummarySentence() {
        String pageContent = pagePlainText;
        pageContent = pageContent.replace("\n", "");
        int endOfSentance = pageContent.indexOf('.') + 1;

        this.summarySentance = pageContent.substring(0, endOfSentance);
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

    public Map<String, String> getKeyValuesPairs() {
        return keyValuesPairs;
    }

    public void setKeyValuesPairs(Map<String, String> keyValuesPairs) {
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
    private Set<String> links;

    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public String getSummarySentance() {
        return summarySentance;
    }

    public void setSummarySentance(String summarySentance) {
        this.summarySentance = summarySentance;
    }

    public void extractKeyValuePairsToContent() {
        events = new HashSet<>();
        lifeSpan = keyValuesPairs.get("life_span");
        if (lifeSpan != null) {
            int indexOfSpace = lifeSpan.indexOf(" ");
            if (indexOfSpace != -1) {
                lifeSpan = lifeSpan.substring(0, indexOfSpace);
            }
        }
        Set<String> possibleEvents = keyValuesPairs.keySet().stream().filter(entry -> entry.contains("event")).collect(Collectors.toSet());
        Map<Map<String, String>, Map<String, String>> mapOfEventsAndDates;
        if (possibleEvents != null) {
            Set<String> eventNames = possibleEvents.stream().filter(entry -> !entry.contains("_")).collect(Collectors.toSet());
            for (String event : eventNames) {
                Event eventObj = new Event(keyValuesPairs.get(event), keyValuesPairs.get("date_" + event));
                events.add(eventObj);
            }
            events.parallelStream().forEach(Event::checkIfTitleIsALink);
            startEvent = new Event(keyValuesPairs.get("event_start"), keyValuesPairs.get("year_start"));

            if (startEvent.hasTitle()) {
                startEvent.checkIfTitleIsALink();
            }
            endEvent = new Event(keyValuesPairs.get("event_end"), keyValuesPairs.get("year_end"));
            if (endEvent.hasTitle()) {
                endEvent.checkIfTitleIsALink();
            }
        }
    }

    public void extractSeeAlsoSet() {
        try {
            int indexOfStar;
            int indexOfEnd;
            String[] split = pageWikiText.split("==See also==");
            //if there is a see also section
            if (split.length > 1) {
                indexOfStar = split[1].indexOf("*");
                indexOfEnd = split[1].indexOf("==");
                if (indexOfStar > indexOfEnd) {
                    return;
                }
                if (indexOfStar > 0 && indexOfEnd > 0) {
                    String seeAlsoSection = split[1].substring(indexOfStar + 1, indexOfEnd).replace("*", "");
                    seeAlsoSet = new HashSet<String>(Arrays.asList(seeAlsoSection.split("\n")));
                    //only accept those that are link elements

                    seeAlsoSet = seeAlsoSet.stream()
                            .filter(seeAlso -> seeAlso.contains("[[") && seeAlso.length() < 70 && !seeAlso.contains("<!--"))
                            .collect(Collectors.toSet());

                    seeAlsoSet.forEach(str -> {
                                str.trim();
                                str.replace("''", "");
                            }
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
