package org.regola.webapp.util;

import org.regola.model.ContestoPlitvice;
import org.regola.model.Moduli;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.WebAuthenticationDetails;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiziDiAutenticazione
{
	private static final String urlLoginJaas = "/jaas.jsp";
	private static final String urlPostJaas = "/j_security_check";
	public static final String TOKEN = "PLITVICE_TOKEN_AUTORIZZAZIONE";
	@SuppressWarnings("unused")
	private static final String RUOLI = "plitvice_ruoli";
	protected static final String ULTIMOMODULO = "plitvice_ultimo_modulo";
	public static final String ANNO_ACCADEMICO = "PLITVICE_ANNO_ACCADEMICO";
	//private static final String PRINCIPAL = "plitvice_principal";
	
	public static final Log log = LogFactory.getLog(ServiziDiAutenticazione.class);

	private static ThreadLocal<ContestoPlitvice> contesto = new ThreadLocal<ContestoPlitvice>();

	public static ContestoPlitvice getContesto()
	{
		return contesto.get();
	}

	public static void setContesto(ContestoPlitvice contesto)
	{
		ServiziDiAutenticazione.contesto.set(contesto);
	}


	/**
	 * Restituisce il cookie che si chiama nome
	 * @param request
	 * @param nome
	 * @return
	 */
	public static Cookie trovaCookie(HttpServletRequest request, String nome)
	{
		Cookie cookies[] = request.getCookies();

		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (nome.equals(cookies[i].getName()))
				{
					return cookies[i];
				}
			}
		}

		return null;
	}

	/**
	 * Costruisce una url completa di protocollo, host e porta
	 * 
	 * @param request
	 * @param response
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public static String costruisciUrl(ServletRequest request, ServletResponse response, String url)
			throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		String contextPath = req.getContextPath();

		boolean includePort = true;
		int serverPort = req.getServerPort();

		if ("http".equals(scheme.toLowerCase()) && (serverPort == 80))
		{
			includePort = false;
		}

		if ("https".equals(scheme.toLowerCase()) && (serverPort == 443))
		{
			includePort = false;
		}

		String redirectUrl = scheme + "://" + serverName + ((includePort) ? (":" + serverPort) : "")
				+ contextPath + url;

		return redirectUrl;
	}

	/**
	 * Effettua il login jaas nel modo peggiore possibile
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @throws ServletException
	 */
	public static void loginJaas(HttpServletRequest request, HttpServletResponse response,
			String username) throws ServletException
	{
		HttpClient client = new HttpClient();
		HttpMethod method = null;
		try
		{
			Cookie sessioneID = trovaCookie(request, "JSESSIONID");

			//prima bisogna fare la GET
			method = new GetMethod(costruisciUrl(request, response, urlLoginJaas));
			method.setRequestHeader("Cookie", "JSESSIONID=" + sessioneID.getValue());
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK)
			{
				System.err.println("Login JAAS Method failed: " + method.getStatusLine());
			}

			//byte[] responseBody = get.getResponseBody();
			//System.out.println(new String(responseBody));

			//poi la POST
			method = new PostMethod(costruisciUrl(request, response, urlPostJaas));

			NameValuePair[] data = { new NameValuePair("j_username", username),
					new NameValuePair("j_password", username) };
			((PostMethod) method).setRequestBody(data);
			method.setRequestHeader("Cookie", "JSESSIONID=" + sessioneID.getValue());
			statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK)
				return;

			if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY)
			{
				System.err.println("Login JAAS Method failed: " + method.getStatusLine());
			}

			String redirectLocation = method.getResponseHeader("location").getValue();
			;

			method = new GetMethod(redirectLocation);
			method.setRequestHeader("Cookie", "JSESSIONID=" + sessioneID.getValue());

			statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK)
			{
				System.err.println("Login JAAS Method failed: " + method.getStatusLine());
			}

		} catch (HttpException e)
		{
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e)
		{
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable e)
		{
			e.printStackTrace();
		} finally
		{
			//Release the connection
			method.releaseConnection();
		}
	}

	/**
	 * Effettua alcune configurazioni necessarie all'applicazione
	 * web. Da chiamare subito dopo evere effettuato un'autenticazione
	 * valida. Nel dettaglio l'imposta l'anno, il ruolo e l'ultimo modulo.
	 * 
	 * @param autenticazione
	 * @param request
	 * @param response
	 */
	public static void postAutenticazioneWeb(ContestoPlitvice autenticazione,
			HttpServletRequest request, HttpServletResponse response)
	{
		request.getSession().setAttribute(TOKEN, autenticazione.getPrincipal());
		request.getSession().setAttribute(ANNO_ACCADEMICO, autenticazione.getAnno());
		request.getSession().setAttribute(ULTIMOMODULO,
				Moduli.valueOf(autenticazione.getPaginaIniziale()));
	}
	
	
	static public void autenticaConAcegiViaToken(String token ,HttpServletRequest request,
			HttpSession session,AuthenticationManager am) throws AuthenticationException
	{
		autenticaConAcegi(token, "", request,session,am);
	}
	

	static public void autenticaConAcegi(String userName, String password, HttpServletRequest request,
			HttpSession session,AuthenticationManager am) throws AuthenticationException
	{
		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userName,
				password);

		authReq.setDetails(new WebAuthenticationDetails(request));

		session.setAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY, userName);

		Authentication auth = am.authenticate(authReq);
		SecurityContext sessionSecCtx = (SecurityContext) session
				.getAttribute(HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY);
		log.debug(String.format("SecurityContext from session [%s]", sessionSecCtx != null ? sessionSecCtx
				.toString() : "null"));
		SecurityContext secCtx = SecurityContextHolder.getContext();
		log.debug(String.format("SecurityContext from holder [%s]", secCtx != null ? secCtx.toString() : "null"));
		secCtx.setAuthentication(auth);
		log.debug("placing SecurityContext from holder into session");
		session.setAttribute(HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY, secCtx);
		
	}
	
	static public String getFullRequestUrl(HttpServletRequest request)
	{
		
		String targetUrl = AbstractProcessingFilter.obtainFullRequestUrl(request);
		if (targetUrl==null) targetUrl="";
		
		if (!targetUrl.startsWith("http://") && !targetUrl.startsWith("https://")) {
			targetUrl = request.getContextPath() + targetUrl;
		}
		
		//String ctxPath = request.getContextPath();
		//int idx = targetUrl.indexOf(ctxPath);
		//String target = targetUrl.substring(idx + ctxPath.length());

		//log.debug(String.format("authentication successful, forwarding to [%s] obtained from [%s]", target,	targetUrl));
		
		return targetUrl;

	}
}
