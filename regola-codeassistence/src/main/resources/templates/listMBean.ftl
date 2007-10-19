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
		//@TODO Imposta un criterio per il figlio, ad esempio
		//getFilter().setAnnoAccademico(2005);

		super.init();
	}
	
	/**
	 * Questo metodo è chiamato per aggiornare il modello
	 * da presentare a video ad esempio dopo una modifica al filtro
	 */
	@Override
	public void refresh()
	{
		super.refresh();
	}
	
	/**
	 * Salta alla pagina di editing
	 */
	public String edit()
	{
		${model_name} v = getCurrentModelItem();
		log.info("Selezionato "+v);
		return "edit";
	}

}