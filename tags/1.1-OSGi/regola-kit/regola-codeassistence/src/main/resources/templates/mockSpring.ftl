
	<bean id="${mock_name}" class="${tipo.type}">
	
	<#list notCollectionsProps as proprieta >
	   <property name="${same(proprieta.name)}" value="${same(proprieta.name)}" />
	</#list>
	
	<#list collectionsProps as proprieta >
	   <property name="${same(proprieta.name)}">
	   		<list>
	   			<ref bean="" />
	   		</list>
	   </property>
	</#list>
	
	</bean>

</beans>