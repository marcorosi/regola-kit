package org.regola.batch.lock;

import java.io.Serializable;

import org.regola.batch.JobContext;
import org.regola.batch.policy.LockPolicy;
import org.regola.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

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

	private static final Logger LOG = LoggerFactory
			.getLogger(LockManagerPolicy.class);

	private final LockManager lockManager;

	public LockManagerPolicy(final LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public boolean acquireExecution(final JobContext<T> context) {
		final String executionLock = executionLock(context);
		final String lockOwner = lockOwner(context);
		try {
			lockManager.acquireLock(executionLock, lockOwner);
			return true;
		} catch (DataAccessException e) {
			LOG.warn("Impossibile acquisire il lock [{}] per [{}]: {}",
					new Object[] { executionLock, lockOwner, e.getMessage() });
		}
		return false;
	}

	public void releaseExecution(final JobContext<T> context) {
		lockManager.releaseLock(executionLock(context), lockOwner(context));
	}

	public boolean acquireItem(final JobContext<T> context) {
		final String itemLock = itemLock(context.getCurrentItem());
		final String lockOwner = lockOwner(context);
		try {
			lockManager.acquireLock(itemLock, lockOwner);
			return true;
		} catch (DataIntegrityViolationException e) {
			LOG.warn("Impossibile acquisire il lock [{}] per [{}]: {}",
					new Object[] { itemLock, lockOwner, e.getMessage() });
		}
		return false;
	}

	public void releaseItem(final JobContext<T> context) {
		lockManager.releaseLock(itemLock(context.getCurrentItem()),
				lockOwner(context));
	}

	protected String executionLock(final JobContext<T> context) {
		return String.format("EXECUTION_%s", context.getJobName());
	}

	protected String itemLock(final T currentItem) {
		return String.format("ITEM_%s_%d", currentItem.getClass()
				.getSimpleName(), itemId(currentItem));
	}

	protected int itemId(final T currentItem) {
		return currentItem.hashCode();
	}

	protected String lockOwner(final JobContext<T> context) {
		return String.format("JOB_%s_%s", context.getId(), ownerId());
	}

	protected long ownerId() {
		return Thread.currentThread().getId();
	}

}