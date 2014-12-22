package org.regola.batch.policy;

import java.io.Serializable;
import java.util.List;

import org.regola.batch.JobContext;

/**
 * Politica che indica quando effettuare commit della coda di commit.
 * <p>
 * La coda di commit contiene tutti gli elementi elaborati con successo e non
 * ancora persistiti nello store.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 * @see org.regola.batch.Job#store(List)
 */
public interface CommitPolicy<T extends Serializable> {

	/**
	 * Deve fare commit degli elementi elaborati con successo e non ancora
	 * persistiti?
	 * 
	 * @return true se è tempo di fare commit.
	 * @see org.regola.batch.JobConfig#getCommitInterval()
	 * @see org.regola.batch.JobConfig#DEFAULT_COMMIT_INTERVAL
	 */
	boolean commitQueued(final JobContext<T> context);

}