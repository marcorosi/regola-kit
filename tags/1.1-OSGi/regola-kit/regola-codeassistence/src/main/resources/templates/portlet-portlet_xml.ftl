	<portlet>
			<portlet-name>${portlet_name}</portlet-name>
			<display-name>Regola kit-Spring Webflow-MVC Portlet</display-name>

			<portlet-class>
				org.springframework.web.portlet.DispatcherPortlet
			</portlet-class>

			<init-param>
				<name>contextConfigLocation</name>
				<value>
					/WEB-INF/flows/${portlet_name}/config.xml
				</value>
			</init-param>

			<init-param>
				<name>viewRendererUrl</name>
				<value>/WEB-INF/servlet/view</value>
			</init-param>

			<expiration-cache>0</expiration-cache>

			<supports>
				<mime-type>text/html</mime-type>
				<portlet-mode>view</portlet-mode>
			</supports>

			<portlet-info>
				<title>Regola kit-Spring Webflow-MVC Portlet</title>
			</portlet-info>
		</portlet>
</portlet-app>
