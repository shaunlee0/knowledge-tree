package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/results/relationship")
public class RelationshipController {

    @Autowired
    GraphService graphService;

    @RequestMapping(value = "{startNodeTitle}", method = RequestMethod.GET)
    public ModelAndView getRelationshipDetails(@PathVariable("startNodeTitle") String startNodeTitle,
                                               @RequestParam("endNodeTitle") String endNodeTitle,
                                               HttpServletRequest request){

        HashMap<String, Object> model = new HashMap<>();

        Set<Relationship> foundRelationships = new HashSet<>();

        SingularWikiEntityDto startNode = graphService.getSingularWikiEntity(startNodeTitle);

        foundRelationships.addAll(
                startNode.getRelatedEntities()
                        .stream()
                        .filter(relationship -> relationship.getEndNode().getTitle().equals(endNodeTitle))
                        .collect(Collectors.toSet()));

        model.put("relationships",foundRelationships);

        return new ModelAndView("relationshipSet", (Map<String, ?>) foundRelationships);

        //TODO: Test this method.
    }
}
