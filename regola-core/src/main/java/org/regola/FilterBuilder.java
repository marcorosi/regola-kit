package org.regola;

public interface FilterBuilder {

	void createFilter(Criteria criteria, ModelFilter modelFilter);

	void createCountFilter(Criteria criteria, ModelFilter modelFilter);

}
