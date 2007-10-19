package org.regola.dao.jpa;

import org.regola.dao.AbstractFinderDaoTest;
import org.springframework.aop.framework.Advised;

public abstract class AbstractJpaGenericDaoTest extends AbstractFinderDaoTest {

	@Override
	protected void flushSession() {
		try {
			// double proxy target resolution
			((JpaGenericDao<?, ?>) ((Advised) ((Advised) getCustomerDao())
					.getTargetSource().getTarget()).getTargetSource()
					.getTarget()).entityManager.flush();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
