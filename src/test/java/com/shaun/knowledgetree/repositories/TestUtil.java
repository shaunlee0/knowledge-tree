package com.shaun.knowledgetree.repositories;

import com.shaun.knowledgetree.domain.PageContentDto;
import com.shaun.knowledgetree.util.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.Matchers.is;

@Component
public class TestUtil {

	StringUtilities stringUtilities = new StringUtilities();

	@Test
	public void utilTest(){
	}

	@Test
	public void porterStemmerTest(){
//		String output = stringUtilities.stemWord("caresses");
//		MatcherAssert.assertThat(output,is("caress"));
//
//		output = stringUtilities.stemWord("ponies");
//		MatcherAssert.assertThat(output,is("poni"));

		//step 1A tests
		MatcherAssert.assertThat(runStemTest("caresses","caress"),is(true));
		MatcherAssert.assertThat(runStemTest("ponies","poni"),is(true));
		MatcherAssert.assertThat(runStemTest("cats","cat"),is(true));

		//step 1B tests
		MatcherAssert.assertThat(runStemTest("walking","walk"),is(true));
		MatcherAssert.assertThat(runStemTest("sing","sing"),is(true));
		MatcherAssert.assertThat(runStemTest("plastered","plaster"),is(true));
		MatcherAssert.assertThat(runStemTest("bled","bled"),is(true));
		MatcherAssert.assertThat(runStemTest("agreed","agre"),is(true));
		MatcherAssert.assertThat(runStemTest("feed","feed"),is(true));
		MatcherAssert.assertThat(runStemTest("sized","size"),is(true));
		MatcherAssert.assertThat(runStemTest("falling","fall"),is(true));
		MatcherAssert.assertThat(runStemTest("roll","roll"),is(true));
		MatcherAssert.assertThat(runStemTest("communism","commun"),is(true));
		MatcherAssert.assertThat(runStemTest("activate","activ"),is(true));
		MatcherAssert.assertThat(runStemTest("dependent","depend"),is(true));
		MatcherAssert.assertThat(runStemTest("irritant","irrit"),is(true));


	}

	private boolean runStemTest(String input, String expectedOutput){
		String output = stringUtilities.stemWord(input);
		return output.equals(expectedOutput);
	}


}
