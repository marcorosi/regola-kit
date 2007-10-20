
   	<bean id="${field(mbean_list_name)}"
		class="${mbean_package}.${mbean_list_name}" scope="session" init-method="init">
		<property name="serviceManager" ref="${service_bean_name}" />
		<property name="confirmDlg" ref="confirmDlg" />
		<property name="columnsDlg" ref="columnsDlg" />
		<property name="orderDlg" ref="orderDlg" />
	</bean>

</beans>    