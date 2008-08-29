package org.regola.security.cas.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.cas.CasProcessingFilter;
import org.regola.security.cas.util.ClusteredUtils;
import org.regola.security.cas.util.SessionIdExtractor;

/**
 * Processa un CAS service ticket con il supporto per il funzionamento in cluster
 * con sticky session. 
 * Vedere {@link org.acegisecurity.ui.cas.CasProcessingFilter}
 * Modificato per aggiungere anche il parametro della lingua passato da CAS.
 * 
 * @author marco
 *
 */
public class ClusteredCasProcessingFilterI18n extends CasProcessingFilter {

    private String languageParameter = "siteLanguage";

    private SessionIdExtractor sessionIdExtractor;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request)
    	throws AuthenticationException 
    {
	    String username = CAS_STATEFUL_IDENTIFIER;
	    String password = request.getParameter("ticket");
	    String sessionId = sessionIdExtractor.getSessionIdFromCookie(request);
	    
	    if (password == null) {
	        password = "";
	    }
	
	    if(sessionId == null)
	    	throw new RuntimeException("Impossibile ricavare il session id dalla richiesta. Il funzionamento in cluster non è possibile.");
	    
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, ClusteredUtils.encodePassword(password,sessionId));
	
	    authRequest.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));
	
	    return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    @Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException {
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication success: " + authResult.toString());
            }

            SecurityContextHolder.getContext().setAuthentication(authResult);

            if (logger.isDebugEnabled()) {
                logger.debug("Updated SecurityContextHolder to contain the following Authentication: '" + authResult + "'");
            }

            // Don't attempt to obtain the url from the saved request if alwaysUsedefaultTargetUrl is set
            String targetUrl = isAlwaysUseDefaultTargetUrl() ? null : obtainFullRequestUrl(request);

            if (targetUrl == null) {
                targetUrl = getDefaultTargetUrl();
            }

            //parametro della lingua
           	targetUrl = aggiungiParametroLingua(targetUrl,request);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Redirecting to target URL from HTTP Session (or default): " + targetUrl);
            }

            onSuccessfulAuthentication(request, response, authResult);

            getRememberMeServices().loginSuccess(request, response, authResult);

            // Fire event
            if (this.eventPublisher != null) {
                eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
            }

            sendRedirect(request, response, targetUrl);
        }

	private String aggiungiParametroLingua(String targetUrl, HttpServletRequest request) 
	{
		String language = request.getParameter(getLanguageParameter());
		
		if(language==null || language.trim().length()==0)
			return targetUrl;
        
		StringBuilder sb = new StringBuilder();
		sb.append(targetUrl);
        if(targetUrl.indexOf("?") > 0)
        	sb.append("&");
        else
        	sb.append("?");
		sb.append(getLanguageParameter())
			.append("=")
			.append(language);
		
		return sb.toString();
	}

	public String getLanguageParameter() {
		return languageParameter;
	}

	/**
	 * Imposta il nome del parametro da passare nella URL 
	 * @param languageParameter
	 */
	public void setLanguageParameter(String languageParameter) {
		this.languageParameter = languageParameter;
	}

	public void setSessionIdExtractor(SessionIdExtractor sessionIdExtractor) {
		this.sessionIdExtractor = sessionIdExtractor;
	}
}
