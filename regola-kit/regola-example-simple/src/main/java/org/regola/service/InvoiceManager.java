package org.regola.service;

import org.regola.service.GenericManager;

import org.regola.dao.GenericDao;
import org.regola.model.Invoice;
import java.lang.Integer;

public interface InvoiceManager extends GenericManager<Invoice,Integer>
{
	public GenericDao<Invoice,Integer> getGenericDao();
}