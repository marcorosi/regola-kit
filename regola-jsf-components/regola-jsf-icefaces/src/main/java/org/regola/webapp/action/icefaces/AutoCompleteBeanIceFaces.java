package org.regola.webapp.action.icefaces;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.AutoCompleteBean;

import com.icesoft.faces.component.selectinputtext.SelectInputText;

public class AutoCompleteBeanIceFaces<T, ID extends Serializable, F extends ModelPattern> 
 extends  AutoCompleteBean<T, ID, F> {
	
	@SuppressWarnings("unchecked")
	public void updateList(ValueChangeEvent event) {
		log.info("updateList");

		// get a new list of matches.
		setMatches(event);

		// Get the auto complete component from the event and assing
		if (event.getComponent() instanceof SelectInputText) {
			SelectInputText autoComplete = (SelectInputText) event
					.getComponent();
			T m;
			if (autoComplete.getSelectedItem() != null)
				m = (T) autoComplete.getSelectedItem().getValue();
			else
				m = getMatch(autoComplete.getValue().toString());

			if (m != null) {
				log.info("Selezionato " + m);
				getFilter().init(m);
				autoComplete.setValue(getFilterDescription());

			}
		}
	}


}
