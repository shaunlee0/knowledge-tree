package com.shaun.knowledgetree.services.relationships;

import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaun on 17/01/2017.
 */
@Component
public class RelationshipService {

    @Autowired
    GraphService graphService;

    @Autowired
    RelationshipService relationshipService;

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
        String sentenceContainingMatch = "";
        List<Relationship> toReturn = new ArrayList<>();

        int titleOccurrences = 0;

        //Count occurrences to work out how many relationships to create if more than one.
        while(tempContent.contains(titleToFind) || tempContent.contains(titleToFind.toLowerCase())){
            titleOccurrences ++;
            System.out.println(titleOccurrences + " : " + titleToFind);
            tempContent = tempContent.replaceFirst(titleToFind,"");
            tempContent = tempContent.replaceFirst(titleToFind.toLowerCase(),"");
            if(titleOccurrences > 50){
                break;
            }
        }

        if(titleOccurrences>4){
            //To generic a term, relationship of little value get the first occurrence only
            titleOccurrences = 1;
        }

        int progressThroughArticle = 0;

        for (int i = 0; i < titleOccurrences; i++) {

            int indexOfMatch = articleContent.indexOf(titleToFind,progressThroughArticle);
            if(indexOfMatch==-1){
                indexOfMatch = articleContent.indexOf(titleToFind.toLowerCase(),progressThroughArticle);
            }

            //No match found.
            if (indexOfMatch==-1){
                return toReturn;
            }

            int indexOfEndOfMatchedSentence = articleContent.indexOf(".", indexOfMatch) + 1;
            progressThroughArticle = indexOfEndOfMatchedSentence;
            int indexOfStartOfMatchedSentence = -1;

            for (int j = indexOfMatch; j > 0; j--) {
                char current = articleContent.charAt(j);
                if (current == '.') {
                    indexOfStartOfMatchedSentence = j + 1;
                    break;
                }
            }
            try{
                if (((indexOfStartOfMatchedSentence > 0) && (indexOfEndOfMatchedSentence > 0)) && (indexOfStartOfMatchedSentence < indexOfEndOfMatchedSentence)) {
                    sentenceContainingMatch = articleContent.substring(indexOfStartOfMatchedSentence, indexOfEndOfMatchedSentence);
                } else {
                    System.out.println("Unable to find matching sentence stipulated by relationship");
                    return toReturn;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            Relationship relationship = new Relationship(startNode,endNode);
            String relationshipContent = extractSemanticContentOfRelationshipInSentence(sentenceContainingMatch,startNode.getTitle(),endNode.getTitle());
            //TODO consider whether the relationship is added regardless of how good the content is.
            toReturn.add(relationship);
            if(relationshipContent!=null){
                relationship.setContent(relationshipContent);
            }
        }
        return toReturn;
    }

    /**
     * For now this is only returning relationships that contain the start node title and end node title, this feels more complete.
     * @param sentenceContainingMatch :
     * @param startNodeTitle :
     * @param endNodeTitle :
     * @return String relationship content.
     */
    private String extractSemanticContentOfRelationshipInSentence(String sentenceContainingMatch, String startNodeTitle, String endNodeTitle) {

        //If sentence directly references the start node return it. No more details need adding just now.
        if(sentenceContainingMatch.contains(startNodeTitle)){
            return sentenceContainingMatch;
        }else{
            return null;
        }
    }
}
