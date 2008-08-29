package org.regola.dao.hibernate;

import org.regola.dao.AbstractFinderDaoTest;
import org.regola.dao.CustomerDao;
import org.springframework.aop.framework.Advised;
import org.springframework.orm.hibernate3.HibernateAccessor;

public class HibernateGenericDaoTest extends AbstractFinderDaoTest {

	// injected, autowire-by-type
	public void setHibernateCustomerDao(CustomerDao customerDao) {
		try {
			((HibernateGenericDao<?, ?>) ((Advised) customerDao)
					.getTargetSource().getTarget()).getHibernateTemplate()
					.setFlushMode(HibernateAccessor.FLUSH_EAGER);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		// customerDao.getHibernateTemplate().setFlushMode(
		// HibernateAccessor.FLUSH_EAGER);
	}

}
