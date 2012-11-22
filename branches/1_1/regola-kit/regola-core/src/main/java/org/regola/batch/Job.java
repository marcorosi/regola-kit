package org.regola.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.util.EnvironmentUtils;

public abstract class Job<T extends Serializable> {

	public interface RunPolicy<T extends Serializable> {

		boolean isSatisfiedBy(final JobContext<T> context);

	}

	public interface LockPolicy<T extends Serializable> {

		boolean acquireExecution(final JobContext<T> context);

		void releaseExecution(final JobContext<T> context);

		boolean acquireItem(final JobContext<T> context);

		void releaseItem(final JobContext<T> context);

	}

	public interface SkipPolicy<T extends Serializable> {

		boolean isSatisfiedBy(final JobContext<T> context);

	}

	public interface RetryPolicy<T extends Serializable> {

		boolean isSatisfiedBy(final JobContext<T> context,
				final RuntimeException e);

		void wait(final JobContext<T> context);

	}

	public interface CommitPolicy<T extends Serializable> {

		boolean isSatisfiedBy(final JobContext<T> context);

	}

	protected final Log LOG = LogFactory.getLog(getClass());

	private int maxItems;
	private RunPolicy<T> runPolicy;
	private LockPolicy<T> lockPolicy;
	private SkipPolicy<T> skipPolicy;
	private RetryPolicy<T> retryPolicy;
	private CommitPolicy<T> commitPolicy;

	public Job() {
		this(JobConfig.defaultConfig());
	}

	public Job(final JobConfig config) {
		maxItems = config.getMaxItems();
		runPolicy = new DefaultRunPolicy<T>(config.isEnabled(), config.getEnvironment(),
				config.getHostname(), config.getExecutionWindow());
		lockPolicy = new NullLockPolicy<T>();
		skipPolicy = new NullSkipPolicy<T>();
		retryPolicy = new DefaultRetryPolicy<T>(config.getMaxTries(),
				config.getRetryDelay());
		commitPolicy = new DefaultCommitPolicy<T>(config.getCommitInterval());
	}

	public void execute() {
		execute(new JobContext<T>(name()));
	}

	protected String name() {
		String className = getClass().getSimpleName();
		if (!"".equals(className)) {
			return className;
		}
		className = getClass().getName();
		if (className.contains(".")) {
			return className.substring(className.lastIndexOf(".") + 1);
		}
		return className;
	}

	public JobResult execute(final JobContext<T> context) {

		LOG.trace("Richiesto avvio del job " + context + "...");

		context.started();

		if (!enabled(context)) {
			LOG.trace("Fine esecuzione del job " + context + ": non abilitato");
			return context.disabled().buildResult();
		}

		LOG.info("Inizio esecuzione del job " + context + "...");
		
		if (!acquireExecution(context)) {
			LOG.warn("Impossibile ottenere il diritto di esecuzione esclusivo del job");
			return context.failed().withMessage("Cannot acquire lock")
					.buildResult();
		}

		try {
			onStart(context);

			final Set<T> commitQueue = new LinkedHashSet<T>();
			LOADING: while (true) {
				context.loading();
				LOG.info("Caricamento degli elementi da elaborare, iterazione "
						+ context.getLoadIteration());
				final List<T> itemsToProcess = load(context);
				LOG.info("Caricati " + itemsToProcess.size()
						+ " elementi da elaborare");
				if (itemsToProcess.isEmpty()) {
					break LOADING;
				}
				NEXT_ITEM: for (T item : itemsToProcess) {
					context.startProcessing(item);

					if (!acquireItem(context)) {
						LOG.warn("Impossibile ottenere il diritto di elaborazione esclusivo dell'elemento");
						continue NEXT_ITEM;
					}
					try {
						T processedItem = null;
						try {
							if (skipPolicy.isSatisfiedBy(context)) {
								context.itemSkipped();
								LOG.info("Elemento ignorato");
								continue NEXT_ITEM;
							}
							processedItem = retriableProcessing(item, context);
						} catch (RuntimeException e) {
							// on processing w/o retry
							context.itemFailed();
							LOG.error("Fallita elaborazione dell'elemento");
							continue NEXT_ITEM;
						}
						commit(processedItem, context, commitQueue);
						context.itemSucceeded();
					} finally {
						releaseItem(context);
						if (context.getProcessed() >= maxItems) {
							LOG.warn("Superato il numero massimo di elementi elaborabili: "
									+ maxItems);
							break LOADING;
						}
					}
				}
			}
			
			context.succeeded();
			
		} catch (RuntimeException e) {
			// on load, commit or release item
			LOG.error("Impossibile procedere nell'esecuzione del job", e);
			context.failed();
		} finally {
			try {
				onFinish(context);
			} finally {
				releaseExecution(context);
			}
		}

		LOG.info("Fine esecuzione del job " + context);
		return context.buildResult();
	}

	protected void onStart(JobContext<T> context) {
	}

	protected void onFinish(JobContext<T> context) {
	}
	
	private T retriableProcessing(final T item, final JobContext<T> context) {
		boolean retrying = false;
		T processedItem = null;
		do {
			if (retrying) {
				LOG.debug("Attesa prossimo tentativo di elaborazione della risorsa...");

				retryPolicy.wait(context);
				retrying = false;
			}
			try {
				processedItem = process(item);
			} catch (RuntimeException e) {
				LOG.error("Errore nell'elaborazione dell'elemento", e);

				if (retryPolicy.isSatisfiedBy(context, e)) {
					context.itemRetried();
					retrying = true;
					LOG.debug("L'elaborazione dell'elemento verrà ritentata, tentativo "
							+ context.getCurrentTry());
				} else {
					throw e;
				}
			}
		} while (retrying);
		return processedItem;
	}

