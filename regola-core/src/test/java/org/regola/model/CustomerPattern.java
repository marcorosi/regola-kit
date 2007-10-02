package org.regola.model;

import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.filter.annotation.In;
import org.regola.filter.annotation.LessThan;
import org.regola.filter.annotation.Like;
import org.regola.filter.annotation.NotEquals;

public class CustomerPattern extends ModelPattern {

	@GreaterThan
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@LessThan("id")
	private Integer lessThanId;

	public Integer getLessThanId() {
		return lessThanId;
	}

	public void setLessThanId(Integer id) {
		this.lessThanId = id;
	}
	
	@Equals
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotEquals("firstName")
	private String notEqualsFirstName;

	public String getNotEqualsFirstName() {
		return notEqualsFirstName;
	}

	public void setNotEqualsFirstName(String firstName) {
		this.notEqualsFirstName = firstName;
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