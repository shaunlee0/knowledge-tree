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
        this.explicitConnection = makeTitlesBold(explicitConnection);
    }

    public String getOnlyEndNodeConnection() {
        return onlyEndNodeConnection;
    }

    public void setOnlyEndNodeConnection(String onlyEndNodeConnection) {
        this.onlyEndNodeConnection = makeTitlesBold(onlyEndNodeConnection);
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

    public String makeTitlesBold(String connectingSentence) {

        String startNodeTitle = startNode.getTitle();
        String endNodeTitle = endNode.getTitle();

        if (connectingSentence != null) {

            //Remove bracket content in title (these never show up in article text)
            int indexOfBracket = startNodeTitle.indexOf("(");
            if (indexOfBracket != -1) {
               startNodeTitle = startNodeTitle.substring(0, indexOfBracket);
            }
            indexOfBracket = endNodeTitle.indexOf("(");
            if (indexOfBracket != -1) {
               endNodeTitle = endNodeTitle.substring(0, indexOfBracket);
            }

            String result = connectingSentence;

            //TODO : Maybe put this method in some static String utilities method?

            if (connectingSentence.contains(startNodeTitle)) {
                String upToStartNode = result.split(startNodeTitle)[0] + "<b>";
                String fromStartNodeToEnd = "</b>" + result.split(startNodeTitle)[1];
                result = upToStartNode + " " + startNodeTitle + fromStartNodeToEnd;
            }else if(connectingSentence.contains(startNodeTitle.toLowerCase())){
                String upToStartNode = result.split(startNodeTitle.toLowerCase())[0] + "<b>";
                String fromStartNodeToEnd = "</b>" + result.split(startNodeTitle.toLowerCase())[1];
                result = upToStartNode + " " + startNodeTitle + fromStartNodeToEnd;
            }

            if (connectingSentence.contains(endNodeTitle)) {
                String upToEndNode = result.split(endNodeTitle)[0] + "<b>";
                String fromEndNodeToEnd = "</b>" + result.split(endNodeTitle)[1];
                result = upToEndNode + " " + endNodeTitle + fromEndNodeToEnd;
            }else if(connectingSentence.contains(endNodeTitle.toLowerCase())){
                String upToEndNode = result.split(endNodeTitle.toLowerCase())[0] + "<b>";
                String fromEndNodeToEnd = "</b>" + result.split(endNodeTitle.toLowerCase())[1];
                result = upToEndNode + endNodeTitle + fromEndNodeToEnd;
            }
            return result;
        }
        return connectingSentence;
    }

}
