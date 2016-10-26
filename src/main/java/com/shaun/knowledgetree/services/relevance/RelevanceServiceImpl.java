package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.article.SingularWikiEntity;

public class RelevanceServiceImpl implements RelevanceService {

    @Override
    public void calculateRelevanceOfEntity(SingularWikiEntity singularWikiEntity) {

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
