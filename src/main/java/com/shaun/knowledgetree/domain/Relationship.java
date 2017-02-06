package com.shaun.knowledgetree.domain;

import org.neo4j.ogm.annotation.*;

import java.util.Set;

@RelationshipEntity(type = "RELATIONSHIP")
public class Relationship {

    @GraphId
    private Long relationshipId;

    @Property
    private String explicitConnection;

    @Property
    private String onlyEndNodeConnection;

    @StartNode
    private SingularWikiEntityDto startNode;

    @EndNode
    private SingularWikiEntityDto endNode;

    private Set<String> synsetFromConnectingSentence;

    public Relationship(SingularWikiEntityDto startNode, SingularWikiEntityDto endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    //ID
    public Long getRelationshipId() {
        return relationshipId;
    }
    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    //Content
    public String getExplicitConnection() {
        return explicitConnection;
    }
    public void setExplicitConnection(String explicitConnection) {
        this.explicitConnection = explicitConnection;
    }
    public String getOnlyEndNodeConnection() {
        return onlyEndNodeConnection;
    }
    public void setOnlyEndNodeConnection(String onlyEndNodeConnection) {
        this.onlyEndNodeConnection = onlyEndNodeConnection;
    }
    public Set<String> getSynsetFromConnectingSentence() {
        return synsetFromConnectingSentence;
    }
    public void setSynsetFromConnectingSentence(Set<String> synsetFromConnectingSentence) {
        this.synsetFromConnectingSentence = synsetFromConnectingSentence;
    }

    //Nodes
    public SingularWikiEntityDto getStartNode() {
        return startNode;
    }
    public void setStartNode(SingularWikiEntityDto startNode) {
        this.startNode = startNode;
    }
    public SingularWikiEntityDto getEndNode() {
        return endNode;
    }
    public void setEndNode(SingularWikiEntityDto endNode) {
        this.endNode = endNode;
    }
}
