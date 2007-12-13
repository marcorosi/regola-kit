package org.regola.webapp.action;

import org.regola.events.Event;
import org.regola.model.Customer;
import java.lang.Integer;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.plug.BasePagePlug;
import org.regola.webapp.action.plug.ListPagePlugAnnotationProxy;
import org.regola.webapp.action.plug.ListPagePlugProxy;

public class CustomerList 
{
    
       public void init()
	{
		listPage.setPlug(new ListPagePlugAnnotationProxy(this));
		
		listPage.setFilter(new CustomerPattern());
		listPage.init();
		
		listPage.getEventBroker().subscribe(this, "customer.persistence.changes");
                
                
		
	}
	
	/**
	 * E' stata fatta qualche variazione all'insieme
	 * degli oggetto di modello.
	 * @param e
	 */
	public void onRegolaEvent(Event e)
	{
		listPage.refresh();
	}
	
	ListPage<Customer, Integer, CustomerPattern> listPage;

	public void setListPage(
			ListPage<Customer, Integer, CustomerPattern> listPage) {
		this.listPage= listPage;
		
	}

	public ListPage<Customer, Integer, CustomerPattern> getListPage() {
		return listPage;
	} 

	

}