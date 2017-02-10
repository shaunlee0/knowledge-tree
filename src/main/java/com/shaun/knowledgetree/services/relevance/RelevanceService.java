package com.shaun.knowledgetree.services.relevance;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.StringUtilities;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RelevanceService {

	//This variable will hold all terms of each document in an array.
	private LinkedHashMap<String, String[]> termsDocsArray = new LinkedHashMap<>();
	private LinkedHashMap<String, Double> cosineSimilarityToRootRankings = new LinkedHashMap<>();
	private List<String> allTerms = new ArrayList<String>(); //to hold all terms
	private HashMap<String, double[]> tfidfDocsVector = new HashMap<>();
	HashMap<String, SingularWikiEntityDto> allEntities = SharedSearchStorage.getAllEntities();
	Set<String> stopWords = new HashSet<>();

	@Autowired
	StringUtilities stringUtilities;

	public RelevanceService() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("txt/stop-words.txt").getFile());

		String path = file.getPath();

		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			stream.forEach(stopWords::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseEntities() {

		int count = 1;
		for (SingularWikiEntityDto entity : allEntities.values()) {
			System.out.println(count + " of " + allEntities.size());
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
			count ++;
		}
	}

	public void tfIdfCalculator() {
		double tf;
		double idf;
		double tfidf;

		//For each article
		for (Map.Entry<String, String[]> docTermsArray : termsDocsArray.entrySet()) {
			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			//For each term get tf-idf
			for (String term : allTerms) {
				System.out.println(count + " of " + allTerms.size());
				tf = new Tfidf().tfCalculator(docTermsArray.getValue(), term);
				idf = new Tfidf().idfCalculator(termsDocsArray.values(), term);
				tfidf = tf * idf;
				tfidfvectors[count] = tfidf;
				count++;
			}
			tfidfDocsVector.put(docTermsArray.getKey(), tfidfvectors);  //storing document vectors;
		}
	}

	public LinkedHashMap<String, Double> getCosineSimilarity() {

		double[] rootNodeVector = tfidfDocsVector.get(SharedSearchStorage.getRootEntity().getTitle());

		//Loop through tfidfDocsVector Elements
		for (Map.Entry<String, double[]> currentEntity : tfidfDocsVector.entrySet()) {
			cosineSimilarityToRootRankings.put(currentEntity.getKey(),new CosineSimilarity().cosineSimilarity
							(
											rootNodeVector,
											currentEntity.getValue()
							)
			);
		}

		//Sort these rankings.
		//Sort all links and occurences by descending order
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

	public double calculateTf(String articleContent, String term) {
		int occurrencesInArticle = StringUtils.countMatches(articleContent, term);
		int totalWordsInArticle = articleContent.split(" ").length;

		if (occurrencesInArticle == 0) {
			occurrencesInArticle = StringUtils.countMatches(articleContent, term.toLowerCase());
		}

		//Calculate Term Frequency
		return (double) occurrencesInArticle / (double) totalWordsInArticle;
	}

	/**
	 * Calculated idf of term termToCheck
	 *
	 * @param allTerms    : all the terms of all the documents
	 * @param termToCheck
	 * @return idf(inverse document frequency) score
	 */
	public double idfCalculator(List<String[]> allTerms, String termToCheck) {
		double count = 0;
		for (String[] ss : allTerms) {
			for (String s : ss) {
				if (s.equalsIgnoreCase(termToCheck)) {
					count++;
					break;
				}
			}
		}
		return Math.log(allTerms.size() / count);
	}

	public double calculateTfidfWeightingForEntity(SingularWikiEntityDto singularWikiEntityDto) {
		double tfidf = 0;
		try {
			String title = singularWikiEntityDto.getTitle();
			String articleContent = singularWikiEntityDto.getPageContent().getPagePlainText();


			//Calculate Term Frequency
			double tf = calculateTf(articleContent, title);


			HashMap<String, Integer> linksAndOccurrences = SharedSearchStorage.getAllLinksAndOccurrences();

			//Calculate inverse document frequency
			int totalArticlesInDomain = allEntities.size();
			int articlesContainingTitle = linksAndOccurrences.get(title);

			double idf = 1 + (double) Math.log((double) totalArticlesInDomain / (double) articlesContainingTitle);

			tfidf = tf * idf;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tfidf;
	}
}
