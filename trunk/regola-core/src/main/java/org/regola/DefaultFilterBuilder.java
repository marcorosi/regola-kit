package org.regola;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.annotation.FilterHandler;

public class DefaultFilterBuilder extends AbstractFilterBuilder {

	@Override
	protected void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {
		FilterHandler handler = annotation.annotationType().getAnnotation(
				FilterHandler.class);
		try {
			handler.value().newInstance().handleAnnotation(annotation,
					property, criteria, filter);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
