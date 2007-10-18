package org.regola.dao;

import java.util.Arrays;
import java.util.List;

import org.regola.model.Customer;
import org.regola.model.Customer.Address;

public abstract class AbstractFinderDaoTest extends AbstractGenericDaoTest {

	private CustomerDao customerFinderDao;

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

	public void testExecuteFinder_byAddress() {
		Address address = newAddress("33 Upland Pl.", "Seattle");
		List<Customer> customers = customerFinderDao.findByAddress(address);

		assertEquals(1, customers.size());
		assertEquals(34, customers.get(0).getId().intValue());
	}

}
