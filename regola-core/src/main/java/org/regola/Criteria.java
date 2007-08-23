package org.regola;

import org.regola.criterion.Order;

/**
 * Astrazione delle Criteria API di Hibernate per poterle usare con altri fwk di
 * persistenza.
 * 
 */
public interface Criteria {

	Criteria add(Criterion criterion);

	Criteria addOrder(Order order);

	Criteria setFirstResult(int firstResult);

	Criteria setMaxResults(int maxResults);

	Criteria setProjection(Projection projection);

}
