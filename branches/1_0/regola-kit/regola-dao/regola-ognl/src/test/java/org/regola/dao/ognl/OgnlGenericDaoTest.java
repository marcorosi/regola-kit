package org.regola.dao.ognl;

import java.util.ArrayList;
import java.util.List;
import org.regola.dao.ognl.OgnlGenericDao;
import org.regola.dao.AbstractFinderDaoTest;
import org.regola.dao.CustomerDao;
import org.regola.dao.GenericDao;
import org.regola.dao.MemoryCustomerDao;
import org.regola.dao.MemoryGenericDao;
import org.regola.model.Customer;
import org.regola.model.CustomerPattern;
import org.regola.model.Invoice;
import org.regola.model.Order;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class OgnlGenericDaoTest extends
		AbstractDependencyInjectionSpringContextTests {

	private MemoryCustomerDao  customerDao;
	//private MemoryGenericDao<Invoice, Integer> invoiceDao;

	@Override
	public String getConfigPath() {
		return "applicationContext.xml";

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onSetUp() {
		// lookup Daos
		customerDao = (MemoryCustomerDao) applicationContext
				.getBean("customerDao");
		
	}

	public List<Customer> fixtureCustomer() {
		List<Customer> list = new ArrayList<Customer>();
		Customer customer; Invoice invoice; 

		customer = new Customer();
		customer.setId(1);
		customer.setFirstName("Ken");
		customer.setLastName("Davis");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("via Giovanni Bormida");
		customer.getAddress().setCity("Milano");
		customer.setInvoices(new ArrayList<Invoice>());
		invoice = new Invoice(); invoice.setId(101);
		customer.getInvoices().add(invoice);
		list.add(customer);
		
		customer = new Customer();
		customer.setId(2);
		customer.setFirstName("Adam");
		customer.setLastName("Smith");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("piazza Leopardi");
		customer.getAddress().setCity("Bologna");
		customer.setInvoices(new ArrayList<Invoice>());
		invoice = new Invoice(); invoice.setId(201);
		customer.getInvoices().add(invoice);
		list.add(customer);
		
		customer = new Customer();
		customer.setId(3);
		customer.setFirstName("Ken");
		customer.setLastName("Abrahms");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("corso Indipendenza");
		customer.getAddress().setCity("Milano");
		customer.setInvoices(new ArrayList<Invoice>());
		invoice = new Invoice(); invoice.setId(101);
		customer.getInvoices().add(invoice);
		list.add(customer);


		return list;

	}
	
	public void testLike() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressStreet("*Gio*");
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 1);
		assertEquals(1,customerDao.count(pattern));
		
	}
	
	public void testILike() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressCity("*ILA*");
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 2);
		assertEquals(2,customerDao.count(pattern));
		
	}
	
	public void testIn() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setLastNames( new String[] {"Davis", "Smith" });
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 2);
		assertEquals(2,customerDao.count(pattern));
		
	}

	public void testEquals() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Ken");
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 2);
		assertEquals("Ken", list.get(0).getFirstName());
		assertEquals("Ken", list.get(1).getFirstName());
		assertEquals(2,customerDao.count(pattern));
		
		
		pattern.addSortedProperty("lastName", Order.asc);
		
		pattern.setInvoiceId(101);
		list = customerDao.find(pattern);
		assertTrue(list.size() == 2);
		
		pattern.setInvoiceId(201);
		list = customerDao.find(pattern);
		assertTrue(list.size() == 0);

		
	}
	
	public void testIntersection() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Ken");
		pattern.setId(2);
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 1);
		assertEquals("Ken", list.get(0).getFirstName());
		
		assertEquals(1,customerDao.count(pattern));
		
	}
	
	public void testEmpty() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Kenyo");
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 0);
		assertEquals(0,customerDao.count(pattern));
		
	}
	
	public void testAll() {

		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);

		CustomerPattern pattern = new CustomerPattern();
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == target.size());
		assertEquals(target.size(),customerDao.count(pattern));
		
	}


}
