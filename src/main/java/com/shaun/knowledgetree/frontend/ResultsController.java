package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.services.relevance.RelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 45923Sh(Shaun Lee) on 14/12/2016.
 */
@Controller
@RequestMapping("/results")
public class ResultsController {

    @Autowired
    RelevanceService relevanceService;

    public String getRelevanceToRoot(String toCompareTitle) {
        return "";
    }

    @RequestMapping("")
    public String openResultsPage() {
        return "results";
    }

    @RequestMapping("/relevance")
    public ModelAndView getRelevanceRankings(HttpServletRequest request) {

        request.getSession().setAttribute("relevanceRankings", relevanceService.rankEntitiesByRelevanceToRoot());
        return new ModelAndView("results");
    }

}
