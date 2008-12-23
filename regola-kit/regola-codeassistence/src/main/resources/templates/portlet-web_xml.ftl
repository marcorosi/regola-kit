<!-- Generated Portlet Wrapper Servlet for Apache Pluto deployment -->
	<servlet>
		<servlet-name>${portlet_name}</servlet-name>
		<servlet-class>org.apache.pluto.core.PortletServlet</servlet-class>
		<init-param>
			<param-name>portlet-name</param-name>
			<param-value>${portlet_name}</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>${portlet_name}</servlet-name>
		<url-pattern>/PlutoInvoker/${portlet_name}</url-pattern>
	</servlet-mapping>
	
</web-app>