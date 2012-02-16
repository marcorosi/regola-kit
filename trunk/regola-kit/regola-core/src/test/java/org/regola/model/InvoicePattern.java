package org.regola.model;

import org.regola.filter.annotation.Equals;

public class InvoicePattern extends ModelPattern {

	private static final long serialVersionUID = 1L;

	public InvoicePattern() {
		super(true);
	}

	@Equals("customer.address.city")
	private String customerAddressCity;

	public String getCustomerAddressCity() {
		return customerAddressCity;
	}

	public void setCustomerAddressCity(String customerAddressCity) {
		this.customerAddressCity = customerAddressCity;
	}
}
