package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pdtyreus
 */
@Repository
public interface PersonRepository extends GraphRepository<Person> {
    
}
