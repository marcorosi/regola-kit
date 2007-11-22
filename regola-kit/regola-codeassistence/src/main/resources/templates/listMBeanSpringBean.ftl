
	<bean id="${field(mbean_list_name)}" class="${mbean_package}.${mbean_list_name}" scope="session" init-method="init" >
        <property name="listPage">
            <bean class="org.regola.webapp.action.ListPage"  >
                <property name="component">
                    <bean class="org.regola.webapp.action.icefaces.ListPageIceFaces">
                    </bean>
                </property>
                <property name="serviceManager" ref="${service_bean_name}" />
                <property name="eventBroker" ref="eventBroker"  />
                <property name="confirmDlg" ref="confirmDlg" />
                <property name="columnsDlg" ref="columnsDlg" />
                <property name="orderDlg" ref="orderDlg" />
            </bean>
        </property>
    </bean>	
</beans>    