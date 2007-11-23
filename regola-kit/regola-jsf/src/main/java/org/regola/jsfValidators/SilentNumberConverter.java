package org.regola.jsfValidators;

import java.lang.reflect.InvocationTargetException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.regola.events.DuckTypingEventBroker;
import org.regola.jsfValidators.event.ConversionErrorEvent;

/*
 * Non interrompe il jsf life-cycle impedendo il lancio dell'eccezione
 *  di conversione.
 *  Vale solo per i tipi Number
 */
public class SilentNumberConverter implements Converter
{
	
	protected static final String CONVERSION_ERROR_MESSAGE = "formato errato";

	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value)
			throws ConverterException
	{
        if (facesContext == null) throw new NullPointerException("facesContext");
        if (uiComponent == null) throw new NullPointerException("uiComponent");
        if (value == null || value.trim().equals("")) return null;
        
        Class targetClass = uiComponent.getValueBinding("value").getType(facesContext);

        try
		{
        	//vengono intercettati anche i binding ai tipi primitivi
        	if(targetClass.getCanonicalName().equals("int"))
        		return new Integer(value).intValue();
        	if(targetClass.getCanonicalName().equals("float"))
        		return new Float(value).floatValue();
        	if(targetClass.getCanonicalName().equals("double"))
        		return new Double(value).doubleValue();
        	if(targetClass.getCanonicalName().equals("long"))
        		return new Long(value).longValue();
        	if(targetClass.getCanonicalName().equals("short"))
        		return new Short(value).shortValue();
        
			return targetClass.getConstructor(String.class).newInstance(value);
			
		} catch (InvocationTargetException e)
		{
			if(e.getCause().getClass().getCanonicalName().equals("java.lang.NumberFormatException"))
			{
				//TODO: generare msg errore e trasmetterlo
				String propertyName = getPropertyName(uiComponent.getValueBinding("value").getExpressionString());
				
				notifyError(facesContext, propertyName, CONVERSION_ERROR_MESSAGE);
				
				//System.out.println("Errore di conversione per " + propertyName);
				return null;
			}else
				throw new RuntimeException(e);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value)
			throws ConverterException
	{
		return ""+value;
	}
	
	protected String getPropertyName(String valueBindingExpression)
	{
		return valueBindingExpression
				.replaceAll("}", "")
					.substring(valueBindingExpression.lastIndexOf(".")+1);
	}
	
	protected void notifyError(FacesContext context, String propertyName, String msg)
	{
		/*
		 * versione con collector
		 */
		//ConversionErrorsCollector.collectConversionError(context, propertyName, CONVERSION_ERROR_MESSAGE);
		
		/*
		 * versione con Event Broker
		 */		
		DuckTypingEventBroker eventBroker = (DuckTypingEventBroker) context.getApplication()
		        .getVariableResolver().resolveVariable(context, "eventBroker");
		/*
		 * oppure:
		 * DuckTypingEventBroker eventBroker = context.getExternalContext().getSessionMap().get("eventBroker");
		 */
		
		eventBroker.publish("form.errors.conversion", new ConversionErrorEvent(propertyName, msg));
	}

}
