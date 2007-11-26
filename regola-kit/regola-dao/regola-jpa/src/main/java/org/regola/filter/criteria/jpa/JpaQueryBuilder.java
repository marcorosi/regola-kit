package org.regola.filter.criteria.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractQueryBuilder;

public class JpaQueryBuilder extends AbstractQueryBuilder {

	private EntityManager entityManager;
	private int firstResult = -1;
	private int maxResults = -1;
	private boolean rowCount;

	public JpaQueryBuilder(Class<?> entityClass, EntityManager entityManager) {
		this.entityManager = entityManager;
		setEntityClass(entityClass);
		addRootEntity();
	}

	public Query getQuery() {
		String queryString = buildQuery();
		log.debug("Query: " + queryString);
		Query query = entityManager.createQuery(queryString);
		for (String param : getParams().keySet()) {
			query.setParameter(param, getParams().get(param));
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
		}
		for (int i = 0; i < count; i++) {
			query.append("(");
			query.append(getFilters().get(i));
			query.append(") ");
			if (i < count - 1) {
				query.append(" and ");
			}
		}
		count = getOrderBy().size();
		if (count > 0) {
			query.append("order by ");
		}
		for (int i = 0; i < count; i++) {
			query.append(getOrderBy().get(i));
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
				+ property.getName() + ") like :"
				+ newParameter(value.toLowerCase() + "%"));
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
				+ " <> :" + newParameter(value));
	}

	@Override
	public Criteria addOrder(Order order) {
		Property property = getProperty(order.getPropertyName());
		if (order.isAscending()) {
			getOrderBy().add(
					property.getEntity().getAlias() + "." + property.getName()
							+ " asc");
		} else {
			getOrderBy().add(
					property.getEntity().getAlias() + "." + property.getName()
							+ " desc");
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
