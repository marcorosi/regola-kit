package org.regola.webapp.action;

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
		Customer v = getCurrentModelItem();
		log.info("Selezionato "+v);
		return "edit";
	}

}