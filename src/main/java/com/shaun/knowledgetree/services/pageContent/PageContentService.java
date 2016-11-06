package com.shaun.knowledgetree.services.pageContent;

import com.shaun.knowledgetree.domain.Category;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.Common;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Shaun on 29/05/2016.
 */
@Component
public class PageContentService {

    /**
     * Method to extract the categories from the page content.
     * This is done by grepping the categories from the content,
     * checking these categories do not already exist within the global
     * categories, if they do retrieve that category and return it
     * if not add it then return it.
     *
     * @param pageText : Page text being parsed for categories.
     * @return : The set of categories to be returned and added to the page content
     */
    public Set<Category> extractCategories(String pageText) {
        Set<Category> toReturn = new HashSet<>();
        String[] categories = pageText.split("Category:");
        for (int i = 1; i < categories.length; i++) {
            categories[i] = categories[i].replace("]]\n[[", "");
            int indexOfEndOfCategory = categories[i].indexOf("|");
            if (indexOfEndOfCategory != -1) {
                categories[i] = categories[i].substring(0, indexOfEndOfCategory);
            }

            boolean categoryFoundInAllCategories = false;

            for (Category category : Common.getGraph().getAllCategories()) {
                if (category.getName().equals(categories[i])) {
                    toReturn.add(category);
                    categoryFoundInAllCategories = true;
                }
            }

            if (!categoryFoundInAllCategories) {
                Category newCategoryFound = new Category(categories[i]);
                Common.getGraph().getAllCategories().add(newCategoryFound);
                toReturn.add(newCategoryFound);
            }

        }
        return toReturn;
    }

    public Map<String, String> extractKeyValuePairs(String content) {
        Map<String, String> map = new HashMap<>();
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] keys = lines[i].split("= ");
            if (keys.length > 1) {

                keys[0] = keys[0].replace("|", "").trim();
                //Remove html tags
                keys[1] = Jsoup.parse(keys[1]).text();

//                //Remove confusing alternate term for brevity
//                int index = keys[1].indexOf("|");
//                if(index!=-1){
//                    keys[1] = keys[1].substring(0,index);
//                }

                //If either key or value is under 70 length we deem it valid and add it to map
                if (keys[0].length() < 70 || keys[1].length() < 70) {
                    map.put(keys[0], keys[1]);
                }
            }
        }

        return map;
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
        String sentenceContainingMatch = "";
        List<Relationship> toReturn = new ArrayList<>();

        int titleOccurrences = 0;

        //Count occurrences to work out how many relationships to create if more than one.
        while(tempContent.contains(titleToFind) || tempContent.contains(titleToFind.toLowerCase())){
            titleOccurrences ++;
            System.out.println(titleOccurrences + " : " + titleToFind);
            tempContent = tempContent.replaceFirst(titleToFind,"");
            tempContent = tempContent.replaceFirst(titleToFind.toLowerCase(),"");
        }

        if(titleOccurrences>1){
            System.out.println("Break");
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
            if(relationshipContent!=null){
                relationship.setContent(relationshipContent);
                toReturn.add(relationship);
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
