<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE tiles-definitions PUBLIC
       "-//Apache Software Foundation//DTD Tiles Configuration 2.0//EN"
       "http://tiles.apache.org/dtds/tiles-config_2_0.dtd">
<tiles-definitions>

	
	<definition name="${flow_name}-form" extends="struttura">
		<put-attribute name="title"	value="Lingua" />
		<put-attribute name="body" value="/WEB-INF/flows/${flow_name}/form.jsp" />
	</definition>

	<definition name="${flow_name}-list" extends="struttura">
		<put-attribute name="title"	value="Lingua" />
		<put-attribute name="body" value="/WEB-INF/flows/${flow_name}/list.jsp" />
	</definition>	
	
</tiles-definitions>