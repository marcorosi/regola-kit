package org.regola.webapp.action;

import org.regola.service.GenericManager;
import org.regola.util.Ognl;
import org.regola.webapp.action.lookup.LookupStrategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import org.regola.model.ModelPattern;

public class FormPage<T, ID extends Serializable, F extends ModelPattern> extends org.regola.webapp.action.BasePage
{
	private RenderManager renderManager;
	private GenericManager<T, ID> serviceManager;
	
	String errore;
	HashMap<String, String> errors = new HashMap<String, String>();
	HashMap<String, Effect> effects = new HashMap<String, Effect>();
	protected Map<String, LookupStrategy> lookups = new HashMap<String, LookupStrategy>();
	
	private PersistentFacesState state;
	@SuppressWarnings("unused")
	private IntervalRenderer clock;

	Effect effectPanel = null;

	protected Class<ID> idClass;
	protected ID id;
	protected T model;

	//rappresentazione in stringa dell'id originale
	//serve per i form dove viene modificata la chiave
	private String originalId;
	
	private String encodedId;
	
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
	
    public <MODEL, MODELID extends Serializable, FILTER extends ModelPattern> 
    void addAutoCompleteLookUp(String property,MODEL model, FILTER filter, GenericManager<MODEL, MODELID> manager)
    {
    	AutoCompleteBean<MODEL, MODELID , FILTER> ac = new AutoCompleteBean<MODEL, MODELID , FILTER>();
		ac.init(model,filter,manager);
		
		lookups.put(property, ac);
    }
	
	public Effect getEffectPanel()
	{
		return effectPanel;
	}

	public void setEffectPanel(Effect effect)
	{
		effectPanel = effect;
	}

	public RenderManager getRenderManager()
	{
		return renderManager;
	}

	public void setRenderManager(RenderManager renderManger)
	{
		this.renderManager = renderManger;
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

	public HashMap<String, Effect> getEffects()
	{
		return effects;
	}

	public PersistentFacesState getState()
	{
		return state;
	}

	public void setState(PersistentFacesState state)
	{
		this.state = state;
	}

	
	public void submit(ActionEvent event)
	{

	}

	public void init()
	{
		state = PersistentFacesState.getInstance();
		if(id != null) log.info("Init con id " + id);
	}
	
	@SuppressWarnings("unchecked")
	public <T> InvalidValue[] validate(T object)
	{
		//	clean errors and effects
		setEffectPanel(null);
		getErrors().clear();
		getEffects().clear();

		//validate bean
		ClassValidator<T> validator = new ClassValidator<T>((Class<T>) object.getClass());
		InvalidValue[] msgs = validator.getInvalidValues(object);

		//populate errors and effects
		for (InvalidValue msg : msgs)
		{
			errors.put(msg.getPropertyName(), msg.getMessage());
			effects.put(msg.getPropertyName(), new Highlight("#FFFF00"));
		}

		return msgs;
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
	{
		int idLength = validate(id).length;
		int modelLength = validate(model).length;
		if (idLength > 0 || modelLength > 0)
			return false;
		return true;
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
}
