package org.regola.filter.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import org.regola.filter.ModelFilter;
import org.regola.filter.builder.FilterAnnotationHandler;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class InHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {

		Object value = getFilterValue(property, filter);
		if (!isSet(value))
			return;

		Collection<?> collectionValue;
		if (Collection.class.isAssignableFrom(value.getClass())) {
			collectionValue = (Collection<?>) value;
		} else if (value.getClass().isArray()) {
			collectionValue = Arrays.asList((Object[]) value);
		} else {
			throw new IllegalArgumentException(
					"L'annotazione @In è applicabile soltanto a valori di tipo Collection o Array");
		}

		In concreteAnnotation = (In) annotation;
		// if the value attribute is not specified we use the same name
		// property of the filter
		String propertyPath = StringUtils.hasLength(concreteAnnotation.value()) ? concreteAnnotation
				.value()
				: property.getName();
		criteria.add(Restrictions.in(propertyPath, collectionValue));
	}
}
