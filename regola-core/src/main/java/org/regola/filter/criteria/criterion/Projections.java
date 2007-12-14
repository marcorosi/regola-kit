package org.regola.filter.criteria.criterion;

import org.regola.filter.criteria.Projection;

public class Projections {

	public static Projection rowCount() {
		return new RowCountProjection();
	}

}
