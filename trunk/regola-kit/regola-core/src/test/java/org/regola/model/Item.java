package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;

class Item implements Serializable {

	private static final long serialVersionUID = -2054179206343306401L;

	public static class ItemId implements Serializable {

		private static final long serialVersionUID = 1889718673921098048L;
		
		public Invoice.InvoiceId invoice;
		public Integer itemNumber;

		public ItemId() {
		}

		public ItemId(String stringRapresentation) {
			String[] invoiceIdAndItemNumber = stringRapresentation.split(",");
			if (invoiceIdAndItemNumber.length == 2) {
				try {
					this.invoice = new Invoice.InvoiceId(
							invoiceIdAndItemNumber[0]);
					this.itemNumber = Integer
							.parseInt(invoiceIdAndItemNumber[1]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"You must provide a string in the format <invoiceId,itemNumber>");
				}
			}
			throw new IllegalArgumentException(
					"You must provide a string in the format <invoiceId,itemNumber>");
		}

		public ItemId(Invoice.InvoiceId invoice, Integer itemNumber) {
			this.invoice = invoice;
			this.itemNumber = itemNumber;
		}

		public Invoice.InvoiceId getInvoice() {
			return invoice;
		}

		public void setInvoice(Invoice.InvoiceId invoice) {
			this.invoice = invoice;
		}
		
		public Integer getInvoiceId() {
			return getInvoice().getId();
		}
		
		public void setInvoiceId(Integer invoiceId) {
			getInvoice().setId(invoiceId);
		}

		public Integer getItemNumber() {
			return itemNumber;
		}

		public void setItemNumber(Integer itemNumber) {
			this.itemNumber = itemNumber;
		}

		public String toString() {
			return getInvoice() + "," + getItemNumber();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ItemId other = (ItemId) obj;
			if (this.invoice != other.invoice
					&& (this.invoice == null || !this.invoice
							.equals(other.invoice))) {
				return false;
			}
			if (this.itemNumber != other.itemNumber
					&& (this.itemNumber == null || !this.itemNumber
							.equals(other.itemNumber))) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 53 * hash
					+ (this.invoice != null ? this.invoice.hashCode() : 0);
			hash = 53
					* hash
					+ (this.itemNumber != null ? this.itemNumber.hashCode() : 0);
			return hash;
		}

	}

	private ItemId id;
	private Integer itemNumber;
	
	private Invoice invoice;
	private Product product;
	private Integer quantity;
	private BigDecimal cost;

	protected Item() {
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
