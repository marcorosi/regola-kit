package org.regola.webapp.action;

import org.regola.service.GenericManager;
import org.regola.util.Ognl;
import org.regola.webapp.action.component.FormPageComponent;
import org.regola.webapp.action.lookup.LookupStrategy;
import org.regola.webapp.action.plug.FormPagePlug;
import org.regola.webapp.action.plug.ListPagePlug;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.InvalidValue;
import org.regola.events.Event;
import org.regola.formsValidation.Amendment;
import org.regola.formsValidation.AmendmentUtils;
import org.regola.formsValidation.AmendmentsClassValidator;
import org.regola.jsfValidators.event.ConversionErrorEvent;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.util.Ognl;
import org.regola.webapp.action.lookup.LookupStrategy;


public  class FormPage<T, ID extends Serializable, F extends ModelPattern> extends org.regola.webapp.action.BasePage
{
	
	private GenericManager<T, ID> serviceManager;
	
	String errore;
	HashMap<String, String> conversionErrors = new HashMap<String, String>();
	HashMap<String, String> errors = new HashMap<String, String>();
	
	protected Map<String, LookupStrategy> lookups = new HashMap<String, LookupStrategy>();
	
	protected Class<ID> idClass;
	protected ID id;
	protected T model;

	//rappresentazione in stringa dell'id originale
	//serve per i form dove viene modificata la chiave
	private String originalId;
	
	private String encodedId;
	
	/*
	 * Contesto di validazione di questo form.
	 * Nell'attuale implementazione è soltanto il path relativo al file xml di configurazione degli emendamenti
	 */
	//protected String amendmentConfiguration;
	protected String validationContext = "";
	protected boolean subscribe = false; //subscribe all'event broker già effettuata/non effettuata
	
	/**
	 * @return true if the model behind this form has validation errors
	 */
	public boolean hasErrors()
	{
		return !errors.isEmpty();
	}
	
	public String getEncodedId() {
		return encodedId;
	}

	public void setEncodedId(String encodedId) {
		this.encodedId = encodedId;
	}
	
	@SuppressWarnings("unchecked")
	public void setId(String s) {
		if (s != null)
		{
			if(id instanceof String)
			{
				id = (ID) s;
			} else 
			{
				Ognl.setValue("encoded", id, s);
				//id.setEncoded(s);	
			}
			init();
		}
	}
	
	public void setTypedID(ID id)
	{
		this.id=id;
	}
	
    public  <MODEL, MODELID extends Serializable, FILTER extends ModelPattern> 
    void addAutoCompleteLookUp(String property,MODEL model, FILTER filter, GenericManager<MODEL, MODELID> manager)
    {
    	getComponent().addAutoCompleteLookUp(property, model, filter, manager);
    }
	
	
	public String getErrore()
	{
		return errore;
	}

	public void setErrore(String errore)
	{
		this.errore = errore;
	}

	public HashMap<String, String> getErrors()
	{
		return errors;
	}

	
	
	public void submit(ActionEvent event)
	{

	}

	public void init()
	{
		getComponent().setPage(this);
		if(id != null) log.info("Init con id " + id);

		//registrazione all'eventbroker per notifica eventi errori conversione
		if(getEventBroker() != null /* per i form che non ne hanno bisogno non lo facciamo iniettare da spring */ 
				&& !subscribe) //per eventuali richiami multipli del metodo init()
		{
			getEventBroker().subscribe(this, "form.errors.conversion");
			subscribe = true;
		}
		
		getComponent().init();

	}
	/*
	@SuppressWarnings("unchecked")
	public <T> InvalidValue[] validate(T object)
	{

		//lettura eventuali emendamenti da eventuale file di configurazione
		List<Amendment> amendments = new AmendmentUtils().readDSLAmendments(amendmentConfiguration, object.getClass());
		
		//	clean errors and effects
		setEffectPanel(null);

		//	clean errors

		getErrors().clear();
		
		
		
		//validate bean
		//versione validatore con emendamenti
		//AmendmentsClassValidator<T> validator = new AmendmentsClassValidator<T>((Class<T>) object.getClass(), amendmentConfiguration);
		AmendmentsClassValidator<T> validator = new AmendmentsClassValidator<T>((Class<T>) object.getClass(), amendments);
		InvalidValue[] msgs = validator.getInvalidValues(object);

		//populate errors and effects
		for (InvalidValue msg : msgs)
		{
			errors.put(msg.getPropertyName(), msg.getMessage());
			
		}
		
		//'sovrascrivo' gli errori semantici con gli eventuali precendenti errori sintattici
		errors.putAll(conversionErrors); //per gli errori si conversione non metto effetti	
		
		getComponent().validate(msgs);

		return msgs;
	}
	*/
	
