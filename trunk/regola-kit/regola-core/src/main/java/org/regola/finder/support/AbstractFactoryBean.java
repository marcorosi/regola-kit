package org.regola.finder.support;

import java.lang.reflect.ParameterizedType;

import org.regola.dao.GenericDao;
import org.regola.finder.FinderExecutor;
import org.regola.finder.FinderIntroductionAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public abstract class AbstractFactoryBean implements FactoryBean,
		InitializingBean {

	private Class<? extends GenericDao<?, ?>> iface;

	public void setInterface(Class<? extends GenericDao<?, ?>> iface) {
		Assert.isTrue(iface.isInterface(),
				"The class specified is not an interface");
		this.iface = iface;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(iface, "The interface to proxy must be specified");
	}

	public Object getObject() throws Exception {
		ProxyFactory factory = new ProxyFactory(new Class[] { iface });
		factory.setTarget(newFinderExecutor(getEntityClass()));
		factory.addAdvisor(new FinderIntroductionAdvisor());
		return factory.getProxy();
	}

	protected Class<?> getEntityClass() {
		ParameterizedType parameterizedGenericDao = findParameterizedGenericDao(iface);
		Assert.notNull(parameterizedGenericDao,
				"Couldn't find parameterized GenericDao");

		return (Class<?>) parameterizedGenericDao.getActualTypeArguments()[0];
	}

	private ParameterizedType findParameterizedGenericDao(Class<?> iface) {
		int i = 0;
		for (Class<?> superInterface : iface.getInterfaces()) {
			if (superInterface == GenericDao.class) {
				return (ParameterizedType) iface.getGenericInterfaces()[i];
			}
			i++;
		}
		return findParameterizedGenericDao(iface.getInterfaces());
	}

	private ParameterizedType findParameterizedGenericDao(Class<?>[] interfaces) {
		for (Class<?> iface : interfaces) {
			ParameterizedType genericDaoInterface = findParameterizedGenericDao(iface);
			if (genericDaoInterface != null) {
				return genericDaoInterface;
			}
		}
		return null;
	}

	protected abstract <T> FinderExecutor<T> newFinderExecutor(Class<T> entityClass);

	public Class<?> getObjectType() {
		// Returns null when is called before this factory bean is fully
		// initialized
		return iface;
	}

	public boolean isSingleton() {
		return true;
	}
}
