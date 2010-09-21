package ${pattern_package}
{
	import org.regola.model.ModelPattern;
	import org.regola.model.ModelProperty;
	
	[Bindable]
	[RemoteClass(alias="${pattern_package}.${filter_name}")]
	public class ${filter_name} extends ModelPattern
	{
	
	<#if idProperties?size == 0 >
    	public var 	id:${id_flex_class};
  	</#if>
  	<#list allProperties as proprieta >
    	public var ${same(proprieta.name)}:${proprieta.flexType} ;
  	</#list>
		
		public function ${filter_name}()
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
	  		sortedProperties.addItem(new ModelProperty("id.${same(idProperties[0].name)}","${field(model_name)}.column.",1));
	  <#else>
	  		sortedProperties.addItem(new ModelProperty("id","${field(model_name)}.column.",1));
	  </#if>
			
		}
		
	}
}