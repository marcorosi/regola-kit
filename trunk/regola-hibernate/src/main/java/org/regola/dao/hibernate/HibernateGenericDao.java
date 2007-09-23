package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.hibernate.HibernateCriteria;
import org.regola.filter.impl.DefaultModelPatternParser;
import org.regola.model.ModelPattern;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author nicola
 */
public class HibernateGenericDao<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDao<T, ID> {

	private Class<T> persistentClass;

	private ModelPatternParser filterBuilder = new DefaultModelPatternParser();

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
	public List<T> find(final ModelPattern filter) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				HibernateCriteria criteriaBuilder = new HibernateCriteria(
						session.createCriteria(persistentClass));
				getFilterBuilder().createQuery(criteriaBuilder, filter);
				return criteriaBuilder.getCriteria().list();
			}
		});
	}

	public int count(final ModelPattern filter) {
		// TODO controllare null result?
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						HibernateCriteria criteriaBuilder = new HibernateCriteria(
								session.createCriteria(persistentClass));
						getFilterBuilder().createCountQuery(criteriaBuilder,
								filter);
						return criteriaBuilder.getCriteria().uniqueResult();
					}
				});
	}

	public ModelPatternParser getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(ModelPatternParser filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(persistentClass);
	}

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

}
