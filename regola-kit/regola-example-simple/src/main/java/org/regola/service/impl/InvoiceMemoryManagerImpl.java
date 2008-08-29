package org.regola.service.impl;

import org.regola.dao.MemoryGenericDao;
import org.regola.model.Invoice;
import org.regola.service.InvoiceManager;

public class InvoiceMemoryManagerImpl extends MemoryGenericManagerImpl<Invoice,Integer> implements InvoiceManager
{
	public InvoiceMemoryManagerImpl(MemoryGenericDao<Invoice,Integer> memGenericDao) {
		super(memGenericDao);
		// TODO Auto-generated constructor stub
	}

}