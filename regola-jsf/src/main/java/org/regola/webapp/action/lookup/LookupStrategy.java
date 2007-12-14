package org.regola.webapp.action.lookup;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.regola.service.GenericManager;
import org.regola.model.ModelPattern;


public interface LookupStrategy<M,ID extends Serializable, F extends ModelPattern> 
{
	/**
	 * Metodo da chiamare dopo la costruzione
	 * @param model
	 * @param filter
	 * @param manager
	 */
	void init(M model, F filter, GenericManager<M, ID> manager );
	
	F getFilter();
	List<M> getSelection();
	
	/*
	 * Restituisce la lista di oggetti tra cui scegliere a video
	 */
	List<SelectItem> getList();
	
}
