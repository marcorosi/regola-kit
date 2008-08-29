package org.regola.webapp.util;

import org.regola.util.Ognl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Questa classe è utile per rendere uniforme l'accesso a oggetti di modello di tipo 
 * diverso dalle JSF
 *   
 * @author marco
 *
 * @param <T> l'oggetto di modello 
 */
public class ReflectionMap<T> extends HashMap<String, Object> implements Map<String, Object>
{
	private static final long serialVersionUID = 1L;

	protected final Log log = LogFactory.getLog(getClass());
	
	T model;
	
	public ReflectionMap(T model)
	{
		if(model == null)
			throw new IllegalArgumentException("Model is null");
		
		this.model=model;
	}
	
	@Override
	public Object get(Object key) {
		try {
			Object o = Ognl.getValue((String)key, model);
			if(log.isDebugEnabled())
				log.debug(String.format("get %s property from object %s returned %s",key,model,o));
			return o;
		} catch (RuntimeException e) {
			log.error(String.format("unable to get %s property from object %s",key,model));
			return null;
		}
	}

	@Override
	public Object put(String key, Object value) {
		try {
			Ognl.setValue((String)key, model, value);
			return this.get(key);  
		} catch (RuntimeException e) {
			log.error(String.format("unable to set %s property to %s in object %s",key,value,model));
			return null;
		}
	}
}
