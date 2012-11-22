package org.regola.batch;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.batch.Job.LockPolicy;
import org.regola.lock.LockManager;

public class LockManagerPolicy<T extends Serializable> implements LockPolicy<T> {
	protected final Log LOG = LogFactory.getLog(getClass());

	private final LockManager lockManager;

	public LockManagerPolicy(final LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public boolean acquireExecution(final JobContext<T> context) {
		return lockManager.acquireLock(executionLock(context),
				lockOwner(context));
	}

	public void releaseExecution(final JobContext<T> context) {
		lockManager.releaseLock(executionLock(context), lockOwner(context));
	}

	public boolean acquireItem(final JobContext<T> context) {
		return lockManager.acquireLock(itemLock(context.getCurrentItem()),
				lockOwner(context));
	}

	public void releaseItem(final JobContext<T> context) {
		lockManager.releaseLock(itemLock(context.getCurrentItem()),
				lockOwner(context));
	}

	protected String executionLock(final JobContext<T> context) {
		return "EXECUTION_" + context.getJobName();
	}

	protected String itemLock(final T currentItem) {
		return "ITEM_" + currentItem.toString();
	}

	protected String lockOwner(final JobContext<T> context) {
		return "JOB_" + context + "_" + Thread.currentThread().getId();
	}

}