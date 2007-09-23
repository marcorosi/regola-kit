package org.regola.filter.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.regola.filter.ModelPatternParser;
import org.regola.filter.annotation.ModelPatternCriterion;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.criterion.Projections;
import org.regola.model.ModelPattern;
import org.springframework.beans.BeanUtils;

public abstract class AbstractModelPatternParser implements ModelPatternParser {

	public void createCountQuery(Criteria criteria, ModelPattern filter) {
		createQuery(criteria, filter);
		criteria.setProjection(Projections.rowCount());
	}

	public void createQuery(Criteria criteria, ModelPattern filter) {
		for (PropertyDescriptor property : BeanUtils
				.getPropertyDescriptors(filter.getClass())) {
			handleFieldAnnotations(property, criteria, filter);
			handleMethodAnnotations(property, criteria, filter);
		}
	}

	protected void handleFieldAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelPattern filter) {
		try {
			Field field = filter.getClass().getDeclaredField(property.getName());
			handleAnnotations(field.getAnnotations(), property, criteria,
					filter);
		} catch (NoSuchFieldException e) {
			// Eccezione ignorata, il campo potrebbe non essere presente
		}
	}

	protected void handleMethodAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelPattern filter) {
		Method method = property.getReadMethod();
		if (method != null) {
			handleAnnotations(method.getAnnotations(), property, criteria,
					filter);
		}
	}

	protected void handleAnnotations(Annotation[] annotations,
			PropertyDescriptor property, Criteria criteria, ModelPattern filter) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(
					ModelPatternCriterion.class)) {
				handleAnnotation(annotation, property, criteria, filter);
			}
		}
	}

	protected abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelPattern filter);

}
