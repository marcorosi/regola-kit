package org.regola.dao;

import org.regola.dao.GenericDao;

import org.regola.model.Customer;
import java.lang.Integer;
import java.util.List;

public interface CustomerDao extends GenericDao<Customer,Integer>
{
	public List<Customer> getAllByHQL();
}