package com.shaun.knowledgetree.enums;

public enum CypherQueries {


    DELETE_ALL_NODES_AND_RELATIONSHIPS("MATCH (n) DETACH DELETE n"),
    MATCH_ALL_NODES_NOT_WITH_LABEL("MATCH (n) WHERE NOT n:%s RETURN n"),
    MATCH_ALL_CATEGORIES_WITH_ELEMENTS_IN_IT("MATCH (n:Category)<-[:IN_CATEGORY]-() RETURN n"),
    MATCH_AN_ENTITY_WITH_A_GIVEN_TITLE("Match (n:SingularWikiEntityDto) where n.title = '%s' return n"),
    FIND_ROOT_ENTITY("Match (n:SingularWikiEntityDto),(g:Graph) " +
            " where n.title = g.searchTerm " +
            " return n"),
    RETURN_PAGE_CONTENTS_THAT_ARE_IN_CATEGORIES("MATCH (n:Category)<-[:IN_CATEGORY]-(o:PageContentDto) RETURN o limit 5"),
    MATCH_ALL_ELEMENTS_WITH_SAME_NAME_THAT_SHOWED_UP_MORE_THAN_ONCE("start n=node(*) " +
            "where exists(n.name) " +
            "with n.name as name, collect(n) as nodelist, count(*) as count " +
            "where count > 1 " +
            "return name, nodelist, count"),
    MATCH_ALL_LINKS_WITH_SAME_LINK_TEXT_THAT_SHOWED_UP_MORE_THAN_ONCE("match (n:Link) " +
            "where exists(n.linkText) " +
            "with n.linkText as linkText, collect(n) as nodelist, count(*) as count " +
            "where count > 1 " +
            "return linkText, nodelist, count "),
    MATCH_ALL_CATEGORIES_WITH_SAME_NAME_THAT_SHOWED_UP_MORE_THAN_ONCE("match (n:Category) " +
            "where exists(n.name) " +
            "with n.name as name, collect(n) as nodelist, count(*) as count " +
            "where count > 1 " +
            "return name, nodelist, count "),
    MATCH_CATEGORIES_WITH_MORE_THAN_ONE_INCOMING_IN_CATEGORY_RELATIONSHIP("MATCH ()-[r:IN_CATEGORY]->(n)  " +
            "WITH n, count(r) as rel_cnt  " +
            "WHERE rel_cnt > 1  " +
            "RETURN n "),
    MATCH_ELEMENTS_THAT_HAVE_MORE_THAN_ONE_OF_A_CERTAIN_INCOMING_RELATIONSHIP("MATCH ()-[r:ROOT_ENTITY]->(n) " +
            "WITH n, count(r) as rel_cnt " +
            "WHERE rel_cnt > 1 " +
            "RETURN n "),
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
