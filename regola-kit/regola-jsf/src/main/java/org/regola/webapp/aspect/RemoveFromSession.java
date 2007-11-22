package org.regola.webapp.aspect;

import org.regola.webapp.action.BasePage;

import java.util.Enumeration;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
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
	
	@SuppressWarnings("unchecked")
	@Around(value = "@annotation(org.regola.webapp.annotation.ScopeEnd)")
	public Object doRemove(ProceedingJoinPoint pjp) 
	{
		log.info("chiamata doRemove su " +pjp.getThis());
		
		Object returnValue = null;
		try {
			returnValue = pjp.proceed();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Object bean = pjp.getThis();
		
		//BasePage bean = (BasePage)thisJoinPoint.getThis();
		if("".equals(returnValue))
		{
			log.info("il bean non sarà rimosso perchè verrà ricaricata le stessa pagina");
		} else 
		{
			try {
				HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
			    Enumeration<String> attributeKeys = session.getAttributeNames();
			    while(attributeKeys.hasMoreElements())
			    {
			    	String key = attributeKeys.nextElement();
			    	Object o = session.getAttribute(key);
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
		
		return returnValue;
	}
	
	
}
