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
import org.unitils.reflectionassert.ReflectionAssert;
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
			return new Customer(rs.getInt("ID"), rs.getString("FIRSTNAME"), rs
					.getString("LASTNAME"), new Address(rs.getString("STREET"),
					rs.getString("CITY")));
		}

	}

	@Test
	public void create() {
		Customer miguel = new Customer(999, "Miguel", "de Icaza", new Address(
				"unknown", "n/a"));

		Integer id = getCustomerDao().create(miguel);

		assertEquals(999, id);

		onDbVerify();

		Customer miguelDb = jdbcTemplate.queryForObject(
				"SELECT * FROM CUSTOMER WHERE ID = ?", new CustomerRowMapper(),
				999);

		assertEqualsCustomers(miguel, miguelDb);
	}

	@Test
	public void read() {
		Customer laura = new Customer(0, "Laura", "Steel", new Address(
				"429 Seventh Av.", "Dallas"));
		Customer lauraDb = getCustomerDao().read(0);

		assertEqualsCustomers(laura, lauraDb);
	}

	@Test
	public void update() {
		Customer laura = getCustomerDao().read(0);

		// Cambio sesso
		laura.setFirstName("Miguel");
		laura.setLastName("de Icaza");
		laura.getAddress().setStreet("unknown");
		laura.getAddress().setCity("n/a");

		getCustomerDao().update(laura);

		onDbVerify();

		Customer lauraDb = jdbcTemplate.queryForObject(
				"SELECT * FROM CUSTOMER WHERE ID = ?", new CustomerRowMapper(),
				0);

		assertEqualsCustomers(laura, lauraDb);
	}

	@Test
	public void delete() {
		Customer laura = getCustomerDao().read(0);

		getCustomerDao().delete(laura);

		onDbVerify();

		assertEquals(49, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER"));
		assertEquals(0, jdbcTemplate
				.queryForInt("SELECT COUNT(*) FROM CUSTOMER WHERE ID = 0"));
	}

	@Ignore
	@Test
	public void save_insert() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void save_update() {
		fail("Not yet implemented");
	}

	@Test
	public void findByModelPattern() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		List<Customer> customers = getCustomerDao().find(pattern);

		assertEquals(5, customers.size());
	}

	@Test
	public void findByModelPattern_emptyFilter() {
		List<Customer> customers = getCustomerDao().find(new CustomerPattern());

		assertEquals(50, customers.size());
	}

	@Test
	public void count() {
		CustomerPattern pattern = new CustomerPattern();
		pattern.setFirstName("Laura");

		int count = getCustomerDao().count(pattern);

		assertEquals(5, count);
	}

	@Test
	public void getAll() {
		List<Customer> customers = getCustomerDao().getAll();

		assertEquals(50, customers.size());
	}

	protected void assertEqualsCustomers(Customer expected, Customer actual) {
		ReflectionAssert
				.assertPropertyRefEquals("id", expected.getId(), actual);
		ReflectionAssert.assertPropertyRefEquals("firstName", expected
				.getFirstName(), actual);
		ReflectionAssert.assertPropertyRefEquals("lastName", expected
				.getLastName(), actual);
		ReflectionAssert.assertPropertyRefEquals("address.street", expected
				.getAddress().getStreet(), actual);
		ReflectionAssert.assertPropertyRefEquals("address.city", expected
				.getAddress().getCity(), actual);
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
