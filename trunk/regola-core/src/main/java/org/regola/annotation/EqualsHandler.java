package org.regola.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.Criteria;
import org.regola.ModelFilter;
import org.regola.criterion.Restrictions;
import org.springframework.beans.BeanWrapperImpl;

public class EqualsHandler extends FilterAnnotationHandler {

	@Override
	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {
		Equals equals = (Equals) annotation;
		Object value = new BeanWrapperImpl(filter).getPropertyValue(property
				.getName());
		criteria.add(Restrictions.eq(equals.value(), value));
	}

}
