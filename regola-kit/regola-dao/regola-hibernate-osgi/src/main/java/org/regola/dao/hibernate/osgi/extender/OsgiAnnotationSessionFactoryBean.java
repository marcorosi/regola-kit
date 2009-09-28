package org.regola.dao.hibernate.osgi.extender;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class OsgiAnnotationSessionFactoryBean extends
		AnnotationSessionFactoryBean {

	public OsgiAnnotationSessionFactoryBean()
	{
		super();
	}
	
	private List<InputStream> mappings = new ArrayList<InputStream>();
	
	protected void postProcessMappings(Configuration config)
	  throws HibernateException
	{
		super.postProcessMappings(config);
		
		for (InputStream mapping: getMappings())
		{
			config.addInputStream(mapping);
		}
	}

	public void setMappings(List<InputStream> mappings) {
		this.mappings = mappings;
	}

	public List<InputStream> getMappings() {
		return mappings;
	}


}
