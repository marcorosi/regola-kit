package org.regola.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.impl.DefaultModelPatternParser;
import org.regola.model.ModelPattern;

public abstract class AbstractGenericDao<T, ID extends Serializable> implements
		GenericDao<T, ID> {

	private Class<T> persistentClass;

	private ModelPatternParser parser = new DefaultModelPatternParser();

	public abstract List<T> find(ModelPattern filter);

	public int count(ModelPattern pattern) {
		return find(pattern).size();
	}

	public abstract T get(ID id);

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

	public List<T> getAll() {
		ModelPattern pattern = new ModelPattern() {
		};
		pattern.disablePaging();
		return find(pattern);
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

	public ModelPatternParser getParser() {
		return parser;
	}

	public void setParser(ModelPatternParser filterBuilder) {
		this.parser = filterBuilder;
	}

}
