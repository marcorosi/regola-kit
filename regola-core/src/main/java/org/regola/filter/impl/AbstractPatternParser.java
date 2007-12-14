package org.regola.filter.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.regola.filter.ModelPatternParser;
import org.regola.filter.annotation.ModelPatternFilter;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.criteria.Order;
import org.regola.filter.criteria.criterion.Projections;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.springframework.beans.BeanUtils;

public abstract class AbstractPatternParser implements ModelPatternParser {

	public void createCountQuery(Criteria criteria, ModelPattern pattern) {
		handleFilters(criteria, pattern);

		criteria.setProjection(Projections.rowCount());
	}

	public void createQuery(Criteria criteria, ModelPattern pattern) {
		handleFilters(criteria, pattern);

		// handle paging
		if (pattern.isPagingEnabled()) {
			criteria.setFirstResult(pattern.getCurrentPage()
					* pattern.getPageSize());
			criteria.setMaxResults(pattern.getPageSize());
		}

		// handle order
		for (ModelProperty property : pattern.getSortedProperties()) {
			if (property.isOrderAscending()) {
				criteria.addOrder(Order.asc(property.getName()));
			} else {
				criteria.addOrder(Order.desc(property.getName()));
			}
		}
	}

	protected void handleFilters(Criteria criteria, ModelPattern pattern) {
		for (PropertyDescriptor property : BeanUtils
				.getPropertyDescriptors(pattern.getClass())) {
			handleFieldAnnotations(property, criteria, pattern);
			handleMethodAnnotations(property, criteria, pattern);
		}
	}

	protected void handleFieldAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelPattern pattern) {
		try {
			Field field = pattern.getClass().getDeclaredField(
					property.getName());
			handleAnnotations(field.getAnnotations(), property, criteria,
					pattern);
		} catch (NoSuchFieldException e) {
			// Eccezione ignorata, il campo potrebbe non essere presente
		}
	}

	protected void handleMethodAnnotations(PropertyDescriptor property,
			Criteria criteria, ModelPattern pattern) {
		Method method = property.getReadMethod();
		if (method != null) {
			handleAnnotations(method.getAnnotations(), property, criteria,
					pattern);
		}
	}

	protected void handleAnnotations(Annotation[] annotations,
			PropertyDescriptor property, Criteria criteria, ModelPattern pattern) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(
					ModelPatternFilter.class)) {
				handleAnnotation(annotation, property, criteria, pattern);
			}
		}
	}

	protected abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelPattern pattern);

}
