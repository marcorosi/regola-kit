package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.JobContext;

/**
 * Politica di interruzione dell'elaborazione dell'elemento corrente (prima che
 * venga processato) e/o degli elementi successivi.
 * <p>
 * Nel caso l'elaborazione dell'elemento corrente sia fallita, la logica di
 * riesecuzione o di abbandono dell'elemento in errore viene controllata dalla
 * {@link RetryPolicy}.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public interface SkipPolicy<T extends Serializable> {

	/**
	 * Deve saltare l'elemento corrente?
	 * <p>
	 * NOTA: Questa policy viene verificata PRIMA dell'elaborazione
	 * dell'elemento.
	 * 
	 * @return true se deve saltare l'elaborazione dell'elemento corrente.
	 */
	boolean skipBeforeProcessing(final JobContext<T> context);

	/**
	 * Deve saltare tutti gli elementi successivi a quello corrente?
	 * <p>
	 * NOTA: Questa policy viene verificata DOPO l'elaborazione di un elemento,
	 * nel caso sia stato elaborato con successo oppure sia stato ignorato
	 * (skipped).
	 * 
	 * @return true se si deve interrompere l'elaborazione degli elementi
	 *         successivi.
	 */
	boolean skipRemaining(JobContext<T> context);

	/**
	 * Deve saltare tutti gli elementi successivi a quello corrente?
	 * <p>
	 * Questa policy viene verificata DOPO l'elaborazione di un elemento, nel
	 * caso si sia verificata un'eccezione.
	 * 
	 * @param e
	 *            eccezione verificatasi durante l'elaborazione dell'elemento.
	 * @return true se si deve interrompere l'elaborazione degli elementi
	 *         successivi.
	 */
	boolean onErrorSkipRemaining(JobContext<T> context, Exception e);

}