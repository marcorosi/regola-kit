package org.regola.webapp.action;

import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.model.pattern.ItemPattern;
import org.regola.events.Event;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.plug.ListPagePlugProxy;

public class ItemList 
{
	/**
	 * Init è chiamato dopo tutte le dipendenze iniettate da Spring 
	 */
	public void init()
	{
		listPage.setPlug(new ListPagePlugProxy(this));
		
		//@TODO Imposta un criterio per il filtro, ad esempio
		listPage.setFilter(new ItemPattern());
		
		listPage.getEventBroker().subscribe(this, "item.persistence.changes");
		
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
	
	ListPage<Item, ItemId, ItemPattern> listPage;

	public void setListPage(
			ListPage<Item, ItemId, ItemPattern> listPage) {
		this.listPage= listPage;
		
	}

	public ListPage<Item, ItemId, ItemPattern> getListPage() {
		return listPage;
	} 

}