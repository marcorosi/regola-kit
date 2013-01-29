package org.regola.batch;

import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RunExpressionTest {

	@SuppressWarnings("deprecation")
	private static final Date TO_PERIOD = new Date(112, 10, 1, 10, 30);
	@SuppressWarnings("deprecation")
	private static final Date FROM_PERIOD = new Date(112, 9, 31, 10, 0);
	@SuppressWarnings("deprecation")
	private static final Date DATE_1900_01_01_03_59 = new Date(0, 0, 1, 3, 59);
	@SuppressWarnings("deprecation")
	private static final Date DATE_2012_11_01_10_29 = new Date(112, 10, 1, 10,
			29);
	@SuppressWarnings("deprecation")
	private static final Date DATE_2012_11_01_09_29 = new Date(112, 10, 1, 9,
			29);
	@SuppressWarnings("deprecation")
	private static final Date DATE_2013_11_01_10_28 = new Date(113, 10, 1, 10,
			28);
	@SuppressWarnings("deprecation")
	private static final Date DATE_2013_11_01_09_28 = new Date(113, 10, 1, 9,
			28);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetNumbers_single() {
		assertSameSet(RunExpression.getNumbers("17", 0, 23), 17);

	}

	@Test
	public void testGetNumbers_list() {
		assertSameSet(RunExpression.getNumbers("10,12,23", 0, 23), 10, 12, 23);
	}

	@Test
	public void testGetNumbers_oneRange() {
		assertSameSet(RunExpression.getNumbers("10-17", 0, 23), 10, 11, 12, 13,
				14, 15, 16, 17);
	}

	@Test
	public void testGetNumbers_twoRanges() {
		assertSameSet(RunExpression.getNumbers("10-12, 20-22", 0, 23), 10, 11,
				12, 20, 21, 22);
	}

	@Test
	public void testGetNumbers_oneRangeWithStep() {
		assertSameSet(RunExpression.getNumbers("10-17/2", 0, 23), 10, 12, 14,
				16);
	}

	@Test
	public void testGetNumbers_twoRangesWithStep() {
		assertSameSet(RunExpression.getNumbers("10-12/1, 20-22/2", 0, 23), 10,
				11, 12, 20, 22);
	}

	@Test
	public void testWildCard() throws Exception {
		// anytime
		RunExpression expr = new RunExpression("* *", null, null);
		assertTrue(expr.isSatifiedBy(DATE_1900_01_01_03_59));
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_10_29));
		assertTrue(expr.isSatifiedBy(DATE_2013_11_01_10_28));

		// anyday/hour/minute in date range
		expr = new RunExpression("* *", FROM_PERIOD, TO_PERIOD);
		assertFalse(expr.isSatifiedBy(DATE_1900_01_01_03_59));
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_10_29));
		assertFalse(expr.isSatifiedBy(DATE_2013_11_01_10_28));

		// anyday/minute at 10
		expr = new RunExpression("10 *", null, null);
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_10_29));
		assertFalse(expr.isSatifiedBy(DATE_2012_11_01_09_29));
		assertTrue(expr.isSatifiedBy(DATE_2013_11_01_10_28));
		assertFalse(expr.isSatifiedBy(DATE_2013_11_01_09_28));

		// anyday/hour at 29th minute
		expr = new RunExpression("* 29", null, null);
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_10_29));
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_09_29));
		assertFalse(expr.isSatifiedBy(DATE_2013_11_01_10_28));
		assertFalse(expr.isSatifiedBy(DATE_2013_11_01_09_28));

	}

	@Test
	public void testPreciseMatch() throws Exception {

		RunExpression expr = new RunExpression("9 29 ", FROM_PERIOD, TO_PERIOD);
		// 1 minuto prima
		assertFalse(expr.isSatifiedBy(new Date(
				DATE_2012_11_01_09_29.getTime() - 60000L)));
		// alla scadenza esatta
		assertTrue(expr.isSatifiedBy(DATE_2012_11_01_09_29));
		// 30 secondi dopo
		assertTrue(expr.isSatifiedBy(new Date(
				DATE_2012_11_01_09_29.getTime() + 30000L)));
		// 1 minuto dopo
		assertFalse(expr.isSatifiedBy(new Date(
				DATE_2012_11_01_09_29.getTime() + 60000L)));

	}
	
	private void assertSameSet(Set<Integer> actual, Integer... expected) {
		assertThat(actual, is((Set<Integer>) new HashSet<Integer>(
				asList(expected))));
	}

}
