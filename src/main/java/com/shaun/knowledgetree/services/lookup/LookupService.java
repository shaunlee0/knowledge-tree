package com.shaun.knowledgetree.services.lookup;

import com.shaun.knowledgetree.article.SingularWikiEntity;

import java.util.List;
import java.util.Set;

/**
 * Created by Shaun on 14/05/2016.
 */
public interface LookupService {

    SingularWikiEntity findRoot(String title) throws InterruptedException;

    Set<SingularWikiEntity> findPages(List<String> titles, SingularWikiEntity rootEntity) throws InterruptedException;

    Set<SingularWikiEntity> findEntities(SingularWikiEntity parent, SingularWikiEntity rootEntity) throws InterruptedException;

    Set<String> extractExternalLinksFromHtml(String html);

}
