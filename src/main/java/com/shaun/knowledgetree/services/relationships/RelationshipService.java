package com.shaun.knowledgetree.services.relationships;

import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Created by shaun on 17/01/2017.
 */
@Component
public class RelationshipService {

    @Autowired
    GraphService graphService;

    Set<String> stopWords = new HashSet<>();

    public RelationshipService() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("txt/stop-words.txt").getFile());

        String path = file.getPath();

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to extract relationship(s) and content, returning the aggregated relationships.
     */
    public List<Relationship> extractRelationshipContentFromPageContent(SingularWikiEntityDto startNode, SingularWikiEntityDto endNode) {
        return findRelationshipContentOfStartToEndNode(startNode, endNode);
    }

    /**
     * Method to extract relationship content saving to the relationship
     *
     * @param startNode : The content we are searching through
     * @param endNode   :  Title we are searching for the terminal of the relationship
     * @return relationship(s) and content between start node and end node.
     */
    private List<Relationship> findRelationshipContentOfStartToEndNode(SingularWikiEntityDto startNode, SingularWikiEntityDto endNode) {
        List<Relationship> toReturn = new ArrayList<>();

        try {

            String articleContent = startNode.getPageContent().getPagePlainText();
            String tempContent = startNode.getPageContent().getPagePlainText();
            String titleToFind = endNode.getTitle();
            String endNodeArticle = endNode.getPageContent().getPagePlainText();
            String startNodeAcronym = null;

            //Remove bracket content in title (these never show up in article text)
            int indexOfBracket = titleToFind.indexOf("(");
            if (indexOfBracket != -1) {
                titleToFind = titleToFind.substring(0, indexOfBracket);
            }
            int indexOfOpeningBracketInStartNodeArticle = articleContent.indexOf("(");
            int indexOfClosingBracketInStartNodeArticle = articleContent.indexOf(")");
            int indexOfFirstFullStop = articleContent.indexOf(".");

            //Extract substitute acronym if possible.
            if (indexOfOpeningBracketInStartNodeArticle != -1 || indexOfClosingBracketInStartNodeArticle != -1) {
                if (indexOfOpeningBracketInStartNodeArticle < indexOfFirstFullStop) {
                    startNodeAcronym = articleContent.substring(indexOfOpeningBracketInStartNodeArticle + 1, indexOfClosingBracketInStartNodeArticle);
                    if (!StringUtils.isAllUpperCase(startNodeAcronym)) {
                        startNodeAcronym = null;
                    }
                }
            }


            toReturn.addAll(findRelationshipSentences(titleToFind, tempContent, articleContent, startNode, endNode, null));

            //Use acronym if applicable.
            if (startNodeAcronym != null) {
                toReturn.addAll(findRelationshipSentences(titleToFind, tempContent, articleContent, startNode, endNode, startNodeAcronym));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    private List<Relationship> findRelationshipSentences(String titleToFind, String tempContent, String articleContent,
                                                         SingularWikiEntityDto startNode, SingularWikiEntityDto endNode, String startNodeAcronym) {
        String sentenceContainingMatch = "";
        List<Relationship> toReturn = new ArrayList<>();

        int titleOccurrences = 0;

        //Count occurrences to work out how many relationships to create if more than one.
        while (containsIgnoreCase(tempContent, titleToFind)) {
            titleOccurrences++;
            tempContent = tempContent.replaceFirst(titleToFind, "");
            tempContent = tempContent.replaceFirst(titleToFind.toLowerCase(), "");
            if (titleOccurrences > 50) {
                break;
            }
        }

        if (titleOccurrences > 4) {
            //Too generic a term, relationship of little value get the first occurrence only
            titleOccurrences = 1;
        }

        int progressThroughArticle = 0;

        for (int i = 0; i < titleOccurrences; i++) {
            int indexOfMatch = articleContent.indexOf(titleToFind, progressThroughArticle);
            if (indexOfMatch == -1) {
                indexOfMatch = articleContent.indexOf(titleToFind.toLowerCase(), progressThroughArticle);
            }
            //No match found, so return.
            if (indexOfMatch == -1) {
                return toReturn;
            }

            //Find end of matching sentence.
            int indexOfEndOfMatchedSentence = articleContent.indexOf(".", indexOfMatch) + 1;
            progressThroughArticle = indexOfEndOfMatchedSentence;
            int indexOfStartOfMatchedSentence = -1;

            //Find start of the matching sentence.
            for (int j = indexOfMatch; j > 0; j--) {
                char current = articleContent.charAt(j);
                if (current == '.') {
                    indexOfStartOfMatchedSentence = j + 1;
                    break;
                }
            }
            try {
                if (((indexOfStartOfMatchedSentence > 0) && (indexOfEndOfMatchedSentence > 0)) && (indexOfStartOfMatchedSentence < indexOfEndOfMatchedSentence)) {
                    sentenceContainingMatch = articleContent.substring(indexOfStartOfMatchedSentence, indexOfEndOfMatchedSentence);
                } else {
                    System.out.println("Unable to find matching sentence stipulated by relationship");
                    return toReturn;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Relationship relationship = new Relationship(startNode, endNode);
            extractSemanticContentOfRelationshipInSentence(sentenceContainingMatch, startNode.getTitle(), relationship, startNodeAcronym);
            toReturn.add(relationship);
        }
        return toReturn;
    }

    /**
     * This method decides the relationship content. For now if both start && end node exist it is an explicit connection.
     * Otherwise it is just set as only end node connection.
     *
     * @param sentenceContainingMatch : sentence to analyse.
     * @param startNodeTitle          :
     * @param relationship            : to configure.
     * @param startNodeAcronym        : If an acronym can be used to make the connection use it.
     */
    private void extractSemanticContentOfRelationshipInSentence(String sentenceContainingMatch, String startNodeTitle, Relationship relationship, String startNodeAcronym) {

        int indexOfBracket = startNodeTitle.indexOf("(");
        if (indexOfBracket != -1) {
            startNodeTitle = startNodeTitle.substring(0, indexOfBracket);
        }

        //If sentence directly references the start node return it. No more details need adding just now.
        if (containsIgnoreCase(sentenceContainingMatch, startNodeTitle)) {
            relationship.setExplicitConnection(sentenceContainingMatch);
            //Otherwise check acronym.
        } else if (startNodeAcronym != null) {
            if (containsIgnoreCase(sentenceContainingMatch, startNodeAcronym)) {
                relationship.setExplicitConnection(sentenceContainingMatch);
            }
        } else {
            relationship.setOnlyEndNodeConnection(sentenceContainingMatch);
        }
    }

    public void extractSynsetsAndStoreToRelationship(Relationship relationship) {
        boolean explicitRelationship = relationship.getExplicitConnection() != null;
        boolean endNodeOnlyRelationship = relationship.getOnlyEndNodeConnection() != null;

        Set<String> relationshipKeyWords = new LinkedHashSet<>();
        Set<String> synsetOfSentence = new LinkedHashSet<>();

        if (explicitRelationship) {

            Set<String> sentence = new LinkedHashSet<String>(Arrays.asList(relationship.getExplicitConnection().split(" ")));
            sentence.remove("");

            addWordsIfValid(sentence, relationshipKeyWords);

        } else if (endNodeOnlyRelationship) {
            Set<String> sentence = new LinkedHashSet<String>(Arrays.asList(relationship.getOnlyEndNodeConnection().split(" ")));
            sentence.remove("");

            addWordsIfValid(sentence, relationshipKeyWords);

        }

        System.out.println(relationshipKeyWords);

        for (String word : relationshipKeyWords) {
            //Make curl request using relationship key words.
            String wordnetResponse = makeWordNetRequest(word);

            String regex = "(?<=;s=).*?(?=\"|&)";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(wordnetResponse);
            while (m.find()) {
                String foundStr = m.group();
                if (!foundStr.isEmpty() && !foundStr.equals(word)) {
                    synsetOfSentence.add(foundStr);
                }
            }
        }

        relationship.setSynsetFromConnectingSentence(synsetOfSentence);

    }

    private void addWordsIfValid(Set<String> sentence, Set<String> relationshipKeyWords) {
        for (String word : sentence) {
            if (!word.isEmpty()) {
                word = word.replace("\"", "");
                word = word.replace(".", "");
                if (wordIsValid(word)) {
                    relationshipKeyWords.add(word);
                }
            }
        }
    }

    private boolean wordIsValid(String word) {
        return (!stopWords.contains(word)
                && (!Character.isUpperCase(word.charAt(0)))
                && (!word.matches(".*\\d+.*"))
                && (!word.contains("/"))
                && (word.length() > 3));
    }

    private String makeWordNetRequest(String word) {

        String wordNetResponse = "";

        String requestUrl = "http://wordnetweb.princeton.edu/perl/webwn?s=%s&sub=Search+WordNet&o2=&o0=&o8=1&o1=&o7=&o5=&o9=&o6=&o3=&o4=&h=00000";

        requestUrl = String.format(requestUrl, word);

        try {
            wordNetResponse = getHTML(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordNetResponse;
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public List<Relationship> getRelationships(SingularWikiEntityDto startNode, String endNodeTitle) {

        List<Relationship> foundRelationships = new ArrayList<>();

        for (Relationship relationship : startNode.getRelatedEntities()) {
            if (relationship.getEndNode().getTitle().equals(endNodeTitle)){
                foundRelationships.add(relationship);
            }
        }

        foundRelationships.forEach(this::extractSynsetsAndStoreToRelationship);

        return foundRelationships;
    }
}
