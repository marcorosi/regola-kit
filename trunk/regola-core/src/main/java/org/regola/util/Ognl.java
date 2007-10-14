package org.regola.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ognl.OgnlException;

public class Ognl {
	protected static final Log log = LogFactory.getLog(Ognl.class);

	static public Object getValue(String exp, Object root) {
		try {
			return ognl.Ognl.getValue(exp, root);
		} catch (OgnlException oe) {
			/**
			 * Per evitare di fermarci con sotto-oggetti nulli di un grafo, ad
			 * esempio per gli errori -- source is null for getProperty(null,
			 * "id") -- non solleviamo l'eccezione ma la riportiamo solo nel log
			 */
			log.error(oe);

		}

		// valore restituito a seguito di un eccezione
		return "-";
	}

	static public void setValue(String exp, Object root, Object value) {
		try {
			ognl.Ognl.setValue(exp, root, value);
		} catch (OgnlException oe) {
			throw new RuntimeException(oe);
		}
	}
}
