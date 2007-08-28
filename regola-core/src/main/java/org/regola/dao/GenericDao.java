package org.regola.dao;

import java.io.Serializable;
import java.util.List;

import org.regola.filter.ModelFilter;

public interface GenericDao<T, ID extends Serializable> {

	ID create(T object);

	T read(ID id);

	void update(T object);

	void delete(T object);

	void save(T object);

	List<T> find(ModelFilter filter);

	int count(ModelFilter filter);

	List<T> getAll();

}