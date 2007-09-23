package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.LessThan;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractCriteriaAnnotationHandler;

public class LessThanHandler extends AbstractCriteriaAnnotationHandler {

	public LessThanHandler() {
		super(LessThan.class);
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((LessThan) annotation).value();
	}

	@Override
	protected void handleCriterion(Annotation annotation, String propertyPath,
			Object criterionValue, Criteria criteria) {
		criteria.add(Restrictions.lt(propertyPath, criterionValue));
	}
}
