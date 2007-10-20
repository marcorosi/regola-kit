package org.regola.dao;

import java.util.List;

import org.regola.model.Customer;
import org.regola.model.Customer.Address;

public interface CustomerDao extends GenericDao<Customer, Integer> {

	public List<Customer> findByLastNameAndCity(String lastName, String city);

	public List<Customer> findByAddressCity(Address address);

	public List<Customer> findByAddress(Address address);

}
