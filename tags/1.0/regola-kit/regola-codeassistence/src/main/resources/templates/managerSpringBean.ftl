    <bean id="${service_bean_name}" class="${service_impl_package}.${service_impl_name}">
		<constructor-arg>
			<bean class="${dao_impl_package}.${dao_impl_name}"
				autowire="byType">
				<constructor-arg value="${model_class}" />
			</bean>
		</constructor-arg>
    </bean>  
</beans>    