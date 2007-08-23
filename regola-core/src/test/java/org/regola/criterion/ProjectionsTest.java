package org.regola.criterion;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.regola.Projection;

public class ProjectionsTest {

	@Test
	public void rowCount() {
		assertSame(Projections.rowCount(), Projection.ROW_COUNT);
	}
}
