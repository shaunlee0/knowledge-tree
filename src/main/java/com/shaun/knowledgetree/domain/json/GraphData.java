package com.shaun.knowledgetree.domain.json;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
				"comment",
				"nodes",
				"edges"
})
public class GraphData {

	@JsonProperty("comment")
	private String comment;
	@JsonProperty("nodes")
	private List<Node> nodes = null;
	@JsonProperty("edges")
	private List<Edge> edges = null;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

	@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonProperty("nodes")
	public List<Node> getNodes() {
		return nodes;
	}

	@JsonProperty("nodes")
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@JsonProperty("edges")
	public List<Edge> getEdges() {
		return edges;
	}

	@JsonProperty("edges")
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
