package com.shaun.knowledgetree.model;

import com.shaun.knowledgetree.model.SingularWikiEntity;
import com.shaun.knowledgetree.util.Common;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by Shaun Lee on 14/05/2016.
 * This class is designed to signify the relevance of a specific SingularWikiEntity
 */
public class Relevance {

    private float score;
    private SingularWikiEntity wikiBeingEvaluated;


    public Relevance(SingularWikiEntity wikiBeingEvaluated){
        this.score = 0.0f;
        this.wikiBeingEvaluated = wikiBeingEvaluated;
    }

    public void evaluateRelevance() {

    }



    public void factorDepthFromRootIntoScore(){
        int depthFromRoot = wikiBeingEvaluated.getDepthFromRoot();

        switch(depthFromRoot){
            case 1:
                //Do not decrement the current score.
                break;
            case 2:
                //Retain three quarters of the score
                score = score * 0.75f;
                break;
            case 3:
                //Retain one half of the score
                score = score * 0.5f;
        }
    }

    public SingularWikiEntity getWikiBeingEvaluated() {
        return wikiBeingEvaluated;
    }

    public void setWikiBeingEvaluated(SingularWikiEntity wikiBeingEvaluated) {
        this.wikiBeingEvaluated = wikiBeingEvaluated;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