	private void commit(final T processedItem, final JobContext<T> context,
			final Set<T> commitQueue) {

		if (processedItem == null) {
			return;
		}

		commitQueue.add(processedItem);

		if (commitPolicy.isSatisfiedBy(context)) {
			LOG.debug("Salvataggio di " + commitQueue.size()
					+ " elementi elaborati con successo");
			store(new ArrayList<T>(commitQueue));
			commitQueue.clear();
		}
	}

	/**
	 * default operation do nothing: override to delegate to a transactional
	 * store
	 */
	protected void store(final List<T> commitQueue) {
	}

	/**
	 * put here your item processing logic
	 */
	protected abstract T process(final T item);

	protected boolean acquireExecution(final JobContext<T> context) {
		return true;
	}

	protected void releaseExecution(final JobContext<T> context) {
	}

	protected boolean acquireItem(final JobContext<T> context) {
		return true;
	}

	protected void releaseItem(final JobContext<T> context) {
	}

	protected boolean enabled(final JobContext<T> context) {
		return runPolicy.isSatisfiedBy(context);
	}

	/**
	 * put here your items read logic
	 */
	protected abstract List<T> load(final JobContext<T> context);

	public static class DefaultRunPolicy<T extends Serializable> implements
			RunPolicy<T> {
		private static final Log LOG = LogFactory
				.getLog(DefaultRunPolicy.class);

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
			this.hostname = hostname == null ? hostname : hostname
					.toLowerCase();
			this.executionWindow = executionWindow;
		}

		public boolean isSatisfiedBy(final JobContext<T> context) {
			if (!enabled) {
				return false;
			}
			if (hostname != null) {
				if (context.getHostname() == null
						|| !context.getHostname().toLowerCase()
								.contains(hostname)) {
					LOG.debug("Disabilitato per hostname: host="
							+ context.getHostname() + ", richiesto=" + hostname);
					return false;
				}
			}
			if (environment != null) {
				if (!environment.equalsIgnoreCase(context.getEnvironment())) {
					LOG.debug("Disabilitato per environment: env="
							+ context.getEnvironment() + ", richiesto="
							+ environment);
					return false;
				}
			}
			return executionWindow.isSatifiedBy(now());
		}

		protected Date now() {
			return new Date();
		}

	}

	protected static class NullLockPolicy<T extends Serializable> implements
			LockPolicy<T> {

		public boolean acquireExecution(final JobContext<T> context) {
			return true;
		}

		public void releaseExecution(final JobContext<T> context) {
		}

		public boolean acquireItem(final JobContext<T> context) {
			return true;
		}

		public void releaseItem(final JobContext<T> context) {
		}

	}

	protected static class NullSkipPolicy<T extends Serializable> implements
			SkipPolicy<T> {

		public boolean isSatisfiedBy(final JobContext<T> context) {
			return false;
		}

	}

	public static class DefaultRetryPolicy<T extends Serializable> implements
			RetryPolicy<T> {
		protected final Log LOG = LogFactory.getLog(getClass());
		private final int tries;
		private final long delay;

		public DefaultRetryPolicy() {
			this(JobConfig.DEFAULT_MAX_TRIES, JobConfig.DEFAULT_RETRY_DELAY);
		}

		public DefaultRetryPolicy(final int tries, final long delay) {
			this.tries = tries;
			this.delay = delay;
		}

		public boolean isSatisfiedBy(final JobContext<T> context,
				final RuntimeException e) {
			return e instanceof RetryableFailureException
					&& context.getCurrentTry() < tries;
		}

		public void wait(final JobContext<T> context) {
			try {
				Thread.sleep(delay * context.getCurrentTry());
			} catch (InterruptedException e) {
				LOG.error("Attesa nuovo tentativo interrotta", e);
			}
		}
	}

	public static class DefaultCommitPolicy<T extends Serializable> implements
			CommitPolicy<T> {

		private final int commitInterval;
		private int commitCounter = 0;

		public DefaultCommitPolicy() {
			this(JobConfig.DEFAULT_COMMIT_INTERVAL);
		}

		public DefaultCommitPolicy(final int commitInterval) {
			this.commitInterval = commitInterval;
		}

		public boolean isSatisfiedBy(final JobContext<T> context) {
			commitCounter++;
			if (commitCounter >= commitInterval) {
				commitCounter = 0;
				return true;
			}
			return false;
		}
	}

	public SkipPolicy<T> getSkipPolicy() {
		return skipPolicy;
	}

	public void setSkipPolicy(final SkipPolicy<T> skipPolicy) {
		this.skipPolicy = skipPolicy;
	}

	public RetryPolicy<T> getRetryPolicy() {
		return retryPolicy;
	}

	public void setRetryPolicy(final RetryPolicy<T> retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public CommitPolicy<T> getCommitPolicy() {
		return commitPolicy;
	}

	public void setCommitPolicy(final CommitPolicy<T> commitPolicy) {
		this.commitPolicy = commitPolicy;
	}

	public LockPolicy<T> getLockPolicy() {
		return lockPolicy;
	}

	public void setLockPolicy(final LockPolicy<T> lockPolicy) {
		this.lockPolicy = lockPolicy;
	}

	public RunPolicy<T> getRunPolicy() {
		return runPolicy;
	}

	public void setRunPolicy(final RunPolicy<T> runPolicy) {
		this.runPolicy = runPolicy;
	}

}