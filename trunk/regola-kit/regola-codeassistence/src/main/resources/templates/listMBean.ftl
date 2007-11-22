package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};
import org.regola.events.Event;
import org.regola.webapp.action.ListPage;
import org.regola.webapp.action.plug.ListPagePlugProxy;

public class ${mbean_list_name} 
{
	/**
	 * Init è chiamato dopo tutte le dipendenze iniettate da Spring 
	 */
	public void init()
	{
		listPage.setPlug(new ListPagePlugProxy(this));
		
		//@TODO Imposta un criterio per il filtro, ad esempio
		listPage.setFilter(new ${filter_name}());
		
		listPage.getEventBroker().subscribe(this, "${field(model_name)}.persistence.changes");
		
		listPage..init();
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
	
	ListPage<${model_name}, ${id_name}, ${filter_name}> listPage;

	public void setListPage(
			ListPage<${model_name}, ${id_name}, ${filter_name}> listPage) {
		this.listPage= listPage;
		
	}

	public ListPage<${model_name}, ${id_name}, ${filter_name}> getListPage() {
		return listPage;
	} 

}