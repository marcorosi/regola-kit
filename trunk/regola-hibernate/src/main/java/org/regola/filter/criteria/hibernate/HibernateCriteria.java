package org.regola.filter.criteria.hibernate;

import java.util.Collection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.impl.AbstractCriteriaBuilder;

/**
 * @author nicola
 */
public class HibernateCriteria extends AbstractCriteriaBuilder {

	private org.hibernate.Criteria criteria;

	public HibernateCriteria(org.hibernate.Criteria criteria) {
		this.criteria = criteria;
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
		criteria.add(Restrictions.ilike(property, value + "%"));
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
		criteria.add(Restrictions.in(property, value));
	}

	@Override
	public Criteria addOrder(Order order) {
		if (order.isAscending()) {
			criteria.addOrder(org.hibernate.criterion.Order.asc(order
					.getPropertyName()));
		} else {
			criteria.addOrder(org.hibernate.criterion.Order.desc(order
					.getPropertyName()));
		}
		return this;
	}

	@Override
	public Criteria setFirstResult(int firstResult) {
		criteria.setFirstResult(firstResult);
		return this;
	}

	@Override
	public Criteria setMaxResults(int maxResults) {
		criteria.setMaxResults(maxResults);
		return this;
	}

	@Override
	public void setRowCount() {
		criteria.setProjection(Projections.rowCount());
	}

	public org.hibernate.Criteria getCriteria() {
		return criteria;
	}

}
