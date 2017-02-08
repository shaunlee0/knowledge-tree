package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import com.shaun.knowledgetree.services.relevance.RelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @Autowired
    private RelevanceService relevanceService;

    @RequestMapping(value = "root", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SingularWikiEntityDto getRootEntity() {
        return graphService.getRootEntity();
    }

    public String getDataForVisualGraph() {
        return null;
    }


    @RequestMapping(value = "article/{title}", method = RequestMethod.GET)
    public ModelAndView getSingularWikiEntity(@PathVariable("title") String title) {
        HashMap<String, Object> model = new HashMap<>();
        SingularWikiEntityDto singularWikiEntityDto = graphService.getSingularWikiEntity(title);
        double tfidf = relevanceService.calculateTfidfWeightingForEntity(singularWikiEntityDto);
        model.put("tfidf",tfidf);
        model.put("article",singularWikiEntityDto);
        return new ModelAndView("article",model);
    }
}
