package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.services.relevance.RelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Results controller to carry out all actions post the initial search phase.
 */
@Controller
@RequestMapping("/results")
public class ResultsController {

	@Autowired
	RelevanceService relevanceService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String openResultsPage() {
		return "results";
	}

	@RequestMapping(value = "/relevance", method = RequestMethod.GET)
	public ModelAndView getRelevanceRankings(HttpServletRequest request) {
		if (request.getSession().getAttribute("relevanceRankings") == null) {
			request.getSession().setAttribute("relevanceRankings", relevanceService.rankEntitiesByRelevanceToRoot());
			return new ModelAndView("relevance");
		} else {
			return new ModelAndView("relevance");
		}
	}

}
