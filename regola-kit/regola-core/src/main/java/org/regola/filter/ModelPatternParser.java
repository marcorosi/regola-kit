package org.regola.filter;

import org.regola.filter.criteria.Criteria;
import org.regola.model.ModelPattern;

/**
 * Coordinare la costruzione di una query attraverso l'astrazione
 * {@link Criteria} passata ai suoi metodi in modo che rispecchi i criteri di
 * filtro e ordinamento contenuti in un'istanza di {@link ModelPattern}.
 * <p>
 * Questa classe ricopre il ruolo di <i>director</i> nel pattern Builder [GoF],
 * mentre l'istanza di {@link Criteria} rappresenta il <i>builder</i> che crea
 * effettivamente un prodotto specifico, che sarà in genere una query
 * nell'implementazione concreta del framework di persistenza.
 */
public interface ModelPatternParser {

	/**
	 * Configura criteria in base al contenuto di modelFilter in modo che possa
	 * essere utilizzato successivamente per effettuare una selezione
	 * (filtraggio) di oggetti del modello.
	 * 
	 * @param criteria
	 *            l'instanza da configurare
	 * @param modelPattern
	 *            contiente le informazioni per popolare criteria
	 */
	void createQuery(Criteria criteria, ModelPattern modelPattern);

	/**
	 * Configura criteria in base al contenuto di modelFilter in modo che possa
	 * essere utilizzato successivamente per determinare il numero di elementi
	 * di una selezione (filtraggio) di oggetti del modello.
	 * 
	 * @param criteria
	 *            l'instanza da configurare
	 * @param modelPattern
	 *            contiente le informazioni per popolare criteria
	 */
	void createCountQuery(Criteria criteria, ModelPattern modelPattern);

}
