package ${dao_package};

import java.io.Serializable;

import it.kion.regola.validation.ModelFilter;
import it.kion.regola.validation.PropertyFilter;
import it.kion.regola.validation.PropertyFilter.Order;
import it.kion.regola.validation.annotations.Equals;

public class ${filter_name} extends ModelFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

	public ${filter_name}()
	{
	  <#list idProperties as proprieta >
	    defineColumn("id.${same(proprieta.name)}","${field(model_name)}.column.");
	  </#list>
	  <#if idProperties?size == 0 >
	    defineColumn("id","${field(model_name)}.column.");
	  </#if>
	  <#list modelProperties as proprieta >
	    defineColumn("${same(proprieta.name)}","${field(model_name)}.column.");
	  </#list>
	  
	  <#if idProperties?size &gt; 0 >
	  getSortedColumns().add(new PropertyFilter("id.${same(idProperties[0].name)}","${field(model_name)}.column.",Order.asc));
	  <#else>
	  getSortedColumns().add(new PropertyFilter("id","${field(model_name)}.column.",Order.asc));
	  </#if>
	}

  <#if idProperties?size == 0 >
  protected	${id_class} id;
  </#if>

  <#list allProperties as proprieta >
  protected	${proprieta.propertyType.name} ${same(proprieta.name)};
  </#list>	
  
  <#if idProperties?size == 0 >
    
    @Equals(propertyPath="id")
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
	
	@Equals(propertyPath="id.${same(proprieta.name)}")
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
  
	@Equals(propertyPath="${same(proprieta.name)}")
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