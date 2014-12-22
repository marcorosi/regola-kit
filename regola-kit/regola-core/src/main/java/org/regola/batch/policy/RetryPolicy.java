package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.JobContext;

/**
 * Politica di rielaborazione di un elemento in errore.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public interface RetryPolicy<T extends Serializable> {

	/**
	 * Deve ritentare l'elaborazione di un elemento che ha dato precedentemente
	 * errore?
	 * 
	 * @param e
	 *            errore che si è verificato durante l'elaborazione
	 *            dell'elemento corrente.
	 * @return true se deve ritentare
	 */
	boolean retryOnError(final JobContext<T> context, final RuntimeException e);

	/**
	 * Invocato subito prima di eseguire un nuovo tentativo di elaborazione.
	 */
	void onRetrying(final JobContext<T> context);

}