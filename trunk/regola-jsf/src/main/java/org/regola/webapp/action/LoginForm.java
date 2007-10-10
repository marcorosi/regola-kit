package org.regola.webapp.action;

import org.regola.webapp.util.FacesUtils;
import org.regola.webapp.util.ServiziDiAutenticazione;

import javax.faces.event.ActionEvent;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.hibernate.validator.NotEmpty;

import com.icesoft.faces.context.effects.Shake;

//@Bean(name = "loginForm", scope = Scope.SESSION)
public class LoginForm extends FormPage
{
	String username = "";
	String password = "";

	//@Property(value = "#{authenticationManager}")
	private AuthenticationManager authenticationManager;

	@NotEmpty
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@NotEmpty
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
	
	@Override
	public void init()
	{
		super.init();
	}

	
	@Override
	public void submit(ActionEvent event)
	{
		super.submit(event);
		
		if (validate(this).length > 0)
			return;

		try
		{
			ServiziDiAutenticazione.autenticaConAcegi(getUsername(), getPassword(), FacesUtils.getRequest(),
					FacesUtils.getSession(), authenticationManager);
			setErrore("Login effettuato");
		} catch (AuthenticationException e)
		{
			setErrore("Username/Password errate.");
			setEffectPanel(new Shake());
			setPassword("");
		}
	}


	public AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

}
