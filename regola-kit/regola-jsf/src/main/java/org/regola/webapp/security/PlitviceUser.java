package org.regola.webapp.security;

import org.regola.model.ContestoPlitvice;

import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;

public class PlitviceUser extends User
{
	private static final long serialVersionUID = 3487089368654950179L;

	private ContestoPlitvice contesto;
	
	public PlitviceUser(String username, String password, GrantedAuthorityImpl[] autorizzazioni, ContestoPlitvice c) 
	{
		super(username, password, true, true, true, true, autorizzazioni);
		contesto = c;
	}

	public ContestoPlitvice getContesto()
	{
		return contesto;
	}	
}
