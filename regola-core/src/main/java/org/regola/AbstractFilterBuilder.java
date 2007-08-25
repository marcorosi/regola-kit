package org.regola;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.regola.annotation.FilterAnnotationHandler;
import org.regola.annotation.FilterHandler;
import org.regola.criterion.Projections;
import org.springframework.beans.BeanUtils;

public abstract class AbstractFilterBuilder implements FilterBuilder {

	private Map<Class<Annotation>, FilterAnnotationHandler> handlers = new HashMap<Class<Annotation>, FilterAnnotationHandler>();

	public void createCountFilter(Criteria criteria, ModelFilter filter) {
		createFilter(criteria, filter);
		criteria.setProjection(Projections.rowCount());
	}

	public void createFilter(Criteria criteria, ModelFilter filter) {
		for (PropertyDescriptor property : BeanUtils
				.getPropertyDescriptors(filter.getClass())) {
			handleFieldAnnotations(property, criteria, filter);
			handleMethodAnnotations(property, criteria, filter);
		}
	}

	protected void handleFieldAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelFilter filter) {
		try {
			Field field = filter.getClass().getField(property.getName());
			handleAnnotations(field.getAnnotations(), property, criteria,
					filter);
		} catch (NoSuchFieldException e) {
			// Eccezione ignorata, il campo potrebbe non essere presente
		}
	}

	protected void handleMethodAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelFilter filter) {
		Method method = property.getReadMethod();
		if (method != null) {
			handleAnnotations(method.getAnnotations(), property, criteria,
					filter);
		}
	}

	protected void handleAnnotations(Annotation[] annotations,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(
					FilterHandler.class)) {
				handleAnnotation(annotation, property, criteria, filter);
			}
		}
	}

	protected abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter);

	public Map<Class<Annotation>, FilterAnnotationHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(
			Map<Class<Annotation>, FilterAnnotationHandler> handlers) {
		this.handlers = handlers;
	}

}
