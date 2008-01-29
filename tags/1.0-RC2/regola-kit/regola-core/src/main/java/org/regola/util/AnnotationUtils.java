package org.regola.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

public class AnnotationUtils {

	public static Field[] findFieldsByAnnotation(Class<?> clazz,
			final Class<? extends Annotation> annotationClass) {

		final List<Field> fields = new ArrayList<Field>();

		ReflectionUtils.doWithFields(clazz,
				new ReflectionUtils.FieldCallback() {

					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {
						fields.add(field);
					}

				}, new ReflectionUtils.FieldFilter() {

					public boolean matches(Field field) {
						return field.getAnnotation(annotationClass) != null;
					}

				});

		return fields.toArray(new Field[fields.size()]);
	}

	public static Method[] findMethodsByAnnotation(Class<?> clazz,
			final Class<? extends Annotation> annotationClass) {

		final List<Method> methods = new ArrayList<Method>();

		ReflectionUtils.doWithMethods(clazz,
				new ReflectionUtils.MethodCallback() {

					public void doWith(Method method)
							throws IllegalArgumentException,
							IllegalAccessException {
						methods.add(method);
					}

				}, new ReflectionUtils.MethodFilter() {

					public boolean matches(Method method) {
						return method.getAnnotation(annotationClass) != null;
					}

				});

		return methods.toArray(new Method[methods.size()]);
	}
}
