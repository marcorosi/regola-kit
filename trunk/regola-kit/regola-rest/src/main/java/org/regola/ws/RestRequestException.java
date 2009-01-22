package org.regola.ws;

public class RestRequestException extends RuntimeException {

	private static final long serialVersionUID = -3818590982328863748L;

	public RestRequestException(String msg, Throwable t)
	{
		super(msg,t);
	}

	public RestRequestException(Throwable t)
	{
		super(t);
	}

	public RestRequestException(String msg)
	{
		super(msg);
	}
	
}
