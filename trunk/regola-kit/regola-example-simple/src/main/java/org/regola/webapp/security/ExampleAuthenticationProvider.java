package org.regola.webapp.security;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExampleAuthenticationProvider extends
	AbstractUserDetailsAuthenticationProvider
{
	private final Log logger = LogFactory.getLog(getClass());
	
	private static final GrantedAuthorityImpl[] autorizzazioniStandard = {
		new GrantedAuthorityImpl("user"),
		new GrantedAuthorityImpl("admin") };
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1) throws AuthenticationException
	{
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("Autenticazione di " + username);
		}

		String password = (String) authentication.getCredentials();
		
		if("test".equals(username) && "test".equals(password))
		{
			//this object will be binded to the security context
			return new User(username,password,true,true,true,true,autorizzazioniStandard);
		} else {
			throw new BadCredentialsException("prova con: test test");
		}
	}
}
