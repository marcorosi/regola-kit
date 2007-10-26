package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {

	private static final long serialVersionUID = 4383798182621390586L;

	public static class InvoiceId implements Serializable {

		private static final long serialVersionUID = -1230612359610052825L;

		public Integer id;

		public InvoiceId() {
		}

		public InvoiceId(String id) {
			this.id = Integer.valueOf(id);
		}

		public InvoiceId(Integer id) {
			this.id = id;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final InvoiceId other = (InvoiceId) obj;
			if (this.id != other.id
					&& (this.id == null || !this.id.equals(other.id))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
			return hash;
		}

		public String toString() {
			return "" + this.id;
		}
	}

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
}