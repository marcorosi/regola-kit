package org.regola.webapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.InterruptibleFileWatchdog;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.util.WebUtils;

public class Log4jConfigListener implements ServletContextListener {

	private static InterruptibleFileWatchdog watchdog;

	//@Override
	public void contextInitialized(ServletContextEvent sce) {
		initLogging(sce.getServletContext());
	}

	//@Override
	public void contextDestroyed(ServletContextEvent sce) {
		shutdownLogging(sce.getServletContext());
	}

	
	/** Parameter specifying the location of the log4j config file */
	public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";

	/** Parameter specifying the refresh interval for checking the log4j config file */
	public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";

	/** Parameter specifying whether to expose the web app root system property */
	public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";


	/**
	 * Initialize log4j, including setting the web app root system property.
	 * @param servletContext the current ServletContext
	 * @see WebUtils#setWebAppRootSystemProperty
	 */
	public static void initLogging(ServletContext servletContext) {
		// Expose the web app root system property.
		if (exposeWebAppRoot(servletContext)) {
			WebUtils.setWebAppRootSystemProperty(servletContext);
		}

		// Only perform custom log4j initialization in case of a config file.
		String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (location != null) {
			// Perform actual log4j initialization; else rely on log4j's default initialization.
			try {
				// Return a URL (e.g. "classpath:" or "file:") as-is;
				// consider a plain file path as relative to the web application root directory.
				if (!ResourceUtils.isUrl(location)) {
					// Resolve system property placeholders before resolving real path.
					location = SystemPropertyUtils.resolvePlaceholders(location);
					location = WebUtils.getRealPath(servletContext, location);
				}

				// Write log message to server log.
				servletContext.log("Initializing log4j from [" + location + "]");

				// Check whether refresh interval was specified.
				String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
				if (intervalString != null) {
					// Initialize with refresh interval, i.e. with log4j's watchdog thread,
					// checking the file in the background.
					try {
						long refreshInterval = Long.parseLong(intervalString);
						initLogging(location, refreshInterval, servletContext);
					}
					catch (NumberFormatException ex) {
						throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
					}
				}
				else {
					// Initialize without refresh check, i.e. without log4j's watchdog thread.
					Log4jConfigurer.initLogging(location);
				}
			}
			catch (FileNotFoundException ex) {
				//throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());

				// fallback to default initialization
				servletContext.log("Log4j config file not found [" + location
						+ "]: fallback to default initialization.", ex);
			}
		}
	}
	
	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** Extension that indicates a log4j XML config file: ".xml" */
	public static final String XML_FILE_EXTENSION = ".xml";
	
	public static void initLogging(String location, final long refreshInterval, final ServletContext servletContext) throws FileNotFoundException {
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		File file = ResourceUtils.getFile(resolvedLocation);
		if (!file.exists()) {
			throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
		}
		if (resolvedLocation.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
			watchdog = new InterruptibleFileWatchdog(file.getAbsolutePath(), servletContext.getServletContextName()) {
				{
					setDelay(refreshInterval);
				}

				@Override
				protected void doOnChange() {
					new DOMConfigurator().doConfigure(filename,
							LogManager.getLoggerRepository());
				}
			};
		} else {
			watchdog = new InterruptibleFileWatchdog(file.getAbsolutePath(), servletContext.getServletContextName()) {
				{
					setDelay(refreshInterval);
				}

				@Override
				protected void doOnChange() {
					new PropertyConfigurator().doConfigure(filename,
							LogManager.getLoggerRepository());
				}
			};
		}
		servletContext.log("Starting log4j watchdog thread");
		watchdog.start();
	}

	/**
	 * Shut down log4j, properly releasing all file locks
	 * and resetting the web app root system property.
	 * @param servletContext the current ServletContext
	 * @see WebUtils#removeWebAppRootSystemProperty
	 */
	public static void shutdownLogging(ServletContext servletContext) {
		servletContext.log("Shutting down log4j");
		try {
			Log4jConfigurer.shutdownLogging();
			if (watchdog != null) {
				servletContext.log("Shutting down log4j watchdog thread");
				try {
					watchdog.close();
					watchdog.setDelay(0);
				} catch (IOException e) {
					servletContext.log("Failed to close log4j watchdog thread", e);
				}
				watchdog.interrupt();
			}
		} catch (SecurityException e) {
			servletContext.log("Failed to close log4j watchdog thread", e);
		} finally {
			// bye bye
			watchdog = null; 

			// Remove the web app root system property.
			if (exposeWebAppRoot(servletContext)) {
				WebUtils.removeWebAppRootSystemProperty(servletContext);
			}
		}
	}

	/**
	 * Return whether to expose the web app root system property,
	 * checking the corresponding ServletContext init parameter.
	 * @see #EXPOSE_WEB_APP_ROOT_PARAM
	 */
	private static boolean exposeWebAppRoot(ServletContext servletContext) {
		String exposeWebAppRootParam = servletContext.getInitParameter(EXPOSE_WEB_APP_ROOT_PARAM);
		return (exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam));
	}

}
