package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RelevanceService {

    public RelevanceService() {

    }

    public String calculateRelevanceOfEntityToRoot(SingularWikiEntity singularWikiEntity) {
        return "";
    }


    private int findLinkOcurences(String searching, String toFind) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1 && !toFind.isEmpty()) {

            lastIndex = searching.indexOf(toFind, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += toFind.length();
            }
        }

        return count;
    }

    public double calculateTfidfWeightingForEntity(SingularWikiEntityDto singularWikiEntityDto) {
        String title = singularWikiEntityDto.getTitle();
        String articleContent = singularWikiEntityDto.getPageContent().getPagePlainText();
        int occurrencesInArticle = StringUtils.countMatches(articleContent, title);
        int totalWordsInArticle = articleContent.split(" ").length;

        if (occurrencesInArticle == 0) {
            occurrencesInArticle = StringUtils.countMatches(articleContent, title.toLowerCase());
        }

        //Calculate Term Frequency
        double tf = (double) occurrencesInArticle / (double) totalWordsInArticle;

        HashMap<String, SingularWikiEntityDto> allEntities = SharedSearchStorage.getAllEntities();
        HashMap<String, Integer> linksAndOccurrences = SharedSearchStorage.getAllLinksAndOccurrences();

        //Calculate inverse document frequency
        int totalArticlesInDomain = allEntities.size();
        int articlesContainingTitle = linksAndOccurrences.get(title);

        double idf = 1 + Math.log(totalArticlesInDomain / articlesContainingTitle);

        return tf * idf;
    }
}
