package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.Like;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.filter.impl.AbstractCriteriaAnnotationHandler;

public class LikeHandler extends AbstractCriteriaAnnotationHandler {

	public LikeHandler() {
		super(Like.class);
	}

	@Override
	protected void checkValue(Object criterionValue) {
		if (!(criterionValue instanceof String)) {
			throw new IllegalArgumentException(
					"L'annotazione @Like è applicabile soltanto a valori di tipo String");
		}
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((Like) annotation).value();
	}

	@Override
	protected void handleCriterion(Annotation annotation, String propertyPath,
			Object criterionValue, Criteria criteria) {
		if (((Like) annotation).caseSensitive()) {
			criteria.add(Restrictions.like(propertyPath,
					(String) criterionValue));
		} else {
			criteria.add(Restrictions.ilike(propertyPath,
					(String) criterionValue));
		}
	}
}