package org.regola.dao.jpa;

import static org.junit.Assert.fail;

import org.springframework.aop.framework.Advised;

import org.regola.dao.BaseGenericDaoTest;

public abstract class AbstractJpaGenericDaoTest extends BaseGenericDaoTest {

	protected void onDbVerify() {
		try {
			((JpaGenericDao<?, ?>) ((Advised) getCustomerDao())
					.getTargetSource().getTarget()).entityManager.flush();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
