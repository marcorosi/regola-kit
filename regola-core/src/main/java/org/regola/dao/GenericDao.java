package org.regola.dao;

import java.io.Serializable;
import java.util.List;

import org.regola.filter.ModelFilter;

public interface GenericDao<T, ID extends Serializable> {

	T get(ID id);

	boolean exists(ID id);

	void remove(ID id);

	void removeEntity(T entity);

	T save(T object);

	List<T> find(ModelFilter filter);

	int count(ModelFilter filter);

	List<T> getAll();

}