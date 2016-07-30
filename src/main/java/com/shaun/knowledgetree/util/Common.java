package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.Link;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;

import java.util.HashMap;
import java.util.List;

public class Common {

    private static SingularWikiEntityDto rootEntity;
    public static HashMap<String, SingularWikiEntityDto> allEntities = new HashMap<>();

    public static SingularWikiEntityDto getRootEntity() {
        return rootEntity;
    }

    public static void setRootEntity(SingularWikiEntityDto rootEntity) {
        Common.rootEntity = rootEntity;
    }

    public static HashMap<String, SingularWikiEntityDto> getAllEntities() {
        return allEntities;
    }

    public static void setAllEntities(HashMap<String, SingularWikiEntityDto> allEntities) {
        Common.allEntities = allEntities;
    }

    private static HashMap<String, Integer> allLinksAndOccurences = new HashMap<>();

    public static void findLinksAndOccurences() {
        allEntities.keySet().stream().forEach(key -> {
            List<Link> entityLinks = allEntities.get(key).getPageContent().getLinks();
            entityLinks.forEach(link -> allLinksAndOccurences.put(link.getLinkText(), occurrenceOfLink(link.getLinkText())));
        });
    }

    private static Integer occurrenceOfLink(String linkText) {
        if (allLinksAndOccurences.keySet().contains(linkText)) {
            return allLinksAndOccurences.get(linkText) + 1;
        } else {
            return 1;
        }
    }
}
