package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.JobContext;

/**
 * Politica di lock delle risorse per l'esecuzione concorrente su più nodi di un
 * cluster.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public interface LockPolicy<T extends Serializable> {

	/**
	 * Richiede un lock globale a livello di job, prerequisito per l'inizio
	 * dell'elaborazione.
	 * 
	 * @return true se il lock è stato creato
	 */
	boolean acquireExecution(final JobContext<T> context);

	/**
	 * Rilascia il lock acquisito per l'esecuzione del job.
	 */
	void releaseExecution(final JobContext<T> context);

	/**
	 * Richiede un lock per l'elemento corrente, prerequisito per l'elaborazione
	 * dello stesso.
	 * 
	 * @return true se il lock è stato creato
	 */
	boolean acquireItem(final JobContext<T> context);

	/**
	 * Rilascia il lock acquisito per l'elaborazione dell'elemento corrente.
	 */
	void releaseItem(final JobContext<T> context);

}