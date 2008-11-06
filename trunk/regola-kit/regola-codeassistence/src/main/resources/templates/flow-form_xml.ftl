<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/webflow http://www.springframework.org/schema/webflow/spring-webflow-2.0.xsd">
  
	<var name="${field(model_name)}" class="${model_class}" />
	<var name="formActions" class="org.regola.webapp.flow.FormActionsIceFaces" />
	<var name="errors" class="java.util.HashMap" />
	<!--<var name="effects" class="java.util.HashMap" />-->

	<input name="${field(model_name)}Id" required="true"/>
	
	<on-start>
		<!--<evaluate expression="linguaId eq empty ? linguaManager.get(linguaId) : lingua" result="flowScope.lingua" />-->
		<evaluate expression="universalDao.get(${field(model_name)}.class,${field(model_name)}Id)" result="flowScope.${field(model_name)}" />
	</on-start>
	
	
	<view-state id="form" >
		<transition on="cancel" to="cancel"/>
		<transition on="save" to="confirm">
			<evaluate expression="formActions.validate('${mbean_form_name}Amendments.xml', ${field(model_name)})" />
		</transition>		
	</view-state>
	
	
	<end-state id="cancel" />
	<end-state id="confirm" />
	
  	<bean-import resource="beans.xml"/>

</flow>