package org.regola.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.finder.FinderExecutor;
import org.regola.model.ModelPattern;

/**
 * Implementazione di base dell'interfaccia {@link GenericDao} per agevolare la
 * scrittura dei DAO per i diversi motori di persistenza.
 * 
 * Applica il pattern Template Method [Gof95] per fornire un'implementazione di
 * default di alcuni metodi per le classi derivate.
 * 
 */
public abstract class AbstractGenericDao<T, ID extends Serializable> implements
		GenericDao<T, ID>, FinderExecutor<T> {

	private Class<T> persistentClass;

	private ModelPatternParser patternParser = new DefaultPatternParser();

	public abstract T get(ID id);

	public boolean exists(ID id) {
		return get(id) == null ? false : true;
	}

	public void remove(ID id) {
		T entity = get(id);
		if (entity != null) {
			removeEntity(entity);
		}
	}

	public abstract void removeEntity(T entity);

	public abstract T save(T entity);

	public abstract List<T> find(ModelPattern pattern);

	/**
	 * Attenzione: meglio fare l'overload potendo per evitare performace penose.
	 */
	public int count(ModelPattern pattern) {
		return find(pattern).size();
	}

	public List<T> getAll() {
		@SuppressWarnings("serial")
		ModelPattern pattern = new ModelPattern(false) {
		};
		return find(pattern);
	}

	public abstract List<T> executeFinder(String finder, Object... args);

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	public void setPatternParser(ModelPatternParser parser) {
		this.patternParser = parser;
	}
}
