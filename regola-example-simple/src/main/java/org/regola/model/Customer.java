package org.regola.model;

public class Customer {

	private Integer id;
	private String firstName;
	private String lastName;
	private Address address;

	public static class Address {

		private String street;
		private String city;

		public Address() {
		}

		public Address(String street, String city) {
			this.street = street;
			this.city = city;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(street);
			sb.append(" (");
			sb.append(city);
			sb.append(")");
			return sb.toString();
		}
	}

	public Customer() {
	}

	public Customer(int id, String firstName, String lastName, Address address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(id);
		sb.append("] ");
		sb.append(lastName);
		sb.append(" ");
		sb.append(firstName);
		sb.append(", ");
		sb.append(address.toString());
		return sb.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
