package org.regola.webapp.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.regola.model.ModelProperty;
import org.regola.util.Ognl;

public class DynamicReadList {

	private static final long serialVersionUID = 1L;

	public static String getCellValue(ModelProperty property, Object root) {
		String column = property.getName();
		Object value = null;
		
		if (root != null) {
			value = Ognl.getValue(column, root);
		}

		if (value != null)
			return value.toString();

		return "-";
	}

}
