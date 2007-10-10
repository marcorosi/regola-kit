package org.regola.service;

import org.acegisecurity.AuthenticationException;

public class EccezioneAutenticazioneFallita extends AuthenticationException
{
	private static final long serialVersionUID = 5258534345477394226L;

	/*
	public EccezioneAutenticazioneFallita()
	{
		super();
	}
	*/

	public EccezioneAutenticazioneFallita(String s)
	{
		super(s);
	}

	public EccezioneAutenticazioneFallita(String message, Throwable cause)
	{
		super(message, cause);
	}

	/*
	public EccezioneAutenticazioneFallita(Throwable cause)
	{
		super(cause);
	}
	*/
}
