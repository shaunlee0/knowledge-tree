package com.shaun.knowledgetree.enums;

public enum CypherQueries {


    DELETE_ALL_NODES_AND_RELATIONSHIPS("MATCH (n) DETACH DELETE n"),
    MATCH_ALL_NODES_NOT_WITH_LABEL("MATCH (n) WHERE NOT n:%s RETURN n"),
    GET_ALL_NODES("MATCH (n) RETURN n");
    private String query;

    CypherQueries(String query) {
        this.query = query;
    }
    public String getQuery(){
        return query;
    }
    public String getQuery(String toReplace){
        return query.replace("%s",toReplace);
    }
}
