package ${pattern_package};

import java.io.Serializable;

import org.regola.filter.annotation.Equals;
import org.regola.model.ModelPattern;
import org.regola.model.ModelProperty;
import org.regola.model.Order;

public class ${filter_name} extends ModelPattern implements Serializable
{
   private static final long serialVersionUID = 1L;

   public ${filter_name}()
   {
	  <#list idProperties as proprieta >
	  addProperty("id.${same(proprieta.name)}","${field(model_name)}.column.");
	  </#list>
	  <#if idProperties?size == 0 >
	  addProperty("id","${field(model_name)}.column.");
	  </#if>
	  <#list modelProperties as proprieta >
	  addProperty("${same(proprieta.name)}","${field(model_name)}.column.");
	  </#list>
	  
	  <#if idProperties?size &gt; 0 >
	  getSortedProperties().add(new ModelProperty("id.${same(idProperties[0].name)}","${field(model_name)}.column.",Order.asc));
	  <#else>
	  getSortedProperties().add(new ModelProperty("id","${field(model_name)}.column.",Order.asc));
	  </#if>
	}

  <#if idProperties?size == 0 >
    protected	${id_class} id;
  </#if>
  <#list allProperties as proprieta >
    protected	${proprieta.propertyType.name} ${same(proprieta.name)};
  </#list>	
  <#if idProperties?size == 0 >
 
    @Equals("id")
	public ${id_class} getId()
	{
	  return id;
	}
		
	public void setId(${id_class} id)
	{
	   this.id = id;
	}
  
  </#if>
   <#list idProperties as proprieta >
	
	@Equals("id.${same(proprieta.name)}")
	public ${proprieta.propertyType.name} get${field(proprieta.name)}()
	{
		return ${same(proprieta.name)};
	}

	public void set${field(proprieta.name)}(${proprieta.propertyType.name} ${same(proprieta.name)})
	{
		this.${same(proprieta.name)} = ${same(proprieta.name)};
	}
  </#list>	
  <#list modelProperties as proprieta >
  
	@Equals("${same(proprieta.name)}")
	public ${proprieta.propertyType.name} get${field(proprieta.name)}()
	{
		return ${same(proprieta.name)};
	}

	public void set${field(proprieta.name)}(${proprieta.propertyType.name} ${same(proprieta.name)})
	{
		this.${same(proprieta.name)} = ${same(proprieta.name)};
	}
  </#list>	  
}