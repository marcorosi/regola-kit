package org.regola.dao.hibernate;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.regola.dao.CustomerDao;
import org.regola.model.Customer;
import org.regola.model.Item;

public class CustomerHQLDaoTest extends BaseDaoTestCase 
{
	CustomerDao customerDao;
	
	public void testGetAll()
	{
		List<Customer> l = customerDao.getAll();
		assertNotNull(l);
		assertTrue(l.size() > 0);
	}
	
	public void testSubQuery()
	{
		List<Customer> l = customerDao.getAllByHQL();
		assertNotNull(l);
		assertTrue(l.size() > 0);
	}

	public void test_DISTINCT_ROOT_ENTITY__criteriaAPI()
	{
		Session session = ((CustomerDaoHibernate)customerDao).getHibernateTemplate().getSessionFactory().getCurrentSession();
		
		//List<Customer> l = session.createCriteria(Customer.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("invoices").list();
		
		//outer join
		List<Customer> l = session.createCriteria(Customer.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setFetchMode("invoices",FetchMode.JOIN).list();
		
		assertTrue(l.size() > 0);
	}
	
	/*
	 * clausole in su più di una colonna non vanno su hql.
	 */
	public void xtestSubSelect()
	{
		
		Session session = ((CustomerDaoHibernate)customerDao).getHibernateTemplate().getSessionFactory().getCurrentSession();
		/*
		List<Customer> l = session.createQuery("from Customer c1 where (c1.id,c1.lastName) in" +
						"(select distinct c2.id, c2.lastName from Customer c2 where c2.id > 3)").list();
		*/
		
		List<Item> l = session.createQuery("from Item item where item.id in" +
						"(select distinct item.id from Item item )").list();
		
		assertTrue(l.size() > 0);
		
	}
	
	
	String hql = "select c from Customer c join c.invoices";
	public void test_DISTINCT_ROOT_ENTITY__HQL()
	{			
		Session session = ((CustomerDaoHibernate)customerDao).getHibernateTemplate().getSessionFactory().getCurrentSession();
		
		List<Customer> l = session.createQuery(hql).setFirstResult(0).setMaxResults(20).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		
		//List<Customer> l = session.createCriteria(Customer.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("invoices").list();
		
		//outer join
		//List<Customer> l = session.createCriteria(Customer.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).setFetchMode("invoices",FetchMode.JOIN).list();
		
		assertTrue(l.size() > 0);
		System.out.println("Risultati: " + l.size());
		for (Customer customer : l) {
			System.out.println(customer.getId());
		}
	}	
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

}
