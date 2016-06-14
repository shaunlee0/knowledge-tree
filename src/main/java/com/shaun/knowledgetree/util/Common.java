package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;

import java.util.HashMap;

/**
 * Created by shaun on 14/06/2016.
 */
public class Common {

    public static SingularWikiEntityDto rootEntity;
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
}
