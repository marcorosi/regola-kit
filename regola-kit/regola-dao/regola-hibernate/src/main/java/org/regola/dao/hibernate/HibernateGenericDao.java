package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateCriteria;
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

	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(final ModelPattern pattern) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
//				HibernateCriteria criteriaBuilder = new HibernateCriteria(
//						session.createCriteria(persistentClass));
				HibernateCriteria criteriaBuilder = new HibernateCriteria(
						session, persistentClass, getSessionFactory());
				getPatternParser().createQuery(criteriaBuilder, pattern);
				return criteriaBuilder.getCriteria().list();
			}
		});
	}

	public int count(final ModelPattern pattern) {
		// TODO controllare null result?
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
//						HibernateCriteria criteriaBuilder = new HibernateCriteria(
//								session.createCriteria(persistentClass));
						HibernateCriteria criteriaBuilder = new HibernateCriteria(
								session, persistentClass, getSessionFactory());
						getPatternParser().createCountQuery(criteriaBuilder,
								pattern);
						return criteriaBuilder.getCriteria().uniqueResult();
					}
				});
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
}
