package org.regola.security.cas.util;

public class ClusteredUtils {

	private static final String SEPARATOR = "#clustered_cas_utils#";

	public static String encodePassword(String password, String sessionId) 
	{
		return password+SEPARATOR+sessionId;
	}

	public static String decodePassword(String encpass) {
		if(encpass == null)
			throw new IllegalArgumentException("La string da decodificare è null");
		
		if(encpass.indexOf(SEPARATOR) < 0)
			throw new IllegalArgumentException("Impossibile decodificare la password. É stata codificata in maniera corretta con encodePassword()?");
			
		return encpass.substring(0,encpass.indexOf(SEPARATOR));
	}

	public static String decodeSessionId(String encpass) {
		if(encpass == null)
			throw new IllegalArgumentException("La string da decodificare è null");

		if(encpass.indexOf(SEPARATOR) < 0)
			throw new IllegalArgumentException("Impossibile decodificare la password. É stata codificata in maniera corretta con encodePassword()?");

		return encpass.substring(encpass.indexOf(SEPARATOR)+SEPARATOR.length(),encpass.length());
	}
	
}
