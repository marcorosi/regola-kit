package org.regola.dao.hibernate;

import org.junit.runner.RunWith;
import org.regola.dao.BaseGenericDaoTest;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringBeanByName;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateGenericDaoTest extends BaseGenericDaoTest {

	@SpringBeanByName
	public static void setCustomerDao(HibernateGenericDao<?, ?> customerDao) {
		customerDao.getHibernateTemplate().setFlushMode(
				HibernateAccessor.FLUSH_EAGER);
	}

}
