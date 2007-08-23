package org.regola.criterion;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderTest {

	@Test
	public void defaultOrdering() {
		assertTrue(new Order("property").isAscending());
	}

}
