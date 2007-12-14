package org.regola.webapp.action.webgalileo;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import org.regola.model.ModelPattern;
import org.regola.util.Ognl;
import org.regola.webapp.action.SmartAutoCompleteBean;



public class SmartAutoCompleteBeanWebGalileo<M, T, ID extends Serializable, F extends ModelPattern> 
  extends SmartAutoCompleteBean<M, T, ID, F>

{

	/**
	 * Called when a user has modifed the SelectInputText value. This method
	 * call causes the match list to be updated.
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void updateList(ValueChangeEvent event) {
		log.info("updateList");

		// get a new list of matches.
		setMatches(event);

	}

	
	
}
