package org.regola.dao.jdo;

import org.regola.dao.AbstractFinderDaoTest;
import org.springframework.aop.framework.Advised;

public class JdoGenericDaoTest extends AbstractFinderDaoTest {
	@Override
	protected void flushSession() {
		try {
			// double proxy target resolution
			((JdoGenericDao<?, ?>) ((Advised) getCustomerDao())
					.getTargetSource().getTarget()).getJdoTemplate().flush();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
