package org.regola.batch.commit;

import java.io.Serializable;

import org.regola.batch.JobConfig;
import org.regola.batch.JobContext;
import org.regola.batch.policy.CommitPolicy;

/**
 * La politica di commit di default si basa sul commit interval definito
 * nella {@link JobConfig}.
 * 
 * @see JobConfig#DEFAULT_COMMIT_INTERVAL
 * @see JobConfig#getCommitInterval()
 */
public class DefaultCommitPolicy<T extends Serializable> implements
		CommitPolicy<T> {

	private final int commitInterval;
	private int commitCounter = 0;

	public DefaultCommitPolicy() {
		this(JobConfig.DEFAULT_COMMIT_INTERVAL);
	}

	public DefaultCommitPolicy(final int commitInterval) {
		this.commitInterval = commitInterval;
	}

	public boolean commitQueued(final JobContext<T> context) {
		commitCounter++;
		if (commitCounter >= commitInterval) {
			commitCounter = 0;
			return true;
		}
		return false;
	}
}