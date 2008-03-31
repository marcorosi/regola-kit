package org.regola.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.NotEmpty;

public class Product implements Serializable {

	private static final long serialVersionUID = 507070215255848392L;

	private Integer id;
	private String name;
	private BigDecimal price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@NotEmpty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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
