package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import com.shaun.knowledgetree.services.relationships.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/results/relationship")
public class RelationshipController {

    @Autowired
    GraphService graphService;

    @Autowired
    RelationshipService relationshipService;

    @RequestMapping(value = "{startNodeTitle}", method = RequestMethod.GET)
    public ModelAndView getRelationshipDetails(@PathVariable("startNodeTitle") String startNodeTitle,
                                               @RequestParam("endNodeTitle") String endNodeTitle){

        HashMap<String, Object> model = new HashMap<>();
        SingularWikiEntityDto startNode = graphService.getSingularWikiEntity(startNodeTitle);
        SingularWikiEntityDto endNode = graphService.getSingularWikiEntity(endNodeTitle);
        List<Relationship> foundRelationships = relationshipService.getRelationships(startNode,endNodeTitle);

        model.put("relationships",foundRelationships);
        model.put("startNode",startNode);
        model.put("endNode",endNode);
        return new ModelAndView("relationship", model);
    }
}
