package com.shaun.knowledgetree.services.relationships;

import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Created by shaun on 17/01/2017.
 */
@Component
public class RelationshipService {

    @Autowired
    GraphService graphService;

    public RelationshipService() {

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
        String articleContent = startNode.getPageContent().getPagePlainText();
        String tempContent = startNode.getPageContent().getPagePlainText();
        String titleToFind = endNode.getTitle();

        //Remove bracket content in title (these never show up in article text)
        int indexOfBracket = titleToFind.indexOf("(");
        if (indexOfBracket!=-1){
            titleToFind = titleToFind.substring(0,indexOfBracket);
        }
        String sentenceContainingMatch = "";
        List<Relationship> toReturn = new ArrayList<>();

        int titleOccurrences = 0;

        //Count occurrences to work out how many relationships to create if more than one.
        while (containsIgnoreCase(tempContent, titleToFind)) {
            titleOccurrences++;
            System.out.println(titleOccurrences + " : " + titleToFind);
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

            //No match found.
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
            extractSemanticContentOfRelationshipInSentence(sentenceContainingMatch, startNode.getTitle(), endNode.getTitle(), relationship);
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
     * @param endNodeTitle            :
     * @param relationship            : to configure.
     */
    private void extractSemanticContentOfRelationshipInSentence(String sentenceContainingMatch, String startNodeTitle, String endNodeTitle, Relationship relationship) {

        if(startNodeTitle.contains("Tempor")){
            System.out.println("break");
        }
        int indexOfBracket = startNodeTitle.indexOf("(");
        if (indexOfBracket!=-1){
            startNodeTitle = startNodeTitle.substring(0,indexOfBracket);
        }

        //If sentence directly references the start node return it. No more details need adding just now.
        if (containsIgnoreCase(sentenceContainingMatch, startNodeTitle)) {
            relationship.setExplicitConnection(sentenceContainingMatch);
        } else {
            relationship.setOnlyEndNodeConnection(sentenceContainingMatch);
        }


    }
}
