package org.regola.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Questa classe serve esclusivamente per consentire agli archetipi di generare
 * in automatico la pagina di welcome. Non è pensata per essere usata realmente
 * in produzione o per essere estesa in qualche modo.
 * 
 * @author nicola
 * 
 */
public final class WelcomeController extends AbstractController {
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws Exception {
		// TODO Auto-generated method stub
		return new ModelAndView("welcome");

	}

}
