package org.regola.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * potrebbe servire se ci accorgiamo che l'altro è lento.
 * @author marco
 */
public class FastEventBroker {

    protected final Log log = LogFactory.getLog(getClass());
    
	private Map<Class<? extends Event>,List<EventListener>> listeners;
	private Map<Class<? extends UserEvent>,List<UserEventListener>> userListeners;
	
	public FastEventBroker()
	{
		listeners = new ConcurrentHashMap<Class<? extends Event>, List<EventListener>>();
		userListeners = new ConcurrentHashMap<Class<? extends UserEvent>, List<UserEventListener>>();
	}
	
	public void publish(Event e) 
	{
		List<EventListener> list = getListenersForEvent(e);
		for(EventListener l : list)
		{
			l.onEvent(e);
		}
	}

	private List<EventListener> getListenersForEvent(Event e) 
	{
		log.info("Searching for someone interested in "+e.getClass());
		List<EventListener> list = new ArrayList<EventListener>();
		
		if(e instanceof GlobalEvent)
		{
			for(Class<? extends Event> c : listeners.keySet())
			{
				if(c.isAssignableFrom(e.getClass()))
				{
					log.info("found "+c.toString());
					list.addAll(listeners.get(c));
				}
			}
		} else 
		{
			for(Class<? extends UserEvent> c : userListeners.keySet())
			{
				if(c.isAssignableFrom(e.getClass()))
				{
					log.info("found "+c.toString());
					for(UserEventListener l : userListeners.get(c))
					{
						if(((UserEvent)e).getUserId().equals(l.getUserId()))
							list.add(l);
					}
				}
			}			
		}
		return list;
	}

	public void subscribe(EventListener listener, Class<? extends GlobalEvent> eventClass) 
	{
		List<EventListener> list = listeners.get(eventClass);
		if(list == null)
		{
			list = new CopyOnWriteArrayList<EventListener>();
			listeners.put(eventClass, list);
		}
		
		list.add(listener);
	}

	public void subscribe(UserEventListener listener, Class<? extends UserEvent> eventClass) 
	{
		List<UserEventListener> list = userListeners.get(eventClass);
		if(list == null)
		{
			list = new ArrayList<UserEventListener>();
			userListeners.put(eventClass, list);
		}
		
		list.add(listener);
	}

}
