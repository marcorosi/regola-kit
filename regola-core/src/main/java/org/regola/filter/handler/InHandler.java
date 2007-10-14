package org.regola.filter.handler;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import org.regola.filter.annotation.In;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.regola.model.ModelPattern;

public class InHandler extends AbstractFilterAnnotationHandler {

	public InHandler() {
		super(In.class);
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
					"L'annotazione @In è applicabile soltanto a valori di tipo Collection o Array");
		}
	}

	@Override
	protected String getPropertyPath(Annotation annotation) {
		return ((In) annotation).value();
	}

	@Override
	protected void handleFilter(Annotation annotation, String propertyPath,
			Object filterValue, Criteria criteria) {
		criteria
				.add(Restrictions.in(propertyPath, (Collection<?>) filterValue));
	}
}
