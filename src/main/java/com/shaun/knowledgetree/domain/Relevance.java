package com.shaun.knowledgetree.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by Shaun Lee on 14/05/2016.
 * This class is designed to signify the relevance of a specific SingularWikiEntity
 */
@NodeEntity
public class Relevance {

    private double score;

    @GraphId Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private SingularWikiEntity wikiBeingEvaluated;


    public Relevance(SingularWikiEntity wikiBeingEvaluated){
        this.score = 0.0;
        this.wikiBeingEvaluated = wikiBeingEvaluated;
    }



    public void factorDepthFromRootIntoScore(){
        int depthFromRoot = wikiBeingEvaluated.getDepthFromRoot();

        switch(depthFromRoot){
            case 1:
                //Do not decrement the current score.
                break;
            case 2:
                //Retain three quarters of the score
                score = score * 0.75;
                break;
            case 3:
                //Retain one half of the score
                score = score * 0.5;
        }
    }

    public SingularWikiEntity getWikiBeingEvaluated() {
        return wikiBeingEvaluated;
    }

    public void setWikiBeingEvaluated(SingularWikiEntity wikiBeingEvaluated) {
        this.wikiBeingEvaluated = wikiBeingEvaluated;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
