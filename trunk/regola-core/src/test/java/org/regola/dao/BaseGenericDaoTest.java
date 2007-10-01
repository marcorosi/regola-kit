package org.regola.dao;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.regola.model.Customer;
import org.regola.model.CustomerPattern;
import org.regola.model.Customer.Address;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import static org.unitils.reflectionassert.ReflectionAssert.*;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("classpath:**/applicationContext.xml")
@Transactional(TransactionMode.ROLLBACK)
@Ignore
public class BaseGenericDaoTest {

	@SpringBeanByName
	private GenericDao<Customer, Integer> customerDao;

	@SpringBeanByName
	private SimpleJdbcTemplate jdbcTemplate;

	class CustomerRowMapper implements ParameterizedRowMapper<Customer> {

		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return newCustomer(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs
					.getString("LASTNAME"), rs.getString("STREET"), rs
					.getString("CITY"));
		}

	}

	@Test
	public void get() {
		Customer laura = newCustomer(0, "Laura", "Steel", "429 Seventh Av.",
				"Dallas");
		Customer lauraDb = getCustomerDao().get(0);

		assertEqualsCustomers(laura, lauraDb);
	}

	@Test
	public void get_invalidId() {
		assertNull(getCustomerDao().get(50));
	}

	@Test
	public void exists() {
		assertTrue(getCustomerDao().exists(0));
	}

	@Test
	public void exists_invalidId() {
		assertFalse(getCustomerDao().exists(50));
	}

	@Test
	public void remove() {
		getCustomerDao().remove(0);

		onDbVerify();

		assertEquals(49, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER"));
		assertEquals(0, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER WHERE ID = 0"));
	}

	@Test
	public void remove_invalidId() {
		getCustomerDao().remove(50);

		onDbVerify();

		assertEquals(50, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER"));
	}

	@Test
	public void removeEntity() {
		Customer laura = getCustomerDao().get(0);

		getCustomerDao().removeEntity(laura);

		onDbVerify();

		assertEquals(49, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER"));
		assertEquals(0, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER WHERE ID = 0"));
	}

	@Test
	public void save_insert() {
		Customer miguel = newCustomer(999, "Miguel", "de Icaza", "unknown",
				"n/a");

		getCustomerDao().save(miguel);

		onDbVerify();

		Customer miguelDb = jdbcTemplate.queryForObject(
				"SELECT * FROM CUSTOMER WHERE ID = ?", new CustomerRowMapper(),
				999);

		assertEqualsCustomers(miguel, miguelDb);
	}

	@Test
	public void save_update() {
		Customer laura = getCustomerDao().get(0);

		laura.setFirstName("Miguel");
		laura.setLastName("de Icaza");
		laura.getAddress().setStreet("unknown");
		laura.getAddress().setCity("n/a");

		getCustomerDao().save(laura);

		onDbVerify();

		Customer lauraDb = jdbcTemplate.queryForObject(
				"SELECT * FROM CUSTOMER WHERE ID = ?", new CustomerRowMapper(),
				0);

		assertEqualsCustomers(laura, lauraDb);
	}

	@Test
	public void findByModelPattern_equals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(5, customers.size());
	}

	@Test
	public void findByModelPattern_notEquals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotEqualsFirstName("Laura");

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(45, customers.size());
	}
	
	@Test
	public void findByModelPattern_greaterThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setId(5);

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(44, customers.size());
	}
	
	@Test
	public void findByModelPattern_lessThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLessThanId(5);

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(5, customers.size());
	}
	
	@Test
	public void findByModelPattern_in() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLastNames(new String[] { "Clancy", "Fuller", "Ott" });

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(16, customers.size());
	}

	@Test
	public void findByModelPattern_like() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressStreet("5");

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(8, customers.size());
	}

	@Test
	public void findByModelPattern_ilike() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressCity("o");

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(11, customers.size());
	}

	@Test
	public void findByModelPattern_emptyFilter() {
		List<Customer> customers = getCustomerDao().find(new CustomerPattern());

		assertEquals(50, customers.size());
	}

	@Test
	public void count_equals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		int count = getCustomerDao().count(pattern);

		assertEquals(5, count);
	}


	@Test
	public void count_notEquals() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setNotEqualsFirstName("Laura");

		int count = getCustomerDao().count(pattern);

		assertEquals(45, count);
	}
	
	@Test
	public void count_greaterThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setId(5);

		int count = getCustomerDao().count(pattern);

		assertEquals(44, count);
	}
	
	@Test
	public void count_lessThan() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLessThanId(5);

		int count = getCustomerDao().count(pattern);

		assertEquals(5, count);
	}
	
	@Test
	public void count_in() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setLastNames(new String[] { "Clancy", "Fuller", "Ott" });

		int count = getCustomerDao().count(pattern);

		assertEquals(16, count);
	}

	@Test
	public void count_like() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressStreet("5");

		int count = getCustomerDao().count(pattern);

		assertEquals(8, count);
	}

	@Test
	public void count_ilike() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setAddressCity("o");

		int count = getCustomerDao().count(pattern);

		assertEquals(11, count);
	}
	
	@Test
	public void count_emptyFilter() {
		assertEquals(50, getCustomerDao().count(new CustomerPattern()));
	}

	@Test
	public void getAll() {
		List<Customer> customers = getCustomerDao().getAll();

		assertEquals(50, customers.size());
	}

	protected Customer newCustomer(Integer id, String firstName,
			String lastName, String street, String city) {
		return new Customer(id, firstName, lastName, new Address(street, city));
	}

	protected void assertEqualsCustomers(Customer expected, Customer actual) {
		assertPropertyRefEquals("id", expected.getId(), actual);
		assertPropertyRefEquals("firstName", expected.getFirstName(), actual);
		assertPropertyRefEquals("lastName", expected.getLastName(), actual);

		Address address = expected.getAddress();

		assertPropertyRefEquals("address.street", address.getStreet(), actual);
		assertPropertyRefEquals("address.city", address.getCity(), actual);
	}

	protected void onDbVerify() {
	}

	public GenericDao<Customer, Integer> getCustomerDao() {
		return customerDao;
	}

	public SimpleJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
