package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.PageContentDto;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageContentRepository extends GraphRepository<PageContentRepository> {

	@Query("MATCH (n:PageContentDto) where n.title = {title} RETURN n")
	PageContentDto findPageContentByTitle(@Param("title") String titleToFind);
}

