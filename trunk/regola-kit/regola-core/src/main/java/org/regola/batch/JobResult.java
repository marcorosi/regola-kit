package org.regola.batch;

import java.util.Date;

/**
 * Value object contenente l'esito dell'esecuzione di un job.
 */
public final class JobResult {

	private final boolean executed;
	private final boolean successful;
	private final boolean cancelled;
	private final int processedItems;
	private final int skippedItems;
	private final int retriedItems;
	private final int failedItems;
	private final int succeededItems;
	private final Date startedAt;
	private final Date finishedAt;
	private final String message;

	public JobResult(final JobContext<?> context) {
		this(context.isExecuted(), context.isSucceeded(),
				context.isCancelled(), context.getProcessedItems(), context
						.getSkippedItems(), context.getRetriedItems(), context
						.getFailedItems(), context.getSucceededItems(), context
						.getStartedAt(), context.getFinishedAt(), context
						.getMessage());
	}

	JobResult(boolean executed, boolean succeeded, boolean cancelled,
			int processedItems, int skippedItems, int retriedItems,
			int failedItems, int succeededItems, Date started, Date finished,
			String message) {
		this.executed = executed;
		this.successful = succeeded;
		this.cancelled = cancelled;
		this.processedItems = processedItems;
		this.skippedItems = skippedItems;
		this.retriedItems = retriedItems;
		this.failedItems = failedItems;
		this.succeededItems = succeededItems;
		this.startedAt = new Date(started.getTime());
		this.finishedAt = new Date(finished.getTime());
		this.message = message;
	}

	public static JobResult disabled() {
		return new JobResult(false, false, false, 0, 0, 0, 0, 0, null, null,
				null);
	}

	public boolean isExecuted() {
		return executed;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public boolean isCancelled() {
		return cancelled;
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

	public Date getStartedAt() {
		return startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "JobResult [executed=" + executed + ", successful=" + successful
				+ ", cancelled=" + cancelled + ", processedItems="
				+ processedItems + ", skippedItems=" + skippedItems
				+ ", retriedItems=" + retriedItems + ", failedItems="
				+ failedItems + ", succeededItems=" + succeededItems
				+ ", startedAt=" + startedAt + ", finishedAt=" + finishedAt
				+ ", message=" + message + "]";
	}

}