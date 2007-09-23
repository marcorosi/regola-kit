package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.NotEquals;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractCriteriaAnnotationHandler;

public class NotEqualsHandler extends AbstractCriteriaAnnotationHandler {

	public NotEqualsHandler() {
		super(NotEquals.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((NotEquals) annotation).value();
	}

	@Override
	protected void handleCriterion(Annotation annotation, String propertyPath,
			Object criterionValue, Criteria criteria) {
		criteria.add(Restrictions.ne(propertyPath, criterionValue));
	}

}