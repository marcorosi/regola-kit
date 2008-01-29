package org.regola.filter.criteria;

public interface Projection {

	public interface Builder {

		void setRowCount();

	}

	public void dispatch(Builder builder);

}
