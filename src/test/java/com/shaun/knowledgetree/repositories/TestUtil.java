package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.PageContentDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {

	@Autowired
	PageContentRepository pageContentRepository;

	@Test
	public void utilTest(){
		PageContentDto pageContentDto = pageContentRepository.findPageContentByTitle("Papal States");
		System.out.println(pageContentDto);
	}
}
