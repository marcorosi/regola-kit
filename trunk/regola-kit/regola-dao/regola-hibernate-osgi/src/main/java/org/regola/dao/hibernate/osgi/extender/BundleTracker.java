package org.regola.dao.hibernate.osgi.extender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BundleTracker implements BundleListener, BundleActivator {
	
	enum UpdateAction { ADD, REMOVE };
	
	private String HIBERNATE_CONTRIBUTION = "Hibernate-Contribution";
	
	private static DynamicConfiguration dynamicConfiguration;
	
	private static Logger logger = LoggerFactory.getLogger(BundleTracker.class);
	
	private BundleContext bundleContext;

	private static BundleTracker instance;

	public void start(BundleContext context) {
		instance = this;
		bundleContext = context;
		startTrackingBundles();
	}
	 
	public void stop(BundleContext context) {
	
	}

	public void bundleChanged(BundleEvent bundleEvent) {
		try {
			logger.info("Bundle event received for bundle: " + bundleEvent.getBundle().getSymbolicName());
			
			if (bundleEvent.getType() == BundleEvent.STARTED) {
				updateBundleClasses(bundleEvent.getBundle(), UpdateAction.ADD);
			} else if (bundleEvent.getType() == BundleEvent.STOPPED) {
				updateBundleClasses(bundleEvent.getBundle(), UpdateAction.REMOVE);
			} 
		} catch(RuntimeException re) {
			logger.error("Error processing bundle event", re);
			throw re;
		}
	}

	private void updateBundleClasses(Bundle bundle, UpdateAction updateAction) {
		Object hibernateContribution = bundle.getHeaders().get(HIBERNATE_CONTRIBUTION);
		if (hibernateContribution != null) {
			String[] classes = parseHibernateClasses((String) hibernateContribution);
			switch (updateAction) {
				case ADD:
					logger.info("Adding classes from hibernate configuration: " + writeArray(classes));
					dynamicConfiguration.addAnnotatedClasses(bundle, classes);
					break;
				case REMOVE:
					logger.info("Removing classes from hibernate configuration: " + writeArray(classes));
					dynamicConfiguration.removeAnnotatedClasses(bundle, classes);
					break;
				default:
					throw new IllegalArgumentException("" + updateAction);
			}
		}
	}

	private String writeArray(String[] classes) {
		if (classes.length == 0) return "";
		StringBuffer sb = new StringBuffer();
		for(String s : classes) {
			sb.append(s).append(", ");
		}
		return sb.toString().substring(0, sb.length()-2);
	}

	private String[] parseHibernateClasses(String hibernateContribution) {
		logger.info("Hibernate-contribution: " + hibernateContribution);
		
		String [] hcSplit = hibernateContribution.split(";");
		if (hcSplit.length != 2) {
			throwIllegalArgumentException();
		}
		String dbConnection = hcSplit[0].trim();
		String classesString = hcSplit[1].trim();
		Pattern p = Pattern.compile("classes *= *\"(.*)\"");		
		Matcher m = p.matcher(classesString);
		if (!m.find()) {
			throwIllegalArgumentException();
		}
		String[] classes = m.group(1).split(",");
		return classes;
	}

	private void throwIllegalArgumentException() {
		throw new IllegalArgumentException("Hibernate-Contribution must be of the form " +
			"Hibernate-Contribution: db-connection; class=\"org.example.model.SomeEntity\"");
	}

	public static void setDynamicConfiguration(
			DynamicConfiguration dynamicConfiguration) {
    	BundleTracker.dynamicConfiguration = dynamicConfiguration;
    	startTrackingBundles();
	}

	/**
	 * Start tracking bundles after dynamic configuration has been 
	 * initialized
	 */
	private static void startTrackingBundles() {
		logger.info("Have dynamic configuration: " + (dynamicConfiguration != null));
		logger.info("Have instance: " + (instance != null));
		if (dynamicConfiguration != null && instance != null) {
			// now start tracking bundles
			logger.info("Starting to track bundle events");
			instance.bundleContext.addBundleListener(instance);
			instance.processBundles();
		}
	} 

	/**
	 * process bundles started before I started listening for bundles
	 */
	private void processBundles() {
		for (Bundle b : instance.bundleContext.getBundles()) {
			if (b.getState() == Bundle.ACTIVE) {
				updateBundleClasses(b, UpdateAction.ADD);
			}
		}
	}
     
}
