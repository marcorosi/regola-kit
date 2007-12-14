package org.regola.service.impl;

/*
 * Adapted from org.appfuse.service.impl.GenericManagerImpl
 */

import org.regola.dao.GenericDao;
import org.regola.service.GenericManager;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.model.ModelPattern;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * <p>
 * To register this class in your Spring context file, use the following XML.
 * 
 * <pre>
 *      &lt;bean id=&quot;userManager&quot; class=&quot;org.appfuse.service.impl.GenericManagerImpl&quot;&gt;
 *          &lt;constructor-arg&gt;
 *              &lt;bean class=&quot;org.appfuse.dao.hibernate.GenericDaoHibernate&quot;&gt;
 *                  &lt;constructor-arg value=&quot;org.appfuse.model.User&quot;/&gt;
 *                  &lt;property name=&quot;sessionFactory&quot; ref=&quot;sessionFactory&quot;/&gt;
 *              &lt;/bean&gt;
 *          &lt;/constructor-arg&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * <p>
 * If you're using iBATIS instead of Hibernate, use:
 * 
 * <pre>
 *      &lt;bean id=&quot;userManager&quot; class=&quot;org.appfuse.service.impl.GenericManagerImpl&quot;&gt;
 *          &lt;constructor-arg&gt;
 *              &lt;bean class=&quot;org.appfuse.dao.ibatis.GenericDaoiBatis&quot;&gt;
 *                  &lt;constructor-arg value=&quot;org.appfuse.model.User&quot;/&gt;
 *                  &lt;property name=&quot;dataSource&quot; ref=&quot;dataSource&quot;/&gt;
 *                  &lt;property name=&quot;sqlMapClient&quot; ref=&quot;sqlMapClient&quot;/&gt;
 *              &lt;/bean&gt;
 *          &lt;/constructor-arg&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class GenericManagerImpl<T, PK extends Serializable> implements
		GenericManager<T, PK> {
	protected final Log log = LogFactory.getLog(getClass());

	protected GenericDao<T, PK> genericDao;

	public GenericManagerImpl(GenericDao<T, PK> genericDao) {
		this.genericDao = genericDao;
	}

	public List<T> getAll() {
		return genericDao.getAll();
	}

	public T get(PK id) {
		return genericDao.get(id);
	}

	public void save(T object) {
		genericDao.save(object);
	}

	public void remove(PK id) {
		genericDao.remove(id);
	}

	public List<T> find(ModelPattern filter) {
		return genericDao.find(filter);
	}

	public int countFind(ModelPattern filter) {
		return genericDao.count(filter);
	}

	public void update(T object, PK id) {
		remove(id);
		save(object);
	}
}
