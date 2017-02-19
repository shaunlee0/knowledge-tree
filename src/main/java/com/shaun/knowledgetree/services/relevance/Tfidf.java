package com.shaun.knowledgetree.services.relevance;

import java.util.Collection;
import java.util.List;

public class Tfidf {

	/**
	 * Calculated the tf of term termToCheck
	 *
	 * @param totalterms  : Array of all the words under processing document
	 * @param termToCheck : term of which tf is to be calculated.
	 * @return tf(term frequency) of term termToCheck
	 */
	public double tfCalculator(String[] totalterms, String termToCheck) {
		double count = 0;  //to count the overall occurrence of the term termToCheck
		for (String s : totalterms) {
			if (s.equalsIgnoreCase(termToCheck)) {
				count++;
			}
		}
		return count / totalterms.length;
	}

	/**
	 * Calculated idf of term termToCheck
	 *
	 * @param allTerms    : all the terms of all the documents
	 * @param termToCheck :
	 * @return idf(inverse document frequency) score
	 */
	public double idfCalculator(Collection<String[]> allTerms, String termToCheck) {
		double count = 0;
		//For all document term arrays
		for (String[] ss : allTerms) {
			//For all strings in those document term arrays, find if they contain the term.
			for (String s : ss) {
				if (s.equalsIgnoreCase(termToCheck)) {
					count++;
					break;
				}
			}
		}

		//TODO : If count < 2 then just return a 0. If this zero is found remove this term from all terms
		// and do not carry out tf on it.
		return Math.log(allTerms.size() / count);
	}
}
