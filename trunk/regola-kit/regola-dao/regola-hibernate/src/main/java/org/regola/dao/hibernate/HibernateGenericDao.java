package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateQueryBuilder;
import org.regola.filter.criteria.hibernate.support.HQLquery;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author nicola
 */
public class HibernateGenericDao<T, ID extends Serializable>  implements GenericDao<T, ID>, FinderExecutor<T> {

	private Class<T> persistentClass;
	
	private SessionFactory sessionFactory;
	
	protected Session getSession()
	{
		return getSessionFactory().getCurrentSession();
	}

	private ModelPatternParser patternParser = new DefaultPatternParser();

	public HibernateGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return (T) getSession().get(persistentClass, id);
	}

	public void removeEntity(T entity) {
		getSession().delete(entity);
	}

	public void remove(ID id) {
		T entity = get(id);
		if (entity != null) {
			getSession().delete(entity);
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
		
		if (!getSession().contains(entity))
		{
			try 
			{
				getSession().saveOrUpdate(entity);
				return entity;
			}
			catch(RuntimeException e)
			{
				if (!(e.getCause() instanceof NonUniqueObjectException))
					throw e;
			}
		}
		
		getSession().merge(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(final ModelPattern pattern) {
		
				HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
						persistentClass, getSession());
				getPatternParser().createQuery(criteriaBuilder, pattern);
				return criteriaBuilder.getQuery().list();
		
	}

	public int count(final ModelPattern pattern) {
		
						HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
								persistentClass, getSession());
						getPatternParser().createCountQuery(criteriaBuilder,
								pattern);
						return ((Number) criteriaBuilder.getQuery().uniqueResult()).intValue();
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	public void setPatternParser(ModelPatternParser patternParser) {
		this.patternParser = patternParser;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		Criteria criteria = getSession().createCriteria(persistentClass);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		SessionFactoryUtils.applyTransactionTimeout(criteria, getSessionFactory());
		
		return criteria.list();
		
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
						persistentClass, getSession());
		getPatternParser().createQuery(criteriaBuilder, pattern);
		return criteriaBuilder.getClausoleHQL();
	}	
	
	public List<T> find(String hql, Map<String, Object> parameters, int firstResult, int maxResults)
	{
		Query query = getSession().createQuery(hql);
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
		Query query = getSession().createQuery(hql);
		for (String param : parameters.keySet()) {
			query.setParameter(param, parameters.get(param));
		}
		return ((Number)query.uniqueResult()).intValue();
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}		
}
