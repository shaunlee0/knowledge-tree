package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import com.shaun.knowledgetree.services.relationships.RelationshipService;
import com.shaun.knowledgetree.services.lookup.LookupService;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.services.relevance.RelevanceService;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.shaun.knowledgetree.util.SharedSearchStorage.*;

@Controller
@RequestMapping("search")
@Api(value = "search", description = "Controller for search operations.")
public class SearchController {

    @Autowired
    Neo4jServices neo4jServices;

    @Autowired
    LookupService lookupService;

    @Autowired
    PageContentService pageContentService;

    @Autowired
    RelevanceService relevanceService;

    @Autowired
    GraphService graphService;

    @ApiOperation(value="validateSearch", notes = "Validate whether an article exists on Wikipedia")
    @RequestMapping(value = "validate", method = RequestMethod.GET, params = "searchTerm")
    @ResponseBody
    public String validateSearch(@RequestParam("searchTerm") String searchTerm) {
        boolean result = lookupService.checkArticleExists(searchTerm);
        if (result) {
            return "{\"status\":\"success\"}";
        } else {
            return "{\"status\":\"failure\"}";
        }
    }

    @RequestMapping(value = "{rootNodeTitle}", method = RequestMethod.GET)
    public ModelAndView performSearch(
            @PathVariable("rootNodeTitle") String rootNodeTitle,
            @RequestParam(value = "linkDepthLimit", defaultValue = "20") String linkDepthLimitString,
            @RequestParam(value = "maxGenerations", defaultValue = "1") String maxGenerationsString,
            HttpServletRequest request) {

        StopWatch requestTime = new StopWatch();
        requestTime.start("request");
        int linkDepthLimit = Integer.parseInt(linkDepthLimitString);
        int maxGenerations = 1;
        if(!maxGenerationsString.equals("undefined")){
            maxGenerations = Integer.parseInt(maxGenerationsString);
        }

        boolean result = true;
        HashMap<String, Object> model = new HashMap<>();
        Graph sessionGraph = (Graph) request.getSession().getAttribute("graph");

        if (sessionGraph != null) {
            if (rootNodeTitle.equals(sessionGraph.getSearchTerm())) {
                return new ModelAndView("results");
            }
        }

        removeSessionAttributes(request);
        result = lookupService.performSearch(maxGenerations,rootNodeTitle, linkDepthLimit);
        graphService.writeGraphDataToFile(getGraph(),request.getServletContext().getRealPath("resources/data"));
        request.getSession().setAttribute("graph", getGraph());
        request.getSession().setAttribute("allLinksAndOccurrences", getAllLinksAndOccurrences());

        if (!result) {
            return new ModelAndView("redirect:/error");
        } else {
            model.put("status", "success");
        }
        requestTime.stop();
        System.out.println(requestTime.prettyPrint());
        return new ModelAndView("results", model);

    }

    private void removeSessionAttributes(HttpServletRequest request){
        Enumeration<String> sessionAttributes = request.getSession().getAttributeNames();

        while(sessionAttributes.hasMoreElements()){
            request.getSession().removeAttribute(sessionAttributes.nextElement());
        }
    }
}
