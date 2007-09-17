package org.regola.model;

import org.regola.filter.ModelFilter;
import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.In;
import org.regola.filter.annotation.Like;

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

	@Like(value = "address.street", caseSensitive = true)
	private String addressStreet;

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	@Like("address.city")
	private String addressCity;

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

}
