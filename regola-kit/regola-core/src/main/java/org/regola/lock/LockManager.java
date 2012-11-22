package org.regola.lock;

public interface LockManager {

	boolean acquireLock(final String target, final String owner);

	void releaseLock(final String target, final String owner);

}