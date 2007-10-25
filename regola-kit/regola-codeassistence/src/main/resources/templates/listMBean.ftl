package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};
import org.regola.webapp.action.ListPage;

public class ${mbean_list_name} extends ListPage<${model_name}, ${id_name}, ${filter_name}>
{
	/**
	 * Init è chiamato dopo tutte le dipendenze iniettate da Spring 
	 */
	@Override
	public void init()
	{
		setFilter(new ${filter_name}());
		//@TODO Imposta un criterio per il filtro, ad esempio
		
		getEventBroker().subscribe(this, "${field(model_name)}.persistence.changes");
		
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