package org.regola.webapp.action;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import java.io.InputStream;
import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

/**
 * Questo è un modello sperimentale di test con Selenium.
 * 
 * Da migliorare soprattutto come leggiamo l'host ed il contesto
 * impostati nel pom.
 * 
 * Possiamo creare una classe di base per tutti i test Selenium
 * appena realizzato qualche decina di test veri.
 * 
 * @author nicola
 */
public class CustomerListTest extends
		AbstractAnnotationAwareTransactionalTests {
	
     
   String context;

   public String getContext() {
      return context;
   }

   public void setContext(String context) {
      this.context = context;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }
   
   String host;
   
   public CustomerListTest() throws Exception
   {
      
      InputStream fin = getClass().getResourceAsStream("/jdbc.test.properties") ;
      Properties props = new Properties();
         
      props.load(fin);
      
      setHost(props.getProperty("integration.host"));
      setContext(props.getProperty("integration.context"));
      
   }
   
   Selenium selenium;
	//private SeleniumServer seleniumServer;
   
   @Override
	public String[] getConfigLocations() {
		return new String[] { "classpath*:applicationContext-resources-test.xml",
         "classpath*:applicationContext-dao-test.xml",
         "classpath*:applicationContext-services-test.xml"
      };
	}
  
    
   
   @Override
	public void onSetUp() throws Exception
	{
      //seleniumServer = new SeleniumServer(4446,false,true);
      //seleniumServer.start();
		
      System.out.println(getContext() +" " + getHost());
      
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", getHost());
		selenium.start(); // launch a brower window
	
	}
	
	
   @Override
	public void onTearDown()
	{
		selenium.stop(); // close the browser window
		//seleniumServer.stop();
	}
    
   
	public void testProvaSemplice()
	{
		
      selenium.open( getContext() + "/customer-list.html");
		selenium.click("_id111:customerList:2:_id128");
		selenium.waitForPageToLoad("40000");
		selenium.click("_id111:_id128");
		selenium.waitForPageToLoad("30000");
		selenium.click("_id111:customerList:13:_id128");
		selenium.waitForPageToLoad("30000");
		selenium.click("_id111:_id128");
		selenium.waitForPageToLoad("30000");
		//selenium.click("_id111:customerList:5:_id128");
		//selenium.waitForPageToLoad("30000");
	}

    
	
}
