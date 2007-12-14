package org.regola.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventBroker {

    protected final Log log = LogFactory.getLog(getClass());
    
	private Map<Class<? extends Event>,List<EventListener>> listeners;
	
	public EventBroker()
	{
		listeners = new ConcurrentHashMap<Class<? extends Event>, List<EventListener>>();
	}
	
	public void publish(Event e) 
	{
		List<EventListener> list = getListenersForEvent(e);
		for(EventListener<Event> l : list)
		{
			l.onEvent(e);
		}
	}

	private List<EventListener> getListenersForEvent(Event e) 
	{
		log.info("Searching for someone interested in "+e.getClass());
		List<EventListener> result = new ArrayList<EventListener>();
		List<EventListener> list = listeners.get(e.getClass());
		if(list != null)
		{
			for(EventListener<Event> l : list)
			{
				if(l.waitForThis(e))
				{
					log.info("found "+l.toString());
					result.add(l);					
				}
			}			
		}
		return result;
	}

	//public <E extends Event> void subscribe(EventListener<E> listener, Class<E> eventClass) 
	//public void subscribe(EventListener<? extends Event> listener, Class<? extends Event> eventClass)
	public void subscribe(EventListener<? extends Event> listener)
	{
		if(listener == null)
			throw new IllegalArgumentException("event listener must be set");
		/*
		if(!listener.getEventClass().equals(eventClass))
			throw new IllegalArgumentException(String.format("listener %s is not compatible with event %s",
					listener.toString(),eventClass.toString()));
		*/
		
		Class<? extends Event> eventClass = listener.getEventClass();
		
		List<EventListener> list = listeners.get(eventClass);
		if(list == null)
		{
			list = new CopyOnWriteArrayList<EventListener>();
			listeners.put(eventClass, list);
		}
		
		list.add(listener);
	}

	public boolean unsubscribe(EventListener<? extends Event> listener) 
	{
		List<EventListener> list = listeners.get(listener.getEventClass());
		return list.remove(listener);
	}
}
