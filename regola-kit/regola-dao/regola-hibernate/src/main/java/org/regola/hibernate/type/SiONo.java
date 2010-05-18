package org.regola.hibernate.type;

import org.hibernate.type.CharBooleanType;

public class SiONo extends CharBooleanType {

	@Override
	protected String getFalseString() {
		return "N";
	}

	@Override
	protected String getTrueString() {
		return "S";
	}

}
