package org.regola.webapp.action;

import org.regola.service.GenericManager;
import org.regola.model.ModelPattern;
import org.regola.webapp.action.lookup.BaseLookupStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SmartAutoCompleteBean<M, T, ID extends Serializable, F extends ModelPattern>
		extends BaseLookupStrategy<T, ID, F> {
	protected static Log log = LogFactory.getLog(SmartAutoCompleteBean.class);

	protected List<SelectItem> matchesList = new ArrayList<SelectItem>();
	protected String property  = null;
	protected M objectToUpdate = null;
	
	/*
	 * Valore per ottimizzare il lookup tramite id:
	 * -1 (default) lookup via id DISABILITATO;
	 * 0  lookup via id NON OTTIMIZZATO (effettua sempre la ricerca tramite id prima di quella per descrizione)
	 * >0 lookup via id OTTIMIZZATO (effettua la ricerca tramite tramite id solo quando l'input ha la stessa
	 * 		lunghezza dell'id)
	 */
	private int idModelLenght = -1;	
	
	/**
	 * initialize this component with a model object
	 */
	public void init(M objectToUpdate, T model, F filter, GenericManager<T, ID> manager, String property) 
	{
		if(StringUtils.isEmpty(property))
			throw new IllegalArgumentException("property not set");

		if(objectToUpdate == null)
			throw new IllegalArgumentException("objectToUpdate not set");

		super.init(model, filter, manager);		
		this.property=property;
		this.objectToUpdate=objectToUpdate;
	}
	
	/**
	 * initialize this component with a model object
	 * @param idLength 	se maggiore di zero attiva il lookup anche per codice.
	 * 					Per ottimizzare il lookup: effettua la ricerca per id solo
	 * 					quando il dato inserito ha la stessa lunghezza dell'id (inutile farlo negli altri casi)
	 */
	public void init(M objectToUpdate, T model, F filter, GenericManager<T, ID> manager, String property, int lookupIdLength) 
	{
		if(StringUtils.isEmpty(property))
			throw new IllegalArgumentException("property not set");

		if(objectToUpdate == null)
			throw new IllegalArgumentException("objectToUpdate not set");

		super.init(model, filter, manager);		
		this.property=property;
		this.objectToUpdate=objectToUpdate;
		
		this.idModelLenght = lookupIdLength;
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
	abstract public void updateList(ValueChangeEvent event) ;	
	
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
	protected T getMatch(String value) {
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
	/*
	 * vecchia versione -- ricerca solo per descrizione --
	 *
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
			//TODO: queste due righe erano per riprendere la descrizione Escaped dal filtro
			//		piuttosto che dall'oggetto di modello? E' rimasto a metà questo codice?
			this.matchesList.add(new SelectItem(m, getSelectLabel(m)));

		}
	
	}
	*/
	
	/*
	 * Ricerca sia per codice che per descrizione.
	 */
	@SuppressWarnings("unchecked")
	protected void setMatches(ValueChangeEvent event) {
		Object searchWord = event.getNewValue();
		
		getFilter().reset();
		
		if( ((String)searchWord).length() > 0 
				&& ((String)searchWord).length() == idModelLenght )
		{
			//provo a fare una ricerca per codice
			setFilterId((String) searchWord);
		}else{
			//faccio la ricerca per codice
			setFilterDescription((String) searchWord);
		}
		
		List<T> elenco = getServiceManager().find(getFilter());

		log.info(String.format("Letti %d oggetti", elenco.size()));
		
		/*
		 * E' stato impostato il lookup non ottimizzato e il lookup
		 * per descrizione non ha ritornato risultati --> provo lookup per id
		 */
		if(idModelLenght == 0 && (elenco==null || elenco.size()==0) )
		{
			getFilter().reset();
			setFilterId((String) searchWord);
			elenco = getServiceManager().find(getFilter());
		}

		// assign new matchList
		if (this.matchesList != null) {
			this.matchesList.clear();
			this.matchesList = null;
		}

		this.matchesList = new ArrayList<SelectItem>();
		for (T m : elenco) {
			F f = (F) getFilter().clone();
			f.init(m);
			//TODO: queste due righe erano per riprendere la descrizione Escaped dal filtro
			//		piuttosto che dall'oggetto di modello? E' rimasto a metà questo codice?
			this.matchesList.add(new SelectItem(m, getSelectLabel(m)));

		}
	}

	public int getIdModelLenght() {
		return idModelLenght;
	}

	public void setIdModelLenght(int idModelLenght) {
		this.idModelLenght = idModelLenght;
	}

}
