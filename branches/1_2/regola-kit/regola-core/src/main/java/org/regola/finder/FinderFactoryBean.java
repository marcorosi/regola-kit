package org.regola.finder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

public class FinderFactoryBean extends AbstractFactoryBean {

	/**
	 * Interfaces to be implemented by the proxy. Held in List to keep the order
	 * of registration, to create JDK proxy with specified order of interfaces.
	 */
	private List<Class<?>> interfaces = new ArrayList<Class<?>>();

	/**
	 * Target bean. Must be configured.
	 */
	private Object target;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(interfaces,
				"At least one proxied interface must be present");
		Assert.notNull(target, "Target must not be null");
		super.afterPropertiesSet();
	}

	@Override
	protected Object createInstance() throws Exception {
		ProxyFactory factory = new ProxyFactory(interfaces
				.toArray(new Class[interfaces.size()]));
		factory.setTarget(target);
		factory.addAdvisor(new FinderIntroductionAdvisor());
		return factory.getProxy();
	}

	/**
	 * Return the type of the proxy. Will check the proxy interface (if a single
	 * one), else fall back to the target bean type.
	 */
	public Class<?> getObjectType() {
		if (getProxiedInterfaces().length == 1) {
			return getProxiedInterfaces()[0];
		} else if (target != null) {
			return target.getClass();
		}
		return null;
	}

	/**
	 * Set the names of the interfaces we're proxying. If no interface is given,
	 * a CGLIB for the actual class will be created.
	 * <p>
	 * This is essentially equivalent to the "setInterfaces" method, but mirrors
	 * TransactionProxyFactoryBean's "setProxyInterfaces".
	 * 
	 * @see #setInterfaces
	 */
	public void setProxyInterfaces(Class<?>[] proxyInterfaces)
			throws ClassNotFoundException {
		setInterfaces(proxyInterfaces);
	}

	/**
	 * Set the interfaces to be proxied.
	 */
	public void setInterfaces(Class<?>[] interfaces) {
		Assert.notNull(interfaces, "Interfaces must not be null");
		this.interfaces.clear();
		for (int i = 0; i < interfaces.length; i++) {
			addInterface(interfaces[i]);
		}
	}

	/**
	 * Add a new proxied interface.
	 * 
	 * @param newInterface
	 *            additional interface to proxy
	 */
	public void addInterface(Class<?> newInterface) {
		Assert.notNull(newInterface, "Interface must not be null");
		if (!newInterface.isInterface()) {
			throw new IllegalArgumentException("[" + newInterface.getName()
					+ "] is not an interface");
		}
		if (!this.interfaces.contains(newInterface)) {
			this.interfaces.add(newInterface);
			if (logger.isDebugEnabled()) {
				logger.debug("Added new aspect interface: "
						+ newInterface.getName());
			}
		}
	}

	public Class<?>[] getProxiedInterfaces() {
		return (Class[]) this.interfaces.toArray(new Class[this.interfaces
				.size()]);
	}

	/**
	 * Remove a proxied interface. Does nothing if it isn't proxied.
	 */
	public boolean removeInterface(Class<?> intf) {
		return this.interfaces.remove(intf);
	}

	/**
	 * Set the given object as target.
	 */
	public void setTarget(Object target) {
		this.target = target;
	}
}
