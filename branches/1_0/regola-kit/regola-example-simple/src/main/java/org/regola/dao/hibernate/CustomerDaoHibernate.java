package org.regola.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.regola.dao.CustomerDao;
import org.regola.model.Customer;

public class CustomerDaoHibernate extends HibernateGenericDao<Customer,Integer> implements CustomerDao
{
	public CustomerDaoHibernate(Class<Customer> persistentClass) 
	{
		super(persistentClass);
	}
	
	public List<Customer> getAllByHQL()
	{
		
		/*
		 * Questa va in eccezione: controllare come poter mettere più di una colonna per la IN
		 * 
		String hql = "from Customer c1 where (c1.id,c1.lastName) in" +
						"(select distinct(c2.id,c2.lastName) from Customer c2 where c2.id > 3)";
		*/
		
		String hql = "from Customer c1 where c1.id in" +
			"(select distinct(c2.id) from Customer c2 where c2.id > 3)";		
		
		return getHibernateTemplate().find(hql);
	}
}