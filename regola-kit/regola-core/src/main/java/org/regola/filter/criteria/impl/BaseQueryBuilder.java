package org.regola.filter.criteria.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;

public class BaseQueryBuilder extends AbstractCriteriaBuilder {

	protected static final String PARAM_PREFIX = "p";

	protected final Log log = LogFactory.getLog(getClass());

	private Class<?> entityClass;
	private List<Entity> entities = new ArrayList<Entity>();
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private List<String> filters = new ArrayList<String>();
	private List<String> orderBy = new ArrayList<String>();
	private int firstResult = -1;
	private int maxResults = -1;
	private boolean rowCount = false;

	public static class Entity {
		protected static final String ALIAS_PREFIX = "e";
		private String name;
		private String alias;
		private Map<String, Property> properties = new HashMap<String, Property>();
		private Property joinedBy;

		public Entity(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}

		public Entity(Property joinedBy, String alias) {
			this.joinedBy = joinedBy;
			this.alias = alias;
		}

		public Map<String, Property> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, Property> properties) {
			this.properties = properties;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Property getJoinedBy() {
			return joinedBy;
		}

		public void setJoinedBy(Property joinedBy) {
			this.joinedBy = joinedBy;
		}

		@Override
		public String toString() {
			StringBuilder text = new StringBuilder();
			if (name != null) {
				text.append(name).append(" ");
			}
			text.append("aliased with ").append(alias);
			if (joinedBy != null) {
				text.append(" joined by ").append(joinedBy.getName());
			}
			return text.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((alias == null) ? 0 : alias.hashCode());
			result = prime * result
					+ ((joinedBy == null) ? 0 : joinedBy.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Entity other = (Entity) obj;
			if (alias == null) {
				if (other.alias != null)
					return false;
			} else if (!alias.equals(other.alias))
				return false;
			if (joinedBy == null) {
				if (other.joinedBy != null)
					return false;
			} else if (!joinedBy.equals(other.joinedBy))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	public static class Property {
		private Entity entity;
		private String name;

		public Property(Entity entity, String name) {
			this.entity = entity;
			this.name = name;
			this.entity.getProperties().put(name, this);
		}

		public Entity getEntity() {
			return entity;
		}

		public void setEntity(Entity entity) {
			this.entity = entity;
		}

		public String getName() {
			return name;
		}

		public void setName(String propertyPath) {
			this.name = propertyPath;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((entity == null) ? 0 : entity.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Property other = (Property) obj;
			if (entity == null) {
				if (other.entity != null)
					return false;
			} else if (!entity.equals(other.entity))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}

	public BaseQueryBuilder(Class<?> entityClass) {
		setEntityClass(entityClass);
		addRootEntity();
	}

	protected String newAlias(String prefix) {
		return prefix + entities.size();
	}

	protected Entity newEntity(String name) {
		return new Entity(name, newAlias(Entity.ALIAS_PREFIX));
	}

	protected Entity newEntity(Property joinedBy) {
		return new Entity(joinedBy, newAlias(Entity.ALIAS_PREFIX));
	}

	protected void addRootEntity() {
		entities.add(newEntity(getEntityName(entityClass)));
	}

	protected Entity getRootEntity() {
		return entities.get(0);
	}

	protected String getEntityName(Class<?> clazz) {
		return clazz.getSimpleName();
	}

	protected Property getProperty(String propertyPath) {
		String[] paths = propertyPath.split("\\[\\].?");
		Entity parentEntity = getRootEntity();
		Property property = null;
		for (int i = 0; i < paths.length; i++) {
			String joinProperty = paths[i];
			if (parentEntity.getProperties().containsKey(joinProperty)) {
				property = parentEntity.getProperties().get(joinProperty);
				parentEntity = newEntity(property);
			} else {
				property = new Property(parentEntity, joinProperty);
				parentEntity = newEntity(property);
			}
			if ((i < paths.length - 1) && !entities.contains(parentEntity)) {
				entities.add(parentEntity);
				log.debug("Adding joined entity " + parentEntity);
			}
		}
		return property;
	}

	protected String newParameter(Object value) {
		String param = PARAM_PREFIX + parameters.size();
		parameters.put(param, value);
		return param;
	}

	protected void addFilter(String filter) {
		filters.add(filter);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> params) {
		this.parameters = params;
	}

	public List<String> getFilters() {
		return filters;
	}

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	public List<String> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<String> orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public int getFirstResult() {
		return firstResult;
	}

	protected boolean hasFirstResult() {
		return firstResult != -1;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public int getMaxResults() {
		return maxResults;
	}

	protected boolean hasMaxResults() {
		return maxResults != -1;
	}

	@Override
	public void setRowCount() {
		rowCount = true;
	}

	public boolean isRowCount() {
		return rowCount;
	}

	protected String buildQuery() {
		StringBuilder query = new StringBuilder();
		if (isRowCount()) {
			query.append("select count(distinct ");
			query.append(getRootEntity().getAlias());
			query.append(") from ");
		} else {
			query.append("select distinct ");
			query.append(getRootEntity().getAlias());
			query.append(" from ");
		}

		int count = getEntities().size();
		for (int i = 0; i < count; i++) {
			Entity entity = getEntities().get(i);
			if (entity.getName() != null) {
				query.append(entity.getName());
				query.append(" ");
				query.append(entity.getAlias());
			} else if (entity.getJoinedBy() != null) {
				query.append(entity.getJoinedBy().getEntity().getAlias());
				query.append(".");
				query.append(entity.getJoinedBy().getName());
				query.append(" ");
				query.append(entity.getAlias());
			}
			if (i < count - 1) {
				query.append(" join ");
			} else {
				query.append(" ");
			}
		}

		count = getFilters().size();
		if (count > 0) {
			query.append("where ");
			query.append(joinFilters());
		}

		count = getOrderBy().size();
		if (count > 0) {
			query.append("order by ");
			query.append(joinOrderBy());
		}

		return query.toString();
	}

	protected String joinFilters() {
		StringBuilder text = new StringBuilder();
		int count = getOrderBy().size();
		for (int i = 0; i < count; i++) {
			text.append("(");
			text.append(getFilters().get(i));
			text.append(") ");
			if (i < count - 1) {
				text.append(" ").append(getAndOperator()).append(" ");
			}
		}
		return text.toString();
	}

	protected String getAndOperator() {
		return "and";
	}

	protected String joinOrderBy() {
		StringBuilder text = new StringBuilder();
		int count = getOrderBy().size();
		for (int i = 0; i < count; i++) {
			text.append(getOrderBy().get(i));
			if (i < count - 1) {
				text.append(", ");
			}
		}
		return text.toString();
	}

	@Override
	public void addEquals(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " "
				+ getEqualsOperator() + " "
				+ parameterReference(newParameter(value)));
	}

	protected String getEqualsOperator() {
		return "=";
	}

	protected String propertyReference(Property property) {
		String alias = property.getEntity().getAlias();
		if (alias.length() > 0) {
			return alias + "." + property.getName();
		} else {
			return property.getName();
		}
	}

	protected String parameterReference(String parameter) {
		return ":" + parameter;
	}

	@Override
	public void addGreaterEquals(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " >= "
				+ parameterReference(newParameter(value)));
	}

	@Override
	public void addGreaterThan(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " > "
				+ parameterReference(newParameter(value)));
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		addFilter("lower(" + propertyReference(getProperty(propertyPath))
				+ ") like lower("
				+ parameterReference(newParameter(likePattern(value))) + ")");
	}

	protected String likePattern(String value) {
		return likePattern(value, "_", "%");
	}

	protected String likePattern(String value, String dot, String star) {
		return value.replace(".", dot).replace("*", star);
	}

	@Override
	public void addIn(String propertyPath, Collection<?> value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " in ("
				+ parametersList(value) + ")");
	}

	private String parametersList(Collection<?> values) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		for (Object value : values) {
			if (!first) {
				text.append(", ");
			}
			first = false;
			text.append(parameterReference(newParameter(value)));
		}
		return text.toString();
	}

	@Override
	public void addLessEquals(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " <= "
				+ parameterReference(newParameter(value)));
	}

	@Override
	public void addLessThan(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " < "
				+ parameterReference(newParameter(value)));
	}

	@Override
	public void addLike(String propertyPath, String value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " like "
				+ parameterReference(newParameter(likePattern(value))));
	}

	@Override
	public void addNotEquals(String propertyPath, Object value) {
		addFilter(propertyReference(getProperty(propertyPath)) + " "
				+ getNotEqualsOperator() + " "
				+ parameterReference(newParameter(value)));
	}

	protected String getNotEqualsOperator() {
		return "<>";
	}

	@Override
	public Criteria addOrder(Order order) {
		Property property = getProperty(order.getPropertyName());
		if (order.isAscending()) {
			getOrderBy().add(propertyReference(property) + " asc");
		} else {
			getOrderBy().add(propertyReference(property) + " desc");
		}
		return this;
	}
}