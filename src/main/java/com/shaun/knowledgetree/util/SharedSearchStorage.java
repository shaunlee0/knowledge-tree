package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import org.neo4j.helpers.collection.Iterables;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class encapsulating various shared data and functions pertaining to a search.
 */
public class SharedSearchStorage {

    private static Graph graph;
    private static SingularWikiEntityDto rootEntity;
    public static HashMap<String, SingularWikiEntityDto> allEntities = new HashMap<>();

    public static SingularWikiEntityDto getRootEntity() {
        return rootEntity;
    }

    public static void setRootEntity(SingularWikiEntityDto rootEntity) {
        SharedSearchStorage.rootEntity = rootEntity;
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void setGraph(Graph graph) {
        SharedSearchStorage.graph = graph;
    }

    public static HashMap<String, SingularWikiEntityDto> getAllEntities() {
        return allEntities;
    }

    public static void setAllEntities(HashMap<String, SingularWikiEntityDto> allEntities) {
        SharedSearchStorage.allEntities = allEntities;
    }

    private static HashMap<String, Integer> allLinksAndOccurrences = new LinkedHashMap<>();

    public static HashMap<String, Integer> getAllLinksAndOccurrences() {
        return allLinksAndOccurrences;
    }

    public static void setAllLinksAndOccurrences(HashMap<String, Integer> allLinksAndOccurrences) {
        SharedSearchStorage.allLinksAndOccurrences = allLinksAndOccurrences;
    }

    public static void findLinksAndOccurrences() {
        //for all entities
        allEntities.keySet().forEach(key -> {
            //for each one, get links
            Set<String> entityLinks = allEntities.get(key).getPageContent().getLinks();

            entityLinks.forEach(link ->
                    allLinksAndOccurrences.put(link, occurrenceOfLink(link)));
        });

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
    }

    private static Integer occurrenceOfLink(String linkText) {
        if (allLinksAndOccurrences.keySet().contains(linkText)) {
            return allLinksAndOccurrences.get(linkText) + 1;
        } else {
            return 1;
        }
    }
}
