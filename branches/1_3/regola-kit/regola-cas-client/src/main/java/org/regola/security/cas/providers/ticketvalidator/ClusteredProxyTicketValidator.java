package org.regola.security.cas.providers.ticketvalidator;

/*
 *  Copyright (c) 2000-2003 Yale University. All rights reserved.
 *
 *  THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 *  DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 *  DAMAGE.
 *
 *  Redistribution and use of this software in source or binary forms,
 *  with or without modification, are permitted, provided that the
 *  following conditions are met:
 *
 *  1. Any redistribution must include the above copyright notice and
 *  disclaimer and this list of conditions in any related documentation
 *  and, if feasible, in the redistributed software.
 *
 *  2. Any redistribution must include the acknowledgment, "This product
 *  includes software developed by Yale University," in any related
 *  documentation and, if feasible, in the redistributed software.
 *
 *  3. The names "Yale" and "Yale University" must not be used to endorse
 *  or promote products derived from this software.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import edu.yale.its.tp.cas.client.ProxyTicketValidator;

/**
 * Validates STs and optionally retrieves PGT IOUs.
 * Designed with a bean-like interface for simplicity and generality.
 */
public class ClusteredProxyTicketValidator extends ProxyTicketValidator {

	private static final Log logger = LogFactory.getLog(ClusteredProxyTicketValidator.class);
	
  /*********************************************************************
  // For testing...
  public static void main(String args[]) throws Exception {
    System.setProperty("java.protocol.handler.pkgs",
      "com.sun.net.ssl.internal.www.protocol");
    edu.yale.its.tp.cas.client.ServiceTicketValidator sv
      = new edu.yale.its.tp.cas.client.ServiceTicketValidator();
    sv.setCasValidateUrl("https://portal1.wss.yale.edu/cas/serviceValidate");
    sv.setProxyCallbackUrl("https://portal1.wss.yale.edu/casProxy/receptor");
    sv.setService(args[0]);
    sv.setServiceTicket(args[1]);
    sv.validate();
    System.out.println(sv.getResponse());
    System.out.println();
    if (sv.isAuthenticationSuccesful()) { 
      System.out.println("user: " + sv.getUser());
      System.out.println("pgtIou: " + sv.getPgtIou());
    } else {
      System.out.println("error code: " + sv.getErrorCode());
      System.out.println("error message: " + sv.getErrorMessage());
    }
  }
  */

  //*********************************************************************
  // Private state

  private String casValidateUrl, proxyCallbackUrl, st, service, pgtIou,
    user, errorCode, errorMessage, entireResponse;
  private boolean renew = false;
  private boolean attemptedAuthentication;
  private boolean successfulAuthentication;
  protected List proxyList;

  private String sessionid;
  private String sessiondIdParameterNameForCas;
  
  //*********************************************************************
  // Accessors

  /**
   * Retrieves a list of proxies involved in the current authentication.
   */
  public List getProxyList() {
    return proxyList;
  }

  /**
   * Sets the CAS validation URL to use when validating tickets and
   * retrieving PGT IOUs.
   */
  public void setCasValidateUrl(String x) {
    this.casValidateUrl = x;
  }

  /**
   * Gets the CAS validation URL to use when validating tickets and
   * retrieving PGT IOUs.
   */
  public String getCasValidateUrl() {
    return this.casValidateUrl;
  }

  /**
   * Sets the callback URL, owned logically by the calling service, to
   * receive the PGTid/PGTiou mapping.
   */
  public void setProxyCallbackUrl(String x) {
    this.proxyCallbackUrl = x;
  }

  /**
   * Sets the "renew" flag on authentication.  When set to "true",
   * authentication will only succeed if this was an initial login
   * (forced by the "renew" flag being set on login).
   */
  public void setRenew(boolean b) {
    this.renew = b;
  }

  /**
   * Gets the callback URL, owned logically by the calling service, to
   * receive the PGTid/PGTiou mapping.
   */
  public String getProxyCallbackUrl() {
    return this.proxyCallbackUrl;
  }

