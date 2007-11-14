package org.regola.webapp.action;

import javax.faces.event.ActionEvent;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.hibernate.validator.NotEmpty;
import org.regola.webapp.security.AuthenticationUtils;

import com.icesoft.faces.context.effects.Shake;

/**
 * JSF backing bean for the login page 
 * 
 * @author marco
 *
 */
public class LoginForm extends FormPage
{
	String username = "";
	String password = "";

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
	public void submit(ActionEvent event)
	{
		super.submit(event);
		
		if (validate(this).length > 0)
			return;

		try
		{
			String targetUrl = AuthenticationUtils.acegiProgrammaticLogin(getUsername(), getPassword(), getRequest(),
					getSession(), authenticationManager);
			setErrore("Login effettuato");
			
			if(targetUrl != null)
			{
				String ctxPath = getRequest().getContextPath();
			    int idx = targetUrl.indexOf(ctxPath);
			    String target = targetUrl.substring(idx + ctxPath.length());
			    
			    log.debug(String.format("authentication successful, forwarding to %s obtained from %s", target, targetUrl));
			    
			    forward(target);				
			}
			
		} catch (AuthenticationException e)
		{
			log.debug(e.getMessage());
			setErrore("Username/Password errate ("+e.getMessage()+")");
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
