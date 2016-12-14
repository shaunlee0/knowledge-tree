package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;

import java.util.*;
import java.util.stream.Collectors;

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

    private static HashMap<String, Integer> allLinksAndOccurrences = new LinkedHashMap<>();

    public static HashMap<String, Integer> getAllLinksAndOccurrences() {
        return allLinksAndOccurrences;
    }

    public static void setAllLinksAndOccurrences(HashMap<String, Integer> allLinksAndOccurrences) {
        Common.allLinksAndOccurrences = allLinksAndOccurrences;
    }

    public static void findLinksAndOccurences() {
        //for all entities
        allEntities.keySet().forEach(key -> {
            //for each one, get links
            Set<String> entityLinks = allEntities.get(key).getPageContent().getLinks();

            entityLinks.forEach(link ->
                    allLinksAndOccurrences.put(link, occurrenceOfLink(link)));
        });

//        Set<String> linksKeySet = allLinksAndOccurrences.keySet();
//        for (String link : linksKeySet) {
//            int linkOccurrences = allLinksAndOccurrences.get(link);
//            if (linkOccurrences > 1){
//                System.out.println(link + " has showed up " + linkOccurrences + " times");
//            }
//        }

        //Sort all links and occurences by descending order
        allLinksAndOccurrences = allLinksAndOccurrences.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

        System.out.println("Top ten occurring links over the search domain");
        int count = 1;
        for (Map.Entry<String, Integer> entry : allLinksAndOccurrences.entrySet()) {
            if (count > 10) {
                break;
            } else {
                System.out.println(count + ". " + entry.getKey() + " : " + entry.getValue());
            }
            count++;
        }

    }

    private static Integer occurrenceOfLink(String linkText) {
        if (allLinksAndOccurrences.keySet().contains(linkText)) {
            return allLinksAndOccurrences.get(linkText) + 1;
        } else {
            return 1;
        }
    }
}
