	
	<bean id="${field(mbean_form_name)}" class="${mbean_package}.${mbean_form_name}" scope="session" init-method="init">
		<property name="serviceManager" ref="${service_bean_name}" />
		<property name="encodedId"  ref="id"/>
	</bean>
	
</beans>	
