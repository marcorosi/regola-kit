package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractCriteriaAnnotationHandler;

public class GreaterThanHandler extends AbstractCriteriaAnnotationHandler {

	public GreaterThanHandler() {
		super(GreaterThan.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((GreaterThan) annotation).value();
	}

	@Override
	protected void handleCriterion(Annotation annotation, String propertyPath,
			Object criterionValue, Criteria criteria) {
		criteria.add(Restrictions.gt(propertyPath, criterionValue));
	}
}
