package org.regola.batch.run;

import java.io.Serializable;
import java.util.Date;

import org.regola.batch.JobContext;
import org.regola.batch.RunExpression;
import org.regola.batch.policy.RunPolicy;
import org.regola.util.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Policy di esecuzione del job di default.
 * <p>
 * Si basa su questi criteri:
 * <ul>
 * <li>flag di abilitazione globale del job;
 * <li>ambiente di esecuzione;
 * <li>hostname nel quale viene eseguito;
 * <li>finestra oraria di esecuzione.
 * </ul>
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 * @see org.regola.batch.JobConfig#isEnabled()
 * @see org.regola.batch.JobConfig#getExecutionWindow()
 * @see org.regola.batch.JobConfig#getHostname()
 * @see org.regola.batch.JobConfig#getEnvironment()
 * @see JobContext#getHostname()
 * @see JobContext#getEnvironment()
 */
public class DefaultRunPolicy<T extends Serializable> implements RunPolicy<T> {
	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultRunPolicy.class);

	private final boolean enabled;
	private final String environment;
	private final String hostname;
	private final RunExpression executionWindow;

	public DefaultRunPolicy() {
		this(true, EnvironmentUtils.current(), EnvironmentUtils.hostname(),
				RunExpression.anytime());
	}

	public DefaultRunPolicy(boolean enabled, final String environment,
			final String hostname, final RunExpression executionWindow) {
		this.enabled = enabled;
		this.environment = environment == null ? environment : environment
				.toLowerCase();
		this.hostname = hostname == null ? hostname : hostname.toLowerCase();
		this.executionWindow = executionWindow;
	}

	/**
	 * Può essere eseguito in questo momento ({@link #now()}) e in questo
	 * contesto?
	 */
	public boolean isRunnable(final JobContext<T> context) {
		if (!enabled) {
			return false;
		}
		if (hostname == null) {
			return false;
		}
		if (context.getHostname() == null
				|| !context.getHostname().toLowerCase().contains(hostname)) {
			LOG.debug("Disabilitato per hostname: host={}, richiesto={}",
					context.getHostname(), hostname);
			return false;
		}
		if (environment == null) {
			return false;
		}
		if (!environment.equalsIgnoreCase(context.getEnvironment())) {
			LOG.debug("Disabilitato per environment: env={}, richiesto={}",
					context.getEnvironment(), environment);
			return false;
		}
		return executionWindow.isSatifiedBy(now());
	}

	protected Date now() {
		return new Date();
	}

}