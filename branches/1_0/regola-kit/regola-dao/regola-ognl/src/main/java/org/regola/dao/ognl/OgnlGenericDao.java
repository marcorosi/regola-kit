package org.regola.dao.ognl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.regola.dao.GenericDao;
import org.regola.dao.MemoryGenericDao;
import org.regola.filter.ModelPatternParser;

import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.ognl.OgnlQueryBuilder;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;

import static org.regola.util.Ognl.getValue;

/**
 * @author nicola
 */
public class OgnlGenericDao<T, ID extends Serializable> implements
		MemoryGenericDao<T, ID>, FinderExecutor<T> {

	private Class<T> persistentClass;

	private OgnlQueryBuilder criteria;

	private ModelPatternParser patternParser = new DefaultPatternParser();

	public OgnlGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
		criteria = new OgnlQueryBuilder(persistentClass);
	}

	protected Collection<T> target;

	public Collection<T> getTarget() {
		return target;
	}

	public void setTarget(Collection<T> target) {
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return null;
	}

	public void removeEntity(T entity) {

	}

	public void remove(ID id) {
		T entity = get(id);
		if (entity != null) {
			// getHibernateTemplate().delete(entity);
		}
	}

	public T save(T entity) {
		// getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(final ModelPattern pattern) {
		criteria = new OgnlQueryBuilder(persistentClass);
		
		getPatternParser().createQuery(criteria, pattern);
		
		return (List<T>) criteria.executeQuery(target);

	}

	public int count(final ModelPattern pattern) {
		criteria = new OgnlQueryBuilder(persistentClass);
		
		getPatternParser().createCountQuery(criteria,pattern);
		

		return (Integer) criteria.executeQuery(target);
		
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	public void setPatternParser(ModelPatternParser patternParser) {
		this.patternParser = patternParser;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return null; // getHibernateTemplate().loadAll(persistentClass);
	}

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String finder, Object... args) {
		// final Query namedQuery = prepareQuery(queryName(finder), args);
		// return namedQuery.list();
		return null;
	}

	protected String queryName(String query) {
		return persistentClass.getSimpleName() + "." + query;
	}

	/*
	 * protected Query prepareQuery(String queryName, Object... args) { final
	 * Query namedQuery = getSession().getNamedQuery(queryName); String[]
	 * namedParameters = namedQuery.getNamedParameters(); if
	 * (namedParameters.length == 0) { setPositionalParams(args, namedQuery); }
	 * else { setNamedParams(namedParameters, args, namedQuery); } return
	 * namedQuery; }
	 * 
	 * protected void setPositionalParams(Object[] queryArgs, Query namedQuery) {
	 * if (queryArgs != null) { for (int i = 0; i < queryArgs.length; i++) {
	 * Object arg = queryArgs[i]; namedQuery.setParameter(i, arg); } } }
	 * 
	 * protected void setNamedParams(String[] namedParameters, Object[]
	 * queryArgs, Query namedQuery) { if (queryArgs != null) { for (int i = 0; i <
	 * queryArgs.length; i++) { Object arg = queryArgs[i]; if (arg instanceof
	 * Collection) { namedQuery.setParameterList(namedParameters[i], (Collection<?>)
	 * arg); } else { namedQuery.setParameter(namedParameters[i], arg); } } } }
	 */
}
