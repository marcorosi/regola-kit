
<bean id="${tipo.displayName}Mock" class="${tipo.type}">
<#list tipo.propertyDescriptors as proprieta >
   <property name="${proprieta.name}" value="${reader(proprieta.name)}" />
</#list>
</bean>
