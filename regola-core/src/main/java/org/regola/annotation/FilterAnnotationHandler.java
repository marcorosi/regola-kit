package org.regola.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.Criteria;
import org.regola.ModelFilter;

public abstract class FilterAnnotationHandler {

	public abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter);

}
