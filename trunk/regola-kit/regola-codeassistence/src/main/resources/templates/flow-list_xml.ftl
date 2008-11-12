<?xml version="1.0" encoding="UTF-8"?>
<flow xmlns="http://www.springframework.org/schema/webflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/webflow http://www.springframework.org/schema/webflow/spring-webflow-2.0.xsd">
  
	<var name="listActions"  class="org.regola.webapp.flow.ListActionsIceFaces" />
	<var name="pattern" class="${pattern_package}.${filter_name}" />
	<var name="model" class="${model_class}" />

	<!-- Dialogs -->	
	<var name="columnsDialog" class="org.regola.webapp.flow.dialog.ColumnsDialog" />
	<var name="orderDialog" class="org.regola.webapp.flow.dialog.OrderDialog" />
	
	<view-state id="list">
		<on-render>
			<!--evaluate expression="listActions.refresh(${service_bean_name},pattern)" result="viewScope.list" /-->
			<evaluate expression="listActions.refresh(universalDao, model ,  pattern)" result="viewScope.list" />
			<evaluate expression="viewScope.list.prepareColumns()" result="viewScope.columns" />
		</on-render>
	
		
	
		<transition on="search${model_name}" >
			<render fragments="${field(model_name)}ListFragment"/>
		</transition>
		
		<transition on="select" to="form">
			<set name="flowScope.${field(model_name)}" value="list.selectedRow" />
		</transition>
		
		<transition on="edit" to="form">
			<set name="flowScope.${field(model_name)}" value="null" />
		</transition>
		
		<transition on="cancel${model_name}">
			<evaluate expression="universalDao.removeEntity(list.selectedRow)" />
			<render fragments="${field(model_name)}ListFragment"/>
		</transition>
		
		<!-- Pagination  -->
		<transition on="moveNext">
			<evaluate expression="pattern.nextPage()" />
		</transition>
		<transition on="movePrevious">
			<evaluate expression="pattern.previousPage()" />
		</transition>
		<transition on="moveFirst">
			<evaluate expression="pattern.setCurrentPage(0)" />
		</transition>
		<transition on="moveLast">
			<evaluate expression="pattern.gotoLastPage()" />
		</transition>
		
		<!-- Columns Dialogs -->
		<transition on="showColumnsDlg">
			<set name="columnsDialog.pattern" value="pattern" />
			<evaluate expression="columnsDialog.showModal('Titolo', 'Messaggio')" />
		</transition>
		<transition on="columnsDlg.onOK">
			<evaluate expression="columnsDialog.close()" />
		</transition>
		<transition on="columnsDlg.onCancel">
			<set name="pattern" value="columnsDialog.originalPattern"/>
			<evaluate expression="columnsDialog.close()" />
		</transition>
	
		<!-- Order Dialogs -->
		<transition on="showOrderDlg">
			<set name="orderDialog.pattern" value="pattern" />
			<evaluate expression="orderDialog.showModal('Titolo', 'Messaggio')" />
		</transition>
		<transition on="orderDlg.onOK">
			<evaluate expression="orderDialog.close()" />
		</transition>
		<transition on="orderDlg.onCancel">
			<set name="pattern" value="orderDialog.originalPattern"/>
			<evaluate expression="orderDialog.close()" />
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