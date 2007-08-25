package org.regola.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.Criteria;
import org.regola.ModelFilter;
import org.regola.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class LessThanHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {

		Object value = getFilterValue(property, filter);
		if(!isSet(value))
			return;
		
		LessThan concreteAnnotation = (LessThan) annotation;
		// if the value attribute is not specified we use the same name
		// property of the filter
		String propertyPath = StringUtils.hasLength(concreteAnnotation.value()) ? concreteAnnotation
				.value()
				: property.getName();
		criteria.add(Restrictions.lt(propertyPath, value));
	}
}
