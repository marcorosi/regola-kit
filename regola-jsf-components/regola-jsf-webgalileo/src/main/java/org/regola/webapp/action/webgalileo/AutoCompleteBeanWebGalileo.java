package org.regola.webapp.action.webgalileo;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.AutoCompleteBean;



public class AutoCompleteBeanWebGalileo<T, ID extends Serializable, F extends ModelPattern> 
 extends  AutoCompleteBean<T, ID, F> {
	
	@SuppressWarnings("unchecked")
	public void updateList(ValueChangeEvent event) {
		log.info("updateList");

		// get a new list of matches.
		setMatches(event);

		
	}


}
