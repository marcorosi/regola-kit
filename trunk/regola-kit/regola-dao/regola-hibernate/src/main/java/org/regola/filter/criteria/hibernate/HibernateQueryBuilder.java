package org.regola.filter.criteria.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractQueryBuilder;

public class HibernateQueryBuilder extends AbstractQueryBuilder {

	private static final String PARAM_PREFIX = "p";
	private int count = 0;

	private String newAlias(String prefix) {
		return prefix + (++count);
	}

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

	protected final Log log = LogFactory.getLog(getClass());
	private Session session;
	private Class<?> entityClass;
	private List<Entity> entities = new ArrayList<Entity>();
	private Map<String, Object> params = new HashMap<String, Object>();
	private List<String> filters = new ArrayList<String>();
	private int firstResult = -1;
	private int maxResults = -1;
	private boolean rowCount;
	private List<String> orderBy = new ArrayList<String>();

	public HibernateQueryBuilder(Session session, Class<?> entityClass) {
		this.session = session;
		this.entityClass = entityClass;
		addRootEntity();
	}

	private void addRootEntity() {
		entities.add(new Entity(getEntityName(entityClass)));
	}

	private Entity getRootEntity() {
		return entities.get(0);
	}

	private String getEntityName(Class<?> clazz) {
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

	public Query getQuery() {
		String queryString = buildQuery();
		log.debug("Query: " + queryString);
		Query query = session.createQuery(queryString);
		for (String param : params.keySet()) {
			query.setParameter(param, params.get(param));
		}
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		return query;
	}

	private String buildQuery() {
		StringBuilder query = new StringBuilder();
		if (rowCount) {
			query.append("select count(distinct ");
			query.append(getRootEntity().getAlias());
			query.append(") from ");
		} else {
			query.append("select distinct ");
			query.append(getRootEntity().getAlias());
			query.append(" from ");
		}
		int count = entities.size();
		for (int i = 0; i < count; i++) {
			Entity entity = entities.get(i);
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
		count = filters.size();
		if (count > 0) {
			query.append("where ");
		}
		for (int i = 0; i < count; i++) {
			query.append("(");
			query.append(filters.get(i));
			query.append(") ");
			if (i < count - 1) {
				query.append(" and ");
			}
		}
		count = orderBy.size();
		if (count > 0) {
			query.append("order by ");
		}
		for (int i = 0; i < count; i++) {
			query.append(orderBy.get(i));
			if (i < count - 1) {
				query.append(", ");
			}
		}
		return query.toString();
	}

	@Override
	public void addEquals(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " = :" + newParameter(value));
	}

	private String newParameter(Object value) {
		String param = PARAM_PREFIX + params.size();
		params.put(param, value);
		return param;
	}

	private void addFilter(String filter) {
		filters.add(filter);
	}

	@Override
	public void addGreaterEquals(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " >= :" + newParameter(value));
	}

	@Override
	public void addGreaterThan(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " > :" + newParameter(value));
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		Property property = getProperty(propertyPath);
		addFilter("lower(" + property.getEntity().getAlias() + "."
				+ property.getName() + ") like lower(:"
				+ newParameter(value + "%") + ")");
	}

	@Override
	public void addIn(String propertyPath, Collection<?> value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " in (" + parametersList(value) + ")");
	}

	private String parametersList(Collection<?> values) {
		StringBuilder text = new StringBuilder();
		int count = values.size();
		int i = 0;
		for (Object value : values) {
			text.append(":").append(newParameter(value));
			if (i < count - 1) {
				text.append(", ");
			}
			i += 1;
		}
		return text.toString();
	}

	@Override
	public void addLessEquals(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " <= :" + newParameter(value));
	}

	@Override
	public void addLessThan(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " < :" + newParameter(value));
	}

	@Override
	public void addLike(String propertyPath, String value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " like :" + newParameter(value + "%"));
	}

	@Override
	public void addNotEquals(String propertyPath, Object value) {
		Property property = getProperty(propertyPath);
		addFilter(property.getEntity().getAlias() + "." + property.getName()
				+ " != :" + newParameter(value));
	}

	@Override
	public Criteria addOrder(Order order) {
		Property property = getProperty(order.getPropertyName());
		if (order.isAscending()) {
			orderBy.add(property.getEntity().getAlias() + "."
					+ property.getName() + " asc");
		} else {
			orderBy.add(property.getEntity().getAlias() + "."
					+ property.getName() + " desc");
		}
		return this;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	@Override
	public void setRowCount() {
		rowCount = true;
	}

}
