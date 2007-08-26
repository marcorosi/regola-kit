package org.regola;

import java.util.Collection;

import org.regola.criterion.Order;

public abstract class AbstractCriteriaBuilder implements Criteria,
		Criterion.Builder, Projection.Builder {

	public Criteria add(Criterion criterion) {
		criterion.getOperator().dispatch(this, criterion.getProperty(),
				criterion.getValue());
		return this;
	}

	public abstract Criteria addOrder(Order order);

	public abstract Criteria setFirstResult(int firstResult);

	public abstract Criteria setMaxResults(int maxResults);

	public Criteria setProjection(Projection projection) {
		projection.dispatch(this);
		return this;
	}

	public abstract void addEquals(String property, Object value);

	public abstract void addNotEquals(String property, Object value);

	public abstract void addGreaterThan(String property, Object value);

	public abstract void addLessThan(String property, Object value);

	public abstract void addGreaterEquals(String property, Object value);

	public abstract void addLessEquals(String property, Object value);

	public abstract void addLike(String property, String value);

	public abstract void addIlike(String property, String value);

	public abstract void addIn(String property, Collection<?> value);

	public abstract void setRowCount();

}
