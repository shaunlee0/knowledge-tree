package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.Relationship;
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

import javax.ws.rs.GET;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @Autowired
    private RelevanceService relevanceService;

    @RequestMapping(value = "root", method = RequestMethod.GET)
    public ModelAndView getRootEntity() {
        SingularWikiEntityDto root = graphService.getRootEntity();
        return getSingularWikiEntity(root.getTitle());
    }

    @RequestMapping(value = "visual", method = RequestMethod.GET)
    public String openGraphPage() {
        return "graph";
    }


    @RequestMapping(value = "article/{title}", method = RequestMethod.GET)
    public ModelAndView getSingularWikiEntity(@PathVariable("title") String title) {
        HashMap<String, Object> model = new HashMap<>();
        SingularWikiEntityDto singularWikiEntityDto = graphService.getSingularWikiEntity(title);
        Set<Relationship> relationshipSetNoDuplicates = new HashSet<>();
        for (Relationship relationshipInEntity : singularWikiEntityDto.getRelatedEntities()) {
            boolean exists = false;
            for (Relationship relationshipInSet : relationshipSetNoDuplicates) {
                if (relationshipInSet.getEndNode().getTitle().equals(relationshipInEntity.getEndNode().getTitle())){
                    exists = true;
                }
            }
            if (!exists){
                relationshipSetNoDuplicates.add(relationshipInEntity);
            }

        }
        model.put("article",singularWikiEntityDto);
        model.put("relationshipSet",relationshipSetNoDuplicates);
        return new ModelAndView("article",model);
    }
}
