package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;

public class GreaterThanHandler extends AbstractFilterAnnotationHandler {

	public GreaterThanHandler() {
		super(GreaterThan.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((GreaterThan) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria.add(Restrictions.gt(propertyPath, filterValue));
	}
}
