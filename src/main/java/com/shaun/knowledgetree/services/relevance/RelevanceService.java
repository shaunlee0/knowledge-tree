package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.StringUtilities;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RelevanceService {

    //This variable will hold all terms of each document in an array.
    private LinkedHashMap<String, String[]> termsDocsArray = new LinkedHashMap<>();
    private LinkedHashMap<String, Double> cosineSimilarityToRootRankings = new LinkedHashMap<>();
    private List<String> allTerms = new ArrayList<>(); //to hold all terms
    private HashMap<String, double[]> tfidfDocsVector = new HashMap<>();
    private HashMap<String, SingularWikiEntityDto> allEntities = SharedSearchStorage.getAllEntities();
    private StringUtilities stringUtilities = new StringUtilities();

    public RelevanceService() {

    }

    private void parseEntities() {
        for (SingularWikiEntityDto entity : allEntities.values()) {
            String[] entityDocumentTerms = entity.getPageContent().getPagePlainText().split(" ");
            Set<String> tempEntityDocumentTerms = new HashSet<>();

            //Add non stop words to allTerms, remove non alpha and stop terms from entityDocumentTerms
            for (String term : entityDocumentTerms) {
                if (!allTerms.contains(term) && stringUtilities.wordIsNotStopWord(term)) {
                    allTerms.add(term);
                    tempEntityDocumentTerms.add(term);
                }
            }
            entityDocumentTerms = tempEntityDocumentTerms.toArray(new String[tempEntityDocumentTerms.size()]);
            termsDocsArray.put(entity.getTitle(), entityDocumentTerms);
        }
    }

    private void tfIdfCalculator() {
        double tf;
        double idf;
        double tfidf;

        //For each article
        for (Map.Entry<String, String[]> docTermsArray : termsDocsArray.entrySet()) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            //For each term get tf-idf
            for (String term : allTerms) {
                tf = new Tfidf().tfCalculator(docTermsArray.getValue(), term);
                idf = new Tfidf().idfCalculator(termsDocsArray.values(), term);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.put(docTermsArray.getKey(), tfidfvectors);  //storing document vectors;
        }
    }

    private LinkedHashMap<String, Double> getCosineSimilarity() {

        double[] rootNodeVector = tfidfDocsVector.get(SharedSearchStorage.getRootEntity().getTitle());

        //Loop through tfidfDocsVector Elements
        for (Map.Entry<String, double[]> currentEntity : tfidfDocsVector.entrySet()) {
            cosineSimilarityToRootRankings.put(currentEntity.getKey(), new CosineSimilarity().cosineSimilarity
                    (
                            rootNodeVector,
                            currentEntity.getValue()
                    )
            );
        }

        //Sort these rankings.
        cosineSimilarityToRootRankings = cosineSimilarityToRootRankings.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

        return cosineSimilarityToRootRankings;

    }

    public LinkedHashMap<String, Double> rankEntitiesByRelevanceToRoot() {
        parseEntities();
        tfIdfCalculator();
        return getCosineSimilarity();
    }
}
