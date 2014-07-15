package org.regola.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.regola.dao.GenericDao2;
import org.regola.dao.impl.AbstractGenericDao;
import org.regola.filter.criteria.jpa.JpaQueryBuilder;
import org.regola.model.ModelPattern;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author nicola
 *
 * @param <T>
 * @param <ID>
 */
@Repository
public class JpaGenericDao<T, ID extends Serializable> extends
		AbstractGenericDao<T, ID> implements GenericDao2<T,ID> {

	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JpaGenericDao(Class<T> clazz) {
		setPersistentClass(clazz);
	}

	public T get(ID id) {
		return entityManager.find(getPersistentClass(), id);
	}

	public T save(T entity) {
		return entityManager.merge(entity);
	}

	public void removeEntity(T object) {
		entityManager.remove(object);
	}

	@SuppressWarnings("unchecked")
	public List<T> executeFinder(String finder, Object... args) {
		Query query = entityManager.createNamedQuery(queryName(finder));
		for (int i = 1; i <= args.length; i++) {
			query.setParameter(i, args[i - 1]);
		}
		return query.getResultList();
	}

	protected String queryName(String query) {
		return getPersistentClass().getSimpleName() + "." + query;
	}

	public int count(ModelPattern filter) {
		JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(
				getPersistentClass(), entityManager);
		getPatternParser().createCountQuery(criteriaBuilder, filter);
		return ((Number) criteriaBuilder.getQuery().getSingleResult())
				.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<T> find(ModelPattern filter) {
		JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(
				getPersistentClass(), entityManager);
		getPatternParser().createQuery(criteriaBuilder, filter);
		return criteriaBuilder.getQuery().getResultList();
	}

	public List<T> queryList(String jpql, Object... params) {
		TypedQuery<T> query =  this.entityManager.createQuery(jpql, getPersistentClass());
	      int index = 0;
	      for (Object param : params) query.setParameter(++index, param);
	      return query.getResultList();
	}

	public List<T> queryListPaged(String jpql, int first, int max,
			Object... params) {
		 TypedQuery<T> query =  this.entityManager.createQuery(jpql, getPersistentClass());
	        int index = 0;
	        for (Object param : params) query.setParameter(++index, param);
	        query.setFirstResult(first);
	        query.setMaxResults(max);
	        return query.getResultList();
	}

	public T querySingle(String jpql, Object... params) {
		TypedQuery<T> query =  this.entityManager.createQuery(jpql, getPersistentClass());
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

	public T merge(T entity) {
		T merged = this.entityManager.merge(entity);
	    this.entityManager.flush();
	    return merged;
	}

}
