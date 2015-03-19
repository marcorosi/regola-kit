Regola-kit permette di integrare un'applicazione con un sistema di single sign on implementato con CAS (www.ja-sig.org/products/cas/).
L'applicazione potrà così sfruttare l'autenticazione di un server CAS esistente e sfruttare anche il servizio di proxy authentication.

Le istruzione sono le seguenti:

**aggiungere la dipendenza a regola-cas-client in pom.xml:
```
	 <dependency>
	    <groupId>org.regola</groupId>
	    <artifactId>regola-cas-client</artifactId>
	    <version>1.0.0</version>
	 </dependency>
```**

**aggiungere la servlet di callback in web.xml:
```
	<!-- servlet x il callback url -->
	<servlet>
	  <servlet-name>casproxy</servlet-name>
	  <servlet-class>org.regola.security.cas.ProxyTicketReceptor</servlet-class>
	</servlet>

	...

	<servlet-mapping>
	  <servlet-name>casproxy</servlet-name>
	  <url-pattern>/casProxy/*</url-pattern>
	</servlet-mapping>
```**

**aggiungere il file cas.propeties in WEB-INF:
```
#url del server cas
cas.securityContext.proxyReceptor.proxyUrl=https://localhost:8443/cas/proxy
cas.securityContext.casProcessingFilterEntryPoint.loginUrl=https://localhost:8443/cas/login
cas.securityContext.casProxyTicketValidator.casValidate=https://localhost:8443/cas/proxyValidate
#url relativi a questa applicazione
cas.service.standard=https://localhost:8443/regola-application/j_acegi_cas_security_check
cas.service.proxied=http://localhost:8080/regola-application/service/
cas.service.proxyCallbackUrl=https://localhost:8443/regola-application/casProxy
```**

