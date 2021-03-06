package org.regola.filter.criteria.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.bzdyl.ejb3.criteria.CriteriaFactory;
import net.bzdyl.ejb3.criteria.projections.Projections;
import net.bzdyl.ejb3.criteria.restrictions.Restrictions;

import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractCriteriaBuilder;

public class JpaCriteria extends AbstractCriteriaBuilder {

	private net.bzdyl.ejb3.criteria.Criteria criteria;
	private EntityManager entityManager;
	private int firstResult = -1;
	private int maxResults = -1;

	public JpaCriteria(Class<?> persistentClass, EntityManager entityManager) {
		this.entityManager = entityManager;
		this.criteria = CriteriaFactory.createCriteria(persistentClass
				.getSimpleName());
	}

	@Override
	public void addEquals(String property, Object value) {
		criteria.add(Restrictions.eq(property, value));
	}

	@Override
	public void addNotEquals(String property, Object value) {
		criteria.add(Restrictions.ne(property, value));
	}

	@Override
	public void addGreaterThan(String property, Object value) {
		criteria.add(Restrictions.gt(property, value));
	}

	@Override
	public void addLessThan(String property, Object value) {
		criteria.add(Restrictions.lt(property, value));
	}

	@Override
	public void addLike(String property, String value) {
		criteria.add(Restrictions.like(property, value + "%"));
	}

	@Override
	public void addIlike(String property, String value) {
		criteria.add(Restrictions.like(property, value + "%").ignoreCase());
	}

	@Override
	public void addGreaterEquals(String property, Object value) {
		criteria.add(Restrictions.ge(property, value));
	}

	@Override
	public void addLessEquals(String property, Object value) {
		criteria.add(Restrictions.le(property, value));
	}

	@Override
	public void addIn(String property, Collection<?> value) {
		criteria.add(Restrictions.in(property, value.toArray()));
	}
	
	@Override
	public void addIsNull(String property) {
		throw new IllegalArgumentException("Is Null non disponibile tra i criteria JPA.");
	}	
	
	@Override
	public void addIsNotNull(String property) {
		throw new IllegalArgumentException("Is Not Null non disponibile tra i criteria JPA.");
	}			

	@Override
	public Criteria addOrder(Order order) {
		if (order.isAscending()) {
			criteria.addOrder(net.bzdyl.ejb3.criteria.Order.asc(order
					.getPropertyName()));
		} else {
			criteria.addOrder(net.bzdyl.ejb3.criteria.Order.desc(order
					.getPropertyName()));
		}
		return this;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	@Override
	public void setRowCount() {
		criteria.setProjection(Projections.rowCount());
	}

	public Query getQuery() {
		Query query = criteria.prepareQuery(entityManager);

		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}

		return query;
	}

	@Override
	public void addNotIn(String property, Collection<?> value) {
		throw new IllegalArgumentException("Not In non disponibile tra i criteria JPA.");
		
	}

}
