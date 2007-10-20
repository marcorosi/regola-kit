package org.regola.webapp.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.webapp.action.lookup.BaseLookupStrategy;

import com.icesoft.faces.component.selectinputtext.SelectInputText;

public class AutoCompleteBean<T, ID extends Serializable, F extends ModelPattern>
		extends BaseLookupStrategy<T, ID, F> {
	private static Log log = LogFactory.getLog(AutoCompleteBean.class);

	private List<SelectItem> matchesList = new ArrayList<SelectItem>();

	/**
	 * initialize this component with a model object
	 */
	public void init(T model, F filter, GenericManager<T, ID> manager) {
		super.init(model, filter, manager);
	}

	/**
	 * @return the list of model objects that satisfy the filter
	 */
	public List<T> getSelection() {
		return getServiceManager().find(getFilter());
	}

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

			}
		}
	}

	/**
	 * The list of possible matches for the given SelectInputText value
	 * 
	 * @return list of possible matches.
	 */
	public List<SelectItem> getList() {
		log.info("getList: " + matchesList.size());
		return matchesList;
	}

	@SuppressWarnings("unchecked")
	private T getMatch(String value) {
		T result = null;
		if (matchesList != null) {
			SelectItem si;
			Iterator iter = matchesList.iterator();
			while (iter.hasNext()) {
				si = (SelectItem) iter.next();
				if (value.equals(si.getLabel())) {
					result = (T) si.getValue();
				}
			}
		}
		return result;
	}

	/**
	 * Utility method for building the match list given the current value of the
	 * SelectInputText component.
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	private void setMatches(ValueChangeEvent event) {
		Object searchWord = event.getNewValue();

		getFilter().reset();
		setFilterDescription((String) searchWord);
		List<T> elenco = getServiceManager().find(getFilter());

		log.info(String.format("Letti %d oggetti", elenco.size()));

		// assign new matchList
		if (this.matchesList != null) {
			this.matchesList.clear();
			this.matchesList = null;
		}

		this.matchesList = new ArrayList<SelectItem>();
		for (T m : elenco) {
			F f = (F) getFilter().clone();
			f.init(m);
			this.matchesList.add(new SelectItem(m, getSelectLabel(m)));

		}

	
	}


}
