package org.regola.dao.jpa;

import org.regola.dao.AbstractGenericDaoTest;
import org.springframework.aop.framework.Advised;

public abstract class AbstractJpaGenericDaoTest extends AbstractGenericDaoTest {

	@Override
	protected void flushSession() {
		try {
			((JpaGenericDao<?, ?>) ((Advised) customerDao).getTargetSource()
					.getTarget()).entityManager.flush();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
