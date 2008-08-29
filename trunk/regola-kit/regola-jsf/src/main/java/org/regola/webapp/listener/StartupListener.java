package org.regola.webapp.listener;

/*
 * Adapted from org.appfuse.webapp.listener.StartupListener
 */

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * 
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the 
 * GenericDao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListener implements ServletContextListener {
    private static final Log log = LogFactory.getLog(StartupListener.class);

	public static final String SERVER_INFO = "org.regola.webapp.listener.ApplicationStartupListener.SERVER_INFO";
	public static final String BUILD_INFO = "org.regola.webapp.listener.ApplicationStartupListener.BUILD_INFO";

    
    @SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent event) {
        log.debug("Regola-kit is initializing servlet context...");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }
        
        if (context.getInitParameter(Constants.CSS_THEME) != null) {
            config.put(Constants.CSS_THEME, context.getInitParameter(Constants.CSS_THEME));
        }

        ApplicationContext ctx =  WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        boolean encryptPassword = false;
        
        context.setAttribute(Constants.CONFIG, config);
        context.setAttribute(SERVER_INFO, getServerInfo(context));
        context.setAttribute(BUILD_INFO, getBuildInfo(context));
	

       setupContext(context);
    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
        
    	ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        
    	ctx.toString();

        // get list of possible roles
        //context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        //log.debug("Drop-down initialization complete [OK]");
    }
    
    private String getBuildInfo(ServletContext ctx) {
		String info = "";
		try {
			ResourceBundle resources = ResourceBundle.getBundle("build");
			info += resources.getString("build.number");
			info += " del " + resources.getString("build.date");
		} catch (Exception ex) {
			log.error("Impossibile costruire il numero di versione");
			ex.printStackTrace();
		}

		return info;
	}

	private String getServerInfo(ServletContext ctx) {
		try {
			return "Server: " + java.net.InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			log.error("Impossibile ricavare l'hostname");
			return "";
		}
	}


    /**
     * This is a no-op method.
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
