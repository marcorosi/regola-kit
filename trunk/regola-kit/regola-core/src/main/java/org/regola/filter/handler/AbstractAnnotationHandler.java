package org.regola.filter.handler;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collection;

import org.regola.filter.FilterAnnotationHandler;
import org.regola.filter.criteria.Criteria;
import org.regola.model.ModelPattern;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

/**
 * Fornisce lo scheletro di implementazione per gli handler (gestori) per le
 * annotazioni delle propriet&agrave; di {@link ModelPattern}. <br>
 * Basata sul pattern Template Method [GoF], i tre metodi da implementare nella
 * classi derivate sono:
 * <ul>
 * <li>{@link #checkAnnotation(Annotation)}: (facoltativo) verifica che
 * l'annotazione sia del tipo previsto,
 * <li>{@link #getPropertyPath(Annotation)}: (facoltativo) restituisce la
 * propriet&agrave; della classe di dominio alla quale si applica questo filtro,
 * <li>{@link #handleFilter(Annotation, String, Object, Criteria)}: la logica
 * vera e propria di gestione del filtro.
 * </ul>
 * Partecipa insieme a {@link org.regola.filter.ModelPatternParser} al pattern
 * Builder [GoF] nel ruolo di <i>director</i> su di un query builder concreto (<i>builder</i>).
 * 
 */
public abstract class AbstractAnnotationHandler implements
		FilterAnnotationHandler {

	protected Class<? extends Annotation> handledAnnotation;

	protected AbstractAnnotationHandler(
			Class<? extends Annotation> handledAnnotation) {
		this.handledAnnotation = handledAnnotation;
	}

	public void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, ModelPattern modelPattern,
			Criteria criteria) {

		checkAnnotation(annotation);

		Object filterValue = getPropertyValue(property, modelPattern);

		if (isEmpty(filterValue)) {
			return;
		}

		checkValue(filterValue);

		// if the value attribute is not specified we use the same name
		// property of the filter
		String annotationPropertyPath = getPropertyPath(annotation);
		String propertyPath = StringUtils.hasLength(annotationPropertyPath) ? annotationPropertyPath
				: property.getName();

		handleFilter(annotation, propertyPath, filterValue, criteria);
	}

	protected void checkAnnotation(Annotation annotation) {
		if (!(handledAnnotation.equals(annotation.annotationType()))) {
			throwInvalidAnnotation(handledAnnotation);
		}
	}

	/**
	 * Metodo di utilit&agrave; per recuperare tramite introspezione una
	 * propriet&agrave; da un oggetto di tipo {@link ModelPattern}
	 * 
	 * @param property
	 *            la propriet&agrave; da leggere
	 * @param modelPattern
	 *            l'oggetto di cui leggere la propriet&agrave;
	 * @return il valore della propriet&agrave; richiesta
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

	protected void checkValue(Object filterValue) {
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
	 * Metodo hook per la logica di gestione del filtro. <br/> Vengono passati
	 * annotazione, property path e valore del filtro gi&agrave; verificati,
	 * insieme all'istanza di criteria su cui operare.
	 * 
	 * @param annotation
	 * @param propertyPath
	 * @param filterValue
	 * @param criteria
	 */
	protected abstract void handleFilter(Annotation annotation,
			String propertyPath, Object filterValue, Criteria criteria);
}
