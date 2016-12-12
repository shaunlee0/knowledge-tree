package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.article.SingularWikiEntity;
import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.MovieService;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.entities.WikiEntitiesServicesImpl;
import com.shaun.knowledgetree.services.lookup.LookupServiceImpl;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.util.Common;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/results")
public class ResultsController {

    @Autowired
    MovieService movieService;

    @Autowired
    Neo4jServices neo4jServices;

    @Autowired
    WikiEntitiesServicesImpl wikiEntitiesServicesImpl;

    @Autowired
    LookupServiceImpl lookupServiceImpl;

    @Autowired
    SingularWikiEntityDtoBuilder singularWikiEntityDtoBuilder;

    @Autowired
    PageContentService pageContentService;

	@RequestMapping(value = "{rootNodeTitle}", method = RequestMethod.GET)
	public ModelAndView showResultsDetails(@PathVariable("rootNodeTitle")String rootNodeTitle, HttpServletRequest request){

        boolean result = true;
        HashMap<String,Object> model = new HashMap<>();

        try {

            neo4jServices.clearGraph();
            Common.setGraph(new Graph(rootNodeTitle));

            //Find root
            SingularWikiEntity rootEntity = lookupServiceImpl.findRoot(rootNodeTitle);
            rootEntity.setDepthFromRoot(0);

            SingularWikiEntityDto rootEntityDto = singularWikiEntityDtoBuilder.convertRoot(rootEntity);
            Common.setRootEntity(rootEntityDto);
            Common.getGraph().getEntities().add(rootEntityDto);

            //Our first layer is only a set of size 10
            Set<SingularWikiEntity> firstEntities = lookupServiceImpl.findEntities(rootEntity, rootEntity);

            //For each wiki entity hanging off the root(first entities) convert it and add it to the graph
            firstEntities.forEach(singularWikiEntity -> {
                Common.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
            });

            //Second layer is a set size 100, converting all these and adding to graph
            Set<SingularWikiEntity> allSecondLayerEntities = wikiEntitiesServicesImpl.aggregateAndReturnChildrenFromSetOfEntities(firstEntities, rootEntity);
            allSecondLayerEntities.forEach(singularWikiEntity -> {
                Common.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
            });

            Common.getGraph().getEntities().forEach(singularWikiEntityDto -> {
                if (singularWikiEntityDto.getParent() != null) {

                    //Establish parent to child relationship
                    List<Relationship> parentToChildRelationships = pageContentService.extractRelationshipContentFromPageContent(singularWikiEntityDto.getParent(), singularWikiEntityDto);
                    if (parentToChildRelationships.size() > 0) {
                        singularWikiEntityDto.getParent().getRelatedEntities().addAll(parentToChildRelationships);
                    }

                    //Establish child to parent relationship
                    List<Relationship> childToParentRelationships = pageContentService.extractRelationshipContentFromPageContent(singularWikiEntityDto, singularWikiEntityDto.getParent());
                    if (childToParentRelationships.size() > 0) {
                        singularWikiEntityDto.getRelatedEntities().addAll(childToParentRelationships);
                    }
                }

            });

            neo4jServices.saveGraph(Common.getGraph());
            neo4jServices.removeVerboseRelationships();
            model.put("graph",Common.getGraph());
            System.out.println("Graph saved.");
            Common.findLinksAndOccurences();
        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        if (!result){
            model.put("status","failure");
        }else{
            model.put("status","success");
        }

        return new ModelAndView("results",model);
	}
}
