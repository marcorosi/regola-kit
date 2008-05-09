package org.regola.webapp.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.userdetails.UserDetails;

/**
 * autentica i token di tipo UsernamePasswordAuthenticationToken
 * delegando a authenticationDao
 * 
 * @author marco
 *
 */
public class UsernamePasswordAuthenticationProvider extends
	AbstractUserDetailsAuthenticationProvider
{
	private final Log logger = LogFactory.getLog(getClass());
		
	private AuthenticationDao authenticationDao;
	
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

	public AuthenticationDao getAuthenticationDao() {
		return authenticationDao;
	}

	public void setAuthenticationDao(AuthenticationDao authenticationDao) {
		this.authenticationDao = authenticationDao;
	}
}
