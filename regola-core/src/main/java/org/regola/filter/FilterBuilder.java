package org.regola.filter;

import org.regola.filter.criteria.Criteria;

/**
 * FilterBuilder è l'interfaccia Builder nel pattern omonimo [GoF95].
 * Si occupa di configurare l'oggetto del tipo Criteria passato ai suoi
 * metodi in modo che rispecchiono il contenuto di un'istanza di ModelFilter.
 * 
 * @author nicola
 *
 */
public interface FilterBuilder {

	/**
	 * Configura criteria in base al contenuto di modelFilter in
	 * modo che possa essere utilizzato successivamente per effettuare 
	 * una selezione (filtraggio) di oggetti del modello.
	 * 
	 * @param criteria l'instanza da configurare
	 * @param modelFilter  contiente le informazioni per popolare criteria
	 */
	void createFilter(Criteria criteria, ModelFilter modelFilter);

	/**
	 * Configura criteria in base al contenuto di modelFilter in
	 * modo che possa essere utilizzato successivamente per determinare 
	 * il numero di elementi di una selezione
	 * (filtraggio) di oggetti del modello.
	 * 
	 * @param criteria l'instanza da configurare
	 * @param modelFilter  contiente le informazioni per popolare criteria
	 */
	void createCountFilter(Criteria criteria, ModelFilter modelFilter);

}
