package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.Link;
import com.shaun.knowledgetree.domain.SingularWikiEntity;

import java.util.*;

/**
 * Created by Shaun on 29/05/2016.
 */
public class RelevanceServiceImpl implements RelevanceService {

    @Override
    public void calculateRelevanceOfEntity(SingularWikiEntity singularWikiEntity) {

    }


    public List<Link> assignScoreToEntity(SingularWikiEntity toAssignTo, SingularWikiEntity rootToCompareTo, Set<String> pageLinks) {
        List<Link> links = new ArrayList<>();
        String pageText = toAssignTo.getPageContent().getPageText();
        for (String str:pageLinks) {
            links.add(new Link(str, scoreForLink(toAssignTo, rootToCompareTo, str)));
        }
        toAssignTo.getPageContent().setLinks(links);

        return links;
    }

    /**
     * This gives a score to a string link, by comparing it with the current entity it was found in as well as our root
     * @param toAssignTo -
     * @param rootToCompareTo -
     * @param findStr -
     * @return - Score
     */
    private Integer scoreForLink(SingularWikiEntity toAssignTo,SingularWikiEntity rootToCompareTo, String findStr) {

        int result = 0;
        //how many sentences in?

        //Ocurrances
        int occurences = findLinkOcurences(toAssignTo.getPageContent().getPageText(),findStr);
//        int occurencesInRoot = findLinkOcurences(rootToCompareTo.getPageContent().getPageText(),findStr);

//        if(occurencesInRoot > 0){
//            result = occurences * occurencesInRoot + 1;
//        }else{
//            result = occurences;
//        }
        return 0;
    }

    private int findLinkOcurences(String searching,String toFind){
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1 && !toFind.isEmpty()){

            lastIndex = searching.indexOf(toFind,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += toFind.length();
            }
        }

        return count;
    }
}
