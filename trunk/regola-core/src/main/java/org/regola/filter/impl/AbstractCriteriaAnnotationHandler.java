package org.regola.filter.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collection;

import org.regola.filter.CriteriaAnnotationHandler;
import org.regola.filter.ModelPatternParser;
import org.regola.filter.criteria.Criteria;
import org.regola.model.ModelPattern;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

/**
 * Fornisce lo scheletro di implementazione per gli handler (gestori) per le
 * annotazioni delle proprietà di {@link ModelPattern}. <br>
 * Basata sul pattern Template Method [GoF], i tre metodi da implementare nella
 * classi derivate sono:
 * <ul>
 * <li>{@link #checkAnnotation(Annotation)}: (facoltativo) verifica che
 * l'annotazione sia del tipo previsto,
 * <li>{@link #getPropertyPath(Annotation)}: (facoltativo) restituisce la
 * proprietà della clsse di dominio alla quale si applica questo criterio di
 * filtro,
 * <li>{@link #handleCriterion(Annotation, String, Object, Criteria)}: la
 * logica vera e propria di gestione del criterio.
 * </ul>
 * Partecipa insieme a {@link ModelPatternParser} al pattern Builder [GoF] nel
 * ruolo di <i>director</i> su di un query builder concreto (<i>builder</i>).
 * 
 */
public abstract class AbstractCriteriaAnnotationHandler implements
		CriteriaAnnotationHandler {

	protected Class<? extends Annotation> handledAnnotation;

	protected AbstractCriteriaAnnotationHandler(
			Class<? extends Annotation> handledAnnotation) {
		this.handledAnnotation = handledAnnotation;
	}

	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, ModelPattern modelPattern,
			Criteria criteria) {

		checkAnnotation(annotation);

		Object criterionValue = getPropertyValue(property, modelPattern);

		if (isEmpty(criterionValue)) {
			return;
		}

		checkValue(criterionValue);

		// if the value attribute is not specified we use the same name
		// property of the filter
		String annotationPropertyPath = getPropertyPath(annotation);
		String propertyPath = StringUtils.hasLength(annotationPropertyPath) ? annotationPropertyPath
				: property.getName();

		handleCriterion(annotation, propertyPath, criterionValue, criteria);
	}

	protected void checkAnnotation(Annotation annotation) {
		if (!(handledAnnotation.equals(annotation.annotationType()))) {
			throwInvalidAnnotation(handledAnnotation);
		}
	}

	/**
	 * Metodo di utilità per recuperare tramite introspezione una proprietà da
	 * un oggetto di tipo {@link ModelPattern}
	 * 
	 * @param property
	 *            la proprietà da leggere
	 * @param modelPattern
	 *            l'oggetto di cui leggere la proprietà
	 * @return il valore della proprietà richiesta
	 */
	protected Object getPropertyValue(PropertyDescriptor property,
			ModelPattern modelPattern) {
		return new BeanWrapperImpl(modelPattern).getPropertyValue(property
				.getName());
	}

	/**
	 * Verifica che value sia impostato ad un valore significativo: ad esempio
	 * un Integer diverso da null oppure una stringa diversa da null e non
	 * vuota.
	 * 
	 * @param value
	 * @return true se value contiene un valore significativo
	 */
	protected boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String) {
			return ((String) value).length() == 0;
		}
		if (value instanceof Collection) {
			return ((Collection<?>) value).isEmpty();
		}
		if (value.getClass().isArray()) {
			return ((Object[]) value).length == 0;
		}
		return false;
	}

	protected void checkValue(Object criterionValue) {
		return;
	}

	protected void throwInvalidAnnotation(
			Class<? extends Annotation> requiredAnnotation) {
		throw new RuntimeException("Handler [" + getClass()
				+ "] non associato al tipo di annotazione ["
				+ requiredAnnotation.getName() + "]");
	}

	protected String getPropertyPath(Annotation annotation) {
		return null;
	}

	/**
	 * Metodo hook per la logica di gestione del criterio di filtro. <br/>
	 * Vengono passati annotazione, property path e valore del filtro già
	 * verificati, insieme all'istanza di criteria su cui operare.
	 * 
	 * @param annotation
	 * @param propertyPath
	 * @param criterionValue
	 * @param criteria
	 */
	protected abstract void handleCriterion(Annotation annotation,
			String propertyPath, Object criterionValue, Criteria criteria);
}
