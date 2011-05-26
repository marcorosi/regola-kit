package org.regola.webapp.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * autentica i token di tipo UsernamePasswordAuthenticationToken
 * delegando a authenticationDao
 * 
 * @author marco
 *
 */
public class UsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{
	private final Log logger = LogFactory.getLog(getClass());
		
	private AuthenticationDao authenticationDao;
	

	public AuthenticationDao getAuthenticationDao() {
		return authenticationDao;
	}

	public void setAuthenticationDao(AuthenticationDao authenticationDao) {
		this.authenticationDao = authenticationDao;
	}

	@Override
	  protected void additionalAuthenticationChecks(UserDetails userDetails,
	            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
			
		}

	@Override
	protected org.springframework.security.core.userdetails.UserDetails retrieveUser(
			String username,
			org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication)
			throws org.springframework.security.core.AuthenticationException {
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication for " + username);
		}

		String password = (String) authentication.getCredentials();
		UserDetails result = null;
		try {
			result = authenticationDao.authenticate(username,password);			
        } catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (result == null) {
            throw new AuthenticationServiceException("AuthenticationDao returned null, which is an interface contract violation");
        }
        
        return result;

	}
}
