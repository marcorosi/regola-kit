package org.regola.filter.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.filter.ModelFilter;
import org.regola.filter.builder.FilterAnnotationHandler;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class NotEqualsHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {

		Object value = getFilterValue(property, filter);
		if(!isSet(value))
			return;
		
		NotEquals concreteAnnotation = (NotEquals) annotation;
		// if the value attribute is not specified we use the same name
		// property of the filter
		String propertyPath = StringUtils.hasLength(concreteAnnotation.value()) ? concreteAnnotation
				.value()
				: property.getName();
		criteria.add(Restrictions.ne(propertyPath, value));
	}
}
