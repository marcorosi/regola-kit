package org.regola.batch.skip;

import java.io.Serializable;

import org.regola.batch.JobContext;
import org.regola.batch.policy.SkipPolicy;

public class SkipAll<T extends Serializable> implements SkipPolicy<T> {

	public boolean skipBeforeProcessing(JobContext<T> context) {
		return true;
	}

	public boolean skipRemaining(JobContext<T> context) {
		return true;
	}

	public boolean onErrorSkipRemaining(JobContext<T> context, Exception e) {
		return true;
	}
}