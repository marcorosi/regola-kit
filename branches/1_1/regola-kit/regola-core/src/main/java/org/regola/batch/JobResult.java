package org.regola.batch;

import java.util.Date;

public final class JobResult {

	private final boolean executed;
	private final boolean success;
	private final int processed;
	private final int skipped;
	private final int retried;
	private final int failed;
	private final int succeeded;
	private final Date started;
	private final Date finished;
	private final String message;

	public JobResult(final JobContext<?> context) {
		this(context.isExecuted(), context.isSuccess(), context.getProcessed(),
				context.getSkipped(), context.getRetried(),
				context.getFailed(), context.getSucceeded(), context
						.getStarted(), context.getFinished(), context
						.getMessage());
	}

	JobResult(boolean executed, boolean success, int processed, int skipped,
			int retried, int failed, int succeeded, Date started,
			Date finished, String message) {
		this.executed = executed;
		this.success = success;
		this.processed = processed;
		this.skipped = skipped;
		this.retried = retried;
		this.failed = failed;
		this.succeeded = succeeded;
		this.started = new Date(started.getTime());
		this.finished = new Date(finished.getTime());
		this.message = message;
	}

	public static JobResult disabled() {
		return new JobResult(false, false, 0, 0, 0, 0, 0, null, null, null);
	}

	public boolean isExecuted() {
		return executed;
	}

	public boolean isSuccessful() {
		return success;
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

	public Date getStarted() {
		return started;
	}

	public Date getFinished() {
		return finished;
	}

	public String getMessage() {
		return message;
	}

}