  /**
   * Sets the ST to validate.
   */
  public void setServiceTicket(String x) {
    this.st = x;
  }

  /**
   * Sets the service to use when validating.
   */
  public void setService(String x) {
    this.service = x;
  }

  /**
   * Returns the strongly authenticated username.
   */
  public String getUser() {
    return this.user;
  }

  /**
   * Returns the PGT IOU returned by CAS.
   */
  public String getPgtIou() {
    return this.pgtIou;
  }

  /**
   * Returns <tt>true</tt> if the most recent authentication attempted
   * succeeded, <tt>false</tt> otherwise.
   */
  public boolean isAuthenticationSuccesful() {
    return this.successfulAuthentication;
  }

  /**
   * Returns an error message if CAS authentication failed.
   */
  public String getErrorMessage() {
    return this.errorMessage;
  }

  /**
   * Returns CAS's error code if authentication failed.
   */
  public String getErrorCode() {
    return this.errorCode;
  }

  /**
   * Retrieves CAS's entire response, if authentication was succsesful.
   */
  public String getResponse()  {
    return this.entireResponse;
  }


  //*********************************************************************
  // Actuator

  public void validate()
      throws IOException, SAXException, ParserConfigurationException {
    if (casValidateUrl == null || st == null)
      throw new IllegalStateException("must set validation URL and ticket");
    clear();
    attemptedAuthentication = true;
    StringBuffer sb = new StringBuffer();
    sb.append(casValidateUrl);
    if (casValidateUrl.indexOf('?') == -1)
      sb.append('?');
    else
      sb.append('&');
    sb.append("service=" + service + "&ticket=" + st);
    if (proxyCallbackUrl != null)
      sb.append("&pgtUrl=" + proxyCallbackUrl);
    if (renew)
      sb.append("&renew=true");
    if(getSessionid() != null)
    	sb.append("&"+sessiondIdParameterNameForCas+"=").append(getSessionid());
    String url = sb.toString();
    
    logger.debug("Richiesta di validazione service ticket: "+url);
    //String response = SecureURL.retrieve(url);
    String response = retrieveURL(url);
    
    this.entireResponse = response;

    // parse the response and set appropriate properties
    if (response != null) {
      XMLReader r =
        SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      r.setFeature("http://xml.org/sax/features/namespaces", false);
      r.setContentHandler(newHandler());
      r.parse(new InputSource(new StringReader(response)));
    }
  }

  private int connectionTimeout = 5000;
  private int readTimeout = 5000;

  public String retrieveURL(final String url) 
  {
      HttpURLConnection connection = null;
      BufferedReader in = null;
      try {
          if (logger.isDebugEnabled()) {
        	  logger.debug("Attempting to access " + url);
          }
          final URL logoutUrl = new URL(url);

          connection = (HttpURLConnection) logoutUrl.openConnection();
          connection.setReadTimeout(this.readTimeout);
          connection.setConnectTimeout(this.connectionTimeout);
          connection.setRequestProperty("Connection", "close");
          connection.setRequestProperty("Cookie", "JSESSIONID="+getSessionid());

          in = new BufferedReader(new InputStreamReader(connection
              .getInputStream()));

          String line;
          StringBuffer buf = new StringBuffer();
          while ((line = in.readLine()) != null) {
        	  buf.append(line + "\n");
          }
          
          if (logger.isDebugEnabled()) {
              logger.debug("Finished sending message to" + url);
          }
          
          return buf.toString();
      } catch (final Exception e) {
          logger.error(e,e);
          return "";
      } finally {
          if (in != null) {
              try {
                  in.close();
              } catch (final IOException e) {
                  // can't do anything
              }
          }
          if (connection != null) {
              connection.disconnect();
          }
      }
  }

  //*********************************************************************
  // Response parser

  protected DefaultHandler newHandler() {
	    return new ProxyHandler();
  }

  protected class ProxyHandler extends ClusteredProxyTicketValidator.Handler {

	    //**********************************************
	    // Constants

	    protected static final String PROXIES = "cas:proxies";
	    protected static final String PROXY = "cas:proxy";

