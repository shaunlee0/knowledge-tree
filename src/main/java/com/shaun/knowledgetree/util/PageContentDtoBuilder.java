package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.PageContent;
import com.shaun.knowledgetree.domain.PageContentDto;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used to build neo4j compatible page content objects, replacing the hash-map used for key value pairs
 * with a simple set of strings.
 */
@Component
public class PageContentDtoBuilder {

    public PageContentDto convert(PageContent pageContent) {
        PageContentDto pageContentDto = new PageContentDto();
        pageContentDto.setTitle(pageContent.getTitle());
        pageContentDto.setHtml(pageContent.getHtml());
        pageContentDto.setEvents(pageContent.getEvents());
        pageContentDto.setCategories(pageContent.getCategories());
        pageContentDto.setEndEvent(pageContent.getEndEvent());
        pageContentDto.setStartEvent(pageContent.getStartEvent());
        pageContentDto.setLifeSpan(pageContent.getLifeSpan());
        pageContentDto.setSeeAlsoSet(pageContent.getSeeAlsoSet());
        pageContentDto.setPageWikiText(pageContent.getPageWikiText());
        pageContentDto.setPagePlainText(pageContent.getPagePlainText());
        pageContentDto.setSummarySentence(pageContent.getSummarySentance());
        pageContentDto.setLinks(pageContent.getLinks());
        pageContentDto.setKeyValuesPairs(convertKeyValuePairsToNeo4jFormat(pageContent.getKeyValuesPairs()));
        return pageContentDto;
    }

    private Set<String> convertKeyValuePairsToNeo4jFormat(Map<String, String> keyValuesPairs) {
        Set<String> keyValueStringPairs = new LinkedHashSet<>();

        keyValuesPairs.entrySet().stream().forEach(entry -> {
            keyValueStringPairs.add(entry.toString());
        });

        return keyValueStringPairs;
    }
}
