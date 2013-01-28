package org.regola.batch.policy;

import java.io.Serializable;

import org.regola.batch.Job.LockPolicy;
import org.regola.batch.JobContext;
import org.regola.lock.LockManager;

/**
 * Politica di lock che delega ad un {@link LockManager}.
 * <p>
 * Il nome delle risorse da bloccare e del proprietario del lock viene ricavato
 * dai seguenti metodi:
 * <ul>
 * <li>{@link #executionLock(JobContext)}: identificativo dell'esecuzione dal
 * contesto;
 * <li>{@link #itemLock(Serializable)}: identificativo dell'elemento
 * dall'istanza dello stesso;
 * <li>{@link #lockOwner(JobContext)}: nome del proprietario dal contesto.
 * </ul>
 */
public class LockManagerPolicy<T extends Serializable> implements LockPolicy<T> {

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