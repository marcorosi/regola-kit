package org.regola.filter.criteria.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.regola.filter.criteria.impl.BaseQueryBuilder;

public class HibernateQueryBuilder extends BaseQueryBuilder {

	private Session session;

	public HibernateQueryBuilder(Class<?> entityClass, Session session) {
		super(entityClass);
		this.session = session;
	}

	public Query getQuery() {
		String queryString = buildQuery();
		log.debug("Query: " + queryString);
		Query query = session.createQuery(queryString);
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
	protected String getRootEntityCountAlias() {
		return "*";
	}
}
