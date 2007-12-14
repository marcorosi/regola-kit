package org.regola.dao.hibernate;

import org.regola.dao.hibernate.HibernateGenericDao;

import org.regola.model.Customer;
import java.lang.Integer;
import org.regola.dao.CustomerDao;

public class CustomerDaoHibernate extends HibernateGenericDao<Customer,Integer> implements CustomerDao
{
	public CustomerDaoHibernate(Class<Customer> persistentClass) 
	{
		super(persistentClass);
	}
}