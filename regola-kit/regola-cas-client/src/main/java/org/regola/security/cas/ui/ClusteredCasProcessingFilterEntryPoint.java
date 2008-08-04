package org.regola.security.cas.ui;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.cas.CasProcessingFilterEntryPoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ProcessingFilterEntryPoint x utilizzare CAS in un'applicazione in 
 * cluster con sticky session.
 * Rispetto all'implementazione originale aggiunge sempre il sessionid 
 * alla richiesta di redirect.
 *   
 * @author marco
 *
 */
public class ClusteredCasProcessingFilterEntryPoint extends CasProcessingFilterEntryPoint
{
	protected static final Log logger = LogFactory.getLog(ClusteredCasProcessingFilterEntryPoint.class);
	
	private String sessionIdParamaterName = "jsessionid"; 
	private String sessionIdParamaterNameForCas = "cas_aware_sessionid";
	
    public void commence(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final AuthenticationException authenticationException)
            throws IOException, ServletException {
            final HttpServletRequest request = (HttpServletRequest) servletRequest;
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            final String urlEncodedService = response.encodeURL(getServiceProperties().getService());

            final StringBuffer buffer = new StringBuffer(255);

            //String sessionid = getSessionId(request);
            
            String sessionid = urlEncodedService.substring(urlEncodedService.indexOf(";jsessionid=")+12);
            
            synchronized (buffer) {
                buffer.append(getLoginUrl());
                buffer.append("?service=");
                buffer.append(URLEncoder.encode(urlEncodedService, "UTF-8"));
                buffer.append(getServiceProperties().isSendRenew() ? "&renew=true" : "");
                if(sessionid != null && sessionid.trim().length() > 0)
                	buffer.append("&").append(sessionIdParamaterNameForCas).append("=").append(sessionid);
            }

            logger.debug("redirect to: "+buffer.toString());
            response.sendRedirect(buffer.toString());
        }

	private String getSessionId(HttpServletRequest request) 
	{
		//prima guardo se c'e il cookie
		if(request.getCookies() != null)
		{
			for(Cookie c : request.getCookies())
			{
				if(c.getName().equalsIgnoreCase(sessionIdParamaterName))
					return c.getValue();
			}			
		}
		System.out.println("QS: "+request.getQueryString());
		System.out.println("uri: "+request.getRequestURI());
		System.out.println("url: "+request.getRequestURL());
		System.out.println("id: "+request.getRequestedSessionId());
		//poi guardo sull'url
		if(request.getRequestedSessionId() != null)
			return request.getRequestedSessionId();

		return null;
	}

	public String getSessionIdParamaterName() {
		return sessionIdParamaterName;
	}

	public void setSessionIdParamaterName(String sessionIdParamaterName) {
		this.sessionIdParamaterName = sessionIdParamaterName;
	}

	public String getSessionIdParamaterNameForCas() {
		return sessionIdParamaterNameForCas;
	}

	public void setSessionIdParamaterNameForCas(String sessionIdParamaterNameForCas) {
		this.sessionIdParamaterNameForCas = sessionIdParamaterNameForCas;
	}
}
