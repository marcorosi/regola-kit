package org.regola.filter.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.regola.filter.CriteriaAnnotationHandler;
import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.In;
import org.regola.filter.annotation.LessThan;
import org.regola.filter.annotation.Like;
import org.regola.filter.annotation.ModelPatternCriterion;
import org.regola.filter.annotation.NotEquals;
import org.regola.filter.criteria.Criteria;
import org.regola.filter.handler.EqualsHandler;
import org.regola.filter.handler.GreaterThanHandler;
import org.regola.filter.handler.InHandler;
import org.regola.filter.handler.LessThanHandler;
import org.regola.filter.handler.LikeHandler;
import org.regola.filter.handler.NotEqualsHandler;
import org.regola.model.ModelPattern;

public class DefaultModelPatternParser extends AbstractModelPatternParser {

	private Map<Class<? extends Annotation>, AbstractCriteriaAnnotationHandler> handlers = new HashMap<Class<? extends Annotation>, AbstractCriteriaAnnotationHandler>();

	public DefaultModelPatternParser() {
		addHandler(Equals.class, new EqualsHandler());
		addHandler(NotEquals.class, new NotEqualsHandler());
		addHandler(GreaterThan.class, new GreaterThanHandler());
		addHandler(LessThan.class, new LessThanHandler());
		addHandler(Like.class, new LikeHandler());
		addHandler(In.class, new InHandler());
	}

	private void addHandler(Class<? extends Annotation> annotation,
			AbstractCriteriaAnnotationHandler handler) {
		if (!annotation.isAnnotationPresent(ModelPatternCriterion.class)) {
			throw new IllegalArgumentException(
					"L'annotazione indicata ["
							+ annotation
							+ "] deve essere marcata come criterio di pattern tramite l'annotazione "
							+ ModelPatternCriterion.class.getName());
		}
		handlers.put(annotation, handler);
	}

	@Override
	protected void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelPattern filter) {

		CriteriaAnnotationHandler handler = handlers.get(annotation.annotationType());
		if (handler == null) {
			throw new RuntimeException(
					"Annotazione ["
							+ annotation.getClass()
							+ "] non gestita: aggiungere l'handler specifico fra quelli registrati nella proprietà 'handlers'");
		}

		handler.handleAnnotation(annotation, property, filter, criteria);
	}

	public Map<Class<? extends Annotation>, AbstractCriteriaAnnotationHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(
			Map<Class<? extends Annotation>, AbstractCriteriaAnnotationHandler> handlers) {
		this.handlers = handlers;
	}

}
