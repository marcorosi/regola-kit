package org.regola.filter.criteria.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.regola.filter.criteria.impl.BaseQueryBuilder;

public class JpaQueryBuilder extends BaseQueryBuilder {

	private EntityManager entityManager;

	public JpaQueryBuilder(Class<?> entityClass, EntityManager entityManager) {
		super(entityClass);
		this.entityManager = entityManager;
	}

	public Query getQuery() {
		String queryString = buildQuery();
		log.debug("Query: " + queryString);
		Query query = entityManager.createQuery(queryString);
		for (String param : getParameters().keySet()) {
			query.setParameter(param, getParameters().get(param));
		}
		if (hasFirstResult()) {
			query.setFirstResult(getFirstResult());
		}
		if (hasMaxResults()) {
			query.setMaxResults(getMaxResults());
		}
		return query;
	}

	@Override
	public void addIlike(String propertyPath, String value) {
		Property property = getProperty(propertyPath);
		addFilter("lower(" + property.getEntity().getAlias() + "."
				+ property.getName() + ") like :"
				+ newParameter(likePattern(value.toLowerCase())));
	}
}
