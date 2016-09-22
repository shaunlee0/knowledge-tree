package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Link;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;

import java.util.*;

public class Common {

    private static Graph graph;
    private static SingularWikiEntityDto rootEntity;
    public static HashMap<String, SingularWikiEntityDto> allEntities = new HashMap<>();

    public static SingularWikiEntityDto getRootEntity() {
        return rootEntity;
    }

    public static void setRootEntity(SingularWikiEntityDto rootEntity) {
        Common.rootEntity = rootEntity;
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void setGraph(Graph graph) {
        Common.graph = graph;
    }

    public static HashMap<String, SingularWikiEntityDto> getAllEntities() {
        return allEntities;
    }

    public static void setAllEntities(HashMap<String, SingularWikiEntityDto> allEntities) {
        Common.allEntities = allEntities;
    }

    private static HashMap<String, Integer> allLinksAndOccurences = new LinkedHashMap<>();

    public static void findLinksAndOccurences() {
        //for all entities
        allEntities.keySet().forEach(key -> {
            //for each one, get links
            List<Link> entityLinks = allEntities.get(key).getPageContent().getLinks();

            entityLinks.forEach(link ->
                    allLinksAndOccurences.put(link.getLinkText(), occurrenceOfLink(link.getLinkText())));
        });

//        Set<String> linksKeySet = allLinksAndOccurences.keySet();
//        for (String link : linksKeySet) {
//            int linkOccurrences = allLinksAndOccurences.get(link);
//            if (linkOccurrences > 1){
//                System.out.println(link + " has showed up " + linkOccurrences + " times");
//            }
//        }

        allLinksAndOccurences.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " = " + stringIntegerEntry.getValue()));

        System.out.println(allLinksAndOccurences);

    }

    private static Integer occurrenceOfLink(String linkText) {
        if (allLinksAndOccurences.keySet().contains(linkText)) {
            return allLinksAndOccurences.get(linkText) + 1;
        } else {
            return 1;
        }
    }
}
