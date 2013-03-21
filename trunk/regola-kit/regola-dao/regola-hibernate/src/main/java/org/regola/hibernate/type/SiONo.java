package org.regola.hibernate.type;

import java.io.Serializable;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.PrimitiveType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.java.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.CharTypeDescriptor;

/**
 * tipo per mappare una colonna char contenente "S"/"N" in boolean
 * 
 * @author marco
 *
 */
public class SiONo extends AbstractSingleColumnStandardBasicType<Boolean>
implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> {
	
	private static final long serialVersionUID = -3472703870522040453L;
	
	public static final SiONo INSTANCE = new SiONo();

	public SiONo() {
		super( CharTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE );
	}

	public String getName() {
		return "si_no";
	}

	@SuppressWarnings("rawtypes")
	public Class getPrimitiveClass() {
		return boolean.class;
	}

	public Boolean stringToObject(String xml) throws Exception {
		return fromString( xml );
	}

	public Serializable getDefaultValue() {
		return Boolean.FALSE;
	}

	public String objectToSQLString(Boolean value, Dialect dialect) throws Exception {
		return StringType.INSTANCE.objectToSQLString( value.booleanValue() ? "S" : "N", dialect );
	}

}
