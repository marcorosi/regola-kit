package org.regola.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.regola.model.Customer;
import org.regola.model.CustomerPattern;
import org.regola.model.Customer.Address;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("classpath:**/applicationContext.xml")
@Transactional(TransactionMode.ROLLBACK)
@Ignore
public class BaseGenericDaoTest {

	@SpringBeanByName
	private GenericDao<Customer, Integer> customerDao;

	@Test
	@ExpectedDataSet("GenericDao.create-result.xml")
	public void create() {
		Customer miguel = new Customer(999, "Miguel", "de Icaza", new Address(
				"unknown", "n/a"));

		Integer id = getCustomerDao().create(miguel);

		assertEquals(999, id);
	}

	@Test
	public void read() {
		Customer laura = getCustomerDao().read(0);

		assertEquals(new Customer(0, "Laura", "Steel", new Address(
				"429 Seventh Av.", "Dallas")), laura);
	}

	@Test
	@ExpectedDataSet("GenericDao.update-result.xml")
	public void update() {
		Customer laura = getCustomerDao().read(0);

		// Cambio sesso
		laura.setFirstName("Miguel");
		laura.setLastName("de Icaza");
		laura.getAddress().setStreet("unknown");
		laura.getAddress().setCity("n/a");

		getCustomerDao().update(laura);
	}

	@Test
	public void delete() {
		// fail("Not yet implemented");
	}

	@Test
	public void save_insert() {
		// fail("Not yet implemented");
	}

	@Test
	public void save_update() {
		// fail("Not yet implemented");
	}

	@Test
	public void findByModelPattern() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		List<Customer> customers = getCustomerDao().find(pattern);

		// System.out.println(customers);
		assertEquals(5, customers.size());
	}

	@Test
	public void findByModelPattern_emptyFilter() {
		List<Customer> customers = getCustomerDao().find(new CustomerPattern());

		// System.out.println(customers);
		assertEquals(50, customers.size());
	}

	@Test
	public void count() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		int customers = getCustomerDao().count(pattern);

		assertEquals(5, customers);
	}

	@Test
	public void getAll() {
		List<Customer> customers = getCustomerDao().getAll();

		// System.out.println(customers);
		assertEquals(50, customers.size());
	}

	public GenericDao<Customer, Integer> getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(GenericDao<Customer, Integer> customerDao) {
		this.customerDao = customerDao;
	}

}
