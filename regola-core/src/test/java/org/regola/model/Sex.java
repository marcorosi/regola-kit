package org.regola.model;

import java.io.Serializable;

public class Sex implements Serializable {

	private static final long serialVersionUID = 7689093570986418468L;

	private String id;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
