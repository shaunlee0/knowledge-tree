package com.shaun.knowledgetree.domain;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RelationshipEntity(type = "RELATIONSHIP")
public class Relationship {
    @GraphId
    private Long relationshipId;

    @Property
    private String content;

    @StartNode
    private SingularWikiEntityDto startNode;

    @EndNode
    private SingularWikiEntityDto endNode;

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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
