package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.model.SingularWikiEntity;


public interface RelevanceService {
    void calculateRelevanceOfEntity(SingularWikiEntity singularWikiEntity);
}
