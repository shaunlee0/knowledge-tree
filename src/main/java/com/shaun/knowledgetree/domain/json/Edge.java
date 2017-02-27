package com.shaun.knowledgetree.domain.json;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
				"source",
				"target",
				"caption"
})
public class Edge {

	@JsonProperty("source")
	private Integer source;
	@JsonProperty("target")
	private Integer target;
	@JsonProperty("caption")
	private String caption;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("source")
	public Integer getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(Integer source) {
		this.source = source;
	}

	@JsonProperty("target")
	public Integer getTarget() {
		return target;
	}

	@JsonProperty("target")
	public void setTarget(Integer target) {
		this.target = target;
	}

	@JsonProperty("caption")
	public String getCaption() {
		return caption;
	}

	@JsonProperty("caption")
	public void setCaption(String caption) {
		this.caption = caption;
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
