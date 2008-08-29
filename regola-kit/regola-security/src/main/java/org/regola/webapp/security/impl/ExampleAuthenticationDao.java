package org.regola.webapp.security.impl;


import org.regola.webapp.security.AuthenticationDao;
import org.springframework.dao.DataAccessException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;

/**
 * implementazione di esempio per il servizio di autenticazione
 *  
 * @author marco
 */
public class ExampleAuthenticationDao implements AuthenticationDao {

	private String username = "";
	private String password = "";
	
	private static final GrantedAuthorityImpl[] autorizzazioniStandard = {
		new GrantedAuthorityImpl("user"),
		new GrantedAuthorityImpl("admin") };

	public UserDetails authenticate(String username, String password)
			throws DataAccessException, BadCredentialsException,
			AuthenticationServiceException 
	{
		if(this.username.equals(username) && this.password.equals(password))
		{
			//this object will be binded to the security context
			return new User(username,password,true,true,true,true,autorizzazioniStandard);
		} else {
			throw new BadCredentialsException("prova con: "+this.username+" "+this.password);
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
