package org.regola.batch;

public final class RetryableFailureException extends
		RuntimeException {
	private static final long serialVersionUID = 1L;

	public RetryableFailureException(String message) {
		super(message);
	}

	public RetryableFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}