package org.regola.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class EnvironmentUtils {

	private static final Log LOG = LogFactory.getLog(EnvironmentUtils.class);
	private static final String REGOLA_ENV_VAR = "regola.env";

	public static final String PROD_ENV = "prod";
	public static final String TEST_ENV = "test";
	public static final String DEV_ENV = "dev";

	private EnvironmentUtils() {
	}

	public static boolean isProduction() {
		return PROD_ENV.equals(System.getProperty(REGOLA_ENV_VAR));
	}

	public static boolean isTest() {
		return TEST_ENV.equals(System.getProperty(REGOLA_ENV_VAR));
	}

	public static boolean isDevelopment() {
		return DEV_ENV.equals(System.getProperty(REGOLA_ENV_VAR));
	}

	public static String current() {
		if (isProduction()) {
			return PROD_ENV;
		}
		if (isTest()) {
			return TEST_ENV;
		}
		if (isDevelopment()) {
			return DEV_ENV;
		}
		return null;
	}

	public static String hostname() {
		try {
			return InetAddress.getLocalHost().getHostName().toLowerCase();
		} catch (UnknownHostException e) {
			LOG.error("Impossibile ricavare il nome dell'host: "
					+ e.getMessage());
		}
		return "[unknown host]";
	}
}