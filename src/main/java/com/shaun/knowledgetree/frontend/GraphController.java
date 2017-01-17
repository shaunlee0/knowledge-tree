package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.neo4j.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @RequestMapping(value = "root", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SingularWikiEntityDto getRootEntity() {
        return graphService.getRootEntity();
    }

    public String getDataForVisualGraph() {
        return null;
    }

    public SingularWikiEntityDto getSingularWikiEntity(String title) {
        return null;
    }
}
