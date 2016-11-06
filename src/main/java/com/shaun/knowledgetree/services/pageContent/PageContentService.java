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
     * Method to extract relationship content and saving to the relationship instance itself.
     *
     * @param relationship : Relationship in reference in which we are gathering information on.
     */
    public void extractRelationshipContentFromPageContent(Relationship relationship) {

        String startToEndRelationship = findRelationshipContentOfStartToEndNode(relationship.getStartNode(), relationship.getEndNode());
        if (startToEndRelationship!=null){
            relationship.setContent(startToEndRelationship);
        }

    }

    /**
     * Method to extract relationship content saving to the relationship
     *
     * @param startNode : The content we are searching through
     * @param endNode   :  Title we are searching for the terminal of the relationship
     * @return relationship content between start node and end node.
     */
    private String findRelationshipContentOfStartToEndNode(SingularWikiEntityDto startNode, SingularWikiEntityDto endNode) {
        String articleContent = startNode.getPageContent().getPagePlainText();
        String titleToFind = endNode.getTitle();
        String relationshipContent = null;
        String sentenceContainingMatch = "";

        //Certain pages are just redirects this will ignore such articles.

        int indexOfMatch = articleContent.indexOf(titleToFind);
        if(indexOfMatch==-1){
            indexOfMatch = articleContent.indexOf(titleToFind.toLowerCase());
        }

        //No match found.
        if (indexOfMatch==-1){
            return null;
        }

        int indexOfEndOfMatchedSentence = articleContent.indexOf(".", indexOfMatch);
        int indexOfStartOfMatchedSentence = -1;

        for (int i = indexOfMatch; i > 0; i++) {
            if (articleContent.charAt(i) == '.') {
                indexOfStartOfMatchedSentence = i + 1;
            }
        }
        if (indexOfStartOfMatchedSentence != -1) {
            sentenceContainingMatch = articleContent.substring(indexOfStartOfMatchedSentence, indexOfEndOfMatchedSentence);
        } else {
            System.out.println("Unable to find matching sentence stipulated by relationship");
            return null;
        }

        return extractSemanticContentOfRelationshipInSentence(sentenceContainingMatch, startNode.getTitle(), endNode.getTitle());
    }

    private String extractSemanticContentOfRelationshipInSentence(String sentenceContainingMatch, String startNodeTitle, String endNodeTitle) {

        return null;
    }
}
