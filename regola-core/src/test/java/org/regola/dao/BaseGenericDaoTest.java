package org.regola.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyRefEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.regola.model.Customer;
import org.regola.model.CustomerPattern;
import org.regola.model.Customer.Address;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.unitils.database.UnitilsDataSourceFactoryBean;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
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

	@BeforeClass
	public static void setUpDataBase() throws Exception {
		DataSource dataSource = (DataSource) new UnitilsDataSourceFactoryBean()
				.getObject();
		JdbcOperations tpl = new JdbcTemplate(dataSource);
		tpl
				.execute("CREATE MEMORY TABLE CUSTOMER(ID INTEGER NOT NULL PRIMARY KEY,FIRSTNAME VARCHAR,LASTNAME VARCHAR,STREET VARCHAR,CITY VARCHAR)");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(0,'Laura','Steel','429 Seventh Av.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(1,'Susanne','King','366 - 20th Ave.','Olten')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(2,'Anne','Miller','20 Upland Pl.','Lyon')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(3,'Michael','Clancy','542 Upland Pl.','San Francisco')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(4,'Sylvia','Ringer','365 College Av.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(5,'Laura','Miller','294 Seventh Av.','Paris')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(6,'Laura','White','506 Upland Pl.','Palo Alto')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(7,'James','Peterson','231 Upland Pl.','San Francisco')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(8,'Andrew','Miller','288 - 20th Ave.','Seattle')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(9,'James','Schneider','277 Seventh Av.','Berne')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(10,'Anne','Fuller','135 Upland Pl.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(11,'Julia','White','412 Upland Pl.','Chicago')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(12,'George','Ott','381 Upland Pl.','Palo Alto')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(13,'Laura','Ringer','38 College Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(14,'Bill','Karsen','53 College Av.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(15,'Bill','Clancy','319 Upland Pl.','Seattle')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(16,'John','Fuller','195 Seventh Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(17,'Laura','Ott','443 Seventh Av.','Lyon')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(18,'Sylvia','Fuller','158 - 20th Ave.','Paris')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(19,'Susanne','Heiniger','86 - 20th Ave.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(20,'Janet','Schneider','309 - 20th Ave.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(21,'Julia','Clancy','18 Seventh Av.','Seattle')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(22,'Bill','Ott','250 - 20th Ave.','Berne')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(23,'Julia','Heiniger','358 College Av.','Boston')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(24,'James','Sommer','333 Upland Pl.','Olten')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(25,'Sylvia','Steel','269 College Av.','Paris')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(26,'James','Clancy','195 Upland Pl.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(27,'Bob','Sommer','509 College Av.','Seattle')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(28,'Susanne','White','74 - 20th Ave.','Lyon')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(29,'Andrew','Smith','254 College Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(30,'Bill','Sommer','362 - 20th Ave.','Olten')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(31,'Bob','Ringer','371 College Av.','Olten')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(32,'Michael','Ott','339 College Av.','Boston')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(33,'Mary','King','491 College Av.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(34,'Julia','May','33 Upland Pl.','Seattle')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(35,'George','Karsen','412 College Av.','Chicago')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(36,'John','Steel','276 Upland Pl.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(37,'Michael','Clancy','19 Seventh Av.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(38,'Andrew','Heiniger','347 College Av.','Lyon')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(39,'Mary','Karsen','202 College Av.','Chicago')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(40,'Susanne','Miller','440 - 20th Ave.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(41,'Bill','King','546 College Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(42,'Robert','Ott','503 Seventh Av.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(43,'Susanne','Smith','2 Upland Pl.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(44,'Sylvia','Ott','361 College Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(45,'Janet','May','396 Seventh Av.','Oslo')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(46,'Andrew','May','172 Seventh Av.','New York')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(47,'Janet','Fuller','445 Upland Pl.','Dallas')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(48,'Robert','White','549 Seventh Av.','San Francisco')");
		tpl
				.execute("INSERT INTO CUSTOMER VALUES(49,'George','Fuller','534 - 20th Ave.','Olten')");
	}

	@AfterClass
	public static void tearDownDataBase() throws Exception {
		DataSource dataSource = (DataSource) new UnitilsDataSourceFactoryBean()
				.getObject();
		JdbcOperations tpl = new JdbcTemplate(dataSource);
		tpl.execute("DROP TABLE CUSTOMER");

	}

}
