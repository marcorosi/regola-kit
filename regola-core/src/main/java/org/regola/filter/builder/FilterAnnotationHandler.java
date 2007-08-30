package org.regola.filter.builder;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import org.regola.filter.ModelFilter;
import org.regola.filter.criteria.Criteria;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

/** 
 * FilterAnnotationHandler fornisce i metodi di base per la creazione di handler (gestori) per le annotazioni 
 * delle proprietà di ModelFilter. <br> Basata sul pattern Template Method [GoF96] con un 
 * unico medoto da implementare nella classi di derivate: handleAnnotation() <br> Con riferimento alla classe
 * AbstractFilterBuilder partecipa al pattern Builder [GoF96] nel ruolo di ConcreteBuilder.
 * @author nicola
 */
public abstract class FilterAnnotationHandler {

	/**
	 * Questo è il metodo principale di FilterAnnotationHandler che le sottoclassi
	 * devono implementare. Contiene la logica per la gestione dell'annotazione, 
	 * ovvero si occupa di configurare opportunamento criteria in base
	 * alle informazioni contenute in filter.
	 * @param annotation l'annotazione da gestire
	 * @param property la proprietà di filter annotata con annotation
	 * @param criteria l'oggetto che un handler dovrà configurare in base a filter
	 * @param filter l'istanza del filtro da cui leggere i valori impostati dal client
	 */
	public abstract void handleAnnotation(Annotation annotation,
			PropertyDescriptor property, Criteria criteria, ModelFilter filter);

	/**
	 * Metodo di utilità per recuperare tramite introspezione una proprietà (propriety)
	 * da un oggetto di tipo ModelFilter (filter) 
	 * @param property la proprietà da leggere
	 * @param filter l'oggetto di cui leggere la proprietà
	 * @return il valore della proprietà richiesta
	 */
	protected Object getFilterValue(PropertyDescriptor property, ModelFilter filter) {
		Object value = new BeanWrapperImpl(filter).getPropertyValue(property
				.getName());
		return value;
	}

	/**
	 * Verifica che value sia impostato ad un valore significativo: ad esempio un Integer diverso
	 * da null oppure una stringa diversa da null e non vuota.
	 * @param value
	 * @return true se value contiene un valore significativo
	 */
	protected boolean isSet(Object value)
	{
		if(value instanceof String)
			return StringUtils.hasLength((String) value);
		else
			return value != null;
	}
}
