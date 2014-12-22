package org.regola.batch;

import java.io.Serializable;
import java.util.Date;

import org.regola.util.EnvironmentUtils;

/**
 * Contesto di esecuzione di un job.
 * <p>
 * Tiene traccia dell'ambiente e delle varie fasi di elaborazione di una precisa
 * istanza di esecuzione del job.
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public class JobContext<T extends Serializable> {
	private final String jobName;
	private final String executionId;
	private final String hostname;
	private final String environment;

	private int loadIteration;
	private T currentItem;
	private int currentTry;
	private T lastProcessedItem;

	private boolean executed;
	private boolean succeeded;
	private boolean cancelled;
	private Date startedAt;
	private Date finishedAt;
	private String message;
	private Throwable lastError;

	// stats
	private int processedItems;
	private int skippedItems;
	private int retriedItems;
	private int failedItems;
	private int succeededItems;

	public JobContext(final String jobName) {
		this(jobName, EnvironmentUtils.current(), EnvironmentUtils.hostname());
	}

	public JobContext(final String jobName, final String environment,
			final String hostname) {
		this(jobName, environment, hostname, JobUtils.newExecutionId());
	}

	public JobContext(final String jobName, final String environment,
			final String hostname, final String executionId) {
		this.jobName = jobName;
		this.environment = environment;
		this.hostname = hostname;
		this.executionId = executionId;
	}

	public T getLastProcessedItem() {
		return lastProcessedItem;
	}

	public T getCurrentItem() {
		return currentItem;
	}

	public void startProcessing(T item) {
		currentTry = 1;
		processedItems++;
		currentItem = item;
	}

	public void itemSkipped() {
		skippedItems++;
	}

	public void itemRetried(RuntimeException e) {
		retriedItems++;
		lastError = e;
	}

	public void itemFailed(Throwable e) {
		failedItems++;
	}

	public void itemSucceeded() {
		succeededItems++;
	}

	public void loading() {
		loadIteration++;
	}

	public void retrying() {
		currentTry++;
	}

	public JobContext<T> started() {
		startedAt(now());
		return this;
	}

	public JobContext<T> startedAt(final Date date) {
		executed = true;
		startedAt = new Date(date.getTime());
		return this;
	}

	public JobContext<T> finished() {
		finishedAt(now());
		return this;
	}

	public JobContext<T> finishedAt(final Date date) {
		finishedAt = new Date(date.getTime());
		return this;
	}

	public JobContext<T> cancelled() {
		this.cancelled = true;
		return this;
	}

	public JobContext<T> succeeded() {
		succeededAt(now());
		return this;
	}

	public JobContext<T> succeededAt(final Date date) {
		succeeded = true;
		finishedAt(date);
		return this;
	}

	public JobContext<T> failed(Throwable e) {
		failedAt(now());
		lastError = e;
		return this;
	}

	public JobContext<T> failedAt(final Date date) {
		succeeded = false;
		finishedAt(date);
		return this;
	}

	public JobContext<T> withMessage(final String message) {
		this.message = message;
		return this;
	}

	public boolean isFailed() {
		return !succeeded;
	}

	public JobContext<T> disabled() {
		executed = false;
		finishedAt(now());
		return this;
	}

	public Throwable getLastError() {
		return lastError;
	}

	protected Date now() {
		return new Date();
	}

	JobResult buildResult() {
		return new JobResult(executed, succeeded, cancelled, processedItems,
				skippedItems, retriedItems, failedItems, succeededItems,
				startedAt, finishedAt, message);

	}

	public String getEnvironment() {
		return environment;
	}

	public String getHostname() {
		return hostname;
	}

	public String getExecutionId() {
		return executionId;
	}

	public int getLoadIteration() {
		return loadIteration;
	}

	public int getCurrentTry() {
		return currentTry;
	}

	public int getProcessedItems() {
		return processedItems;
	}

	public int getSkippedItems() {
		return skippedItems;
	}

	public int getRetriedItems() {
		return retriedItems;
	}

	public int getFailedItems() {
		return failedItems;
	}

	public int getSucceededItems() {
		return succeededItems;
	}

	public String getJobName() {
		return jobName;
	}

	public boolean isExecuted() {
		return executed;
	}

	public boolean isSucceeded() {
		return succeeded;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return getId();
	}

	public String getId() {
		return String.format("%s[%s]@%s[%s]", jobName, executionId, hostname,
				environment);
	}
}