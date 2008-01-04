package org.regola.dao.hibernate;

import org.regola.dao.hibernate.HibernateGenericDao;

import org.regola.model.Invoice;
import java.lang.Integer;
import org.regola.dao.InvoiceDao;

public class InvoiceDaoHibernate extends HibernateGenericDao<Invoice,Integer> implements InvoiceDao
{
	public InvoiceDaoHibernate(Class<Invoice> persistentClass) 
	{
		super(persistentClass);
	}
}