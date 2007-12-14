package org.regola.service.impl;

import org.regola.dao.GenericDao;
import org.regola.service.impl.GenericManagerImpl;

import org.regola.model.Customer;
import java.lang.Integer;
import org.regola.service.CustomerManager;

public class CustomerManagerImpl extends GenericManagerImpl<Customer,Integer> implements CustomerManager
{
	public CustomerManagerImpl(GenericDao<Customer,Integer> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
	}

}