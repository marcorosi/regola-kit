package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.NotEquals;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractFilterAnnotationHandler;

public class NotEqualsHandler extends AbstractFilterAnnotationHandler {

	public NotEqualsHandler() {
		super(NotEquals.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((NotEquals) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria.add(Restrictions.ne(propertyPath, filterValue));
	}

}