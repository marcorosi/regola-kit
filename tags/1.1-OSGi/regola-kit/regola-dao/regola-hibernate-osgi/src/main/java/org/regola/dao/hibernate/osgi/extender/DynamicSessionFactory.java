package org.regola.dao.hibernate.osgi.extender;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Hibernate session factory that can get updated during the runtime of the
 * application.
 */
public class DynamicSessionFactory implements FactoryBean, InitializingBean {

	private DynamicConfiguration configuration;

	public void setConfiguration(DynamicConfiguration configuration) {
		this.configuration = configuration;
	}

	public Object getObject() throws Exception {
		return configuration.getSessionFactory();
	}

	public Class getObjectType() {
		return SessionFactory.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
