package org.regola.webapp.action.plug;

import java.io.Serializable;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.ListPage;

public interface ListPagePlug<T, ID extends Serializable, F extends ModelPattern> 
  extends BasePagePlug {
	
	/**
	 * Passa l'oggetto corrispondente alla ListPage attuale
	 * 
	 * @param listPage
	 */
	void setListPage(ListPage<T,ID,F> listPage);
	
	
}
