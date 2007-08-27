package org.regola.filter;

import org.regola.filter.criteria.Criteria;

public interface FilterBuilder {

	void createFilter(Criteria criteria, ModelFilter modelFilter);

	void createCountFilter(Criteria criteria, ModelFilter modelFilter);

}
