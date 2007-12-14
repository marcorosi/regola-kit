package org.regola.webapp.security;

/**
 *  
 * @author marco
 *
 */
public class DaoAccessException extends RuntimeException {

	private static final long serialVersionUID = 6010587227296696779L;

	public DaoAccessException(String msg)
	{
		super(msg);
	}
	
	public DaoAccessException(String msg, Throwable t)
	{
		super(msg,t);
	}
}
