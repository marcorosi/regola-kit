package org.regola.dao.hibernate.osgi.extender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

/**
 * Hibernate session factory that can get updated during the runtime of the
 * application.
 */
public class DynamicConfiguration implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(DynamicConfiguration.class);

	private List<Class> annotatedClasses = new ArrayList<Class>();

//	private Properties hibernateProperties;

	private SessionFactory proxiedSessionFactory;

	private DataSource dataSource;

	private int myhashCode;

//	public void setHibernateProperties(Properties hibernateProperties) {
//		this.hibernateProperties = hibernateProperties;
//	}

	public void setAnnotatedClasses(List<Class> annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
	}

	public void addAnnotatedClass(Class anotadedClass) {
		annotatedClasses.add(anotadedClass);
		createNewSessionFactory();
	}
	
	public void removeAnnotatedClass(Class anotadedClass) {
		annotatedClasses.remove(anotadedClass);
		createNewSessionFactory();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SessionFactory getSessionFactory() {
		return proxiedSessionFactory;
	}
	
	private Resource[] configLocations;
	
	private SessionFactory sessionFactory;

	private void createNewSessionFactory() {
		
		logger.info("Creating new session factory...");
		
//		if (hibernateProperties == null) {
//			throw new IllegalStateException(
//					"Hibernate properties have not been set yet");
//		}
//
//		AnnotationConfiguration cfg = new AnnotationConfiguration();
//		cfg.setProperties(hibernateProperties);
//		for (Class c : annotatedClasses) {
//			cfg.addAnnotatedClass(c);
//		}
//		sessionFactory = cfg.buildSessionFactory();
		
		OsgiAnnotationSessionFactoryBean asfb = new OsgiAnnotationSessionFactoryBean();
		asfb.setDataSource(dataSource);
		asfb.setConfigLocations(configLocations);
		//asfb.setHibernateProperties(hibernateProperties);
		asfb.setAnnotatedClasses(annotatedClasses
				.toArray(new Class[annotatedClasses.size()]));
		
		asfb.setMappings(mappings);
		
		try {
			asfb.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		sessionFactory = (SessionFactory) asfb.getObject();
		proxiedSessionFactory = (SessionFactory) Proxy.newProxyInstance(
				SessionFactory.class.getClassLoader(),
				new Class[] { SessionFactory.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						logger.info("Delegate factory invoked: " + sessionFactory + 
								"" + method.getName());
						return method.invoke(sessionFactory, args);
					}
				});
		
		logger.info("Created new session factory: " + sessionFactory);
		logger.info("Known classes are");
		for(Class c : annotatedClasses) {
			logger.info(c.getName());
		}
	}

	public void afterPropertiesSet() throws Exception {
		createNewSessionFactory();
		BundleTracker.setDynamicConfiguration(this);
	}
	
	private List<InputStream> mappings = new ArrayList<InputStream>();
	

	public void addAnnotatedClasses(Bundle sourceBundle, String[] classes) {
		for(String s : classes) {
			try {
				logger.error("Adding class: " + s);
				annotatedClasses.add(
						sourceBundle.loadClass(s));
				
				// Modifica per leggere i mappaggi
				URL url = sourceBundle.getResource("it/incodice/regola/examples/model/Language.hbm.xml");
				InputStream inputStream = url.openStream();
				
				mappings.add(inputStream);
				
			} catch (ClassNotFoundException e) {
				logger.error("Error adding annotaded class: " + s, e);
				throw new RuntimeException(e);
			} catch (IOException e) {
				logger.error("Error adding mapping resources: " + s, e);
				throw new RuntimeException(e);
			}
		}
		createNewSessionFactory();
	}
	
	public void removeAnnotatedClasses(Bundle sourceBundle, String[] classes) {
		for(String s : classes) {
			for (Class c : annotatedClasses) {
				if (c.getName().equals(s)) {
					//logger.error("Removing class: " + s);
					annotatedClasses.remove(c);
					break;
				}
			}
		}
		
		mappings.clear();
		
		createNewSessionFactory();
	}
	
	public void addAnnotatedClasses(Class[] classes) {
		annotatedClasses.addAll(Arrays.asList(classes));
		createNewSessionFactory();
	}
	
	public void removeAnnotatedClasses(Class[] classes) {
		annotatedClasses.removeAll(Arrays.asList(classes));
		createNewSessionFactory();
	}

	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}

	public Resource[] getConfigLocations() {
		return configLocations;
	}

	public void setMappings(List<InputStream> mappings) {
		this.mappings = mappings;
	}

	public List<InputStream> getMappings() {
		return mappings;
	}

}
