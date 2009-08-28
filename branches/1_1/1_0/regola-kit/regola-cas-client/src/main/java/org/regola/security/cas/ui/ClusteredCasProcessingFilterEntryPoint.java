package org.regola.security.cas.ui;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.cas.CasProcessingFilterEntryPoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.security.cas.util.SessionIdExtractor;

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

	private SessionIdExtractor sessionIdExtractor; 
	
    public void commence(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final AuthenticationException authenticationException)
            throws IOException, ServletException {
            final HttpServletRequest request = (HttpServletRequest) servletRequest;
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            final String urlEncodedService = response.encodeURL(getServiceProperties().getService());

            final StringBuffer buffer = new StringBuffer(255);

            String sessionid = getSessionId(request,urlEncodedService);
            
            synchronized (buffer) {
                buffer.append(getLoginUrl());
                buffer.append("?service=");
                buffer.append(URLEncoder.encode(urlEncodedService, "UTF-8"));
                buffer.append(getServiceProperties().isSendRenew() ? "&renew=true" : "");
                if(sessionid != null && sessionid.trim().length() > 0)
                	buffer.append("&").append(sessionIdExtractor.getSessionIdParamaterNameForCas()).append("=").append(sessionid);
            }

            logger.debug("redirect to: "+buffer.toString());
            response.sendRedirect(buffer.toString());
        }

	public String getSessionId(HttpServletRequest request, String urlEncodedService) 
	{
		return sessionIdExtractor.getSessionId(request, urlEncodedService);
	}

	public SessionIdExtractor getSessionIdExtractor() {
		return sessionIdExtractor;
	}

	public void setSessionIdExtractor(SessionIdExtractor sessionIdExtractor) {
		this.sessionIdExtractor = sessionIdExtractor;
	}
}
