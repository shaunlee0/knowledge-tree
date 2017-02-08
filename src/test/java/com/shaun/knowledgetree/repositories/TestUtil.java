package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.PageContentDto;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {

	@Test
	public void utilTest(){
		int occurrencesInArticleTest = StringUtils.countMatches("foo high two ftthe foo, onec ethe foo","foo");
		MatcherAssert.assertThat(occurrencesInArticleTest,Matchers.is(3));
	}


}
