package org.regola.lock;

/**
 * Gestore di lock pessimistici off-line.
 */
public interface LockManager {

	boolean acquireLock(final String target, final String owner);

	boolean verifyLock(String target, String owner);

	void releaseLock(final String target, final String owner);

	boolean isLocked(String target);

}