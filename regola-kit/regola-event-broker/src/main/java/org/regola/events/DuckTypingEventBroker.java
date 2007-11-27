package org.regola.events;


import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DuckTypingEventBroker {
	
    protected final Log log = LogFactory.getLog(getClass());
    
	//area, lista di sottoscrittori
    private Map<String,List<Object>> listeners;
	
	public DuckTypingEventBroker()
	{
		listeners = new ConcurrentHashMap<String,List<Object>>();
	}
	
	public void publish(String area, Event e) 
	{
		if (e==null) e= new EmptyEvent();
		
		List<Object> list = getListenersForEvent(area);
		for(Object l : list)
		{
			Method onRegolaEvent = findListenerMethod(l.getClass());
			Object paramsObj[] = {e};
						
			try {
				onRegolaEvent.invoke(l, paramsObj);
			} catch (Exception e1) {
				throw new RuntimeException("Could not call method onRegolaEvent on this class " + l.getClass().getCanonicalName());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	Method findListenerMethod(Class clazz)
	{
		Class params[] = {Event.class};
	    
		Method onRegolaEvent;
		try {
			//onRegolaEvent = clazz.getDeclaredMethod("onRegolaEvent", params);
			//faccio cercare anche fra i metodi ereditati
			onRegolaEvent = clazz.getMethod("onRegolaEvent", params);
		} catch (SecurityException e1) {
			log.error("Violazione di sicurezza invocando il metodo onRegolaEvent di " + clazz.getCanonicalName(),e1);
			return null;
		} catch (NoSuchMethodException e1) {
			log.error("Non esiste il metodo onRegolaEvent di " + clazz.getCanonicalName(),e1);
			return null;
		}
		
		return onRegolaEvent;
		
	}

	private List<Object> getListenersForEvent(String e) 
	{
		log.info("Searching for someone interested in "+e.getClass());
		return listeners.get(e);
		
	}

	//public <E extends Event> void subscribe(EventListener<E> listener, Class<E> eventClass) 
	//public void subscribe(EventListener<? extends Event> listener, Class<? extends Event> eventClass)
	public void subscribe(Object listener, String area)
	{
		if(listener == null)
			throw new IllegalArgumentException("event listener must be set");
		
		List<Object> list = listeners.get(area);
		if(list == null)
		{
			list = new CopyOnWriteArrayList<Object>();
			listeners.put(area, list);
		}
		
		list.add(listener);
	}

	public boolean unsubscribe(Object listener, String area) 
	{
		List<Object> list = listeners.get(area);
		return list.remove(listener);
	}
	
	/*
	 * Rimozione listener da tutte le aree.
	 */
	public void unsubscribe(Object listener) 
	{
		for (String area : listeners.keySet())
		{
			List<Object> list = listeners.get(area);
			boolean b = list.remove(listener);
			if(b)
				log.info("[DuckTypingEventBroker] - effettuata unsubscribe di "+listener + " dall'area " + area);
		}
	}
}
