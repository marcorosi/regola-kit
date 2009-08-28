<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/webflow http://www.springframework.org/schema/webflow/spring-webflow-2.0.xsd">

    <persistence-context/>
  
	<var name="listActions"  class="org.regola.webapp.flow.mvc.MvcListActions" />
	<var name="pattern" class="${pattern_package}.${filter_name}" />
	<var name="model" class="${model_class}" />

	<view-state id="list" view="${field(model_name)}-list" model="pattern">
			
		<on-render>
			<!--evaluate expression="listActions.refresh(${service_bean_name},pattern)" result="viewScope.list" /-->
			<evaluate expression="listActions.refresh(universalDao, model ,  pattern)" result="viewScope.list" />			
		</on-render>
	
		<transition on="search${model_name}" >			
		</transition>
		
		<transition on="select${model_name}" to="form">
			<set name="flowScope.${field(model_name)}" value="list.selectedRow" />
		</transition>
		
		<transition on="${field(model_name)}New" to="form">
			<set name="flowScope.${field(model_name)}" value="null" />
		</transition>
		
		<transition on="cancel${model_name}">
			<evaluate expression="universalDao.removeEntity(list.selectedRow)" />
			<render fragments="${field(model_name)}ListFragment"/>
		</transition>
		
		<transition on="edit" to="form">
			<set name="flowScope.${field(model_name)}" value="list.get(requestParameters.idx)" />
		</transition>
		
		
		<!-- Pagination  -->
		<transition on="moveNext">
		    <evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.nextPage()" />
		</transition>
		<transition on="movePrevious">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.previousPage()" />
		</transition>
		<transition on="moveFirst">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.setCurrentPage(0)" />
		</transition>
		<transition on="moveLast">
			<evaluate expression="persistenceContext.clear()" />
			<evaluate expression="pattern.gotoLastPage()" />
		</transition>
		<transition on="pageSize">
			<evaluate expression="persistenceContext.clear()" />
			<!--<evaluate expression="pattern.setPageSize(50)" />-->
		</transition>
		
			
	</view-state>
	
	<subflow-state id="form" subflow="${field(model_name)}-form" >
		<input name="${field(model_name)}Id" value="${field(model_name)}.id" />
		<transition on="cancel" to="list" />
		<transition on="confirm" to="list" />
	</subflow-state>

	<end-state id="finish" />
	
  	<bean-import resource="beans.xml"/>

</flow>