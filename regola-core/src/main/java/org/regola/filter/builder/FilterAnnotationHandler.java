package org.regola.filter.builder;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.filter.ModelFilter;
import org.regola.filter.criteria.Criteria;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

public abstract class FilterAnnotationHandler {

	public abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter);

	protected Object getFilterValue(PropertyDescriptor property, ModelFilter filter) {
		Object value = new BeanWrapperImpl(filter).getPropertyValue(property
				.getName());
		return value;
	}

	protected boolean isSet(Object value)
	{
		if(value instanceof String)
			return StringUtils.hasLength((String) value);
		else
			return value != null;
	}
}
