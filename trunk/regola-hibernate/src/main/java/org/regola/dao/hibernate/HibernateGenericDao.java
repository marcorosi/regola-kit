package org.regola.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.regola.dao.GenericDao;
import org.regola.filter.FilterBuilder;
import org.regola.filter.ModelFilter;
import org.regola.filter.builder.DefaultFilterBuilder;
import org.regola.filter.criteria.hibernate.HibernateCriteria;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author  nicola
 */
public class HibernateGenericDao<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDao<T, ID> {

	private Class<T> persistentClass;

	private FilterBuilder filterBuilder = new DefaultFilterBuilder();

	@SuppressWarnings("unchecked")
	public ID create(T entity) {
		return (ID) getHibernateTemplate().save(entity);
	}

	public T read(ID id) {
		@SuppressWarnings("unchecked")
		T entity = (T) getHibernateTemplate().get(persistentClass, id);

		if (entity == null) {
			throw new ObjectRetrievalFailureException(persistentClass, id);
		}

		return entity;
	}

	public void update(T entity) {
		getHibernateTemplate().update(entity);
	}

	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	public void save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(final ModelFilter filter) {
		return (List<T>) getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						HibernateCriteria criteriaBuilder = new HibernateCriteria(
								session.createCriteria(persistentClass));
						getFilterBuilder()
								.createFilter(criteriaBuilder, filter);
						return criteriaBuilder.getCriteria().list();
					}
				});
	}

	public int count(final ModelFilter filter) {
		// TODO controllare null result
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						HibernateCriteria criteriaBuilder = new HibernateCriteria(
								session.createCriteria(persistentClass));
						getFilterBuilder().createCountFilter(criteriaBuilder,
								filter);
						return criteriaBuilder.getCriteria().uniqueResult();
					}
				});
	}

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return super.getHibernateTemplate().loadAll(this.persistentClass);
	}

}
