package org.regola.events;

import org.apache.commons.lang.StringUtils;

public abstract class UserEventListener implements EventListener<UserEvent> {

	private String userId;

	public UserEventListener(String userId)
	{
		if(StringUtils.isEmpty(userId))
			throw new IllegalArgumentException("To register a user event listener the userId must be set");
		
		this.userId=userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public boolean waitForThis(UserEvent e) {
		return this.userId.equals(e.getUserId());
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends UserEvent> getEventClass() {
		Listener a = this.getClass().getAnnotation(Listener.class);
		return a.eventClass();
	}
}
