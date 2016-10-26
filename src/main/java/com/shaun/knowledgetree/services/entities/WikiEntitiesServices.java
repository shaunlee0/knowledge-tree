package com.shaun.knowledgetree.services.entities;

import com.shaun.knowledgetree.article.SingularWikiEntity;

import java.util.Set;

/**
 * Created by Shaun on 28/05/2016.
 */
public interface WikiEntitiesServices {

    /**
     * Returns the set of all child entities form every element in the set passed in.
     * If first entities contains 10 SingularWikiEntities we return the set of children for all ten combined.
     * @param firstEntities : input to find children for
     * @param rootEntity : used to set root entity.
     * @return : Set comprising of all entities related to the set passed in.
     */
    Set<SingularWikiEntity> aggregateAndReturnChildrenFromSetOfEntities(Set<SingularWikiEntity> firstEntities, SingularWikiEntity rootEntity);
}
