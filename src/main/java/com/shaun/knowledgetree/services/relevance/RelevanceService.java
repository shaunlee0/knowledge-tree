package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.article.SingularWikiEntity;


public interface RelevanceService {
    void calculateRelevanceOfEntity(SingularWikiEntity singularWikiEntity);
}
