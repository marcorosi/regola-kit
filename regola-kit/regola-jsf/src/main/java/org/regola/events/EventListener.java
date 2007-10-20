package org.regola.events;

public interface EventListener<E extends Event> 
//public interface EventListener
{
	public void onEvent(E e);
	//public void onEvent(Event e);

	//public boolean waitForThis(Event e);
	public boolean waitForThis(E e);
	
	public Class<? extends E> getEventClass();
}
