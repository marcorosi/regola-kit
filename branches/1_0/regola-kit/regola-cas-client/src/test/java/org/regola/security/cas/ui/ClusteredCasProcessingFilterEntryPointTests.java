package org.regola.security.cas.ui;

import junit.framework.TestCase;

import org.acegisecurity.ui.cas.ServiceProperties;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;


/**
 * Tests {@link CasProcessingFilterEntryPoint}.
 *
 * @author Ben Alex
 * @version $Id: CasProcessingFilterEntryPointTests.java 2217 2007-10-27 00:45:30Z luke_t $
 */
public class ClusteredCasProcessingFilterEntryPointTests extends TestCase {
    //~ Constructors ===================================================================================================

    public ClusteredCasProcessingFilterEntryPointTests() {
        super();
    }

    public ClusteredCasProcessingFilterEntryPointTests(String arg0) {
        super(arg0);
    }

    //~ Methods ========================================================================================================

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ClusteredCasProcessingFilterEntryPointTests.class);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public void testDetectsMissingLoginFormUrl() throws Exception {
        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setServiceProperties(new ServiceProperties());

        try {
            ep.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("loginUrl must be specified", expected.getMessage());
        }
    }

    public void testDetectsMissingServiceProperties() throws Exception {
        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");

        try {
            ep.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("serviceProperties must be specified", expected.getMessage());
        }
    }

    public void testGettersSetters() {
        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");
        assertEquals("https://cas/login", ep.getLoginUrl());

        ep.setServiceProperties(new ServiceProperties());
        assertTrue(ep.getServiceProperties() != null);
    }

    public void testNormalOperationWithRenewFalse() throws Exception {
        ServiceProperties sp = new ServiceProperties();
        sp.setSendRenew(false);
        sp.setService("https://mycompany.com/bigWebApp/j_spring_cas_security_check");

        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");
        ep.setServiceProperties(sp);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/some_path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ep.afterPropertiesSet();
        ep.commence(request, response, null);

        assertEquals("https://cas/login?service="
            + URLEncoder.encode("https://mycompany.com/bigWebApp/j_spring_cas_security_check", "UTF-8"),
            response.getRedirectedUrl());
    }

    public void testNormalOperationWithRenewTrue() throws Exception {
        ServiceProperties sp = new ServiceProperties();
        sp.setSendRenew(true);
        sp.setService("https://mycompany.com/bigWebApp/j_spring_cas_security_check");

        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");
        ep.setServiceProperties(sp);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/some_path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ep.afterPropertiesSet();
        ep.commence(request, response, null);
        assertEquals("https://cas/login?service="
            + URLEncoder.encode("https://mycompany.com/bigWebApp/j_spring_cas_security_check", "UTF-8") + "&renew=true",
            response.getRedirectedUrl());
    }
    
    //-------------- aggiunti da regola
    
    public void testWithSessionIdInURL() throws Exception {
        ServiceProperties sp = new ServiceProperties();
        sp.setSendRenew(false);
        sp.setService("https://mycompany.com/bigWebApp/j_spring_cas_security_check");

        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");
        ep.setServiceProperties(sp);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/some_path");
        request.addParameter("jsessionid", "1234567890");

        MockHttpServletResponse response = new MockHttpServletResponse();

        ep.afterPropertiesSet();
        ep.commence(request, response, null);

        assertEquals("https://cas/login?service="
            + URLEncoder.encode("https://mycompany.com/bigWebApp/j_spring_cas_security_check", "UTF-8")
            +"&"+ep.getSessionIdParamaterNameForCas()+"=1234567890",
            response.getRedirectedUrl());
    }
    
    public void testWithSessionIdInCookie() throws Exception {
        ServiceProperties sp = new ServiceProperties();
        sp.setSendRenew(false);
        sp.setService("https://mycompany.com/bigWebApp/j_spring_cas_security_check");

        ClusteredCasProcessingFilterEntryPoint ep = new ClusteredCasProcessingFilterEntryPoint();
        ep.setLoginUrl("https://cas/login");
        ep.setServiceProperties(sp);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/some_path");
        Cookie cookie = new Cookie(ep.getSessionIdParamaterName(),"1234567890"); 
        request.setCookies(new Cookie[]{cookie});

        MockHttpServletResponse response = new MockHttpServletResponse();

        ep.afterPropertiesSet();
        ep.commence(request, response, null);

        assertEquals("https://cas/login?service="
            + URLEncoder.encode("https://mycompany.com/bigWebApp/j_spring_cas_security_check", "UTF-8")
            +"&"+ep.getSessionIdParamaterNameForCas()+"=1234567890",
            response.getRedirectedUrl());
    }
}
