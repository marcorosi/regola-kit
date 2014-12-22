package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.JobContext;

/**
 * Politica di abilitazione del job: determina se può essere eseguito.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public interface RunPolicy<T extends Serializable> {

	/**
	 * Può essere eseguito in questo contesto?
	 * 
	 * @return true se l'esecuzione è permessa.
	 */
	boolean isRunnable(final JobContext<T> context);

}