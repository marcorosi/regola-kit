package org.regola.security.cas.ui;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.cas.CasProcessingFilter;
import org.regola.security.cas.SessionIdNotFoundException;
import org.regola.security.cas.util.ClusteredUtils;
import org.regola.security.cas.util.SessionIdExtractor;

/**
 * Estende ClusteredCasProcessingFilterI18n aggiungendo la rigenerazione
 * del session id dopo l'autenticazione per prevenire attacchi di session
 * fixation.
 * 
 * Nota: incompatibile con le IceFaces
 * 
 * @author marco
 * 
 */
public class ClusteredCasProcessingFilterI18nSecure extends ClusteredCasProcessingFilterI18n {

	/**
	 * per motivi di sicurezza invalida la sessione creata prima del login 
	 */
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException {
		HttpSession oldSession = request.getSession(false);

		HashMap<String, Object> tmp = new HashMap<String, Object>();

		if (oldSession != null) {

			Enumeration enumer = oldSession.getAttributeNames();

			while (enumer.hasMoreElements()) {

				String s = (String) enumer.nextElement();

				tmp.put(s, oldSession.getAttribute(s));

			}

			if (logger.isDebugEnabled())
				logger.debug("Sessione " + oldSession.getId() + " valida? "
						+ request.isRequestedSessionIdValid());
			oldSession.invalidate();

		}

		HttpSession newSession = request.getSession(true);

		if (logger.isDebugEnabled())
			logger.debug("E adesso sessione " + newSession.getId() + " valida? "
					+ request.isRequestedSessionIdValid());

		for (Map.Entry<String, Object> entry : tmp.entrySet()) {

			newSession.setAttribute(entry.getKey(), entry.getValue());

		}

		super.onSuccessfulAuthentication(request, response, authResult);
	}
}
