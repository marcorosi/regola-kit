package org.regola.model.pattern;

import java.io.Serializable;
import java.math.BigDecimal;

import org.regola.filter.annotation.Equals;
import org.regola.filter.annotation.GreaterThan;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;

public class CustomerPattern extends ModelPattern implements Serializable
{
   private static final long serialVersionUID = 1L;

   public CustomerPattern()
   {
	  addProperty("id","customer.column.");
	  addProperty("firstName","customer.column.");
	  addProperty("lastName","customer.column.");
	  //addProperty("age","customer.column.");
	  
	  getSortedProperties().add(new ModelProperty("id","customer.column.",Order.asc));
	  getSortedProperties().add(new ModelProperty("invoices[].total","customer.column.",Order.asc));
	}

    protected	java.lang.Integer id;
    protected	java.lang.String firstName;
    protected	java.lang.String lastName;
    
    protected BigDecimal total;
 
    @Equals("id")
	public java.lang.Integer getId()
	{
	  return id;
	}
		
	public void setId(java.lang.Integer id)
	{
	   this.id = id;
	}
  
  
	@Equals("firstName")
	public java.lang.String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(java.lang.String firstName)
	{
		this.firstName = firstName;
	}
  
	@Equals("lastName")
	public java.lang.String getLastName()
	{
		return lastName;
	}

	public void setLastName(java.lang.String lastName)
	{
		this.lastName = lastName;
	}

	@GreaterThan("invoices[].total")
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}