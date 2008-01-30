package org.regola.model.pattern;

import java.io.Serializable;

import org.regola.filter.annotation.Equals;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;

public class InvoicePattern extends ModelPattern implements Serializable
{
   private static final long serialVersionUID = 1L;

   public InvoicePattern()
   {
	  addProperty("id","invoice.column.");
	  //addProperty("customer","invoice.column.");
	  //addProperty("items","invoice.column.");
	  addProperty("total","invoice.column.");
	  
	  getSortedProperties().add(new ModelProperty("id","invoice.column.",Order.asc));
	}

    protected	java.lang.Integer id;
    //protected	org.regola.model.Customer customer;
    //protected	java.util.List items;
    protected	java.math.BigDecimal total;
 
    @Equals("id")
	public java.lang.Integer getId()
	{
	  return id;
	}
		
	public void setId(java.lang.Integer id)
	{
	   this.id = id;
	}
  
	/*
	 * 
	 * 
	@Equals("customer")
	public org.regola.model.Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(org.regola.model.Customer customer)
	{
		this.customer = customer;
	}
  
	@Equals("items")
	public java.util.List getItems()
	{
		return items;
	}

	public void setItems(java.util.List items)
	{
		this.items = items;
	}
	*
  	*/
	
	@Equals("total")
	public java.math.BigDecimal getTotal()
	{
		return total;
	}

	public void setTotal(java.math.BigDecimal total)
	{
		this.total = total;
	}
}