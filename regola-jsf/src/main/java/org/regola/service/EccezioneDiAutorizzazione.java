package org.regola.service;

public class EccezioneDiAutorizzazione extends Exception
{
	private static final long serialVersionUID = 5258534345477394226L;

	public EccezioneDiAutorizzazione()
	{
		super();
	}

	public EccezioneDiAutorizzazione(String s)
	{
		super(s);
	}

	public EccezioneDiAutorizzazione(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EccezioneDiAutorizzazione(Throwable cause)
	{
		super(cause);
	}
}
