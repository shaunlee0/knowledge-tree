package com.shaun.knowledgetree.domain.json;

import com.fasterxml.jackson.annotation.*;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
				"caption",
				"type",
				"id",
				"root"
})
public class Node {

	@JsonProperty("caption")
	private String caption;
	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("root")
	private Boolean root;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Node(SingularWikiEntityDto entity, int currentId) {
		setId(currentId);
		setCaption(entity.getTitle());
		setRoot(entity.isRoot());
		setType("article");
	}

	@JsonProperty("caption")
	public String getCaption() {
		return caption;
	}

	@JsonProperty("caption")
	public void setCaption(String caption) {
		this.caption = caption;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("root")
	public Boolean getRoot() {
		return root;
	}

	@JsonProperty("root")
	public void setRoot(Boolean root) {
		this.root = root;
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