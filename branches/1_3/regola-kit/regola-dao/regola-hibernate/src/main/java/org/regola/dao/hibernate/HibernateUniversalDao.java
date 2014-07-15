package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.regola.dao.UniversalDao;
import org.regola.dao.UniversalDao2;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateQueryBuilder;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementazione dello universal dao senza definizione delle transazioni.
 * @author marco
 * @author nicola
 *
 */
@SuppressWarnings("unchecked")
public class HibernateUniversalDao implements UniversalDao, UniversalDao2 {
	private ModelPatternParser patternParser = new DefaultPatternParser();

	private SessionFactory sessionFactory;

	protected Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	public int count( final Class clazz, final ModelPattern pattern) {
		HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
				clazz, getSession());
		getPatternParser().createCountQuery(criteriaBuilder, pattern);
		return ((Number) criteriaBuilder.getQuery().uniqueResult()).intValue();

	}

	public boolean exists(Class clazz, Serializable id) {
		return get(clazz, id) == null ? false : true;
	}

	public List find(final Class clazz, final ModelPattern pattern) {

		HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
				clazz, getSession());
		getPatternParser().createQuery(criteriaBuilder, pattern);
		return criteriaBuilder.getQuery().list();

	}

	public Object get(Class clazz, Serializable id) {
		return getSession().get(clazz, id);
	}

	public List getAll(Class clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		SessionFactoryUtils.applyTransactionTimeout(criteria,
				getSessionFactory());

		return criteria.list();
	}

	public void remove(Class clazz, Serializable id) {
		Object entity = get(clazz, id);
		if (entity != null) {
			getSession().delete(entity);
		}
	}

	public void removeEntity(Object entity) {
		getSession().delete(entity);
	}

	public Object save(Object entity) {
		if (!getSession().contains(entity)) {
			try {
				getSession().saveOrUpdate(entity);
				return entity;
			} catch (RuntimeException e) {
				if (!(e.getCause() instanceof NonUniqueObjectException))
					throw e;
			}
		}

		getSession().merge(entity);
		return entity;
	}

	public void setPatternParser(ModelPatternParser patternParser) {
		this.patternParser = patternParser;
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public <T> List<T> queryList(Class<T> clazz, String jpql, Object... params) {
		  Query query =   getSession().createQuery(jpql);
	      int index = 0;
	      for (Object param : params) query.setParameter(++index, param);
	      return query.list();
	}

	public <T> List<T> queryListPaged(Class<T> clazz, String jpql, int first, int max,
			Object... params) {
		    Query query =  getSession().createQuery(jpql);
	        int index = 0;
	        for (Object param : params) query.setParameter(++index, param);
	        query.setFirstResult(first);
	        query.setMaxResults(max);
	        return query.list();
	}

	@SuppressWarnings("rawtypes")
	public <T> T querySingle(Class<T> clazz, String jpql, Object... params) {
		Query query =  getSession().createQuery(jpql);
        int index = 0;
        for (Object param : params) query.setParameter(++index, param);
		List list = query.list();
        
        return list != null && list.size()>0 ? (T) list.get(0) : null; 
        
	}

	public int queryUpdate(String jpql, Object... params) {
		Query query =  getSession().createQuery(jpql);
        int index = 0;
        for (Object param : params) query.setParameter(++index, param);
        return query.executeUpdate();
	}

	public void flush() {
		getSession().flush();
		
	}

	public void clear() {
		getSession().clear();
		
	}

	public <T> T merge(T entity) {
		T merged =  (T) getSession().merge(entity);
	    flush();
	    return merged;
	}

}
