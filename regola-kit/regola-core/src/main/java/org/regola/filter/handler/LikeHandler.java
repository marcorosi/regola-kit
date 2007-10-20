package org.regola.filter.handler;

import java.lang.annotation.Annotation;

import org.regola.filter.annotation.Like;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;

public class LikeHandler extends AbstractFilterAnnotationHandler {

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
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		if (((Like) annotation).caseSensitive()) {
			criteria.add(Restrictions.like(propertyPath, (String) filterValue));
		} else {
			criteria
					.add(Restrictions.ilike(propertyPath, (String) filterValue));
		}
	}
}