package org.regola.finder.support.hibernate;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.regola.dao.hibernate.HibernateGenericDao;
import org.regola.finder.FinderExecutor;
import org.regola.finder.support.AbstractFactoryBean;

public class HibernateDaoFactoryBean extends AbstractFactoryBean {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected <T> FinderExecutor<T> newFinderExecutor(Class<T> entityClass) {
		HibernateGenericDao<T, Serializable> dao = new HibernateGenericDao<T, Serializable>(
				entityClass);
		dao.setSessionFactory(sessionFactory);
		//dao.afterPropertiesSet();
		return dao;
	}

}
