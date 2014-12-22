package org.regola.lock.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.regola.dao.hibernate.HibernateGenericDao;
import org.regola.lock.OfflineLock;
import org.regola.lock.OfflineLockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class OfflineLockDaoHibernate extends
		HibernateGenericDao<OfflineLock, String> implements OfflineLockDao {

	private static final Logger LOG = LoggerFactory
			.getLogger(OfflineLockDaoHibernate.class);
	private static final String REMOVE_STALE_LOCKS_QUERY = "removeStaleLocks";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public OfflineLockDaoHibernate() {
		super(OfflineLock.class);
	}

	//@Override
	public void removeStaleLocks(int maxLifespan) {
		final Query delete = getSession().getNamedQuery(
				REMOVE_STALE_LOCKS_QUERY);
		final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		final Date before = new Date(new Date().getTime()
				- (maxLifespan * 60000));

		delete.setTimestamp("before", before);
		LOG.debug("Rimozione degli stale locks più vecchi di [{}]",
				formatter.format(before));
		int deleted = delete.executeUpdate();
		LOG.debug("Rimossi [{}] stale locks", deleted);
	}

	//@Override
	public OfflineLock lock(String target) {
		LOG.debug("Lettura del lock [{}]", target);
		return (OfflineLock) getSession().get(OfflineLock.class, target,
				lockOptions());
	}

	private LockOptions lockOptions() {
		// scope: false=solo root entity (no cascade)
		// timeout: SELECT ... FOR UPDATE NOWAIT
		return new LockOptions(LockMode.PESSIMISTIC_WRITE).setScope(false)
				.setTimeOut(LockOptions.NO_WAIT);
	}
}
