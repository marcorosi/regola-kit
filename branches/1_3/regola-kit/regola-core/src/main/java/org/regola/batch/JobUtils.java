package org.regola.batch;

import java.util.Random;

public final class JobUtils {

	private JobUtils() {
	}

	public static String newExecutionId() {
		return String.valueOf(Math.abs(new Random().nextLong()));
	}
}