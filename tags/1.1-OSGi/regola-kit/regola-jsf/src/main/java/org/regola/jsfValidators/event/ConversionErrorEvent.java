package org.regola.jsfValidators.event;

import org.regola.events.Event;

public class ConversionErrorEvent implements Event
{
	private String propertyName;
	private String msg;
	
	public ConversionErrorEvent()
	{
		
	}
	
	public ConversionErrorEvent(String propertyName, String errorMsg)
	{
		this.propertyName = propertyName;
		this.msg = errorMsg;
	}
	
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public String getPropertyName()
	{
		return propertyName;
	}
	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}
	
	
}
