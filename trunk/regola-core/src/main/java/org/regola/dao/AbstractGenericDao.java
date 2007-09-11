package org.regola.dao;

import java.io.Serializable;
import java.util.List;

import org.regola.filter.FilterBuilder;
import org.regola.filter.ModelFilter;
import org.regola.filter.builder.DefaultFilterBuilder;

public abstract class AbstractGenericDao<T, ID extends Serializable> implements
		GenericDao<T, ID> {

	private Class<T> persistentClass;

	private FilterBuilder filterBuilder = new DefaultFilterBuilder();

	public abstract List<T> find(ModelFilter filter);

	public int count(ModelFilter pattern) {
		return find(pattern).size();
	}

	public abstract T get(ID id);

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

	public List<T> getAll() {
		return find(new ModelFilter() {
		});
	}

	public void remove(ID id) {
		T entity = get(id);
		if (entity != null) {
			removeEntity(entity);
		}
	}

	public abstract void removeEntity(T entity);

	public abstract T save(T entity);

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

}