	    //**********************************************
	    // Parsing state

	    protected List proxyList = new ArrayList();
	    protected boolean proxyFragment = false;

	    //**********************************************
	    // Parsing logic

	    public void startElement(String ns, String ln, String qn, Attributes a) {
	      super.startElement(ns, ln, qn, a);
	      if (authenticationSuccess && qn.equals(PROXIES))
	        proxyFragment = true;
	    }

	    public void endElement(String ns, String ln, String qn)
	        throws SAXException {
	      super.endElement(ns, ln, qn);
	      if (qn.equals(PROXIES))
	        proxyFragment = false;
	      else if (proxyFragment && qn.equals(PROXY))
	        proxyList.add(currentText.toString().trim());
	    }
	 
	    public void endDocument() throws SAXException {
	      super.endDocument();
	      if (authenticationSuccess)
	    	  ClusteredProxyTicketValidator.this.proxyList = proxyList;
	    }
	  }


  protected class Handler extends DefaultHandler {

    //**********************************************
    // Constants

    protected static final String AUTHENTICATION_SUCCESS = 
      "cas:authenticationSuccess";
    protected static final String AUTHENTICATION_FAILURE =
      "cas:authenticationFailure";
    protected static final String PROXY_GRANTING_TICKET =
      "cas:proxyGrantingTicket";
    protected static final String USER = "cas:user";

    //**********************************************
    // Parsing state

    protected StringBuffer currentText = new StringBuffer();
    protected boolean authenticationSuccess = false;
    protected boolean authenticationFailure = false;
    protected String netid, pgtIou, errorCode, errorMessage;
    

    //**********************************************
    // Parsing logic

    public void startElement(String ns, String ln, String qn, Attributes a) {
      // clear the buffer
      currentText = new StringBuffer();

      // check outer elements
      if (qn.equals(AUTHENTICATION_SUCCESS)) {
        authenticationSuccess = true;
      } else if (qn.equals(AUTHENTICATION_FAILURE)) {
        authenticationFailure = true;
        errorCode = a.getValue("code");
        if (errorCode != null)
          errorCode = errorCode.trim();
      }
    }

    public void characters(char[] ch, int start, int length) {
      // store the body, in stages if necessary
      currentText.append(ch, start, length);
    }

    public void endElement(String ns, String ln, String qn)
        throws SAXException {
      if (authenticationSuccess) {
        if (qn.equals(USER))
          user = currentText.toString().trim();
        if (qn.equals(PROXY_GRANTING_TICKET))
          pgtIou = currentText.toString().trim();
      } else if (authenticationFailure) {
        if (qn.equals(AUTHENTICATION_FAILURE))
          errorMessage = currentText.toString().trim();
      }
    }
 
    public void endDocument() throws SAXException {
      // save values as appropriate
      if (authenticationSuccess) {
    	  ClusteredProxyTicketValidator.this.user = user;
    	  ClusteredProxyTicketValidator.this.pgtIou = pgtIou;
    	  ClusteredProxyTicketValidator.this.successfulAuthentication = true;
      } else if (authenticationFailure) {
    	  ClusteredProxyTicketValidator.this.errorMessage = errorMessage;
    	  ClusteredProxyTicketValidator.this.errorCode = errorCode;
    	  ClusteredProxyTicketValidator.this.successfulAuthentication = false;
      } else
        throw new SAXException("no indication of success of failure from CAS");
    }
 }

  //*********************************************************************
  // Utility methods

  /**
   * Clears internally manufactured state.
   */
  protected void clear() {
   user = pgtIou = errorMessage = null;
   attemptedAuthentication = false;
   successfulAuthentication = false;
   proxyList = null;
  }

public String getSessionid() {
	return sessionid;
}

public void setSessionid(String sessionid) {
	this.sessionid = sessionid;
}

public String getSessiondIdParameterNameForCas() {
	return sessiondIdParameterNameForCas;
}

public void setSessiondIdParameterNameForCas(String sessiondIdParameterName) {
	this.sessiondIdParameterNameForCas = sessiondIdParameterName;
}

}
