package org.regola.batch.retry;

import java.io.Serializable;

import org.regola.batch.JobConfig;
import org.regola.batch.JobContext;
import org.regola.batch.RetryableFailureException;
import org.regola.batch.policy.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * La politica di retry di default degli elementi.
 * <p>
 * Si basa su questi criteri:
 * <ul>
 * <li>tipo di eccezione ritentabile, vedi {@link RetryableFailureException};
 * <li>numero massimo di tentativi;
 * <li>ritardo fra un tentativo e il successivo;
 * </ul>
 * Il ritardo prima di un nuovo tentativo viene calcolato in base al numero
 * di tentativo corrente e al ridardo di base, impostato nel costruttore.
 * 
 * @see JobConfig#DEFAULT_MAX_TRIES
 * @see JobConfig#DEFAULT_RETRY_DELAY
 */
public class DefaultRetryPolicy<T extends Serializable> implements
		RetryPolicy<T> {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRetryPolicy.class);

	private final int tries;
	private final long delay;

	public DefaultRetryPolicy() {
		this(JobConfig.DEFAULT_MAX_TRIES, JobConfig.DEFAULT_RETRY_DELAY);
	}

	public DefaultRetryPolicy(final int tries, final long delay) {
		this.tries = tries;
		this.delay = delay;
	}

	public boolean retryOnError(final JobContext<T> context,
			final RuntimeException e) {
		return e instanceof RetryableFailureException
				&& context.getCurrentTry() < tries;
	}

	/**
	 * Il ritardo inizia con {@link #delay} e aumenta di {@link #delay} ad
	 * ogni tentativo.
	 */
	public void onRetrying(final JobContext<T> context) {
		LOG.debug("Attesa prossimo tentativo di elaborazione della risorsa...");
		try {
			Thread.sleep(delay * context.getCurrentTry());
		} catch (InterruptedException e) {
			LOG.error("Attesa nuovo tentativo interrotta", e);
		}
	}
}