<?xml version="1.0" encoding="UTF-8"?>

<!-- model class="${model_class}">
<#list idProperties as proprieta >
    <add type="org.hibernate.validator.NotEmpty" property="id.${same(proprieta.name)}"/>
</#list>
<#if idProperties?size == 0 >
    <add type="org.hibernate.validator.NotEmpty" property="id"/>
</#if>
<#list modelProperties as proprieta >
    <remove type="org.hibernate.validator.NotEmpty" property="${same(proprieta.name)}"/>
</#list>
</model -->

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