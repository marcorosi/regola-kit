package org.regola.batch;

import java.io.Serializable;
import java.util.Date;

import org.regola.util.EnvironmentUtils;

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
	private boolean success;
	private Date started;
	private Date finished;
	private String message;
	private boolean cancelled;

	// stats
	private int processed;
	private int skipped;
	private int retried;
	private int failed;
	private int succeeded;

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
		processed++;
		currentItem = item;
	}

	public void itemSkipped() {
		skipped++;
	}

	public void itemRetried() {
		retried++;
		currentTry++;
	}

	public void itemFailed() {
		failed++;
	}

	public void itemSucceeded() {
		succeeded++;
	}

	public void loading() {
		loadIteration++;
	}

	public JobContext<T> started() {
		startedAt(now());
		return this;
	}

	public JobContext<T> startedAt(final Date date) {
		executed = true;
		started = new Date(date.getTime());
		return this;
	}

	public JobContext<T> finished() {
		finishedAt(now());
		return this;
	}

	public JobContext<T> finishedAt(final Date date) {
		finished = new Date(date.getTime());
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
		success = true;
		finishedAt(date);
		return this;
	}

	public JobContext<T> failed() {
		failedAt(now());
		return this;
	}

	public JobContext<T> failedAt(final Date date) {
		success = false;
		finishedAt(date);
		return this;
	}

	public JobContext<T> withMessage(final String message) {
		this.message = message;
		return this;
	}

	public boolean isFailed() {
		return !success;
	}

	public JobContext<T> disabled() {
		executed = false;
		finishedAt(now());
		return this;
	}

	protected Date now() {
		return new Date();
	}

	JobResult buildResult() {
		return new JobResult(executed, success, processed, skipped, retried,
				failed, succeeded, started, finished, message);

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

	public int getProcessed() {
		return processed;
	}

	public int getSkipped() {
		return skipped;
	}

	public int getRetried() {
		return retried;
	}

	public int getFailed() {
		return failed;
	}

	public int getSucceeded() {
		return succeeded;
	}

	@Override
	public String toString() {
		return jobName + "[" + executionId + "]@" + hostname + "["
				+ environment + "]";
	}

	public String getJobName() {
		return jobName;
	}

	public boolean isExecuted() {
		return executed;
	}

	public boolean isSuccess() {
		return success;
	}

	public Date getStarted() {
		return started;
	}

	public Date getFinished() {
		return finished;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	public String getMessage() {
		return message;
	}

}