package org.regola.dao.ognl;

import static org.regola.util.Ognl.getValue;

import java.io.Serializable;
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
import org.regola.model.EconomicConditionEnum;
import org.regola.model.Invoice;
import org.regola.model.ModelProperty;
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
		customer.setWithGlasses(true);
		customer.setEconomicCondition(EconomicConditionEnum.rich);
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
		customer.setEconomicCondition(EconomicConditionEnum.poor);
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
		customer.setEconomicCondition(EconomicConditionEnum.poor);
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
	
	public void testNotIn() {

		customerDao.setTarget(fixtureCustomer());

		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotInLastNames( new String[] {"Davis", "Smith" });
		
		List<Customer> list = customerDao.find(pattern);

		assertTrue(list.size() == 1);
		assertEquals(1,customerDao.count(pattern));
		
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

		pattern.setInvoiceId(null);
		pattern.setWithGlasses(true);
		pattern.setEconomicCondition(EconomicConditionEnum.rich);
		list = customerDao.find(pattern);
		
		assertEquals(1,list.size());
		assertEquals("Ken", list.get(0).getFirstName());
		assertTrue(list.get(0).getWithGlasses());
		
		assertEquals(1,customerDao.count(pattern));
		
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
	
	public void testExists()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		assertTrue( customerDao.exists(new Integer(2)) );
	}
	
	public void testRemove()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		int size = customerDao.getAll().size();
		customerDao.remove(new Integer(2));
		assertEquals(size-1, customerDao.getAll().size());
		assertNull(customerDao.get(new Integer(2)));
	}
	
	public void testRemoveEntity()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		Customer c = customerDao.get(new Integer(2));
		int size = customerDao.getAll().size();
		customerDao.removeEntity(c);
		assertEquals(size-1, customerDao.getAll().size());
		assertNull(customerDao.get(new Integer(2)));		
	}
	
	public void testSave_InsertNew1()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		int size = customerDao.getAll().size();
		
		//inserimento nuovo con id già impostato 
		Customer customer = new Customer();
		customer.setId(4);
		customer.setFirstName("Paul");
		customer.setLastName("Johnson");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("via Vittorio Alfieri");
		customer.getAddress().setCity("Verona");
		customer.setInvoices(new ArrayList<Invoice>());
		
		customerDao.save(customer);
		
		assertEquals(size+1, customerDao.getAll().size());
		customer = customerDao.get(4);
		assertEquals("Paul", customer.getFirstName());
		assertEquals("Johnson", customer.getLastName());
		
	}
	
	public void testSave_InsertNew2()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		int size = customerDao.getAll().size();
		
		//inserimento nuovo con id  non impostato (null) 
		Customer customer = new Customer();
		customer.setId(null);
		customer.setFirstName("Paul");
		customer.setLastName("Johnson");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("via Vittorio Alfieri");
		customer.getAddress().setCity("Verona");
		customer.setInvoices(new ArrayList<Invoice>());
		
		customerDao.save(customer);
		
		assertEquals(size+1, customerDao.getAll().size());	
	}
	
	public void testSave_Update1()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		int size = customerDao.getAll().size();
		
		//update di un oggetto  già esistente (stesso reference)
		Customer customer = customerDao.get(2);
		customer.setFirstName("firstNameModificato");
		
		customerDao.save(customer);
		
		assertEquals(size, customerDao.getAll().size());
		customer = customerDao.get(2);
		assertEquals("firstNameModificato", customer.getFirstName());
	}
	
	public void testSave_Update2()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		int size = customerDao.getAll().size();
		
		//update di un oggetto  già esistente (diverso reference)
		Customer customer = new Customer();
		customer.setId(2);
		customer.setFirstName("Adam");
		customer.setLastName("Smith");
		customer.setAddress(new Customer.Address());
		customer.getAddress().setStreet("piazza Leopardi");
		customer.getAddress().setCity("Bologna");
		customer.setInvoices(new ArrayList<Invoice>());
		Invoice invoice = new Invoice(); invoice.setId(201);
		customer.getInvoices().add(invoice);
		
		customer.setFirstName("firstNameModificato");
		
		customerDao.save(customer);
		
		assertEquals(size, customerDao.getAll().size());
		customer = customerDao.get(2);
		assertEquals("firstNameModificato", customer.getFirstName());
	}	
	
	public void testOrder()
	{
		List<Customer> target = fixtureCustomer();
		
		customerDao.setTarget(target);
		
		CustomerPattern pattern = new CustomerPattern();
		pattern.addSortedProperty("id", Order.asc);
		
		List<Customer> list = customerDao.find(pattern);
		assertEquals(3, list.size());
		assertEquals(new Integer(1), list.get(0).getId());
		assertEquals(new Integer(2), list.get(1).getId());
		assertEquals(new Integer(3), list.get(2).getId());
		
		ModelProperty property = pattern.getSortedProperties().get(0);
		assertEquals("id", property.getName());
		assertEquals(true, property.isOrderAscending());
		property.flipOrderDirection();
		assertEquals(true, property.isOrderDescending());
		
		list = customerDao.find(pattern);
		assertEquals(3, list.size());
		assertEquals(new Integer(3), list.get(0).getId());
		assertEquals(new Integer(2), list.get(1).getId());
		assertEquals(new Integer(1), list.get(2).getId());
	
		pattern.getVisibleProperties().clear();
		pattern.addSortedProperty("firstName", Order.asc);
		list.get(1).setId(null);
		list.get(1).setFirstName(null);
		list = customerDao.find(pattern);
		assertEquals(3, list.size());		
	}
	
	public void testOgnl()
	{
		List<Customer> target = fixtureCustomer();
		String ognl = "#root";
		Object result = getValue(ognl, target);
		assertEquals(target, result);
		assertEquals(target.size(), ((List)result).size());
		
		ognl = "#root.{?  #this.address.street.matches(\".*Gio.*\")}";
		result = getValue(ognl, target);
		assertFalse(target.equals(result));
		assertTrue(target.size() > ((List)result).size());
	}

	public void testOgnlWithEnum()
	{
		List<Customer> target = fixtureCustomer();
		String ognl = "#root.{?  #this.economicCondition==@org.regola.model.EconomicConditionEnum@rich}";
		@SuppressWarnings("unchecked")
		List<Customer> result = (List<Customer>) getValue(ognl, target);
		assertEquals(1, result.size());
	}

}
