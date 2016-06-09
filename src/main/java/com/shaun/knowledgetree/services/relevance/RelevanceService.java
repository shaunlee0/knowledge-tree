package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.SingularWikiEntity;


public interface RelevanceService {
    void calculateRelevanceOfEntity(SingularWikiEntity singularWikiEntity);
}
