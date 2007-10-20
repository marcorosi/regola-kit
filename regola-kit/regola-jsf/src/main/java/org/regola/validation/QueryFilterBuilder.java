package org.regola.validation;

import org.regola.model.ModelPattern;

public interface QueryFilterBuilder
{
	public void decorate(Object ormQueryBuilder, ModelPattern filter);
	
}
