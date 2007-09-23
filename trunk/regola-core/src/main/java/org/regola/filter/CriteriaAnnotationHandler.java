package org.regola.filter;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.regola.filter.criteria.Criteria;
import org.regola.model.ModelPattern;

public interface CriteriaAnnotationHandler {

	/**
	 * Questo è il metodo principale di CriteriaAnnotationHandler che le
	 * sottoclassi devono implementare. Contiene la logica per la gestione
	 * dell'annotazione, ovvero si occupa di configurare opportunamento criteria
	 * in base alle informazioni contenute in filter.
	 * 
	 * @param annotation
	 *            l'annotazione da gestire
	 * @param property
	 *            la proprietà di filter annotata con annotation
	 * @param filter
	 *            l'istanza del filtro da cui leggere i valori impostati dal
	 *            client
	 * @param criteria
	 *            l'oggetto che un handler dovrà configurare in base a filter
	 */
	public abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, ModelPattern filter, Criteria criteria);

}