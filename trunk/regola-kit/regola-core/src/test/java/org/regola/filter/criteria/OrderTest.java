package org.regola.filter.criteria;

import static org.junit.Assert.*;

import org.junit.Test;
import org.regola.filter.criteria.Order;

public class OrderTest {

	@Test
	public void defaultOrdering() {
		assertTrue(new Order("property").isAscending());
	}

}
