package org.regola.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.regola.model.Customer;
import org.regola.model.Customer.Address;
import org.regola.model.CustomerPattern;
import org.regola.model.Invoice;
import org.regola.model.InvoicePattern;
import org.regola.model.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

/**
 * Nello spring context devono essere presenti almeno 3 bean: 2 di tipo
 * DataSource e GenericDao (autowire by type) e uno chiamato
 * "transactionManager" (di tipo PlatformTransactionManager).
 * 
 */
public abstract class AbstractGenericDaoTest extends
		AbstractAnnotationAwareTransactionalTests {

	private GenericDao<Customer, Integer> customerDao;
	private GenericDao<Invoice, Integer> invoiceDao;

	@Override
	@SuppressWarnings("unchecked")
	protected void onSetUpBeforeTransaction() {
		// lookup Daos
		customerDao = (GenericDao<Customer, Integer>) applicationContext
				.getBean("customerDao");
		invoiceDao = (GenericDao<Invoice, Integer>) applicationContext
				.getBean("invoiceDao");

		// init db if needed
		int count = 0;
		try {
			count = countRowsInTable("CUSTOMER");
		} catch (Exception e) {
			// table can be missing
		} finally {
			logger.debug("Table CUSTOMER record count: " + count);
			if (count == 0) {
				executeSqlScripts();
			}
		}
	}

	protected void executeSqlScripts() {
		try {
			SortedSet<String> scripts = new TreeSet<String>();
			for (Resource resource : applicationContext
					.getResources("classpath:dbscripts/???_*.sql")) {
				scripts.add(resource.getFilename());
			}
			for (String fileName : scripts) {
				executeSqlScript("classpath:dbscripts/" + fileName, false);
			}
		} catch (IOException e) {
			logger.error("Failure opening sql script", e);
		}
	}

	public GenericDao<Customer, Integer> getCustomerDao() {
		return customerDao;
	}

	@Override
	public String getConfigPath() {
		return "applicationContext.xml";
	}

	class CustomerRowMapper implements ParameterizedRowMapper<Customer> {

		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return newCustomer(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs
					.getString("LASTNAME"), rs.getString("STREET"), rs
					.getString("CITY"));
		}

	}

	protected Customer newCustomer(Integer id, String firstName,
			String lastName, String street, String city) {
		return new Customer(id, firstName, lastName, newAddress(street, city));
	}

	protected Address newAddress(String street, String city) {
		return new Address(street, city);
	}

	protected void assertEqualsCustomers(Customer expected, Customer actual) {
		assertNotNull(expected);
		assertNotNull(actual);

		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());

		Address expectedAddress = expected.getAddress();
		Address actualAddress = actual.getAddress();
		assertNotNull(expectedAddress);
		assertNotNull(actualAddress);

		assertEquals(expectedAddress.getStreet(), actualAddress.getStreet());
		assertEquals(expectedAddress.getCity(), actualAddress.getCity());
	}

	protected int countRowsInTable(String tableName, int id) {
		return simpleJdbcTemplate.queryForInt(
				"SELECT COUNT(*) FROM CUSTOMER WHERE ID = ?", id);
	}

	protected Customer mapCustomer(int id) {
		return simpleJdbcTemplate.queryForObject(
				"SELECT * FROM CUSTOMER WHERE ID = ?", new CustomerRowMapper(),
				id);
	}

	protected void flushSession() {
	}

	public void testGet() {
		Customer laura = newCustomer(0, "Laura", "Steel", "429 Seventh Av.",
				"Dallas");
		Customer lauraDb = customerDao.get(0);

		assertEqualsCustomers(laura, lauraDb);
	}

	public void testGet_invalidId() {
		assertNull(customerDao.get(50));
	}

	public void testExists() {
		assertTrue(customerDao.exists(0));
	}

	public void testExists_invalidId() {
		assertFalse(customerDao.exists(50));
	}

	public void testRemove() {
		customerDao.remove(0);

		flushSession();

		assertEquals(49, countRowsInTable("CUSTOMER"));
		assertEquals(0, countRowsInTable("CUSTOMER", 0));
	}

	public void testRemove_invalidId() {
		customerDao.remove(50);

		flushSession();

		assertEquals(50, countRowsInTable("CUSTOMER"));
	}

	public void testRemoveEntity() {
		Customer laura = customerDao.get(0);

		customerDao.removeEntity(laura);

		flushSession();

		assertEquals(49, countRowsInTable("CUSTOMER"));
		assertEquals(0, countRowsInTable("CUSTOMER", 0));
	}

	public void testSave_insert() {
		Customer miguel = newCustomer(999, "Miguel", "de Icaza", "unknown",
				"n/a");

		customerDao.save(miguel);

		flushSession();

		Customer miguelDb = mapCustomer(999);

		assertEqualsCustomers(miguel, miguelDb);
	}

	public void testSave_update() {
		Customer laura = customerDao.get(0);

		laura.setFirstName("Miguel");
		laura.setLastName("de Icaza");
		laura.getAddress().setStreet("unknown");
		laura.getAddress().setCity("n/a");

		customerDao.save(laura);

		flushSession();

		Customer lauraDb = mapCustomer(0);

		assertEqualsCustomers(laura, lauraDb);
	}

	public void testFindByModelPattern_equals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(5, customers.size());
	}

	public void testFindByModelPattern_notEquals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotEqualsFirstName("Laura");
		pattern.disablePaging();

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(45, customers.size());
	}

	public void testFindByModelPattern_greaterThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setId(5);
		pattern.disablePaging();

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(44, customers.size());
	}

	public void testFindByModelPattern_toManyRelation() {
		int INVOICE_ID = 1;
		CustomerPattern pattern = new CustomerPattern();
		pattern.setInvoiceId(INVOICE_ID);
		pattern.disablePaging();

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(1, customers.size());
		assertTrue(customers.get(0).getInvoices().size() > 0);
		for (Invoice i : customers.get(0).getInvoices()) {
			if (i.equals(INVOICE_ID))
				assertEquals(i.getTotal(), new BigDecimal("1610.70"));
		}

		pattern.setInvoiceId(null);
		pattern.setProductName("Iron Iron");

		customers = customerDao.find(pattern);
		assertEquals(18, customers.size());
	}

	public void testFindByModelPattern_toOneRelation() {
		InvoicePattern pattern = new InvoicePattern();
		pattern.setCustomerAddressCity("Dallas");
		pattern.disablePaging();

		List<Invoice> invoices = invoiceDao.find(pattern);
		assertEquals(10, invoices.size());
		for (Invoice i : invoices)
			assertEquals(i.getCustomer().getAddress().getCity(), "Dallas");
	}

	public void testFindByModelPattern_lessThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLessThanId(5);

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(5, customers.size());
	}

	public void testFindByModelPattern_in() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLastNames(new String[] { "Clancy", "Fuller", "Ott" });

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(16, customers.size());
	}
	
	public void testFindByModelPattern_notIn() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotInLastNames(new String[] { "Clancy", "Fuller", "Ott" });
		pattern.disablePaging();
		
		List<Customer> customers = customerDao.find(pattern);

		assertEquals(34, customers.size());
	}

	public void testFindByModelPattern_like() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressStreet("5*");

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(8, customers.size());
	}

	public void testFindByModelPattern_ilike() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressCity("o*");

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(11, customers.size());
	}

	public void testFindByModelPattern_emptyFilter() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.disablePaging();

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(50, customers.size());
	}

	public void testCount_equals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		int count = customerDao.count(pattern);

		assertEquals(5, count);
	}

	public void testCount_notEquals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotEqualsFirstName("Laura");

		int count = customerDao.count(pattern);

		assertEquals(45, count);
	}

	public void testCount_greaterThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setId(5);

		int count = customerDao.count(pattern);

		assertEquals(44, count);
	}

	public void testCount_lessThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLessThanId(5);

		int count = customerDao.count(pattern);

		assertEquals(5, count);
	}

	public void testCount_in() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLastNames(new String[] { "Clancy", "Fuller", "Ott" });

		int count = customerDao.count(pattern);

		assertEquals(16, count);
	}

	public void testCount_like() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressStreet("5*");

		int count = customerDao.count(pattern);

		assertEquals(8, count);
	}

	public void testCount_ilike() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressCity("o*");

		int count = customerDao.count(pattern);

		assertEquals(11, count);
	}

	public void testCount_toManyRelation() {
		int INVOICE_ID = 1;
		CustomerPattern pattern = new CustomerPattern();
		pattern.setInvoiceId(INVOICE_ID);
		pattern.disablePaging();

		int customersCount = customerDao.count(pattern);

		assertEquals(1, customersCount);

		pattern.setInvoiceId(null);
		pattern.setProductName("Iron Iron");

		customersCount = customerDao.count(pattern);
		assertEquals(18, customersCount);
	}

	public void testCount_toOneRelation() {
		InvoicePattern pattern = new InvoicePattern();
		pattern.setCustomerAddressCity("Dallas");
		pattern.disablePaging();

		int invoicesCount = invoiceDao.count(pattern);
		assertEquals(10, invoicesCount);
	}

	public void testCount_emptyFilter() {
		assertEquals(50, customerDao.count(new CustomerPattern()));
	}

	public void testFindByModelPattern_ordering() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.addSortedProperty("lastName", Order.asc);
		pattern.addSortedProperty("firstName", Order.desc);
		pattern.disablePaging();

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(50, customers.size());
		// verifico i primi 10
		assertEquals(37, customers.get(0).getId().intValue());
		assertEquals(03, customers.get(1).getId().intValue());
		assertEquals(21, customers.get(2).getId().intValue());
		assertEquals(26, customers.get(3).getId().intValue());
		assertEquals(15, customers.get(4).getId().intValue());
		assertEquals(18, customers.get(5).getId().intValue());
		assertEquals(16, customers.get(6).getId().intValue());
		assertEquals(47, customers.get(7).getId().intValue());
		assertEquals(49, customers.get(8).getId().intValue());
		assertEquals(10, customers.get(9).getId().intValue());
		// verifico l'ultimo
		assertEquals(11, customers.get(49).getId().intValue());
	}

	public void testFindByModelPattern_paging() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setPageSize(10);
		pattern.setCurrentPage(2);
		pattern.addSortedProperty("id", Order.asc);

		List<Customer> customers = customerDao.find(pattern);

		assertEquals(10, customers.size());
		assertEquals(20, customers.get(0).getId().intValue());
		assertEquals(21, customers.get(1).getId().intValue());
		assertEquals(22, customers.get(2).getId().intValue());
		assertEquals(23, customers.get(3).getId().intValue());
		assertEquals(24, customers.get(4).getId().intValue());
		assertEquals(25, customers.get(5).getId().intValue());
		assertEquals(26, customers.get(6).getId().intValue());
		assertEquals(27, customers.get(7).getId().intValue());
		assertEquals(28, customers.get(8).getId().intValue());
		assertEquals(29, customers.get(9).getId().intValue());
	}

	public void testGetAll() {
		List<Customer> customers = customerDao.getAll();

		assertEquals(50, customers.size());
	}
}
