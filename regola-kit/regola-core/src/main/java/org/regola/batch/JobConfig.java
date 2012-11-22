package org.regola.batch;

import org.regola.util.EnvironmentUtils;

public final class JobConfig {
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_MAX_ITEMS = Integer.MAX_VALUE;
	public static final int DEFAULT_MAX_TRIES = 3;
	public static final long DEFAULT_RETRY_DELAY = 1000L;
	public static final int DEFAULT_COMMIT_INTERVAL = 1;

	// RUNNING
	private boolean enabled;
	private String environment;
	private String hostname;
	private RunExpression executionWindow;
	// PROCESSING
	private int pageSize;
	private int maxItems;
	private long retryDelay;
	private int maxTries;
	// STORE
	private int commitInterval;

	public static final JobConfig defaultConfig() {
		return new JobConfig(true, EnvironmentUtils.current(),
				EnvironmentUtils.hostname(), RunExpression.anytime(),
				DEFAULT_PAGE_SIZE, DEFAULT_MAX_ITEMS, DEFAULT_MAX_TRIES,
				DEFAULT_RETRY_DELAY, DEFAULT_COMMIT_INTERVAL);
	}

	private static final JobConfig defaultConfig(final String environment,
			final String hostname) {
		return new JobConfig(true, environment, hostname,
				RunExpression.anytime(), DEFAULT_PAGE_SIZE, DEFAULT_MAX_ITEMS,
				DEFAULT_MAX_TRIES, DEFAULT_RETRY_DELAY, DEFAULT_COMMIT_INTERVAL);
	}

	public static JobConfig onProductionHost(final String hostname) {
		if (EnvironmentUtils.isProduction()) {
			return defaultConfig(EnvironmentUtils.PROD_ENV, hostname);
		}
		return defaultConfig(EnvironmentUtils.current(), null);
	}

	public JobConfig(boolean enabled, String environment, String hostname,
			RunExpression executionWindow, int pageSize, int maxItems,
			int maxTries, long retryDelay, int commitInterval) {
		this.enabled = enabled;
		this.environment = environment;
		this.hostname = hostname;
		this.executionWindow = executionWindow;
		this.pageSize = pageSize;
		this.maxItems = maxItems;
		this.maxTries = maxTries;
		this.retryDelay = retryDelay;
		this.commitInterval = commitInterval;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public RunExpression getExecutionWindow() {
		return executionWindow;
	}

	public void setExecutionWindow(RunExpression executionWindow) {
		this.executionWindow = executionWindow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public long getRetryDelay() {
		return retryDelay;
	}

	public void setRetryDelay(long retryDelay) {
		this.retryDelay = retryDelay;
	}

	public int getMaxTries() {
		return maxTries;
	}

	public void setMaxTries(int maxTries) {
		this.maxTries = maxTries;
	}

	public int getCommitInterval() {
		return commitInterval;
	}

	public void setCommitInterval(int commitInterval) {
		this.commitInterval = commitInterval;
	}

}