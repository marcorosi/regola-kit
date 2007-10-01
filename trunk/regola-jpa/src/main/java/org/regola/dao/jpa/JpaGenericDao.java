package org.regola.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.regola.dao.impl.AbstractGenericDao;
import org.regola.filter.criteria.jpa.JpaCriteria;
import org.regola.model.ModelPattern;
import org.springframework.stereotype.Repository;

@Repository
public class JpaGenericDao<T, ID extends Serializable> extends
		AbstractGenericDao<T, ID> {

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
	public List<T> find(String finder, Object... args) {
		Query query = entityManager.createNamedQuery(queryName(finder));
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return query.getResultList();
	}

	protected String queryName(String query) {
		return getPersistentClass().getSimpleName() + "." + query;
	}

	public int count(ModelPattern filter) {
		JpaCriteria criteriaBuilder = new JpaCriteria(getPersistentClass(),
				entityManager);
		getFilterBuilder().createCountQuery(criteriaBuilder, filter);
		return ((Number) criteriaBuilder.getQuery().getSingleResult())
				.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<T> find(ModelPattern filter) {
		JpaCriteria criteriaBuilder = new JpaCriteria(getPersistentClass(),
				entityManager);
		getFilterBuilder().createQuery(criteriaBuilder, filter);
		return criteriaBuilder.getQuery().getResultList();
	}

}
