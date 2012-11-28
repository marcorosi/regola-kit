package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.Job.SkipPolicy;
import org.regola.batch.JobConfig;
import org.regola.batch.JobContext;

public abstract class DisabledJobSkipPolicy<T extends Serializable> implements
		SkipPolicy<T> {

	protected abstract JobConfig getJobConfig();

	public boolean isSatisfiedBy(JobContext<T> context) {
		return !getJobConfig().isEnabled();
	}

	public boolean skipRemaining(JobContext<T> context) {
		return !getJobConfig().isEnabled();
	}

	public boolean skipRemaining(JobContext<T> context, RuntimeException e) {
		return !getJobConfig().isEnabled();
	}

}
