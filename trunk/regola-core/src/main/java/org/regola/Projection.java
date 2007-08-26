package org.regola;

public interface Projection {

	public interface Builder {

		void setRowCount();

	}

	public void dispatch(Builder builder);

}
