package ${package}.webapp.security;

import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * Semplice esempio di hook per la profilazione dell'utente.
 *
 * Il metodo {@link #loadUserByUsername(String)} viene richiamato da Spring Security
 * una volta eseguita l'autenticazione.
 * 
 */
public class SimpleUserDetailsService implements UserDetailsService {

	private static final GrantedAuthorityImpl[] autorizzazioniStandard = { new GrantedAuthorityImpl("user"), new GrantedAuthorityImpl("admin") };

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return new User(username, "", true, autorizzazioniStandard);
	}

}
