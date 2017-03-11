package com.shaun.knowledgetree.services.neo4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.PageContentDto;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.domain.json.Edge;
import com.shaun.knowledgetree.domain.json.GraphData;
import com.shaun.knowledgetree.domain.json.Node;
import com.shaun.knowledgetree.repositories.GraphMapRepository;
import com.shaun.knowledgetree.repositories.PageContentRepository;
import com.shaun.knowledgetree.repositories.SingularWikiEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class GraphService {

	@Autowired
	GraphMapRepository graphMapRepository;

	@Autowired
	PageContentRepository pageContentRepository;

	@Autowired
	SingularWikiEntityRepository singularWikiEntityRepository;

	public GraphService() {

	}

	public SingularWikiEntityDto getRootEntity() {
		SingularWikiEntityDto rootEntity = graphMapRepository.findRootEntity();
		PageContentDto pageContentDto = pageContentRepository.findPageContentByTitle(rootEntity.getTitle());
		rootEntity.setPageContentDto(pageContentDto);

		return rootEntity;
	}

	public String getAndExtractDataForVisualGraph() {
		return null;
	}

	public SingularWikiEntityDto getSingularWikiEntity(String title) {
		return singularWikiEntityRepository.findSingularWikiEntityDtoByTitle(title);
	}

	public void writeGraphDataToFile(Graph graph, String path) {
		final int[] currentId = {1};
		GraphData graphData = new GraphData();
		graphData.setComment("Visual graph representation of the " + graph.getSearchTerm() + " domain.");

		List<Node> nodes = new ArrayList<>();
		HashMap<String, Node> nodeAndTitleMap = new LinkedHashMap<>();

		List<Relationship> relationships = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();

		graph.getEntities().forEach(entity -> {
			relationships.addAll(entity.getRelatedEntities());
			nodeAndTitleMap.put(entity.getTitle(), new Node(entity, currentId[0]));
			currentId[0]++;
		});

		relationships.stream().forEach(relationship -> {
			Edge edge = new Edge();
			if (relationship.getExplicitConnection() != null) {
				edge.setCaption(relationship.getExplicitConnection().replaceAll("<b>","").replaceAll("</b>",""));
			} else {
				if (relationship.getOnlyEndNodeConnection() != null) {
					edge.setCaption(relationship.getOnlyEndNodeConnection().replaceAll("<b>","").replaceAll("</b>",""));
				}
			}
			edge.setSource(nodeAndTitleMap.get(relationship.getStartNode().getTitle()).getId());
			edge.setTarget(nodeAndTitleMap.get(relationship.getEndNode().getTitle()).getId());
			edges.add(edge);
		});
		nodes.addAll(nodeAndTitleMap.values());
		graphData.setNodes(nodes);
		graphData.setEdges(edges);

		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(path + "/graphData.json"), graphData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
