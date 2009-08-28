package org.regola.security.cas.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SessionIdExtractor {

	private String sessionIdParamaterName = "jsessionid"; 
	private String sessionIdParamaterNameForCas = "cas_aware_sessionid";
	
	private String getSessionIdParamaterPattern() 
	{
		return ";"+sessionIdParamaterName+"=";
	}
	
	public String getSessionIdFromCookie(HttpServletRequest request) 
	{
		if(request.getCookies() != null)
		{
			for(Cookie c : request.getCookies())
			{
				if(c.getName().equalsIgnoreCase(sessionIdParamaterName))
					return c.getValue();
			}			
		}
		return null;
	}

	public String getSessionId(HttpServletRequest request, String urlEncodedService) 
	{
		//prima guardo se c'e il cookie
		String id = getSessionIdFromCookie(request);

		if(id == null)
		{
			//poi guardo sull'url
			if(urlEncodedService.indexOf(getSessionIdParamaterPattern()) > 0)
				id = urlEncodedService.substring(urlEncodedService.indexOf(getSessionIdParamaterPattern())+getSessionIdParamaterPattern().length()); 
		}

		return id;
	}

	public String getSessionIdParamaterName() {
		return sessionIdParamaterName;
	}

	/**
	 * il nome del parametro sull'url che contiene il session id
	 * @param sessionIdParamaterName
	 */
	public void setSessionIdParamaterName(String sessionIdParamaterName) {
		this.sessionIdParamaterName = sessionIdParamaterName;
	}

	public String getSessionIdParamaterNameForCas() {
		return sessionIdParamaterNameForCas;
	}

	/**
	 * il nome del parametro che contiene il session id da passare nella richiesta a CAS 
	 * @param sessionIdParamaterNameForCas
	 */
	public void setSessionIdParamaterNameForCas(String sessionIdParamaterNameForCas) {
		this.sessionIdParamaterNameForCas = sessionIdParamaterNameForCas;
	}

}
