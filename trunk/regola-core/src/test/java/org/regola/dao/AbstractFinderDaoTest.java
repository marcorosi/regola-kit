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

	public void testExecuteFinderWithTwoParameters() {
		String lastName = "Fuller";
		Address address = newAddress("unused", "Dallas");
		List<Customer> customers = customerFinderDao.findByLastNameAndCity(
				lastName, address);

		assertEquals(2, customers.size());

		List<Integer> customersId = Arrays.asList(new Integer[] {
				customers.get(0).getId(), customers.get(1).getId() });
		
		assertTrue(customersId.contains(10));
		assertTrue(customersId.contains(47));
	}

	public void testExecuteFinderWithOneParameter() {
		Address address = newAddress("33 Upland Pl.", "Seattle");
		List<Customer> customers = customerFinderDao.findByAddress(address);

		assertEquals(1, customers.size());
		assertEquals(34, customers.get(0).getId().intValue());
	}

}
