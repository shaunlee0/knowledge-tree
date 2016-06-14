package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.Graph;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shaun on 14/06/2016.
 */
@Repository
public interface GraphMapRepository extends GraphRepository<Graph> {
}
