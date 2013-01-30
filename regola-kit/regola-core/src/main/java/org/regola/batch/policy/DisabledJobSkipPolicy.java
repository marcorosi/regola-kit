package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.Job.SkipPolicy;
import org.regola.batch.JobConfig;
import org.regola.batch.JobContext;

/**
 * Politica di skip che salta l'esecuzione di qualsiasi elemento (corrente e
 * successivi) in base allo stato di abilitazione globale ricavato dalla
 * {@link JobConfig#isEnabled()}.
 * <p>
 * Il controllo dell'abilitazione del job viene eseguito per ogni invocazione
 * dei metodi di questa policy.
 */
public abstract class DisabledJobSkipPolicy<T extends Serializable> implements
		SkipPolicy<T> {

	protected abstract JobConfig getJobConfig();

	public boolean skipBeforeProcessing(JobContext<T> context) {
		return !getJobConfig().isEnabled();
	}

	public boolean skipRemaining(JobContext<T> context) {
		return !getJobConfig().isEnabled();
	}

	public boolean onErrorSkipRemaining(JobContext<T> context, Exception e) {
		return !getJobConfig().isEnabled();
	}

}
