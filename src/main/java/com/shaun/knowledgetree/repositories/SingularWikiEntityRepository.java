package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shaun on 08/06/2016.
 */
@Repository
public interface SingularWikiEntityRepository extends GraphRepository<SingularWikiEntity> {
}
