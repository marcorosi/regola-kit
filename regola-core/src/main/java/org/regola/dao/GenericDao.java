package org.regola.dao;

import java.io.Serializable;
import java.util.List;

import org.regola.model.ModelPattern;

/**
 * L'interfaccia del generic dao per effettuare le
 * più comuni operazioni di CRUD sul tipo T con chiave
 * relazionale di tipo ID.
 */
public interface GenericDao<T, ID extends Serializable> {

	    
        T get(ID id);

	boolean exists(ID id);

	void remove(ID id);

	void removeEntity(T entity);

	T save(T object);

	List<T> find(ModelPattern pattern);

	int count(ModelPattern pattern);

	List<T> getAll();

}