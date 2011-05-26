package org.regola.util;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.regola.model.Customer;
import org.regola.model.Invoice;

public class OgnlTest extends TestCase {

	public void testSearchById()
	{
		Customer customer = new Customer();
		Collection<Invoice> invoices = new ArrayList<Invoice>();
		for(int i=1; i<=10; i++)
			invoices.add(fixInvoice(i));
		customer.setInvoices(invoices);
		
		int idNonPresente = 12;
		int idPresente = 5;
		assertNull(Ognl.searchById(customer, "invoices", idNonPresente));
		Invoice invoice = (Invoice) Ognl.searchById(customer, "invoices", idPresente);
		assertNotNull(invoice);
		assertEquals(invoice.getId(), new Integer(idPresente));
		//id esplicito
		assertSame(invoice, Ognl.searchById(customer, "invoices", "id", idPresente));
	}

	private Invoice fixInvoice(int id) {
		Invoice invoice = new Invoice();
		invoice.setId(id);
		return invoice;
	}
}
