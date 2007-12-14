package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {

	private static final long serialVersionUID = 4383798182621390586L;

	private Integer id;
	private Customer customer;
	private BigDecimal total;
	private List<Item> items = new ArrayList<Item>();

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	protected void setItems(List<Item> items) {
		this.items = items;
	}

	public BigDecimal getTotal() {
		return total;
	}

	protected void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * This method calculates the invoice total and updates it. It should be
	 * used after any update to the items collection.
	 */
	public void updateTotal() {
		BigDecimal sum = new BigDecimal(0);
		for (Item item : getItems()) {
			sum = sum.add(item.getCost().multiply(
					new BigDecimal(item.getQuantity())));
		}
		setTotal(sum);
	}
	
	public String getEncodedId()
	{
		return ""+id;
	}
	
	public void setEncodedId(String idStr)
	{
		id = Integer.valueOf(idStr);
	}
}