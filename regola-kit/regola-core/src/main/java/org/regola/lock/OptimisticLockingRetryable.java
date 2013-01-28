package org.regola.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * Classe di utilità per eseguire più tentativi (in caso di errore) di
 * un'operazione su datastore che può sollevare un errore di tipo
 * {@link OptimisticLockingFailureException}.
 */
public abstract class OptimisticLockingRetryable {
	private static final Logger LOG = LoggerFactory
			.getLogger(OptimisticLockingRetryable.class);

	private final long delay;

	public OptimisticLockingRetryable(long delay) {
		this.delay = delay;
	}

	protected abstract void attempt();

	protected void recover() {
	}

	public void perform(int times) {
		if (times <= 0) {
			return;
		}
		try {
			OptimisticLockingFailureException optLockFailure = null;
			int i = 0;
			while (i < times) {
				try {
					attempt();
					return;
				} catch (OptimisticLockingFailureException e) {
					optLockFailure = e;
					i++;
					LOG.error(
							"Operazione fallita per modifiche concorrenti in corso, tentativo {} di {}: {}",
							new Object[] { i, times, e.getMessage() });
					if (e instanceof ObjectOptimisticLockingFailureException) {
						ObjectOptimisticLockingFailureException oolfe = (ObjectOptimisticLockingFailureException) e;
						LOG.error("Risorsa in errore: id={}, classe={}",
								oolfe.getIdentifier(),
								oolfe.getPersistentClassName());
					}
					recover();
					LOG.debug("Attesa di {} ms prima del nuovo tentativo...",
							delay);
					Thread.sleep(delay);
				}
			}
			throw optLockFailure;
		} catch (InterruptedException ie) {
			LOG.error("Ripetizione interrotta: {} - {}", ie.getMessage(),
					ie.getCause() == null ? "n/a" : ie.getCause().getMessage());
		}
	}
}
