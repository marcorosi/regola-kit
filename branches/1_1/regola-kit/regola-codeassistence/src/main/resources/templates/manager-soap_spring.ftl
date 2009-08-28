     
   <jaxws:endpoint id="${field(soap_service_name)}" 
		implementor="#${service_bean_name}" address="/services/${soap_service_name}">
		<!--jaxws:features>
			<bean class="org.apache.cxf.feature.LoggingFeature" />
		</jaxws:features-->
		<jaxws:inInterceptors>
			<bean class="org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor">
				<constructor-arg>
					<map>
						<entry key="action" value="UsernameToken Timestamp" />
						<entry key="passwordType" value="PasswordText" />
						<entry key="passwordCallbackClass" value="${service_impl_package}.${service_interface_name}PasswordCallBack" />
					</map>
				</constructor-arg>
			</bean>
		</jaxws:inInterceptors>
	</jaxws:endpoint>
    
    
</beans>    