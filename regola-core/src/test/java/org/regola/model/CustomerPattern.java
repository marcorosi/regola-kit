package org.regola.model;

import org.regola.filter.ModelFilter;
import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.In;

public class CustomerPattern extends ModelFilter {

	@Equals
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@In("lastName")
	private String[] lastNames;

	public String[] getLastNames() {
		return lastNames;
	}

	public void setLastNames(String[] lastNames) {
		this.lastNames = lastNames;
	}
}
