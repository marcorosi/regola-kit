package org.regola.dao.jdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.regola.dao.GenericDao;
import org.regola.filter.FilterBuilder;
import org.regola.filter.ModelFilter;
import org.regola.filter.builder.DefaultFilterBuilder;
import org.regola.filter.criteria.jdo.JdoCriteria;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoCallback;
import org.springframework.orm.jdo.support.JdoDaoSupport;

/**
 * JDO implementation for {@link org.regola.dao.GenericDao}
 * 
 * Uses Spring {@link org.springframework.orm.jdo.JdoTemplate} facility.
 * 
 * @author davide.romanini
 * 
 */
public class JdoGenericDao<T, ID extends Serializable> extends JdoDaoSupport
		implements GenericDao<T, ID> {

	/**
	 * TODO: these fields are common to other implementations, they could be
	 * pulled up in a base class...
	 */
	private Class<T> persistentClass;

	private FilterBuilder filterBuilder = new DefaultFilterBuilder();

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	public JdoGenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public int count(final ModelFilter filter) {
		Long count = (Long) getJdoTemplate().execute(new JdoCallback() {
			public Object doInJdo(PersistenceManager pm) {
				JdoCriteria criteriaBuilder = new JdoCriteria(persistentClass,
						pm);
				getFilterBuilder().createCountFilter(criteriaBuilder, filter);
				Query q = criteriaBuilder.getJdoQuery();
				return q.executeWithMap(criteriaBuilder.getParametersMap());
			}
		});
		return count.intValue();
	}

	public void delete(T object) {
		getJdoTemplate().deletePersistent(object);
	}

	/**
	 * Finds results matching the filter
	 * 
	 * The JDO implementation creates and executes a JDO Query. The query is
	 * *not* closed (with closeAll()) after execution. I'm not sure if this
	 * could have an impact on memory utilization. All resources should be freed
	 * at PersistenceManager shutdown.
	 */
	@SuppressWarnings(value = "unchecked")
	public List<T> find(final ModelFilter filter) {
		return new ArrayList<T>(getJdoTemplate().executeFind(new JdoCallback() {
			public Object doInJdo(PersistenceManager pm) {
				JdoCriteria criteriaBuilder = new JdoCriteria(persistentClass,
						pm);
				getFilterBuilder().createFilter(criteriaBuilder, filter);
				Query q = criteriaBuilder.getJdoQuery();
				return q.executeWithMap(criteriaBuilder.getParametersMap());

			}
		}));
	}

	@SuppressWarnings(value = "unchecked")
	public List<T> getAll() {
		return new ArrayList<T>(getJdoTemplate().find(persistentClass));
	}

	@SuppressWarnings(value = "unchecked")
	public T get(ID id) {
		try {
			T instance = (T) getJdoTemplate()
					.getObjectById(persistentClass, id);
			return instance;
		} catch (ObjectRetrievalFailureException e) {
			return null;
		}
	}

	public T save(T object) {
		getJdoTemplate().makePersistent(object);
		return object;
	}

	public boolean exists(ID id) {
		try {
			getJdoTemplate().getObjectById(persistentClass, id);
			return true;
		} catch (ObjectRetrievalFailureException e) {
			return false;
		}
	}

	public void remove(ID id) {
		try {
			Object toRemove = getJdoTemplate().getObjectById(persistentClass,
					id);
			getJdoTemplate().deletePersistent(toRemove);
		} catch (ObjectRetrievalFailureException e) {
			// nothing
		}
	}

	public void removeEntity(T entity) {
		getJdoTemplate().deletePersistent(entity);

	}

}
