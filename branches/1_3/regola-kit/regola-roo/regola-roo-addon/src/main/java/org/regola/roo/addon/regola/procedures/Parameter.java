package org.regola.roo.addon.regola.procedures;

import java.lang.reflect.Array;
import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Date;

import org.springframework.roo.model.JavaType;

public class Parameter {
	private String parameterName;
	private int parameterType;
	private int sqlType;
	private String typeName;
	private boolean nullable;

	/**
	 * Constructor taking all the properties
	 */
	public Parameter(String columnName, int columnType, int sqlType, String typeName, boolean nullable) {
				
		this.parameterName = columnName;
		this.parameterType = columnType;
		this.sqlType = sqlType;
		this.typeName = typeName;
		this.nullable = nullable;
	}


	/**
	 * Get the parameter name.
	 */
	public String getParameterName() {
		return parameterName;
	}
	
	static public String toCamel(String label) {
		StringBuilder sb = new StringBuilder();

        String[] tokens = label.toLowerCase().split("[-,_]");
        
        boolean first = true;
        for (String i : tokens) {
        	if (first) {
        		sb.append(i);
        		first = false;
        	}
        	else sb.append(StringUtils.capitalize(i));
        }

        return sb.toString();
		
	}
	
	
	public String getParameterCamelName() {
		   return  toCamel(parameterName);
 	}

	/**
	 * Get the parameter type.
	 */
	public int getParameterType() {
		return parameterType;
	}

	/**
	 * Get the parameter SQL type.
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * Get the parameter type name.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Get whether the parameter is nullable.
	 */
	public boolean isNullable() {
		return nullable;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public JavaType getJavaType() {
		
		switch (sqlType) {
			
			case Types.BOOLEAN  : return  JavaType.BOOLEAN_OBJECT;
			
			case Types.CHAR:
			case Types.NCHAR : return  JavaType.STRING;
			
			case Types.DATE :
			case Types.TIME :
			case Types.TIMESTAMP : return new  JavaType("java.util.Date");
			
			case Types.DECIMAL:
			case Types.DOUBLE: 
			case Types.FLOAT : 
			case Types.NUMERIC : return JavaType.LONG_OBJECT;
			
			case Types.BIGINT: 
			case Types.INTEGER : return JavaType.INT_OBJECT;
			
			case Types.CLOB:
			case Types.NCLOB : 
			case Types.NVARCHAR :
			case Types.VARCHAR : 
			case Types.LONGNVARCHAR : return JavaType.STRING;
			
			default : return JavaType.OBJECT;
			
		}
	}

}
