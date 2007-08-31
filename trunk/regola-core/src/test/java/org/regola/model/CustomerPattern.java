package org.regola.model;

import org.regola.filter.ModelFilter;
import org.regola.filter.annotation.Equals;

public class CustomerPattern extends ModelFilter {

	@Equals
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
