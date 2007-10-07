package org.regola.dao.hibernate;

import org.regola.dao.AbstractGenericDaoTest;
import org.springframework.orm.hibernate3.HibernateAccessor;

public class HibernateGenericDaoTest extends AbstractGenericDaoTest {

	// injected, autowire-by-type
	public void setHibernateCustomerDao(HibernateGenericDao<?, ?> customerDao) {
		customerDao.getHibernateTemplate().setFlushMode(
				HibernateAccessor.FLUSH_EAGER);
	}

}
