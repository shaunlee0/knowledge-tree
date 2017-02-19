package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingularWikiEntityRepository extends GraphRepository<SingularWikiEntityDto> {

	SingularWikiEntityDto findSingularWikiEntityDtoByTitle(String title);

}
