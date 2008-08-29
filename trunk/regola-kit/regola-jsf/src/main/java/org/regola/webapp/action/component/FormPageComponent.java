/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.regola.webapp.action.component;

import java.io.Serializable;

import org.hibernate.validator.InvalidValue;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.webapp.action.BasePage;
import org.regola.webapp.action.FormPage;

/**
 *
 * @author nicola
 */
public interface FormPageComponent<T, ID extends Serializable, F extends ModelPattern> extends BasePageComponent {

	 public <MODEL, MODELID extends Serializable, FILTER extends ModelPattern> void addAutoCompleteLookUp(String property, MODEL model, FILTER filter, GenericManager<MODEL, MODELID> manager);
	 
	 @SuppressWarnings("hiding")
	 public <T> InvalidValue[] validate(InvalidValue[] msgs) ;
	 
	 public void setPage(FormPage<T,ID,F> page);
		   
	
}
