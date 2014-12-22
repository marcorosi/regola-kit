package org.regola.batch.run;

import java.io.Serializable;

import org.regola.batch.JobContext;
import org.regola.batch.policy.RunPolicy;

public class RunAlways<T extends Serializable> implements RunPolicy<T> {

	public boolean isRunnable(JobContext<T> context) {
		return true;
	}
}