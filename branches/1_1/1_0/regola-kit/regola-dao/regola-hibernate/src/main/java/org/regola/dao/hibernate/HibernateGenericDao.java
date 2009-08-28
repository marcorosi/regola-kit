package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateQueryBuilder;
import org.regola.filter.criteria.hibernate.support.HQLquery;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author nicola
 */
public class HibernateGenericDao<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDao<T, ID>, FinderExecutor<T> {

	private Class<T> persistentClass;

	private ModelPatternParser patternParser = new DefaultPatternParser();

	public HibernateGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return (T) getHibernateTemplate().get(persistentClass, id);
	}

	public void removeEntity(T entity) {
		getHibernateTemplate().delete(entity);
	}

	public void remove(ID id) {
		T entity = get(id);
		if (entity != null) {
			getHibernateTemplate().delete(entity);
		}
	}

	/**
	 * saveOrUpdate() si limita a fare un reattach
	 * per cui, se nel contesto di persistenza,
	 * è già stato caricato entity (ovvero un istanza
	 * diversa da entity ma relativa alla stesso record
	 * sul database) si solleva un'eccezione di
	 * NotUniqueObjectException.
	 */
	public T save(T entity) {
		
		if (!getHibernateTemplate().contains(entity))
		{
			try 
			{
				getHibernateTemplate().saveOrUpdate(entity);
				return entity;
			}
			catch(RuntimeException e)
			{
				if (!(e.getCause() instanceof NonUniqueObjectException))
					throw e;
			}
		}
		
		getHibernateTemplate().merge(entity);
		return entity;
	}
	/*
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}
	*/

	@SuppressWarnings("unchecked")
	public List<T> find(final ModelPattern pattern) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				// HibernateCriteria criteriaBuilder = new HibernateCriteria(
				// session.createCriteria(persistentClass));
				// HibernateCriteria criteriaBuilder = new HibernateCriteria(
				// session, persistentClass, getSessionFactory());
				HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
						persistentClass, session);
				getPatternParser().createQuery(criteriaBuilder, pattern);
				return criteriaBuilder.getQuery().list();
			}
		});
	}

	public int count(final ModelPattern pattern) {
		// TODO controllare null result?
		return ((Number) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						// HibernateCriteria criteriaBuilder = new
						// HibernateCriteria(
						// session.createCriteria(persistentClass));
						HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
								persistentClass, session);
						getPatternParser().createCountQuery(criteriaBuilder,
								pattern);
						return criteriaBuilder.getQuery().uniqueResult();
					}
				})).intValue();
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	public void setPatternParser(ModelPatternParser patternParser) {
		this.patternParser = patternParser;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(persistentClass);
	}

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String finder, Object... args) {
		final Query namedQuery = prepareQuery(queryName(finder), args);
		return namedQuery.list();
	}

	protected String queryName(String query) {
		return persistentClass.getSimpleName() + "." + query;
	}

	protected Query prepareQuery(String queryName, Object... args) {
		final Query namedQuery = getSession().getNamedQuery(queryName);
		String[] namedParameters = namedQuery.getNamedParameters();
		if (namedParameters.length == 0) {
			setPositionalParams(args, namedQuery);
		} else {
			setNamedParams(namedParameters, args, namedQuery);
		}
		return namedQuery;
	}

	protected void setPositionalParams(Object[] queryArgs, Query namedQuery) {
		if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				namedQuery.setParameter(i, arg);
			}
		}
	}

	protected void setNamedParams(String[] namedParameters, Object[] queryArgs,
			Query namedQuery) {
		if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				if (arg instanceof Collection) {
					namedQuery.setParameterList(namedParameters[i],
							(Collection<?>) arg);
				} else {
					namedQuery.setParameter(namedParameters[i], arg);
				}
			}
		}
	}
	
	// **************** parte custom del solo HibernateGenericDao *************************************
	
	public HQLquery getClausoleHQL(final ModelPattern pattern) {
		HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
						persistentClass, getHibernateTemplate().getSessionFactory().getCurrentSession());
		getPatternParser().createQuery(criteriaBuilder, pattern);
		return criteriaBuilder.getClausoleHQL();
	}	
	
	public List<T> find(String hql, Map<String, Object> parameters, int firstResult, int maxResults)
	{
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		for (String param : parameters.keySet()) {
			query.setParameter(param, parameters.get(param));
		}
		if (firstResult >= 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResults >= 0) {
			query.setMaxResults(maxResults);
		}
		return query.list();		
	}
	
	public int count(String hql, Map<String, Object> parameters) {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		for (String param : parameters.keySet()) {
			query.setParameter(param, parameters.get(param));
		}
		return ((Number)query.uniqueResult()).intValue();
	}	
	
}
