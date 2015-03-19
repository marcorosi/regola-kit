# Introduzione #

In Regola la sicurezza è implementata con Acegi http://www.acegisecurity.org/.
In questa pagina vengono riportati alcuni dettagli dell'integrazione.

# Dettagli #

In Acegi l'operazione di autenticazione è compito dell'AuthenticationManager che la delega a sua volta a uno o più AuthenticationProvider.
In generale ogni AuthenticationProvider è competente per un particolare tipo di richiesta di autenticazione (ad esempio autenticazione tramite username e password, autenticazione tramite certificato X.509 ecc.).


Nel caso di una classica autenticazione con username e password la best practice di Acegi sarebbe di utilizzare DaoAuthenticationProvider e definire un proprio UserDetailsService. Questo servizio ricava i dettagli di un utente a partire dalla **sola username** e spetta poi al framework verificare la correttezza della password e comportarsi di conseguenza.


In alcuni casi però questo sistema risulta restrittivo, ad esempio capita di delegare l'autenticazione a servizi esterni (come un web service) che eseguono essi stessi la verifica delle credenziali.


Per questo motivo in Regola si è deciso di definire un proprio AuthenticationProvider (org.regola.webapp.security.UsernamePasswordAuthenticationProvider) che delega l'autenticazione passando sia username che password.
Per l'utente finale si tratterà quindi di implementare l'interfaccia org.regola.webapp.security.AuthenticationDao.


Esiste già un'implementazione di esempio ExampleAuthenticationDao che al momento si trova in regola-jsf. Forse sarebbe più comodo per l'utente averla nell'archetipo in modo da accedere immediamente al codice.


# Da fare #

  * Aggiungere un metodo a BasePage che ritorna i dettagli dell'utente autenticato
  * Aggiungere documentazione su Acegi?