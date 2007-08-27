package org.regola.filter.criteria.criterion;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.regola.filter.criteria.Projection;
import org.regola.filter.criteria.criterion.Projections;
import org.regola.filter.criteria.criterion.RowCountProjection;

public class ProjectionsTest {

	@Test
	public void rowCount() {
		Projection rowCount = Projections.rowCount();
		
		assertNotNull(rowCount);
		assertTrue(RowCountProjection.class.isAssignableFrom(rowCount
				.getClass()));
	}
}
