package com.shaun.knowledgetree.neo4j;

/**
 * Created by Shaun on 04/06/2016.
 */
public enum CypherQueries {


    DELETE_ALL_NODES_AND_RELATIONSHIPS("MATCH (n) DETACH DELETE n"),
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
