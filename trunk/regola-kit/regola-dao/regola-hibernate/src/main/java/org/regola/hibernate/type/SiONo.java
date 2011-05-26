package org.regola.hibernate.type;

import org.hibernate.type.CharBooleanType;

@SuppressWarnings("deprecation")
public class SiONo extends CharBooleanType {

	private static final long serialVersionUID = 5095948002689497550L;

	public SiONo() {
		super('S', 'N');
	}

}
