package org.regola.webapp.action;

import org.regola.events.Event;
import org.regola.model.Customer;
import java.lang.Integer;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.ListPage;

public class CustomerList extends ListPage<Customer, Integer, CustomerPattern>
{
	/**
	 * Init è chiamato dopo tutte le dipendenze iniettate da Spring 
	 */
	@Override
	public void init()
	{
		setFilter(new CustomerPattern());
		//@TODO Imposta un criterio per il filtro, ad esempio
		
		getEventBroker().subscribe(this, "customer.persistence.changes");
		
		super.init();
	}
	
	/**
	 * E' stata fatta qualche variazione all'insieme
	 * degli oggetto di modello.
	 * @param e
	 */
	public void onRegolaEvent(Event e)
	{
		refresh();
	}

}