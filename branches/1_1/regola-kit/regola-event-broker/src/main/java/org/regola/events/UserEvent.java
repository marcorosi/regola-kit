package org.regola.events;

import org.apache.commons.lang.StringUtils;

/**
 * This is the base class for user specific events.
 * Use this for events that must be notified only to a specific user 
 * 
 * @author marco
 *
 */
public abstract class UserEvent implements Event {

	private String userId;

	public UserEvent(String userId)
	{
		if(StringUtils.isEmpty(userId))
			throw new IllegalArgumentException("To register a user event the userId must be set");
		
		this.userId=userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
}
