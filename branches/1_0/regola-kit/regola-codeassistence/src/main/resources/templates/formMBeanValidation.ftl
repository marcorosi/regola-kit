<?xml version="1.0" encoding="UTF-8"?>
<model class="${model_class}">
<#list idProperties as proprieta >
    <add type="org.hibernate.validator.NotEmpty" property="id.${same(proprieta.name)}"/>
</#list>
<#if idProperties?size == 0 >
    <add type="org.hibernate.validator.NotEmpty" property="id"/>
</#if>
<#list modelProperties as proprieta >
    <remove type="org.hibernate.validator.NotEmpty" property="${same(proprieta.name)}"/>
</#list>
</model>