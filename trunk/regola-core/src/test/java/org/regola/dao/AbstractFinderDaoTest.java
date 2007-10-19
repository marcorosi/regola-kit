package org.regola.dao;

import java.util.Arrays;
import java.util.List;

import org.regola.model.Customer;
import org.regola.model.Customer.Address;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.SystemProfileValueSource;

public abstract class AbstractFinderDaoTest extends AbstractGenericDaoTest {

	private CustomerDao customerFinderDao;

	public class DefaultEnabledSystemProfileValueSource implements
			ProfileValueSource {

		private ProfileValueSource delegate;

		public DefaultEnabledSystemProfileValueSource() {
			this.delegate = SystemProfileValueSource.getInstance();
		}

		public String get(String key) {
			String profileValue = delegate.get(key);
			return profileValue == null ? "enabled" : profileValue;
		}
	}

	public AbstractFinderDaoTest() {
		profileValueSource = new DefaultEnabledSystemProfileValueSource();
	}

	public CustomerDao getCustomerFinderDao() {
		return customerFinderDao;
	}

	public void setCustomerFinderDao(CustomerDao customerDao) {
		this.customerFinderDao = customerDao;
	}

	public void testExecuteFinder_byLastNameAndCity() {
		List<Customer> customers = customerFinderDao.findByLastNameAndCity(
				"Fuller", "Dallas");

		assertEquals(2, customers.size());

		List<Integer> customersId = Arrays.asList(new Integer[] {
				customers.get(0).getId(), customers.get(1).getId() });

		assertTrue(customersId.contains(10));
		assertTrue(customersId.contains(47));
	}

	@IfProfileValue(name = "testExecuteFinder_byAddressCity", value = "enabled")
	public void testExecuteFinder_byAddressCity() {
		Address address = newAddress("unused", "Lyon");
		List<Customer> customers = customerFinderDao.findByAddressCity(address);

		assertEquals(4, customers.size());

		List<Integer> customersId = Arrays.asList(new Integer[] {
				customers.get(0).getId(), customers.get(1).getId(),
				customers.get(2).getId(), customers.get(3).getId() });

		assertTrue(customersId.contains(02));
		assertTrue(customersId.contains(17));
		assertTrue(customersId.contains(28));
		assertTrue(customersId.contains(38));
	}

	@IfProfileValue(name = "testExecuteFinder_byAddress", value = "enabled")
	public void testExecuteFinder_byAddress() {
		Address address = newAddress("33 Upland Pl.", "Seattle");
		List<Customer> customers = customerFinderDao.findByAddress(address);

		assertEquals(1, customers.size());
		assertEquals(34, customers.get(0).getId().intValue());
	}

}
