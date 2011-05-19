package org.regola.webapp.flow;

import static org.regola.webapp.util.ResourcesUtil.getBundle;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.regola.util.ELFunction;

public class Messages extends ELFunction  {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public String get(Object key) {
		String message;

		try {
			message = getBundle().getString(key.toString());

		} catch (java.util.MissingResourceException mre) {
			log.warn("Missing key for '" + key + "'");
			return "???" + key + "???";
		}

		return message;

	}
	
}
