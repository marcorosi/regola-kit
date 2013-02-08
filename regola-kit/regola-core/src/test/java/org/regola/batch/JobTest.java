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

	private Job<Serializable> job;
	private boolean execute;
	protected StringBuilder lifeCycle;
	protected boolean acquireExecution;
	protected boolean acquireItem;
	protected int loadIterations;

	@Before
	public void setUp() throws Exception {
		lifeCycle = new StringBuilder();
		execute = true;
		acquireExecution = true;
		acquireItem = true;
		loadIterations = 2;
		job = new Job<Serializable>() {
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
				lifeCycle.append(" load");
				loadIterations--;
				if (loadIterations > 0) {
					return asList((Serializable) null);
				}
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
				return null;
			}

			@Override
			protected void store(List<Serializable> commitQueue) {
				lifeCycle.append(" store");
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
	public void jobLifeCycleWith2Items() {
		loadIterations = 3;
		assertLifeCycle(" enabled acquireExecution onStart load acquireItem process releaseItem load acquireItem process releaseItem load onFinish releaseExecution");
	}

	@Test
	public void jobLifeCycleWith1Items() {
		loadIterations = 2;
		assertLifeCycle(" enabled acquireExecution onStart load acquireItem process releaseItem load onFinish releaseExecution");
	}

	@Test
	public void jobLifeCycleWith0Items() {
		loadIterations = 1;
		assertLifeCycle(" enabled acquireExecution onStart load onFinish releaseExecution");
	}

	protected void assertLifeCycle(String stringSequence) {
		job.execute();
		assertThat(lifeCycle.toString(), is(stringSequence));
	}
}
