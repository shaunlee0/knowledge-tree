package com.shaun.knowledgetree.services.neo4j;

import com.shaun.knowledgetree.domain.PageContentDto;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.repositories.GraphMapRepository;
import com.shaun.knowledgetree.repositories.PageContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphService {

	@Autowired
	GraphMapRepository graphMapRepository;

	@Autowired
	PageContentRepository pageContentRepository;

	public GraphService(){

	}

	public SingularWikiEntityDto getRootEntity(){
		SingularWikiEntityDto rootEntity = graphMapRepository.findRootEntity();
		PageContentDto pageContentDto = pageContentRepository.findPageContentByTitle(rootEntity.getTitle());
		rootEntity.setPageContentDto(pageContentDto);

		return rootEntity;
	}

}