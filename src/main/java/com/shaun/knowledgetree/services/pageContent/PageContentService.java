package com.shaun.knowledgetree.services.pageContent;

import com.shaun.knowledgetree.domain.Category;

import java.util.Map;
import java.util.Set;

/**
 * Created by Shaun on 29/05/2016.
 */
public interface PageContentService {

    Set<Category> extractCategories(String pageText);

    Map<String,String> extractKeyValuePairs(String content);
}
