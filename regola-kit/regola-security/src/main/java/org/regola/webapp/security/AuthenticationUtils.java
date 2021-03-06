package org.regola.webapp.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionContextIntegrationFilter;


public class AuthenticationUtils
{
	public static final Log log = LogFactory.getLog(AuthenticationUtils.class);

	/**
	 * esegue il login programmatico ad Acegi
	 * 
	 * @return l'url richiesto prima dell'autenticazione
	 * 
	 * @throws AuthenticationException
	 */
	static public String acegiProgrammaticLogin(String userName, String password, HttpServletRequest request,
			HttpSession session,AuthenticationManager am) throws AuthenticationException
	{
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userName,
				password);

		authReq.setDetails(new WebAuthenticationDetails(request));

		session.setAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY, userName);

		Authentication auth = am.authenticate(authReq);
		SecurityContext sessionSecCtx = (SecurityContext) session
				.getAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY);
		log.debug(String.format("SecurityContext from session [%s]", sessionSecCtx != null ? sessionSecCtx
				.toString() : "null"));
		SecurityContext secCtx = SecurityContextHolder.getContext();
		secCtx.setAuthentication(auth);
		log.debug(String.format("SecurityContext from holder [%s]", secCtx != null ? secCtx.toString() : "null"));
		log.debug("placing SecurityContext from holder into session");
		session.setAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY, secCtx);

		return AbstractProcessingFilter.obtainFullSavedRequestUrl(request);
	}
	

	
	/**
	 * Restituisce il cookie che si chiama nome
	 * 
	 * @param request
	 * @param nome
	 * @return
	 */
	public static Cookie searchCookie(HttpServletRequest request, String nome)
	{
		Cookie cookies[] = request.getCookies();

		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (nome.equals(cookies[i].getName()))
				{
					return cookies[i];
				}
			}
		}

		return null;
	}
}
