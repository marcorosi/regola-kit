package org.regola.webapp.aspect;

import org.regola.webapp.action.BasePage;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * si occupa di rimuovere dalla sessione http i managed bean
 * la rimozione scatta all'uscita dei metodi annotati con @ScopeEnd
 *  
 * @author marco
 */
@Aspect
public class RemoveFromSession {

	private static Log log = LogFactory.getLog(RemoveFromSession.class);
	
	@AfterReturning(pointcut = "@annotation(org.regola.webapp.annotation.ScopeEnd)")
	public void doRemove(JoinPoint thisJoinPoint) 
	{
		log.info("chiamata doRemove su " +thisJoinPoint.getThis());
		
		BasePage bean = (BasePage)thisJoinPoint.getThis();
		if("".equals(bean.getReturnPage()))
		{
			log.info("il bean non sarà rimosso perchè verrà ricaricata le stessa pagina");
		} else 
		{
			try {
				HttpSession session = ((HttpServletRequest)bean.getFacesContext().getExternalContext().getRequest()).getSession();
			    Enumeration<String> attributeKeys = session.getAttributeNames();
			    while(attributeKeys.hasMoreElements())
			    {
			    	String key = attributeKeys.nextElement();
			    	if(bean == session.getAttribute(key))
			    	{
			    		log.info("rimozione di "+key);
			    		session.removeAttribute(key);
			    		break;
			    	}
			    }
			} catch (Exception e) {
				log.error("Impossibile rimuovere il bean dalla sessione",e);
			}			
		}
	}
}
