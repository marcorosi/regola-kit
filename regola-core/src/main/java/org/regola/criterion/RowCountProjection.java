package org.regola.criterion;

import org.regola.Projection;

public class RowCountProjection implements Projection {

	public void dispatch(Builder builder) {
		builder.setRowCount();
	}

}
