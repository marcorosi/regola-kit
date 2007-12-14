<?xml version="1.0" encoding="UTF-8"?>
<list>
  <!-- AmendedModelClass>
  	<modelClass>${model_class}</modelClass>
  		<amendments>
<#list idProperties as proprieta >
  		<Amendment>
  			<amendmentType>add</amendmentType>
			<validationType>org.hibernate.validator.NotEmpty</validationType>
	 		<modelProperty>id.${same(proprieta.name)}</modelProperty>
		</Amendment>
</#list>
<#if idProperties?size == 0 >
  		<Amendment>
  			<amendmentType>add</amendmentType>
			<validationType>org.hibernate.validator.NotEmpty</validationType>
	 		<modelProperty>id</modelProperty>
		</Amendment>
</#if>
<#list modelProperties as proprieta >
  		<Amendment>
  			<amendmentType>remove</amendmentType>
			<validationType>org.hibernate.validator.NotEmpty</validationType>
	 		<modelProperty>${same(proprieta.name)}</modelProperty>
		</Amendment>
</#list>
		</amendments>
  </AmendedModelClass -->
</list>