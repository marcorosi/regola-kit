package org.regola.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.regola.dao.CustomerDao;
import org.regola.filter.criteria.hibernate.support.HQLquery;
import org.regola.model.Customer;
import org.regola.model.ModelProperty;
import org.regola.model.pattern.CustomerPattern;

public class CustomerDaoTest extends BaseDaoTestCase {

	CustomerDao customerDao;
	
	
	public void testSimpleFind()
	{
		CustomerPattern filter = new CustomerPattern();
		filter.setId(0);
		filter.setLastName("Steel");
		List<Customer> l = customerDao.find(filter);
		assertTrue(l.size() == 1);
	}
	
	public void testJoinFind()
	{
		CustomerPattern filter = new CustomerPattern();
		
		filter.setId(0);
		filter.setLastName("Steel");
		filter.setTotal(new BigDecimal(100));
		//filter.setInvoiceId(3);
		
		List<Customer> l = customerDao.find(filter);
		assertTrue(l.size() == 1);
	}	
	
	public void testJoinCount()
	{
		CustomerPattern filter = new CustomerPattern();
		
		filter.setId(0);
		filter.setLastName("Steel");
		filter.setTotal(new BigDecimal(100));
		
		Integer count = customerDao.count(filter);
		assertTrue(count == 1);
	}		
	
	public void testHQLCustom()
	{
		CustomerPattern filter = new CustomerPattern();
		
		filter.setId(0);
		filter.setLastName("Steel");
		//filter.setTotal(new BigDecimal(100));
		
		HQLquery hqlQuery = ((CustomerDaoHibernate)customerDao).getClausoleHQL(filter);
		assertNotNull(hqlQuery);
		assertNotNull(hqlQuery.getParameters());
		
		assertNotNull(hqlQuery.getSelect());
		assertFalse("".equals(hqlQuery.getSelect().trim()));
		
		assertNotNull(hqlQuery.getFrom());
		assertFalse("".equals(hqlQuery.getFrom().trim()));
		
		assertNotNull(hqlQuery.getWhere());
		assertFalse("".equals(hqlQuery.getWhere().trim()));
		
		assertNotNull(hqlQuery.getOrderBy());
		assertFalse("".equals(hqlQuery.getOrderBy().trim()));		
		
		String hqlString = "select " + hqlQuery.getSelect() + " from " + hqlQuery.getFrom() + " where " + hqlQuery.getWhere() + " order by " + hqlQuery.getOrderBy();
		List<Customer> l = ((CustomerDaoHibernate)customerDao).find(hqlString, hqlQuery.getParameters(), hqlQuery.getFirstResult(), hqlQuery.getMaxResult());
		assertTrue(l.size() == 1);
	}	
	
	public void testIsNull()
	{
		CustomerPattern1 filter = new CustomerPattern1();
		filter.setCityNull(true);
		
		filter.setPagingEnabled(false);
		filter.setSortedProperties(new ArrayList<ModelProperty>());
		
		List<Customer> l = customerDao.find(filter);
		//assertTrue(l.size() > 0); //solo nel mio db locale ho dei customer con city null.		
	}
	
	public void testIsNotNull()
	{
		CustomerPattern1 filter = new CustomerPattern1();
		filter.setCityNull(false);
		
		filter.setPagingEnabled(false);
		filter.setSortedProperties(new ArrayList<ModelProperty>());
		
		List<Customer> l = customerDao.find(filter);
		assertTrue(l.size() > 0);		
	}
	
	public void testIsNotNullDisattivo()
	{
		CustomerPattern1 filter = new CustomerPattern1();
		
		filter.setPagingEnabled(false);
		filter.setSortedProperties(new ArrayList<ModelProperty>());
		
		List<Customer> l = customerDao.find(filter);
		assertTrue(l.size() > 0);		
	}
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}	
	
}
