package org.regola.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.regola.util.EnvironmentUtils;
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
	 * Politica di abilitazione del job: determina se può essere eseguito.
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 */
	public interface RunPolicy<T extends Serializable> {

		/**
		 * Può essere eseguito in questo contesto?
		 * 
		 * @return true se l'esecuzione è permessa.
		 */
		boolean isRunnable(final JobContext<T> context);

	}

	/**
	 * Politica di lock delle risorse per l'esecuzione concorrente su più nodi
	 * di un cluster.
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 */
	public interface LockPolicy<T extends Serializable> {

		/**
		 * Richiede un lock globale a livello di job, prerequisito per l'inizio
		 * dell'elaborazione.
		 * 
		 * @return true se il lock è stato creato
		 */
		boolean acquireExecution(final JobContext<T> context);

		/**
		 * Rilascia il lock acquisito per l'esecuzione del job.
		 */
		void releaseExecution(final JobContext<T> context);

		/**
		 * Richiede un lock per l'elemento corrente, prerequisito per
		 * l'elaborazione dello stesso.
		 * 
		 * @return true se il lock è stato creato
		 */
		boolean acquireItem(final JobContext<T> context);

		/**
		 * Rilascia il lock acquisito per l'elaborazione dell'elemento corrente.
		 */
		void releaseItem(final JobContext<T> context);

	}

	/**
	 * Politica di interruzione dell'elaborazione dell'elemento corrente (prima
	 * che venga processato) e/o degli elementi successivi.
	 * <p>
	 * Nel caso l'elaborazione dell'elemento corrente sia fallita, la logica di
	 * riesecuzione o di abbandono dell'elemento in errore viene controllata
	 * dalla {@link RetryPolicy}.
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 */
	public interface SkipPolicy<T extends Serializable> {

		/**
		 * Deve saltare l'elemento corrente?
		 * <p>
		 * NOTA: Questa policy viene verificata PRIMA dell'elaborazione
		 * dell'elemento.
		 * 
		 * @return true se deve saltare l'elaborazione dell'elemento corrente.
		 */
		boolean skipBeforeProcessing(final JobContext<T> context);

		/**
		 * Deve saltare tutti gli elementi successivi a quello corrente?
		 * <p>
		 * NOTA: Questa policy viene verificata DOPO l'elaborazione di un
		 * elemento, nel caso sia stato elaborato con successo oppure sia stato
		 * ignorato (skipped).
		 * 
		 * @return true se si deve interrompere l'elaborazione degli elementi
		 *         successivi.
		 */
		boolean skipRemaining(JobContext<T> context);

		/**
		 * Deve saltare tutti gli elementi successivi a quello corrente?
		 * <p>
		 * Questa policy viene verificata DOPO l'elaborazione di un elemento,
		 * nel caso si sia verificata un'eccezione.
		 * 
		 * @param e
		 *            eccezione verificatasi durante l'elaborazione
		 *            dell'elemento.
		 * @return true se si deve interrompere l'elaborazione degli elementi
		 *         successivi.
		 */
		boolean onErrorSkipRemaining(JobContext<T> context, Exception e);

	}

	/**
	 * Politica di rielaborazione di un elemento in errore.
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 */
	public interface RetryPolicy<T extends Serializable> {

		/**
		 * Deve ritentare l'elaborazione di un elemento che ha dato
		 * precedentemente errore?
		 * 
		 * @param e
		 *            errore che si è verificato durante l'elaborazione
		 *            dell'elemento corrente.
		 * @return true se deve ritentare
		 */
		boolean retryOnError(final JobContext<T> context,
				final RuntimeException e);

		/**
		 * Invocato subito prima di eseguire un nuovo tentativo di elaborazione.
		 */
		void onRetrying(final JobContext<T> context);

	}

	/**
	 * Politica che indica quando effettuare commit della coda di commit.
	 * <p>
	 * La coda di commit contiene tutti gli elementi elaborati con successo e
	 * non ancora persistiti nello store.
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 * @see Job#store(List)
	 */
	public interface CommitPolicy<T extends Serializable> {

		/**
		 * Deve fare commit degli elementi elaborati con successo e non ancora
		 * persistiti?
		 * 
		 * @return true se è tempo di fare commit.
		 * @see JobConfig#getCommitInterval()
		 * @see JobConfig#DEFAULT_COMMIT_INTERVAL
		 */
		boolean commitQueued(final JobContext<T> context);

	}

	protected final Logger LOG = LoggerFactory.getLogger(getClass());
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
		runPolicy = new DefaultRunPolicy<T>(config.isEnabled(),
				config.getEnvironment(), config.getHostname(),
				config.getExecutionWindow());
		lockPolicy = new NullLockPolicy<T>();
		skipPolicy = new NullSkipPolicy<T>();
		retryPolicy = new DefaultRetryPolicy<T>(config.getMaxTries(),
				config.getRetryDelay());
		commitPolicy = new DefaultCommitPolicy<T>(config.getCommitInterval());
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

			final Set<T> commitQueue = new LinkedHashSet<T>();
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
					if (context.getProcessed() >= maxItems) {
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
						abortProcessing = processAndCommit(item, context,
								commitQueue);
					} finally {
						releaseItem(context);
					}
					if (abortProcessing) {
						context.cancelled();
						LOG.warn("Annullamento: non verranno elaborati altri elementi");
						break NEXT_ITEM;
					}
				}
			}

			flush(commitQueue);
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
				context.isSuccess() ? "riuscito" : "FALLITO!");
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
	 * {@link #processAndRetryOnErrors(Serializable, JobContext)} e a
	 * {@link #commit(Serializable, JobContext, Set)}.
	 * 
	 * @return true se l'elaborazione degli elementi rimanenti deve
	 *         interrompersi.
	 * @throws Throwable 
	 */
	private boolean processAndCommit(T item, JobContext<T> context,
			Set<T> commitQueue) throws Throwable {
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
		commit(processedItem, context, commitQueue);
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
	 * Applica la politica di commit e, se raggiunto il commit interval, delega
	 * a {@link #store(List)}.
	 */
	private void commit(final T processedItem, final JobContext<T> context,
			final Set<T> commitQueue) {

		if (processedItem == null) {
			return;
		}

		commitQueue.add(processedItem);

		if (commitPolicy.commitQueued(context)) {
			flush(commitQueue);
		}
	}

	private void flush(final Set<T> commitQueue) {
		if (commitQueue.isEmpty()) {
			return;
		}
		LOG.debug("Salvataggio di {} elementi elaborati con successo",
				commitQueue.size());
		store(new ArrayList<T>(commitQueue));
		commitQueue.clear();
	}

	/**
	 * L'operazione di default non fa nulla: ridefinire per delegare ad un
	 * repository transazionale.
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

	/**
	 * Policy di esecuzione del job di default.
	 * <p>
	 * Si basa su questi criteri:
	 * <ul>
	 * <li>flag di abilitazione globale del job;
	 * <li>ambiente di esecuzione;
	 * <li>hostname nel quale viene eseguito;
	 * <li>finestra oraria di esecuzione.
	 * </ul>
	 * 
	 * @param <T>
	 *            tipo degli elementi elaborati dal job
	 * @see JobConfig#isEnabled()
	 * @see JobConfig#getExecutionWindow()
	 * @see JobConfig#getHostname()
	 * @see JobConfig#getEnvironment()
	 * @see JobContext#getHostname()
	 * @see JobContext#getEnvironment()
	 */
	public static class DefaultRunPolicy<T extends Serializable> implements
			RunPolicy<T> {
		private static final Logger LOG = LoggerFactory
				.getLogger(DefaultRunPolicy.class);

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

		/**
		 * Può essere eseguito in questo momento ({@link #now()}) e in questo
		 * contesto?
		 */
		public boolean isRunnable(final JobContext<T> context) {
			if (!enabled) {
				return false;
			}
			if (hostname == null) {
				return false;
			}
			if (context.getHostname() == null
					|| !context.getHostname().toLowerCase().contains(hostname)) {
				LOG.debug("Disabilitato per hostname: host={}, richiesto={}",
						context.getHostname(), hostname);
				return false;
			}
			if (environment == null) {
				return false;
			}
			if (!environment.equalsIgnoreCase(context.getEnvironment())) {
				LOG.debug("Disabilitato per environment: env={}, richiesto={}",
						context.getEnvironment(), environment);
				return false;
			}
			return executionWindow.isSatifiedBy(now());
		}

		protected Date now() {
			return new Date();
		}

	}

	/**
	 * La politica di lock di default non effettua nessun lock delle risorse.
	 */
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

	/**
	 * La politica di skip di default non ignora nessun elemento.
	 */
	protected static class NullSkipPolicy<T extends Serializable> implements
			SkipPolicy<T> {

		public boolean skipBeforeProcessing(final JobContext<T> context) {
			return false;
		}

		public boolean skipRemaining(JobContext<T> context) {
			return false;
		}

		public boolean onErrorSkipRemaining(JobContext<T> context, Exception e) {
			return false;
		}

	}

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
	public static class DefaultRetryPolicy<T extends Serializable> implements
			RetryPolicy<T> {
		private static final Logger LOG = LoggerFactory
				.getLogger(DefaultRetryPolicy.class);

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

	/**
	 * La politica di commit di default si basa sul commit interval definito
	 * nella {@link JobConfig}.
	 * 
	 * @see JobConfig#DEFAULT_COMMIT_INTERVAL
	 * @see JobConfig#getCommitInterval()
	 */
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

		public boolean commitQueued(final JobContext<T> context) {
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