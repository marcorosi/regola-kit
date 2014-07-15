package org.regola.filter.handler;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import org.regola.filter.annotation.NotIn;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.model.ModelPattern;

public class NotInHandler extends AbstractAnnotationHandler {

	public NotInHandler() {
		super(NotIn.class);
	}

	@Override
	protected Object getPropertyValue(PropertyDescriptor property,
			ModelPattern modelPattern) {
		Object value = super.getPropertyValue(property, modelPattern);
		if (value != null && value.getClass().isArray()) {
			return Arrays.asList((Object[]) value);
		} else {
			return value;
		}
	}

	@Override
	protected void checkValue(Object criterionValue) {
		if (!(criterionValue instanceof Collection)) {
			throw new IllegalArgumentException(
					"L'annotazione @NotIn è applicabile soltanto a valori di tipo Collection o Array");
		}
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((NotIn) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria.add(Restrictions.notIn(propertyPath, (Collection<?>) filterValue));
	}
}
