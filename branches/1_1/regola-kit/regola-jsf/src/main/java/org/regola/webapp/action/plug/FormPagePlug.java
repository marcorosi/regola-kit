package org.regola.webapp.action.plug;

import java.io.Serializable;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.FormPage;

public interface FormPagePlug<T, ID extends Serializable, F extends ModelPattern> extends BasePagePlug {
	
	/**
	 * Passa l'oggetto corrispondente al FormPage attuale
	 * 
	 * @param listPage
	 */
	void setFormPage(FormPage<T,ID,F> formPage);

}
