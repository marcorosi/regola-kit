package org.regola.dao.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.regola.dao.AbstractFinderDaoTest;
import org.regola.dao.CustomerDao;
import org.springframework.aop.framework.Advised;
import org.springframework.orm.hibernate3.HibernateAccessor;

public class HibernateGenericDaoTest extends AbstractFinderDaoTest {

	protected SessionFactory  sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

//	// injected, autowire-by-type only to keep sessionFactory
//	public void setSessionFactory(CustomerDao customerDao) {
//		
//		try {
//			sessionFactory= ((HibernateGenericDao<?, ?>) ((Advised) customerDao)
//					.getTargetSource().getTarget()).getSessionFactory();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			fail(e.getMessage());
//		}
//	}
	
	@Override
	protected void flushSession() {
		sessionFactory.getCurrentSession().flush();
	}


}
