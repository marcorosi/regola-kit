package org.regola.service.impl;

import org.regola.dao.GenericDao;
import org.regola.service.impl.GenericManagerImpl;

import org.regola.model.Invoice;
import java.lang.Integer;
import org.regola.service.InvoiceManager;

public class InvoiceManagerImpl extends GenericManagerImpl<Invoice,Integer> implements InvoiceManager
{
	public InvoiceManagerImpl(GenericDao<Invoice,Integer> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
	}
	
	public GenericDao<Invoice,Integer> getGenericDao()
	{
		return genericDao;
	}

}