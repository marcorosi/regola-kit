    <bean id="${dao_bean_name}" class="${dao_impl_package}.${dao_impl_name}">
        	<constructor-arg value="${model_class}" />
		    <property name="sessionFactory" ref="sessionFactory"/>
    </bean>  
</beans>    