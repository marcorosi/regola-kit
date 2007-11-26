package org.regola.filter.criteria.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Criterion;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.Projection;

public abstract class AbstractQueryBuilder implements Criteria,
		Criterion.Builder, Projection.Builder {

	private static final String PARAM_PREFIX = "p";

	private Class<?> entityClass;
	private List<Entity> entities = new ArrayList<Entity>();
	private Map<String, Object> params = new HashMap<String, Object>();
	private List<String> filters = new ArrayList<String>();
	private List<String> orderBy = new ArrayList<String>();
	private int count = 0;

	protected final Log log = LogFactory.getLog(getClass());

	public class Entity {
		private static final String PREFIX = "e";
		private String name;
		private String alias;
		private Map<String, Property> properties = new HashMap<String, Property>();
		private Property joinedBy;

		public Entity(String name) {
			this.name = name;
			this.alias = newAlias(PREFIX);
		}

		public Entity(Property joinedBy) {
			this.joinedBy = joinedBy;
			this.alias = newAlias(PREFIX);
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

	public class Property {
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

	public Criteria add(Criterion criterion) {
		criterion.getOperator().dispatch(this, criterion.getProperty(),
				criterion.getValue());
		return this;
	}

	public abstract Criteria addOrder(Order order);

	public abstract Criteria setFirstResult(int firstResult);

	public abstract Criteria setMaxResults(int maxResults);

	public Criteria setProjection(Projection projection) {
		projection.dispatch(this);
		return this;
	}

	public abstract void addEquals(String property, Object value);

	public abstract void addNotEquals(String property, Object value);

	public abstract void addGreaterThan(String property, Object value);

	public abstract void addLessThan(String property, Object value);

	public abstract void addGreaterEquals(String property, Object value);

	public abstract void addLessEquals(String property, Object value);

	public abstract void addLike(String property, String value);

	public abstract void addIlike(String property, String value);

	public abstract void addIn(String property, Collection<?> value);

	public abstract void setRowCount();

	protected String newAlias(String prefix) {
		return prefix + (++count);
	}

	protected void addRootEntity() {
		entities.add(new Entity(getEntityName(entityClass)));
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
				parentEntity = new Entity(property);
			} else {
				property = new Property(parentEntity, joinProperty);
				parentEntity = new Entity(property);
			}
			if ((i < paths.length - 1) && !entities.contains(parentEntity)) {
				entities.add(parentEntity);
				log.debug("Adding joined entity " + parentEntity);
			}
		}
		return property;
	}

	protected String newParameter(Object value) {
		String param = PARAM_PREFIX + params.size();
		params.put(param, value);
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

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
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
}
