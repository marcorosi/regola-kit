package org.regola.model.pattern;

import java.io.Serializable;

import org.regola.filter.annotation.Equals;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;

public class SimpleDTOPattern extends ModelPattern implements Serializable
{
   private static final long serialVersionUID = 1L;

   public SimpleDTOPattern()
   {
	  addProperty("id","simpleDTO.column.");
	  
	  getSortedProperties().add(new ModelProperty("id","simpleDTO.column.",Order.asc));
	}

    protected	java.lang.String id;
 
    @Equals("id")
	public java.lang.String getId()
	{
	  return id;
	}
		
	public void setId(java.lang.String id)
	{
	   this.id = id;
	}
  
}