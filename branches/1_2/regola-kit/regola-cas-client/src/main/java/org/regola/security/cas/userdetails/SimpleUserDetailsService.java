package org.regola.security.cas.userdetails;

import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;

/**
 * Questa è un semplice esempio di integrazione con cas; una volta effettuata
 * l'autenticazione centralizzata viene richiamato il metodo
 * {@link #loadUserByUsername(String)} passandogli come unico parametro il netID
 * dell'utente appena autenticato.
 * 
 * 
 * @author nicola
 * 
 */
public class SimpleUserDetailsService implements UserDetailsService {

	private static final GrantedAuthorityImpl[] autorizzazioniStandard = { new GrantedAuthorityImpl("user"), new GrantedAuthorityImpl("admin") };

	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		return new User(s, "", true, autorizzazioniStandard);
	}

}




 