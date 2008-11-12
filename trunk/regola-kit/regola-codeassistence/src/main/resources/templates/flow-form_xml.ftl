<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/webflow http://www.springframework.org/schema/webflow/spring-webflow-2.0.xsd">
  
  	<persistence-context/>
  
	<var name="${field(model_name)}" class="${model_class}" />
	<var name="formActions" class="org.regola.webapp.flow.FormActionsIceFaces" />
	<var name="errors" class="java.util.HashMap" />
	<!--<var name="effects" class="java.util.HashMap" />-->

	<input name="${field(model_name)}Id" required="false"/>
	

	
	<action-state id="newOrEdit">
    	<evaluate expression="${field(model_name)}Id eq null" />
    	<transition on="yes" to="form" />
    	<transition on="no" to="form" >
			<evaluate expression="universalDao.get(${field(model_name)}.class,${field(model_name)}Id)" result="flowScope.${field(model_name)}" />
    	</transition>
	</action-state>
	
	<view-state id="form" >
		<transition on="cancel" to="cancel"/>
		<transition on="save" to="saveOrUpdate">
			<evaluate expression="formActions.validate('${mbean_form_name}Amendments.xml', ${field(model_name)})" />
		</transition>		
	</view-state>
	
	<action-state id="saveOrUpdate">
		<evaluate expression="${field(model_name)}Id == null" />
    	<transition on="yes" to="confirm" >
			<evaluate expression="universalDao.save(${field(model_name)})"  />
    	</transition>
    	<transition on="no" to="confirm" />
	</action-state>
	
	
	<end-state id="cancel" />
	<end-state id="confirm" commit="true" />
	
  	<bean-import resource="beans.xml"/>

</flow>