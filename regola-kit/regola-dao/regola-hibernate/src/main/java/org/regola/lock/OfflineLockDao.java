package org.regola.lock;

import org.regola.dao.GenericDao;

public interface OfflineLockDao extends GenericDao<OfflineLock, String> {

	void removeStaleLocks(int maxLifespan);

	OfflineLock lock(String target);

}
