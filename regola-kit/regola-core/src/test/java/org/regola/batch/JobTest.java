package org.regola.batch;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.*;

import java.io.Serializable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JobTest {

	private static final Serializable DUMMY = Integer.MAX_VALUE;
	private Job<Serializable> job;
	private boolean execute;
	private StringBuilder lifeCycle;
	private boolean acquireExecution;
	private boolean acquireItem;
	private Serializable[] items = {};
	private Serializable[] returnOrThrow = {};
	private JobConfig config;
	private JobContext<Serializable> context;
	private JobResult result;

	@Before
	public void setUp() throws Exception {
		config = JobConfig.defaultConfig();
		config.setPageSize(2);
		config.setCommitInterval(3);
		config.setRetryDelay(0);
		lifeCycle = new StringBuilder();
		execute = true;
		acquireExecution = true;
		acquireItem = true;
		items = new Serializable[] {};
		job = new Job<Serializable>(config) {
			@Override
			protected boolean enabled(JobContext<Serializable> context) {
				lifeCycle.append(" enabled");
				return execute;
			}

			@Override
			protected void onStart(JobContext<Serializable> context) {
				lifeCycle.append(" onStart");
			}

			@Override
			protected boolean acquireExecution(JobContext<Serializable> context) {
				lifeCycle.append(" acquireExecution");
				return acquireExecution;
			}

			@Override
			protected List<Serializable> load(JobContext<Serializable> context) {
				lifeCycle.append(" load ");
				int idx = context.getLoadIteration() - 1;
				final int start = idx * config.getPageSize();
				if (start < items.length && idx >= 0) {
					int size = Math.min(config.getPageSize(), items.length
							- start);
					lifeCycle.append(size);
					Serializable[] loaded = new Serializable[size];
					System.arraycopy(items, start, loaded, 0, size);
					return asList(loaded);
				}
				lifeCycle.append("0");
				return emptyList();
			}

			@Override
			protected boolean acquireItem(JobContext<Serializable> context) {
				lifeCycle.append(" acquireItem");
				return acquireItem;
			}

			@Override
			protected Serializable process(Serializable item) throws Exception {
				lifeCycle.append(" process");
				final int idx = context.getProcessed() - 1;
				if (idx < returnOrThrow.length && idx >= 0) {
					if (returnOrThrow[idx] instanceof Exception) {
						throw (Exception) returnOrThrow[idx];
					}
					if (returnOrThrow[idx] instanceof Error) {
						throw (Error) returnOrThrow[idx];
					}
					return returnOrThrow[idx];
				}
				return null;
			}

			@Override
			protected void store(List<Serializable> commitQueue) {
				lifeCycle.append(" store ").append(commitQueue.size());
			}

			@Override
			protected void releaseItem(JobContext<Serializable> context) {
				lifeCycle.append(" releaseItem");
			}

			@Override
			protected void releaseExecution(JobContext<Serializable> context) {
				lifeCycle.append(" releaseExecution");
			}

			@Override
			protected void onFinish(JobContext<Serializable> context) {
				lifeCycle.append(" onFinish");
			}
		};
		context = new JobContext<Serializable>(job.name());
	}

	@Test
	public void aSimpleJobIsConfigured() {
		assertThat(job.getRunPolicy(), notNullValue());
		assertThat(job.getLockPolicy(), notNullValue());
		assertThat(job.getSkipPolicy(), notNullValue());
		assertThat(job.getRetryPolicy(), notNullValue());
		assertThat(job.getCommitPolicy(), notNullValue());
	}

	@Test
	public void aDisabledJobCannotRun() {
		execute = false;
		assertFalse(job.execute().isExecuted());
	}

	@Test
	public void useClassAsDefaultJobName() {
		assertThat(job.name(), containsString(JobTest.class.getSimpleName()));
	}

	@Test
	public void processing0Items() {
		items = new Serializable[] {};
		expectRun(" enabled acquireExecution onStart load 0 onFinish releaseExecution");
		expectResult(0, 0, 0, 0, 0);
	}

	@Test
	public void processing1Item_nullNotStored() {
		items = new Serializable[] { null };
		expectRun(" enabled acquireExecution onStart load 1 acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(1, 0, 0, 0, 1);
	}
	@Test
	public void processing1Item() {
		items = new Serializable[] { null };
		returnOrThrow = new Serializable[] { DUMMY };
		expectRun(" enabled acquireExecution onStart load 1 acquireItem process releaseItem load 0 store 1 onFinish releaseExecution");
		expectResult(1, 0, 0, 0, 1);
	}

	@Test
	public void processing2Items_nullsNotStored() {
		items = new Serializable[] { null, null };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(2, 0, 0, 0, 2);
	}
	
	@Test
	public void processing2Items() {
		items = new Serializable[] { null, null };
		returnOrThrow = new Serializable[] { DUMMY, DUMMY };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 0 store 2 onFinish releaseExecution");
		expectResult(2, 0, 0, 0, 2);
	}

	@Test
	public void processing3Items_nullsNotStored() {
		items = new Serializable[] { null, null, null };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 1 acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(3, 0, 0, 0, 3);
	}
	
	@Test
	public void processing3Items() {
		items = new Serializable[] { null, null, null };
		returnOrThrow = new Serializable[] { DUMMY, DUMMY, DUMMY };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 1 acquireItem process releaseItem store 3 load 0 onFinish releaseExecution");
		expectResult(3, 0, 0, 0, 3);
	}

	@Test
	public void processing4Items_nullsNotStored() {
		items = new Serializable[] { null, null, null, null };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 2 acquireItem process releaseItem acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(4, 0, 0, 0, 4);
	}
	
	@Test
	public void processing4Items() {
		items = new Serializable[] { null, null, null, null };
		returnOrThrow = new Serializable[] { DUMMY, DUMMY, DUMMY, DUMMY };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 2 acquireItem process releaseItem store 3 acquireItem process releaseItem load 0 store 1 onFinish releaseExecution");
		expectResult(4, 0, 0, 0, 4);
	}
	
	@Test
	public void processing2Items_withRuntimeException() {
		items = new Serializable[] { null, null };
		returnOrThrow = new Serializable[] { new RuntimeException("runtime") };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(2, 0, 1, 0, 1);
	}

	@Test
	public void processing2Items_withCheckedException() {
		items = new Serializable[] { null, null };
		returnOrThrow = new Serializable[] { new Exception("checked") };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(2, 0, 1, 0, 1);
	}

	@Test
	public void processing2Items_withRetryableFailureException() {
		items = new Serializable[] { null, null };
		returnOrThrow = new Serializable[] { new RetryableFailureException(
				"retryable") };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process process process releaseItem acquireItem process releaseItem load 0 onFinish releaseExecution");
		expectResult(2, 0, 1, 1, 1);
	}

	@Test
	public void processing2Items_withError() {
		items = new Serializable[] { null, null };
		returnOrThrow = new Serializable[] { new Error("error") };
		expectRun(" enabled acquireExecution onStart load 2 acquireItem process releaseItem onFinish releaseExecution");
		expectResult(1, 0, 1, 0, 0);
	}

	protected void expectRun(String stringSequence) {
		result = job.execute(context);
		assertThat(lifeCycle.toString(), is(stringSequence));
	}

	private void expectResult(int processed, int skipped, int failed,
			int retried, int succeeded) {
		assertEquals(processed, result.getProcessed());
		assertEquals(skipped, result.getSkipped());
		assertEquals(retried, result.getRetried());
		assertEquals(failed, result.getFailed());
		assertEquals(succeeded, result.getSucceeded());
	}

}
