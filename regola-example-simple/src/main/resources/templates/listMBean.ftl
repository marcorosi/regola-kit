package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};
import it.kion.regola.webapp.action.ListPage;

public class ${mbean_list_name} extends ListPage<${model_name}, ${id_name}, ${filter_name}>
{
	/**
	 * Questo metodo e' chiamato dopo che la classe e'
	 * stata costruita e tutte le dipendenze iniettate da Spring 
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
	 * Questo metodo e' chiamato per aggiornare il modello
	 * da presentare a video ad esempio dopo una modifica al filtro
	 */
	@Override
	public void refresh()
	{
		super.refresh();
	}
	

	/**
	 * manda alla pagina di editing
	 */
	public String edit()
	{
		${model_name} v = getCurrentModelItem();
		log.info("Selezionato "+v);
		return "edit";
	}

}