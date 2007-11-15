package org.regola.webapp.security;

import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.dao.DataAccessException;

/**
 * Interfaccia per i servizi di autenticazione tramite username e password
 * 
 * @author marco
 */
public interface AuthenticationDao {

	/**
	 * verify the user identity 
	 * 
	 * @param username
	 * @param password
	 * 
	 * @return a fully populated user record (never <code>null</code>)
	 * 
	 * @throws BadCredentialsException if the user cannot be authenticated 
	 * @throws DataAccessException if user could not be found for a repository-specific reason
	 * @throws AuthenticationServiceException if the implementation violate the interface contract
	 * 
	 */
	UserDetails authenticate(String username, String password) 
		throws DataAccessException, BadCredentialsException, AuthenticationServiceException;

}
