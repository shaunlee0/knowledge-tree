package com.shaun.knowledgetree.frontend;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.relationships.RelationshipService;
import com.shaun.knowledgetree.services.lookup.LookupService;
import com.shaun.knowledgetree.services.neo4j.MovieService;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.services.relevance.RelevanceService;
import com.shaun.knowledgetree.util.SharedSearchStorage;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("search")
public class SearchController {

	@Autowired
	MovieService movieService;

	@Autowired
	Neo4jServices neo4jServices;

	@Autowired
	LookupService lookupService;

	@Autowired
	SingularWikiEntityDtoBuilder singularWikiEntityDtoBuilder;

	@Autowired
	PageContentService pageContentService;

	@Autowired
	RelationshipService relationshipService;

    @Autowired
    RelevanceService relevanceService;

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
	public ModelAndView performSearch(@PathVariable("rootNodeTitle") String rootNodeTitle,
																		@RequestParam(value = "linkDepthLimit",defaultValue = "20") String linkDepthLimitString,
																		@RequestParam("maxGenerations") String maxGenerationsString,
																		HttpServletRequest request) {

		int linkDepthLimit = Integer.parseInt(linkDepthLimitString);
		int maxGenerations = Integer.parseInt(maxGenerationsString);

		boolean result = true;
		HashMap<String, Object> model = new HashMap<>();
		Graph sessionGraph = (Graph) request.getSession().getAttribute("graph");

		if (sessionGraph != null) {
			if (rootNodeTitle.equals(sessionGraph.getSearchTerm())) {
				return new ModelAndView("results");
			}
		}

		try {
			SharedSearchStorage sharedSearchStorage = new SharedSearchStorage();
			System.out.println("Searching for " + rootNodeTitle);
			request.getSession().removeAttribute("graph");
			neo4jServices.clearGraph();
			SharedSearchStorage.setGraph(new Graph(rootNodeTitle));

			//Find root
			SingularWikiEntity rootEntity = lookupService.findRoot(rootNodeTitle);
			rootEntity.setDepthFromRoot(0);

			SingularWikiEntityDto rootEntityDto = singularWikiEntityDtoBuilder.convertRoot(rootEntity);
			SharedSearchStorage.setRootEntity(rootEntityDto);
			SharedSearchStorage.getGraph().getEntities().add(rootEntityDto);

			//Our first layer is only a set of size 10
			Set<SingularWikiEntity> firstEntities = lookupService.findEntities(rootEntity, rootEntity, linkDepthLimit);

			//For each wiki entity hanging off the root(first relationships) convert it and add it to the graph
			firstEntities.forEach(singularWikiEntity -> {
				SharedSearchStorage.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
			});

			//If using two generations do the following
			if(maxGenerations == 2){

				//Second layer is a set size 100, converting all these and adding to graph
				Set<SingularWikiEntity> allSecondLayerEntities = lookupService.aggregateAndReturnChildrenFromSetOfEntities(firstEntities, rootEntity, linkDepthLimit);
				allSecondLayerEntities.forEach(singularWikiEntity -> {
					SharedSearchStorage.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
				});

			}

			SharedSearchStorage.getGraph().getEntities().forEach(singularWikiEntityDto -> {
				if (singularWikiEntityDto.getParent() != null) {

					//Establish parent to child relationship
					List<Relationship> parentToChildRelationships = relationshipService.extractRelationshipContentFromPageContent(singularWikiEntityDto.getParent(), singularWikiEntityDto);
					if (parentToChildRelationships.size() > 0) {
						singularWikiEntityDto.getParent().getRelatedEntities().addAll(parentToChildRelationships);
					}

					//Establish child to parent relationship
					List<Relationship> childToParentRelationships = relationshipService.extractRelationshipContentFromPageContent(singularWikiEntityDto, singularWikiEntityDto.getParent());
					if (childToParentRelationships.size() > 0) {
						singularWikiEntityDto.getRelatedEntities().addAll(childToParentRelationships);
					}
				}
			});


			SharedSearchStorage.findLinksAndOccurrences();
			neo4jServices.saveGraph(SharedSearchStorage.getGraph());
			neo4jServices.removeVerboseRelationships();
			System.out.println("Graph saved.");
            HashMap<String, Double> entitiesAndRelevance = new LinkedHashMap<>();

            for (SingularWikiEntityDto entity : SharedSearchStorage.getGraph().getEntities()) {
                double tfidf = relevanceService.calculateTfidfWeightingForEntity(entity);
                entitiesAndRelevance.put(entity.getTitle(), tfidf);
            }

            //Sort all links and occurences by descending order
            entitiesAndRelevance = entitiesAndRelevance.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (x, y) -> {
                                throw new AssertionError();
                            },
                            LinkedHashMap::new
                    ));


            request.getSession().setAttribute("relevanceRankings",entitiesAndRelevance);
			request.getSession().setAttribute("graph", SharedSearchStorage.getGraph());
			request.getSession().setAttribute("allLinksAndOccurrences", SharedSearchStorage.getAllLinksAndOccurrences());
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		if (!result) {
			model.put("status", "failure");
		} else {
			model.put("status", "success");
		}

		return new ModelAndView("results", model);

	}
}
