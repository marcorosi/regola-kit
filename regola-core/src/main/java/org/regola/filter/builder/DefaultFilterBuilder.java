package org.regola.filter.builder;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.regola.filter.ModelFilter;
import org.regola.filter.criteria.Criteria;

public class DefaultFilterBuilder extends AbstractFilterBuilder {

	private Map<Class<Annotation>, FilterAnnotationHandler> handlers = new HashMap<Class<Annotation>, FilterAnnotationHandler>();

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

	public Map<Class<Annotation>, FilterAnnotationHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(
			Map<Class<Annotation>, FilterAnnotationHandler> handlers) {
		this.handlers = handlers;
	}

}
