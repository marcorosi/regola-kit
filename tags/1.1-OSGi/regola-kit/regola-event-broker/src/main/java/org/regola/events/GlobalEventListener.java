package org.regola.events;

public abstract class GlobalEventListener implements EventListener<GlobalEvent> {
	
	public boolean waitForThis(GlobalEvent e) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends GlobalEvent> getEventClass() {
		Listener a = this.getClass().getAnnotation(Listener.class);
		return a.eventClass();
	}
}
