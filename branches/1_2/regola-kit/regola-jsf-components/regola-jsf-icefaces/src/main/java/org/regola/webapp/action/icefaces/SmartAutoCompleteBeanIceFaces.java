package org.regola.webapp.action.icefaces;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import org.regola.model.ModelPattern;
import org.regola.util.Ognl;
import org.regola.webapp.action.SmartAutoCompleteBean;

import com.icesoft.faces.component.selectinputtext.SelectInputText;

public class SmartAutoCompleteBeanIceFaces<M, T, ID extends Serializable, F extends ModelPattern> 
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

				log.info(String.format("Aggiornamento dell'oggetto %s: %s -> %s",objectToUpdate,property,m));					
				Ognl.setValue(property, objectToUpdate, m);
			}
		}
	}

	
	
}
