package org.regola.webapp.action;

import org.regola.model.Invoice;
import java.lang.Integer;
import org.regola.model.pattern.InvoicePattern;
import org.regola.dao.ognl.OgnlGenericDao;
import org.regola.events.Event;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.plug.ListPagePlugProxy;

public class InvoiceList 
{
	/**
	 * Init è chiamato dopo tutte le dipendenze iniettate da Spring 
	 */
	public void init()
	{
		listPage.setPlug(new ListPagePlugProxy(this));
		
		//@TODO Imposta un criterio per il filtro, ad esempio
		listPage.setFilter(new InvoicePattern());
		
		listPage.getEventBroker().subscribe(this, "invoice.persistence.changes");
		
		listPage.init();
	}
	
	/**
	 * E' stata fatta qualche variazione all'insieme
	 * degli oggetto di modello.
	 * @param e
	 */
	public void onRegolaEvent(Event e)
	{
		listPage.getFilter().setCurrentPage(0);
		listPage.refresh();
	}
	
	ListPage<Invoice, Integer, InvoicePattern> listPage;

	public void setListPage(
			ListPage<Invoice, Integer, InvoicePattern> listPage) {
		this.listPage= listPage;
	}

	public ListPage<Invoice, Integer, InvoicePattern> getListPage() {
		return listPage;
	} 

}