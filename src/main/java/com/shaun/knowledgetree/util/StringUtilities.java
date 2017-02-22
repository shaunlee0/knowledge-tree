package com.shaun.knowledgetree.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.util.StemmerUtil;
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
    public final Set<Character> charVowels = new HashSet<>();
    public final Set<String> stringVowels = new HashSet<>();
    public Stemmer stemmer = new Stemmer();

    public StringUtilities() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("txt/stop-words.txt").getFile());

        String path = file.getPath();

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        charVowels.add('a');
        charVowels.add('e');
        charVowels.add('i');
        charVowels.add('o');
        charVowels.add('u');
        stringVowels.add("a");
        stringVowels.add("e");
        stringVowels.add("i");
        stringVowels.add("o");
        stringVowels.add("u");
    }

    public boolean wordIsNotStopWord(String word) {
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

    public String stemWord(String word) {

        return stemmer.stem(word);
    }

    private String applyStemmerStepTwo(String word) {

        return word;
    }

    private String applyStemmerStepOneB(String word) {

        word = ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(word,"ing",null);
        word = ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(word,"eed","ee");
        word = ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(word,"ed",null);
        word = ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(word,"at","ate");
        word = ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(word,"bl","ble");
        word = ifSuffixFoundReplaceWith(word,"iz","ize");


        return word;
    }

    private String ifSuffixFoundReplaceWithIfVowelFoundInRestOfWord(String word, String suffix, String replaceString){
        if (word.endsWith(suffix)) {
            String[] wordSplit = word.split(suffix);
            for (String vowel : stringVowels) {

                //If the word contains any of the vowels
                if (wordSplit[0].contains(vowel)) {
                    word = word.substring(0, word.length() - suffix.length());
                    if (replaceString != null){
                        word = word + replaceString;
                    }
                    break;
                }
            }
        }

        return word;
    }

    private String ifSuffixFoundReplaceWith(String input, String suffix, String replaceString) {
        if (input.endsWith(suffix)){
            input = input.substring(0,input.length() - suffix.length());
            input = input + replaceString;
        }

        return input;
    }

    private String applyStemmerStepOneA(String word) {

        word = ifSuffixFoundReplaceWith(word,"sses","ss");
        word = ifSuffixFoundReplaceWith(word,"ies","i");

        if (!word.endsWith("ss")) {
            if (word.endsWith("s")) {
                word = word.substring(0, word.length() - 1);
            }
        }

        return word;
    }

}
