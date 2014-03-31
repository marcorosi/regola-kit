package org.regola.hibernate.type;

import org.hibernate.type.BooleanType;
import org.hibernate.type.descriptor.java.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.CharTypeDescriptor;

/**
 * tipo per mappare una colonna char contenente "S"/"N" in boolean
 * 
 * @author marco
 *
 */
public class SiONo extends BooleanType {
	
	private static final long serialVersionUID = -8166749321573908374L;

	public SiONo() {
		super(CharTypeDescriptor.INSTANCE
				, new BooleanTypeDescriptor('S','N'));
	}

}
