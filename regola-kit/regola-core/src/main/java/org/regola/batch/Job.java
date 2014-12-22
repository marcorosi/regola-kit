package org.regola.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.regola.batch.commit.DefaultCommitPolicy;
import org.regola.batch.lock.NullLockPolicy;
import org.regola.batch.policy.CommitPolicy;
import org.regola.batch.policy.LockPolicy;
import org.regola.batch.policy.RetryPolicy;
import org.regola.batch.policy.RunPolicy;
import org.regola.batch.policy.SkipPolicy;
import org.regola.batch.retry.DefaultRetryPolicy;
import org.regola.batch.run.DefaultRunPolicy;
import org.regola.batch.skip.NullSkipPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template di esecuzione di job batch.
 * <p>
 * Il job può essere creato a partire da una {@link JobConfig} oppure no (in
 * quest'ultimo caso usa la {@link JobConfig#defaultConfig()}).
 * <p>
 * Il comportamento del job è scomposto in varie policy, quali {@link RunPolicy}, {@link LockPolicy}, {@link SkipPolicy}, {@link RetryPolicy},
 * {@link CommitPolicy}, le policy possono essere cambiate (sostituite)
 * sull'istanza del job, prima dell'avvio dell'elaborazione.
 * <p>
 * Il job viene fatto partire con il metodo {@link #execute()} oppure con
 * {@link #execute(JobContext)} se il ciclo di vita del contesto di esecuzione è
 * esterno a quello del job.
 * <p>
 * Gli hook principali del job (confrontare con gli eventi del ciclo di vita del
 * {@link JobContext contesto}), da implementare obbligatoriamente, sono:
 * <ol>
 * <li>{@link #load(JobContext)}: <b>obbligatorio</b>, logica di caricamento
 * degli elementi;
 * <li>{@link #process(Serializable)}: <b>obbligatorio</b>, logica di
 * elaborazione di un elemento;
 * </ol>
 * 
 * Gli hook opzionali, ove non ridefiniti, delegano alle policy relative:
 * <ol>
 * <li>{@link #name()}: <i>opzionale</i>, ridefinisce il nome del job;
 * <li>{@link #enabled(JobContext)}: <i>opzionale</i>, ridefinisce il controllo
 * di abilitazione all'esecuzione;
 * <li>{@link #onStart(JobContext)}: <i>opzionale</i>, evento di inizio
 * elaborazione degli elementi;
 * <li>{@link #acquireExecution(JobContext)}: <i>opzionale</i>, ridefinisce il
 * lock globale di esecuzione;
 * <li>{@link #acquireItem(JobContext)}: <i>opzionale</i>, invocato per ogni
 * elemento, ridefininsce il lock per l'elaborazione dello stesso;
 * <li>{@link #store(List)}: <i>opzionale</i>, eventuale logica di persistenza,
 * al termine dell'elaborazione di uno o più elementi;
 * <li>{@link #releaseItem(JobContext)}: <i>opzionale</i>, invocato per ogni
 * elemento, ridefininsce il rilascio del lock per lo stesso;
 * <li>{@link #releaseExecution(JobContext)}: <i>opzionale</i> ridefininsce il
 * rilascio del lock di esecuzione;
 * <li>{@link #onFinish(JobContext)}: <i>opzionale</i>, evento di fine
 * elaborazione.
 * </ol>
 * <p>
 * 
 * @param <T>
 *            tipo degli elementi elaborati dal job
 */
public abstract class Job<T extends Serializable> {

	/**
	 * Contenitore degli elementi elaborati con successo (ovvero restituiti
	 * dalla {@link Job#process(Serializable)}) e diversi da <code>null</code>
	 * che dovranno essere passati alla {@link Job#store(List)} in base ai
	 * criteri della {@link Job#getCommitPolicy()}.
	 */
	protected static class CommitQueue<I extends Serializable> {
		private static final Logger LOG = LoggerFactory
				.getLogger(CommitQueue.class);
		private final Job<I> job;
		private final List<I> processed = new ArrayList<I>();

		public CommitQueue(Job<I> owner) {
			job = owner;
		}

		/**
		 * Aggiunge un elemento (se diverso da <code>null</code>) alla coda di
		 * commit.
		 */
		public void add(I processedItem) {
			if (processedItem == null) {
				return;
			}
			LOG.trace("Aggiunta di 1 elemento alla coda di commit: {}",
					processedItem);
			processed.add(processedItem);
		}

		/**
		 * Applica la politica di commit e, se raggiunto il commit interval,
		 * svuota la coda di commit.
		 */
		public void commitIfNecessary(final JobContext<I> context) {
			if (job.commitPolicy.commitQueued(context)) {
				flush();
			}
		}

		/**
		 * Delega a {@link Job#store(List)} se la coda di commit contiene degli
		 * elementi.
		 */
		private void flush() {
			if (processed.isEmpty()) {
				return;
			}
			LOG.debug("Salvataggio di {} elementi in coda", processed.size());
			job.store(processed);
			processed.clear();
		}
	}

	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	private int maxItems;
	private RunPolicy<T> runPolicy;
	private LockPolicy<T> lockPolicy;
	private SkipPolicy<T> skipPolicy;
	private RetryPolicy<T> retryPolicy;
	private CommitPolicy<T> commitPolicy;
	private final CommitQueue<T> commitQueue;

	public Job() {
		this(JobConfig.defaultConfig());
	}

	public Job(final JobConfig config) {
		maxItems = config.getMaxItems();
		runPolicy = new DefaultRunPolicy<T>(config.isEnabled(),
				config.getEnvironment(), config.getHostname(),
				config.getExecutionWindow());
		lockPolicy = new NullLockPolicy<T>();
		skipPolicy = new NullSkipPolicy<T>();
		retryPolicy = new DefaultRetryPolicy<T>(config.getMaxTries(),
				config.getRetryDelay());
		commitPolicy = new DefaultCommitPolicy<T>(config.getCommitInterval());
		commitQueue = new CommitQueue<T>(this);
	}

	/**
	 * Avvia l'esecuzione del job con una nuova istanza di {@link JobContext},
	 * delegando a {@link #execute(JobContext)}.
	 * 
	 * @return esito dell'esecuzione.
	 * @see #execute(JobContext)
	 */
	public JobResult execute() {
		return execute(new JobContext<T>(name()));
	}

	/**
	 * Per default usa il nome semplice della classe.
	 * 
	 * @return il nome del job
	 */
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

	/**
	 * Avvia l'esecuzione del job usando il contesto passato come parametro.
	 * 
	 * @return esito dell'esecuzione.
	 */
	public JobResult execute(final JobContext<T> context) {

		LOG.trace("Richiesto avvio del job {}...", context);

		context.started();

		if (!enabled(context)) {
			LOG.trace("Fine esecuzione del job {}: non abilitato", context);
			return context.disabled().buildResult();
		}

		LOG.info("Inizio esecuzione del job {}...", context);

		if (!acquireExecution(context)) {
			LOG.warn("Impossibile ottenere il diritto di esecuzione esclusivo del job");
			return context.failed(null).withMessage("Cannot acquire lock")
					.buildResult();
		}

		try {
			onStart(context);

			boolean abortProcessing = false;
			LOADING: while (!abortProcessing) {
				context.loading();
				LOG.info(
						"Caricamento degli elementi da elaborare, iterazione {}",
						context.getLoadIteration());
				final List<T> itemsToProcess = load(context);
				LOG.info("Caricati {} elementi da elaborare",
						itemsToProcess.size());
				if (itemsToProcess.isEmpty()) {
					break LOADING;
				}
				NEXT_ITEM: for (T item : itemsToProcess) {
					if (context.getProcessedItems() >= maxItems) {
						LOG.warn(
								"Superato il numero massimo di elementi elaborabili: {}",
								maxItems);
						break LOADING;
					}
					context.startProcessing(item);

					if (!acquireItem(context)) {
						LOG.warn("Impossibile ottenere il diritto di elaborazione esclusivo dell'elemento corrente");
						continue NEXT_ITEM;
					}
					try {
						abortProcessing = skipOrProcess(item, context);
					} finally {
						releaseItem(context);
					}
					commitQueue.commitIfNecessary(context);
					if (abortProcessing) {
						context.cancelled();
						LOG.warn("Annullamento: non verranno elaborati altri elementi");
						break NEXT_ITEM;
					}
				}
			}

			commitQueue.flush();
			context.succeeded();

		} catch (Throwable e) {
			// on start, load, commit or acquire/release item
			// on Throwable/Error
			LOG.error("Impossibile procedere nell'esecuzione del job", e);
			context.failed(e);
		} finally {
			try {
				onFinish(context);
			} finally {
				releaseExecution(context);
			}
		}

		LOG.info("Fine esecuzione del job {}: {}", context,
				context.isSucceeded() ? "riuscito" : "FALLITO!");
		return context.buildResult();
	}

	/**
	 * In questa fase il job è abilitato ed ha acquisito l'eventuale lock di
	 * esecuzione.
	 */
	protected void onStart(JobContext<T> context) {
	}

	/**
	 * In questa fase l'elaborazione degli elementi è terminata (con successo o
	 * in seguito ad un errore) e il job si trova subito prima di rilasciare
	 * l'eventuale lock di esecuzione.
	 */
	protected void onFinish(JobContext<T> context) {
	}

	/**
	 * Applica la politica di skip e delega a
	 * {@link #processAndRetryOnErrors(Serializable, JobContext)}.
	 * <p>
	 * Se l'elaborazione ha successo l'elemento viene accodato
	 * {@link CommitQueue#add(Serializable)}.
	 * 
	 * @return true se l'elaborazione degli elementi rimanenti deve
	 *         interrompersi.
	 * @throws Throwable
	 */
	private boolean skipOrProcess(T item, JobContext<T> context)
			throws Throwable {
		if (skipPolicy.skipBeforeProcessing(context)) {
			context.itemSkipped();
			LOG.info("Elemento ignorato");
			return skipPolicy.skipRemaining(context);
		}
		T processedItem = null;
		try {
			processedItem = processAndRetryOnErrors(item, context);
		} catch (Exception e) {
			// on processing w/o retry (RE) or checked (E)
			context.itemFailed(e);
			LOG.error("Fallita elaborazione dell'elemento");
			return skipPolicy.onErrorSkipRemaining(context, e);
		} catch (Throwable e) {
			context.itemFailed(e);
			LOG.error("Fallita elaborazione dell'elemento");
			throw e;
		}
		context.itemSucceeded();
		commitQueue.add(processedItem);
		return skipPolicy.skipRemaining(context);
	}

	/**
	 * Applica la politica di retry e delega a {@link #process(Serializable)}.
	 * 
	 * @throws Exception
	 */
	private T processAndRetryOnErrors(final T item, final JobContext<T> context)
			throws Exception {
		boolean processed = false;
		boolean retrying = false;
		T processedItem = null;
		do {
			try {
				processedItem = process(item);
				processed = true;
			} catch (RuntimeException e) {
				LOG.error("Errore nell'elaborazione dell'elemento", e);

				if (retryPolicy.retryOnError(context, e)) {
					if (!retrying) { // solo la prima volta
						context.itemRetried(e);
						retrying = true;
					}
					LOG.debug(
							"L'elaborazione dell'elemento verrà ritentata, tentativo {}",
							context.getCurrentTry());
					context.retrying();
					retryPolicy.onRetrying(context);
				} else {
					throw e;
				}
			}
		} while (!processed);
		return processedItem;
	}

	/**
	 * L'operazione di default non fa nulla: ridefinire per persistere gli
	 * elementi tramite un repository transazionale.
	 */
	protected void store(final List<T> commitQueue) {
	}

	/**
	 * Logica di elaborazione di un elemento.
	 * 
	 * @return un elemento (non è garantito che sia lo stesso elemento o la
	 *         stessa istanza dell'elemento elaborato, passato come parametro).
	 * @throws Exception
	 *             per le {@link RuntimeException} l'elaborazione dell'elemento
	 *             può essere ritentata in base alla {@link #getRetryPolicy()},
	 *             mentre per le checked exception l'elemento rimane in errore e
	 *             si procede col successivo in base alla
	 *             {@link #getSkipPolicy()}.
	 */
	protected abstract T process(final T item) throws Exception;

	/**
	 * @see LockPolicy#acquireExecution(JobContext)
	 */
	protected boolean acquireExecution(final JobContext<T> context) {
		return lockPolicy.acquireExecution(context);
	}

	/**
	 * @see LockPolicy#releaseExecution(JobContext)
	 */
	protected void releaseExecution(final JobContext<T> context) {
		lockPolicy.releaseExecution(context);
	}

	/**
	 * @see LockPolicy#acquireItem(JobContext)
	 */
	protected boolean acquireItem(final JobContext<T> context) {
		return lockPolicy.acquireItem(context);
	}

	/**
	 * @see LockPolicy#releaseItem(JobContext)
	 */
	protected void releaseItem(final JobContext<T> context) {
		lockPolicy.releaseItem(context);
	}

	/**
	 * @see RunPolicy#isRunnable(JobContext)
	 */
	protected boolean enabled(final JobContext<T> context) {
		return runPolicy.isRunnable(context);
	}

	/**
	 * Logica di caricamento degli elementi da elaborare.
	 * <p>
	 * Viene invocato più volte fino a quando restituirà zero elementi.
	 */
	protected abstract List<T> load(final JobContext<T> context);

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