package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.Stemmer;
import com.shaun.knowledgetree.util.StringUtilities;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;

import static com.shaun.knowledgetree.util.SharedSearchStorage.stopWatch;

@Component
public class RelevanceService {

    //This variable will hold all terms of each document in an array.
    private LinkedHashMap<String, String[]> termsDocsArray = new LinkedHashMap<>();
    private LinkedHashMap<String, Double> cosineSimilarityToRootRankings = new LinkedHashMap<>();
    private List<String> allTerms = new ArrayList<>(); //to hold all terms
    private HashMap<String, double[]> tfidfDocsVector = new HashMap<>();
    private HashMap<String, SingularWikiEntityDto> allEntities = SharedSearchStorage.getAllEntities();
    private StringUtilities stringUtilities = new StringUtilities();
    private Stemmer stemmer = new Stemmer();

    public RelevanceService() {

    }

    private void parseEntities() {

        StopWatch stopWatch = new StopWatch();
//        stopWatch.start("old");
//
//        //Slow old method.
//        for (SingularWikiEntityDto entity : allEntities.values()) {
//            String[] entityDocumentTerms = entity.getPageContent().getPagePlainText().split(" ");
//            Set<String> tempEntityDocumentTerms = new HashSet<>();
//
//            //Add non stop words to allTerms, remove non alpha and stop terms from entityDocumentTerms
//            for (String term : entityDocumentTerms) {
//                if (!allTerms.contains(term) && stringUtilities.wordIsNotStopWord(term)) {
//                    allTerms.add(term);
//                    tempEntityDocumentTerms.add(term);
//                }
//            }
//            entityDocumentTerms = tempEntityDocumentTerms.toArray(new String[tempEntityDocumentTerms.size()]);
//            termsDocsArray.put(entity.getTitle(), entityDocumentTerms);
//        }
//
//        stopWatch.stop();

        stopWatch.start("new");
        allEntities.values().stream().forEach(entity->{
            String[] entityDocumentTerms = entity.getPageContent().getPagePlainText().split(" ");
            Set<String> tempEntityDocumentTerms = new HashSet<>();

            //Add non stop words to allTerms, remove non alpha and stop terms from entityDocumentTerms
            for (String term : entityDocumentTerms) {
                term = stemmer.stem(term);
                if (stringUtilities.wordIsNotStopWord(term)) {
                    if(!allTerms.contains(term)){
                        allTerms.add(term);
                    }
                    tempEntityDocumentTerms.add(term);
                }
            }

            entityDocumentTerms = tempEntityDocumentTerms.toArray(new String[tempEntityDocumentTerms.size()]);
            termsDocsArray.put(entity.getTitle(), entityDocumentTerms);

        });

        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    private void tfIdfCalculator() {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("parallel stream");
        //One thread
        termsDocsArray.entrySet().parallelStream().forEach(entry -> {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            //For each term get tf-idf
            for (String term : allTerms) {
                double tf = new Tfidf().tfCalculator(entry.getValue(), term);
                double idf = new Tfidf().idfCalculator(termsDocsArray.values(), term);
                double tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.put(entry.getKey(), tfidfvectors);  //storing document vectors;
        });

        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    private LinkedHashMap<String, Double> getCosineSimilarity() {

        double[] rootNodeVector = tfidfDocsVector.get(SharedSearchStorage.getRootEntity().getTitle());

        tfidfDocsVector.entrySet().parallelStream().forEach(currentEntity -> {
            cosineSimilarityToRootRankings.put(currentEntity.getKey(), new CosineSimilarity().cosineSimilarity
                    (
                            rootNodeVector,
                            currentEntity.getValue()
                    )
            );
        });


        //Sort these rankings.
        cosineSimilarityToRootRankings = cosineSimilarityToRootRankings.entrySet().parallelStream()
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
        if(stopWatch.isRunning()){
            stopWatch.stop();
        }
        clearStorage();
        parseEntities();
        tfIdfCalculator();
        LinkedHashMap<String, Double> toReturn = getCosineSimilarity();
        return toReturn;
    }

    /**
     * When running multiple relevance processes the storage from the old search must be cleared.
     */
    private void clearStorage() {
        termsDocsArray.clear();
        cosineSimilarityToRootRankings.clear();
        allTerms.clear();
        tfidfDocsVector.clear();
    }

}
