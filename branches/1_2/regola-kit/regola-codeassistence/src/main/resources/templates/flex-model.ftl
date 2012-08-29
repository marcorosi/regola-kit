package ${model_package}
{
	
	[Bindable]
	[RemoteClass(alias="${model_class}")]
	public class ${model_name} 
	{
	
	<#if idProperties?size == 0 >
    	public var 	id:${id_flex_class};
  	</#if>
  	<#list allProperties as proprieta >
    	public var ${same(proprieta.name)}:${proprieta.flexType} ;
  	</#list>
		
			
	}
}