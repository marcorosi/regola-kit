/**
 * Copyright (C) 2008 nicola 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.regola.ws;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.regola.xml.JAXBMarshaller;

/**
 * {@link RestClient} è una piccola classe che dovrebbe rendere agevole
 * l'invocazione di servizi di tipo REST realizzati con Regola kit. <br>
 * <br>
 * Ad esempio: <br>
 * 
 * <code><pre>
 * String result = get(url, param1, param2, ..., paramN);
 * </pre></code> I parametri sono utilizzati per costruire la url che diventa
 * qualcosa di simile ad ( url/param1/param2/.../paramN).<br>
 * 
 * E' anche possibile passare una singola entità nel body della richiesta HTTP,
 * tipicamente una frammento di xml che rappresenta un'istanza di un oggetto. Si
 * veda a questo proposito {@link JAXBMarshaller#toXml(String, String, Object)}.
 * 
 * <code> <pre>
 * Dto dto = new Dto();
 * String dtoXml = toXml("org.regola.ws", "dto", dto);
 * String result = post(url, dtoXml, 1, "salve!");
 * </pre></code>
 * 
 * Infine le stringe restituite dal servizio web possono essere trasformate in
 * oggetti in modo analogo.
 * 
 * <br>
 * <code> <pre>
 * String result = put(url, dtoXml, 1, "salve!");
 * Dto dto = fromXml("org.regola.ws", result);
 * </pre></code> <br>
 * 
 * @see JAXBMarshaller
 * 
 * @author nicola
 * 
 */
public class RestClient {

	private static final Log logger = LogFactory.getLog(RestClient.class);

	/**
	 * Chiama un metodo http, è usato internamente per invocare un servizio di
	 * tipo REST
	 * 
	 * @param method
	 * @return
	 */
	protected static String call(HttpMethod method) {
		HttpClient httpclient = new HttpClient();

		try {
			int result = httpclient.executeMethod(method);
			logger.debug("Response status code: " + result);
			logger.debug("Response body: " + method.getResponseBodyAsString());

			return method.getResponseBodyAsString();

		} catch (HttpException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			method.releaseConnection();
		}

	}

	/**
	 * Costruisce la url appendendo i parametri,
	 * 
	 * @param url
	 *            restituisce url/param1/param2/.../paramN
	 * @param params
	 *            l'elenco dei parametri
	 * @return
	 */
	static protected String buildUrl(String url, Object... params) {

		if (url == null) 
			throw new RuntimeException("URL could not be null!");

		if (url.endsWith("/"))
			throw new RuntimeException("URL could not end with a /");

		String[] split = url.split("\\?");
		
		for (Object param : params) {
			split[0] += "/" + param;
		}
		
		if (split.length > 1)
			split[0] += "?" + split[1];

		return split[0];
	}

	/**
	 * Invoca un servizio col metodo http get. Se il valore di ritorno è la
	 * rappresentazione xml di un qualche oggetto è possibile ottenere l'oggetto
	 * di partenza utilizzando JAXB.
	 * 
	 * @see JAXBMarshaller#fromXml(String, String)
	 * 
	 * @param url
	 * @param params
	 *            elenco di tipi primitivi
	 * @return
	 * @throws RestRequestException
	 */
	public static String get(String url, Object... params) {//throws Exception {
		url = buildUrl(url, params);

		URL Url;
		try {
			Url = new URL(url);
			InputStream in = Url.openStream();
			return getStringFromInputStream(in);
		} catch (Exception e) {
			throw new RestRequestException("Error calling a GET for the url "+url,e);
		}
	}

	/**
	 * Invoca un servizio col metodo http put. Da notare il parametro xmlEntity
	 * che andrà inserito nel corpo della richiesta e generalmente rappresenta
	 * un'istanza di qualche oggetto trasformata in xml tramite JAXB.
	 * 
	 * @see JAXBMarshaller#toXml(String, String, Object)
	 * @see JAXBMarshaller#fromXml(String, String)
	 * 
	 * @param url
	 * @param xmlEntity
	 * @param params
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public static String put(String url, String xmlEntity, Object... params) throws JAXBException, UnsupportedEncodingException {

		url = buildUrl(url, params);

		PutMethod put = new PutMethod(url);
		put.addRequestHeader("Accept", "text/xml");

		RequestEntity entity = new StringRequestEntity(xmlEntity, "text/xml", "ISO-8859-1");
		put.setRequestEntity(entity);

		return call(put);
	}

	/**
	 * Invoca un servizio col metodo http delete. Se il valore di ritorno è la
	 * rappresentazione xml di un qualche oggetto è possibile ottenere l'oggetto
	 * di partenza utilizzando JAXB.
	 * 
	 * @see JAXBMarshaller#fromXml(String, String)
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static String delete(String url, Object... params) throws JAXBException, UnsupportedEncodingException {

		url = buildUrl(url, params);

		DeleteMethod delete = new DeleteMethod(url);
		delete.addRequestHeader("Accept", "text/xml");

		return call(delete);
	}

	/**
	 * Invoca un servizio col metodo http post. Da notare il parametro xmlEntity
	 * che andrà inserito nel corpo della richiesta e generalmente rappresenta
	 * un'istanza di qualche oggetto trasformata in xml tramite JAXB.
	 * 
	 * @see JAXBMarshaller#toXml(String, String, Object)
	 * @see JAXBMarshaller#fromXml(String, String)
	 * 
	 * @param url
	 * @param xmlEntity
	 * @param params
	 * @return
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public static String post(String url, String xmlEntity, Object... params) throws JAXBException, UnsupportedEncodingException {

		url = buildUrl(url, params);

		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Accept", "text/xml");

		RequestEntity entity = new StringRequestEntity(xmlEntity, "text/xml", "ISO-8859-1");
		post.setRequestEntity(entity);

		String xml = call(post);

		return xml;
	}

	/**
	 * Metodo di utilità per salvare direttamente su file un {@link InputStream}
	 * .
	 * 
	 * @param input
	 * @param filePath
	 * @throws IOException
	 */
	static public void saveInputStream(InputStream input, String filePath) throws IOException {
		FileOutputStream downloadFileStream = new FileOutputStream(filePath);
		int datax = 0;
		while ((datax = input.read()) != -1) {
			downloadFileStream.write(datax);
		}
	}

	/**
	 * Metodo di utilità per convertire un {@link InputStream} in una
	 * {@link String}
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	static public String getStringFromInputStream(InputStream in) throws Exception {
		CachedOutputStream bos = new CachedOutputStream();
		IOUtils.copy(in, bos);
		in.close();
		bos.close();
		// System.out.println(bos.getOut().toString());
		return bos.getOut().toString();
	}
}
