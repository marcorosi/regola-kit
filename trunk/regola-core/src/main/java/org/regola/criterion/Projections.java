package org.regola.criterion;

import org.regola.Projection;

public class Projections {

	public static Projection rowCount() {
		return new RowCountProjection();
	}

}
