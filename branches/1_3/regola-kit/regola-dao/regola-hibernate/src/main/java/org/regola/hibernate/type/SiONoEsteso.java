package org.regola.hibernate.type;

import java.io.Serializable;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.PrimitiveType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.java.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

/**
 * tipo per mappare una colonna varchar contenente "SI"/"NO" in boolean
 * 
 * NOTA: con hibernate 3.6.9 non funziona usare @see SiONo 
 * 
 * @author marco
 *
 */
@Deprecated
public class SiONoEsteso extends AbstractSingleColumnStandardBasicType<Boolean>
		implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> 
{
	private static final long serialVersionUID = 3516822713158383145L;

	public static final SiONoEsteso INSTANCE = new SiONoEsteso();

	public SiONoEsteso() {
		super(VarcharTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE);
	}

	public String getName() {
		return "si_no_esteso";
	}

	@SuppressWarnings("rawtypes")
	public Class getPrimitiveClass() {
		return boolean.class;
	}

	public Boolean stringToObject(String xml) throws Exception {
		return fromString(xml);
	}

	public Serializable getDefaultValue() {
		return Boolean.FALSE;
	}

	public String objectToSQLString(Boolean value, Dialect dialect)
			throws Exception {
		return StringType.INSTANCE.objectToSQLString(
				value.booleanValue() ? "SI" : "NO", dialect);
	}

}
