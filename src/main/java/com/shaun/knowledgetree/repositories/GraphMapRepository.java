package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.Graph;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shaun on 14/06/2016.
 */
@Repository
public interface GraphMapRepository extends GraphRepository<Graph> {

    @Query("MATCH (n) DETACH DELETE n")
    void deleteAllNodesAndRelationShips();

    @Query("match ()-[r:PARENT]->() delete r")
    void removeParentRelationships();

    @Query("match ()-[r:ROOT_ENTITY]->() delete r")
    void removeRootEntityRelationships();

    @Query("match ()-[r:ENTITIES]->() delete r")
    void removeGraphEntitiesRelationships();

}
