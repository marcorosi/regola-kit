package org.regola.criterion;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.regola.Projection;

public class ProjectionsTest {

	@Test
	public void rowCount() {
		Projection rowCount = Projections.rowCount();
		
		assertNotNull(rowCount);
		assertTrue(RowCountProjection.class.isAssignableFrom(rowCount
				.getClass()));
	}
}
