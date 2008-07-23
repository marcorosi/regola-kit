package org.regola.filter.criteria.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.regola.filter.criteria.hibernate.support.HQLquery;
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
	
	public HQLquery getClausoleHQL() {
		HQLquery hqlQuery = new HQLquery();
		
		hqlQuery.setParameters(getParameters());
		hqlQuery.setFirstResult(getFirstResult());
		hqlQuery.setMaxResult(getMaxResults());
		hqlQuery.setRootEntity(getRootEntity());
		
		String queryHqlString = buildQuery();
		
		String[] pezzi = queryHqlString.split("from");
		hqlQuery.setSelect(pezzi[0].replace("select", "").replace("distinct", ""));
		String resto = pezzi[1];
		if(resto.contains("where"))
		{
			pezzi = resto.split("where");
			hqlQuery.setFrom(pezzi[0]);
			resto = pezzi[1];
		}
		if(resto.contains("order by"))
		{
			pezzi = resto.split("order by");
			hqlQuery.setOrderBy(pezzi[1]);
		}
		if(hqlQuery.getFrom() == null)
			hqlQuery.setFrom(pezzi[0]);
		else
			hqlQuery.setWhere(pezzi[0]);
		
		return hqlQuery;
	}	
}
