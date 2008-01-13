package org.regola.model.pattern;

import java.io.Serializable;

import org.regola.filter.annotation.Equals;
import org.regola.model.ModelPattern;

public class ItemPattern extends ModelPattern implements Serializable
{
   private static final long serialVersionUID = 1L;

   public ItemPattern()
   {
	  addProperty("id.invoiceId","item.column.");
	  addProperty("id.itemNumber","item.column.");
	  addProperty("cost","item.column.");
	  //addProperty("invoice","item.column.");
	  addProperty("product.name","item.column.");
	  addProperty("quantity","item.column.");
	  
	  //getSortedProperties().add(new ModelProperty("id.invoiceId","item.column.",Order.asc));
	  
	  setPageSize(10);
	}

    protected	java.lang.Integer invoiceId;
    protected	java.lang.Integer itemNumber;
    protected	java.math.BigDecimal cost;
    protected	org.regola.model.Invoice invoice;
    protected	org.regola.model.Product product;
    protected	java.lang.Integer quantity;
	
	@Equals("id.invoiceId")
	public java.lang.Integer getInvoiceId()
	{
		return invoiceId;
	}

	public void setInvoiceId(java.lang.Integer invoiceId)
	{
		this.invoiceId = invoiceId;
	}
	
	@Equals("id.itemNumber")
	public java.lang.Integer getItemNumber()
	{
		return itemNumber;
	}

	public void setItemNumber(java.lang.Integer itemNumber)
	{
		this.itemNumber = itemNumber;
	}
  
	@Equals("cost")
	public java.math.BigDecimal getCost()
	{
		return cost;
	}

	public void setCost(java.math.BigDecimal cost)
	{
		this.cost = cost;
	}
  
	@Equals("invoice")
	public org.regola.model.Invoice getInvoice()
	{
		return invoice;
	}

	public void setInvoice(org.regola.model.Invoice invoice)
	{
		this.invoice = invoice;
	}
  
	@Equals("product")
	public org.regola.model.Product getProduct()
	{
		return product;
	}

	public void setProduct(org.regola.model.Product product)
	{
		this.product = product;
	}
  
	@Equals("quantity")
	public java.lang.Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(java.lang.Integer quantity)
	{
		this.quantity = quantity;
	}
	
}