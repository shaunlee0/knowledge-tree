package com.shaun.knowledgetree.services.pageContent;

import java.util.Map;
import java.util.Set;

/**
 * Created by Shaun on 29/05/2016.
 */
public interface PageContentService {

    Set<String> getCategories(String pageText);

    Map<String,String> getKeyValuePairs(String content);
}
