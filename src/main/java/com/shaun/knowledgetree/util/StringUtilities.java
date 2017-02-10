package com.shaun.knowledgetree.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class StringUtilities {

	public final Set<String> stopWords = new HashSet<>();

	public StringUtilities(){

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("txt/stop-words.txt").getFile());

		String path = file.getPath();

		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			stream.forEach(stopWords::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean wordIsNotStopWord(String word){
		return !stopWords.contains(word) && !word.matches(".*\\d+.*")
						&& StringUtils.isAlpha(word);
	}

	public boolean wordIsValid(String word) {
		return (!stopWords.contains(word)
						&& (!Character.isUpperCase(word.charAt(0)))
						&& (!word.matches(".*\\d+.*"))
						&& (!word.contains("/"))
						&& (word.length() > 3)
						&& (StringUtils.isAlpha(word)));
	}

}
