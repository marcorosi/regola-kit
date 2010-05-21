package org.regola.webapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.regola.model.ModelProperty;
import org.regola.util.Ognl;

public class DynamicReadList {

	private static final long serialVersionUID = 1L;
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public static String getCellValue(ModelProperty property, Object root) {
		String column = property.getName();
		Object value = null;
		
		if (root != null) {
			value = Ognl.getValue(column, root);
		}

		if (value != null)
		{
			if(value instanceof Date)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				return sdf.format(value);
			}
			else
				return value.toString();
		}

		return "-";
	}

}
