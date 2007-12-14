		
	<bean id="${field(mbean_form_name)}" class="${mbean_package}.${mbean_form_name}" scope="session" init-method="init">
        <property name="formPage"  >
            <bean class="org.regola.webapp.action.FormPage" scope="session">
                <property name="component">
                    <bean  class="org.regola.webapp.action.icefaces.FormPageIceFaces" scope="session"/>
                </property>
                <property name="serviceManager" ref="${service_bean_name}" />
                <property name="encodedId"  ref="id"/>
                <property name="eventBroker" ref="eventBroker" />
            </bean>
        </property>
    </bean>
	
</beans>	
