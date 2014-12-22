package org.regola.batch.lock;

import java.io.Serializable;

import org.regola.batch.JobContext;
import org.regola.batch.policy.LockPolicy;

/**
 * La politica di lock di default non effettua nessun lock delle risorse.
 */
public class NullLockPolicy<T extends Serializable> implements
		LockPolicy<T> {

	public boolean acquireExecution(final JobContext<T> context) {
		return true;
	}

	public void releaseExecution(final JobContext<T> context) {
	}

	public boolean acquireItem(final JobContext<T> context) {
		return true;
	}

	public void releaseItem(final JobContext<T> context) {
	}

}