package org.regola.lock;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Gestore di lock pessimistici off-line.
 */
public interface LockManager {

	/**
	 * Blocca la risorsa in modo esclusivo per il proprietario.
	 * <p>
	 * Effettua pulizia dei lock vecchi (orfani o obsoleti, da rimuovere).
	 * 
	 * @param target
	 *            risorsa da bloccare
	 * @param owner
	 *            proprietario del blocco
	 * @throws DataAccessException
	 *             segnala il fallimento dell'acquisizione del lock, tipicamente
	 *             una {@link DataIntegrityViolationException} o
	 *             {@link CannotAcquireLockException}.
	 */
	void acquireLock(final String target, final String owner)
			throws DataAccessException;

	boolean verifyLock(String target, String owner);

	void releaseLock(final String target, final String owner);

	boolean isLocked(String target);

}