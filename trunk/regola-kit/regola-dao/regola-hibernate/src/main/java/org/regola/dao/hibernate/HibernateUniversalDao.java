package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.regola.dao.UniversalDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateQueryBuilder;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public class HibernateUniversalDao implements UniversalDao {
	private ModelPatternParser patternParser = new DefaultPatternParser();

	private SessionFactory sessionFactory;

	protected Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	@Transactional(readOnly = true)
	public int count(final Class clazz, final ModelPattern pattern) {
		HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
				clazz, getSession());
		getPatternParser().createCountQuery(criteriaBuilder, pattern);
		return ((Number) criteriaBuilder.getQuery().uniqueResult()).intValue();

	}

	@Transactional(readOnly = true)
	public boolean exists(Class clazz, Serializable id) {
		return get(clazz, id) == null ? false : true;
	}

	@Transactional(readOnly = true)
	public List find(final Class clazz, final ModelPattern pattern) {

		HibernateQueryBuilder criteriaBuilder = new HibernateQueryBuilder(
				clazz, getSession());
		getPatternParser().createQuery(criteriaBuilder, pattern);
		return criteriaBuilder.getQuery().list();

	}

	@Transactional(readOnly = true)
	public Object get(Class clazz, Serializable id) {
		return getSession().get(clazz, id);
	}

	@Transactional(readOnly = true)
	public List getAll(Class clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		SessionFactoryUtils.applyTransactionTimeout(criteria,
				getSessionFactory());

		return criteria.list();
	}

	@Transactional(readOnly = false)
	public void remove(Class clazz, Serializable id) {
		Object entity = get(clazz, id);
		if (entity != null) {
			getSession().delete(entity);
		}
	}

	@Transactional(readOnly = false)
	public void removeEntity(Object entity) {
		getSession().delete(entity);
	}

	@Transactional(readOnly = false)
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

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
