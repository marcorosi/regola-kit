package org.regola.webapp.aspect;

import org.regola.events.DuckTypingEventBroker;
import org.regola.util.wrapper.FormPageWrapper;
import org.regola.webapp.action.BasePage;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.ListPage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
		Object beanTarget = pjp.getTarget();
		
		//BasePage bean = (BasePage)thisJoinPoint.getThis();
		if("".equals(returnValue))
		{
			log.info("il bean non sarà rimosso perchè verrà ricaricata le stessa pagina");
		} else 
		{
			
			//unsubscribe del bean nell'eventualità che sia registrato all'eventBroker
			/*
			 * va fatta con il beanTarget (è quel riferimento che è stato registrato al broker e non quello del proxy!)
			 */
			unsubscribeFromBroker(beanTarget);
			
			/*
			 * rimozione dalla session va fatta invece passando il 'proxy'
			 * (spring registra il riferimento ad essa in sessione)
			 */
			removeBeanFromSession(bean);
		}
		
		return returnValue;
	}
	
	private void removeBeanFromSession(Object bean)
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
	
	private void unsubscribeFromBroker(Object beanTarget)
	{
		try{
			//==========================================================================================
			//Form bean: in questo caso subscribe (per errori conversione) viene fatta dal formPage
			//==========================================================================================
			Method m = beanTarget.getClass().getMethod("getFormPage");
			if(m != null)  //è un form bean
			{
				FormPage fp = (FormPage)m.invoke(beanTarget); //restituisce l'istanza del proxy di Spring
				FormPage fpTarget = fp.getWrapper().getFormPage();
				DuckTypingEventBroker broker = fpTarget.getEventBroker();
				if(broker != null)
				{
					log.info("unsubscribe di " + fpTarget.getClass().getCanonicalName() + " dall'eventBroker.");
					broker.unsubscribe(fpTarget);
				}
			}else
			{
				//=============================================================================================
				//List bean: in questo caso subscribe (per nuovi salvataggi) viene fatta dal listBean stesso
				//=============================================================================================
				m = beanTarget.getClass().getMethod("getListPage");
				if(m != null)  //è un list bean
				{
					ListPage lp = (ListPage)m.invoke(beanTarget);
					DuckTypingEventBroker broker = lp.getEventBroker();
					if(broker != null)
					{
						log.info("unsubscribe di " + beanTarget.getClass().getCanonicalName() + " dall'eventBroker.");
						broker.unsubscribe(beanTarget);
					}					
				}
			}
		} catch (Exception e) {
			log.error("Errore nel reperimento dell'eventBroker per effettuare la unsubscribe: " + e.getClass() + " - " + e.getMessage());
		}		
	}
	
	
}
