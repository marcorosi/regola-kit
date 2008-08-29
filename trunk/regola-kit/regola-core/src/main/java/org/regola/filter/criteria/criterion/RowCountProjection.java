package org.regola.filter.criteria.criterion;

import org.regola.filter.criteria.Projection;

public class RowCountProjection implements Projection {

	public void dispatch(Builder builder) {
		builder.setRowCount();
	}

}
