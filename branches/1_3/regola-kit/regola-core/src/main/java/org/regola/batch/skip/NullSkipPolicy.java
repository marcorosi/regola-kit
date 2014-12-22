package org.regola.batch.skip;

import java.io.Serializable;

import org.regola.batch.JobContext;
import org.regola.batch.policy.SkipPolicy;

/**
 * La politica di skip di default non ignora nessun elemento.
 */
public class NullSkipPolicy<T extends Serializable> implements
		SkipPolicy<T> {

	public boolean skipBeforeProcessing(final JobContext<T> context) {
		return false;
	}

	public boolean skipRemaining(JobContext<T> context) {
		return false;
	}

	public boolean onErrorSkipRemaining(JobContext<T> context, Exception e) {
		return false;
	}

}