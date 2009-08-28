package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {

	private static final long serialVersionUID = -2054179206343306401L;

	private ItemId id;

	//private Integer itemNumber;

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

	public ItemId getId() {
		return id;
	}

	public void setId(ItemId id) {
		this.id = id;
	}
	
	/*
	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}
	*/
}