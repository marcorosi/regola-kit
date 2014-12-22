package org.regola.service.impl;

import org.regola.lock.LockManager;
import org.regola.lock.OfflineLock;
import org.regola.lock.OfflineLockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LockManagerImpl extends GenericManagerImpl<OfflineLock, String>
		implements LockManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(LockManagerImpl.class);

	@Autowired
	public LockManagerImpl(OfflineLockDao dao) {
		super(dao);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void acquireLock(String target, String owner)
			throws DataAccessException {
		final OfflineLockDao dao = (OfflineLockDao) genericDao;
		try {
			// all'inizio fa sempre pulizia dei vecchi lock
			dao.removeStaleLocks(5);

			final OfflineLock lock = dao.lock(target);

			if (lock == null) {
				LOG.debug("Creazione nuovo lock [{}] per [{}]", target, owner);
				dao.save(new OfflineLock(target, owner));
			} else if (!ownedBy(lock, owner)) {
				LOG.debug("Lock [{}] già acquisito da altro proprietario: {}",
						target, lock.getOwner());
				throw new CannotAcquireLockException(
						"Lock già acquisito da altro proprietario");
			}

		} catch (DataAccessException e) {
			LOG.warn("Fallita acquisizione del lock [{}] per [{}]: {}",
					new Object[] { target, owner, e.getMessage() });
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public boolean verifyLock(String target, String owner) {
		OfflineLock lock = genericDao.get(target);
		return lock != null && ownedBy(lock, owner);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void releaseLock(String target, String owner) {
		final OfflineLockDao dao = (OfflineLockDao) genericDao;
		final OfflineLock lock = dao.lock(target);
		if (lock == null) {
			LOG.debug("Nessun lock trovato: {}", target);
			return;
		}
		if (!ownedBy(lock, owner)) {
			LOG.debug(
					"Il lock esistente per [{}] appartiene ad altro proprietario [{}]",
					target, lock.getOwner());
			return;
		}
		dao.remove(target);
	}

	protected boolean ownedBy(final OfflineLock lock, String owner) {
		return lock != null && lock.getOwner() != null
				&& lock.getOwner().equals(owner);
	}

	@Transactional(readOnly = true)
	public boolean isLocked(String target) {
		return genericDao.exists(target);
	}

}
