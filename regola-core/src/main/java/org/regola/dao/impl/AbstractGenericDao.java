package org.regola.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.regola.dao.GenericDao;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.impl.DefaultModelPatternParser;
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

	private ModelPatternParser patternParser = new DefaultModelPatternParser();

	/**
	 * Attenzione: meglio fare l'overload potendo per evitare performace penose.
	 */
	public int count(ModelPattern pattern) {
		return find(pattern).size();
	}

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