	@SuppressWarnings("unchecked")
	public <T> boolean validate(T object)
	{	
		
		if(object == null)
			return true;
		
		//lettura eventuali emendamenti da eventuale file di configurazione
		List<Amendment> amendments = new AmendmentUtils().readDSLAmendments(validationContext /*amendmentConfiguration*/, (Class<T>) object.getClass());
		
		//	clean errors
		getErrors().clear();
		
		//validate bean
		/*
		 * versione originale
		ClassValidator<T> validator = new ClassValidator<T>((Class<T>) object.getClass());
		*/
		//versione validatore con emendamenti
		//AmendmentsClassValidator<T> validator = new AmendmentsClassValidator<T>((Class<T>) object.getClass(), amendmentConfiguration);
		AmendmentsClassValidator<T> validator = new AmendmentsClassValidator<T>((Class<T>) object.getClass(), amendments);
		InvalidValue[] msgs = validator.getInvalidValues(object);

		//populate errors and effects
		for (InvalidValue msg : msgs)
		{
			errors.put(msg.getPropertyName(), msg.getMessage());
		}
		
		getComponent().validate(msgs);
		
		//'sovrascrivo' gli errori semantici con gli eventuali precendenti errori sintattici
		errors.putAll(conversionErrors); //per gli errori si conversione non metto effetti	

		return errors.isEmpty();
	}	

	public GenericManager<T, ID> getServiceManager()
	{
		return serviceManager;
	}

	public void setServiceManager(GenericManager<T, ID> businessManager)
	{
		this.serviceManager = businessManager;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T verbale) {
		this.model = verbale;
	}
	
	/**
	 * 
	 * @param property
	 * @param lookup
	 * @return true se la proprietà è stata processata
	 */
	public boolean updateModelFromLookup(String property, LookupStrategy lookup)
	{
		return false;
	}

	public String save()
	{
		processLookups();
		
		if(!validate())
		{
			setReturnPage(""); //TODO: sostiruire con aspetto afterReturning
			return "";
		}
		
		saveOrUpdate();
		setReturnPage("save"); //TODO: sostiruire con aspetto afterReturning
		return "save";
	}
	
	public void processLookups()
	{
		
		for (String property : lookups.keySet())
		{
			LookupStrategy lookup = lookups.get(property);
			
			if (!updateModelFromLookup(property, lookup))
			{
				List selections = lookup.getSelection();
				
				if (selections.size() > 1)
				{
					throw new RuntimeException("Can't menage lookups with multiple selections: please override updateModelFromLookup() for the property: "+ property);
				} else
				{
					//lo scrivo nel modello
					Ognl.setValue(property, getModel(), selections.get(0));
				}
			}	
		}
		
	}
	
	public boolean validate()
	{	/*
		int idLength=0;
		if (id!=null)  idLength = validate(id).length;
		int modelLength = validate(model).length;
		*/
		boolean esitoId = validate(id);
		boolean esitoModel = validate(model);
		
		//pulizia errori di conversione
		conversionErrors.clear();
		
		/*
		if (idLength > 0 || modelLength > 0)
			return false;
		return true;
		*/
		return esitoId && esitoModel;
	}
	
	public void saveOrUpdate()
	{
		if(StringUtils.isEmpty(originalId))
		{
			getServiceManager().save(model);
		} else 
		{
			ID oId = rebuildId(originalId);
			if(id.equals(oId))
				getServiceManager().save(model);
			else
				getServiceManager().update(model, oId);	
		}
	}

	@SuppressWarnings("unchecked")
	private ID rebuildId(String encodedId) 
	{
		try
		{
			//ID id = this.id.getClass().newInstance();
			ID id = idClass.newInstance();
			if(id instanceof String)
			{
				id = (ID) encodedId;
			} else 
			{
				Ognl.setValue("encoded", id, encodedId);	
			}

			return id;
			
		} catch (Exception e) {
			throw new RuntimeException("Impossibile ricostruire l'id dell'oggetto da aggiornare. EncodedId: "+encodedId,e);
		}
	}

	public String cancel()
	{
		setReturnPage("cancel"); //TODO: sostiruire con aspetto afterReturning
		return "cancel";
	}
	
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String idOriginale) {
		this.originalId = idOriginale;
	}

	public Map<String, LookupStrategy> getLookups() {
		return lookups;
	}

	public void setLookups(Map<String, LookupStrategy> lookups) {
		this.lookups = lookups;
	}

	public Class<ID> getIdClass() {
		return idClass;
	}

	public void setIdClass(Class<ID> idClass) {
		this.idClass = idClass;
	}

	public String getValidationContext()
	{
		return validationContext;
	}

	public void setValidationContext(String validationContext)
	{
		this.validationContext = validationContext;
	}
	
	/**
	 * E' avvenuto un errore di conversione per una proprietà dell'ogetto di modello
	 */
	public void onRegolaEvent(Event e)
	{
		ConversionErrorEvent error = (ConversionErrorEvent)e;
		conversionErrors.put(error.getPropertyName(), error.getMsg());
	}
	
	
	

	@SuppressWarnings("unchecked")
	@Override
	public FormPagePlug<T, ID, F> getPlug() {
		return (FormPagePlug<T, ID, F>) plug;
	}
	
	public void setPlug(FormPagePlug<T, ID, F> plug) {
		super.setPlug(plug);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormPageComponent<T, ID, F> getComponent() {
		return (FormPageComponent<T, ID, F>) component;
	}
	
	public void setComponent(FormPageComponent<T, ID, F> component) 
	{
	    super.setComponent(component);
	}
	
}
