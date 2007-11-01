package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {

	private static final long serialVersionUID = -2054179206343306401L;

	public static class ItemId implements Serializable {

		private static final long serialVersionUID = -7440178962501455406L;

		private Integer invoiceId;
		private Integer itemNumber;

		public ItemId() {
		}

		public Integer getInvoiceId() {
			return invoiceId;
		}

		public void setInvoiceId(Integer invoiceId) {
			this.invoiceId = invoiceId;
		}

		public Integer getItemNumber() {
			return itemNumber;
		}

		public void setItemNumber(Integer itemNumber) {
			this.itemNumber = itemNumber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((getInvoiceId() == null) ? 0 : getInvoiceId().hashCode());
			result = prime
					* result
					+ ((getItemNumber() == null) ? 0 : getItemNumber()
							.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ItemId other = (ItemId) obj;
			if (getInvoiceId() == null) {
				if (other.getInvoiceId() != null)
					return false;
			} else if (!getInvoiceId().equals(other.getInvoiceId()))
				return false;
			if (getItemNumber() == null) {
				if (other.getItemNumber() != null)
					return false;
			} else if (!getItemNumber().equals(other.getItemNumber()))
				return false;
			return true;
		}

	}

	private ItemId id;

	private Integer itemNumber;

	private Invoice invoice;
	private Product product;
	private Integer quantity;
	private BigDecimal cost;

	public Item() {
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	protected ItemId getId() {
		return id;
	}

	protected void setId(ItemId id) {
		this.id = id;
	}

	protected Integer getItemNumber() {
		return itemNumber;
	}

	protected void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}
}
