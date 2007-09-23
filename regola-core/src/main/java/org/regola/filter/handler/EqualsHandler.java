package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.Equals;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractCriteriaAnnotationHandler;

public class EqualsHandler extends AbstractCriteriaAnnotationHandler {

	public EqualsHandler() {
		super(Equals.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((Equals) annotation).value();
	}

	@Override
	protected void handleCriterion(Annotation annotation, String propertyPath,
			Object criterionValue, Criteria criteria) {
		criteria.add(Restrictions.eq(propertyPath, criterionValue));
	}
}
