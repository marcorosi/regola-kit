package org.regola.filter.criteria.hibernate.support;

import java.util.Map;

import org.regola.filter.criteria.impl.BaseQueryBuilder.Entity;

public class HQLquery {
	
	private Entity rootEntity;

	private String select;
	
	private String from;
	
	private String where;
	
	private String orderBy;
	
	private Map<String, Object> parameters;
	
	private int firstResult;
	
	private int maxResult;

	
	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Entity getRootEntity() {
		return rootEntity;
	}

	public void setRootEntity(Entity rootEntity) {
		this.rootEntity = rootEntity;
	}
	
}
