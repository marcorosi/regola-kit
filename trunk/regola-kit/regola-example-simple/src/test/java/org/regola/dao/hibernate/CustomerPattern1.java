package org.regola.dao.hibernate;

import org.regola.filter.annotation.IsNull;
import org.regola.model.pattern.CustomerPattern;

public class CustomerPattern1 extends CustomerPattern {
	
	@IsNull("address.city")
	protected Boolean cityNull;

	public Boolean getCityNull() {
		return cityNull;
	}

	public void setCityNull(Boolean cityNull) {
		this.cityNull = cityNull;
	}	
	
}
