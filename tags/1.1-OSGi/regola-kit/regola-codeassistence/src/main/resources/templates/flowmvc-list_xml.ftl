<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/webflow http://www.springframework.org/schema/webflow/spring-webflow-2.0.xsd">

    <persistence-context/>
  
	<var name="listActions"  class="org.regola.webapp.flow.mvc.MvcListActions" />
	<var name="pattern" class="${pattern_package}.${filter_name}" />
	<var name="model" class="${model_class}" />

	<action-state id="caricaDati">
		<!--evaluate expression="listActions.refresh(${service_bean_name},pattern)" result="flowScope.list" /-->
		<evaluate expression="listActions.refresh(universalDao, model, pattern)" result="flowScope.list" />
		<transition on="*" to="list" />
	</action-state>

	<view-state id="list" view="${field(model_name)}-list" model="pattern">
	
		<transition on="search${model_name}" to="caricaDati">
		</transition>
		
		<transition on="new" to="form">
			<set name="flowScope.${field(model_name)}Id" value="null" />
		</transition>
		
		<transition on="cancel${model_name}" to="remove">
			<set name="requestScope.toBeRemoved" value="list.get(requestParameters.idx)" />
		</transition>
		
		<transition on="edit" to="form">
			<set name="flowScope.${field(model_name)}Id" value="list.get(requestParameters.idx).id" />
		</transition>
		
		<!-- Pagination  -->
		<transition on="moveNext" to="caricaDati">
		    <evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.nextPage()" />
		</transition>
		<transition on="movePrevious" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.previousPage()" />
		</transition>
		<transition on="moveFirst" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.setCurrentPage(0)" />
		</transition>
		<transition on="moveLast" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.gotoLastPage()" />
		</transition>
		<transition on="pageSize" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
			<!--<evaluate expression="pattern.setPageSize(50)" />-->
		</transition>

	</view-state>

	<subflow-state id="form" subflow="${field(model_name)}-form">
		<input name="${field(model_name)}Id" value="${field(model_name)}Id" />		
		<transition on="cancel" to="list" />
		<transition on="confirm" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
		</transition>
	</subflow-state>
	
	<subflow-state id="remove" subflow="rimuoviEntita" >
		<input name="documento" value="toBeRemoved" />
		<transition on="finish" to="caricaDati">
			<evaluate expression="persistenceContext.clear()" />
		</transition>
	</subflow-state>

	<end-state id="finish" />
	
  	<bean-import resource="beans.xml"/>

</flow>