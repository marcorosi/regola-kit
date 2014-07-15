package org.regola.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.regola.dao.UniversalDao;
import org.regola.dao.UniversalDao2;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.jpa.JpaQueryBuilder;
import org.regola.filter.impl.DefaultPatternParser;
import org.regola.model.ModelPattern;

/**
 * Implementazione per JPA del nostro buon universal DAO
 * 
 * @author nicola
 *
 */
@SuppressWarnings("rawtypes")
public class JpaUniversalDao implements UniversalDao, UniversalDao2 {

	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private ModelPatternParser patternParser = new DefaultPatternParser();

	
	@SuppressWarnings("serial")
	public List getAll(Class clazz) {
		ModelPattern pattern = new ModelPattern(false) {};
		return find(clazz,pattern);
	}

	public boolean exists(Class clazz, Serializable id) {
		return get(clazz, id) == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	public Object get(Class clazz, Serializable id) {
		return entityManager.find(clazz, id);
	}

	public Object save(Object entity) {
		return entityManager.merge(entity);
	}

	public void remove(Class clazz, Serializable id) {
		Object entity = get(clazz, id);
		if (entity != null) {
			removeEntity(entity);
		}
	}

	public void removeEntity(Object entity) {
		entityManager.remove(entity);

	}

	public List find(Class clazz, ModelPattern pattern) {
		JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(
				clazz, entityManager);
		getPatternParser().createQuery(criteriaBuilder, pattern);
		return criteriaBuilder.getQuery().getResultList();
	}

	public int count(Class clazz, ModelPattern pattern) {
		JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(
				clazz, entityManager);
		getPatternParser().createCountQuery(criteriaBuilder, pattern);
		return ((Number) criteriaBuilder.getQuery().getSingleResult())
				.intValue();
	}

	public ModelPatternParser getPatternParser() {
		return patternParser;
	}

	public void setPatternParser(ModelPatternParser patternParser) {
		this.patternParser = patternParser;
	}

	public <T> List<T> queryList(Class<T> clazz, String jpql, Object... params) {
		  TypedQuery<T> query =  this.entityManager.createQuery(jpql, clazz);
	      int index = 0;
	      for (Object param : params) query.setParameter(++index, param);
	      return query.getResultList();
	}

	public <T> List<T> queryListPaged(Class<T> clazz, String jpql, int first, int max,
			Object... params) {
		 TypedQuery<T> query =  this.entityManager.createQuery(jpql, clazz);
	        int index = 0;
	        for (Object param : params) query.setParameter(++index, param);
	        query.setFirstResult(first);
	        query.setMaxResults(max);
	        return query.getResultList();
	}

	public <T> T querySingle(Class<T> clazz, String jpql, Object... params) {
		TypedQuery<T> query =  this.entityManager.createQuery(jpql, clazz);
        int index = 0;
        for (Object param : params) query.setParameter(++index, param);
        return query.getSingleResult();
	}

	public int queryUpdate(String jpql, Object... params) {
		Query query =  this.entityManager.createQuery(jpql);
        int index = 0;
        for (Object param : params) query.setParameter(++index, param);
        return query.executeUpdate();
	}

	public void flush() {
	     this.entityManager.flush();
	}

	public void clear() {
		this.entityManager.clear();
		
	}

	public <T> T merge(T entity) {
		T merged = this.entityManager.merge(entity);
	    this.entityManager.flush();
	    return merged;
	}

}
