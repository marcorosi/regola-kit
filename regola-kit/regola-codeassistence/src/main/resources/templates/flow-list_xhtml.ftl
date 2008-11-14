<!DOCTYPE composition PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component"
	template="/WEB-INF/facelets/template/anonimo.xhtml"
	xmlns:sf="http://www.springframework.org/tags/faces">

	
	<ui:define name="titolo">
		<ice:outputText value="${r"#{"}msg['${field(model_name)}.title']}" />
	</ui:define>
	<ui:define name="sottotitolo">
		<ice:outputText value="${r"#{"}msg['${field(model_name)}.subtitle']}" />
	</ui:define>
	<ui:define name="titolobarra">
		<ice:outputText value="${r"#{"}msg['${field(model_name)}.bartitle']}" />
	</ui:define>

	<ui:define name="corpo">
		<ui:fragment id="${field(model_name)}ToolbarFragment">
			<h:form id="${field(model_name)}ToolbarFragment">
		
		<ice:panelGrid columns="2">
		
			<ice:commandLink value="${r"#{"}msg['${field(model_name)}.title']}"
				  		styleClass="buttonNew"
				  		action="${field(model_name)}New" />
			
			<ice:panelGrid styleClass="ptoolbar" columns="100">
			  
			  <ice:commandButton image="https://starc.unibo.it/images/option.gif" 
			  	title="${r"#"}{msg['button.tooltip.option']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	action="showColumnsDlg" />
			  	
			  <ice:commandButton image="https://starc.unibo.it/images/order.gif" 
			  	title="${r"#"}{msg['button.tooltip.order']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	action="showOrderDlg" />
			  
			  <!--TODO: add here fast filters -->
			  <ice:outputText value="${r"#"}{msg['${field(model_name)}.column.id']}" />
 			  <ice:inputText value="${r"#"}{pattern.id}" />
			  
			  <ice:commandButton image="https://starc.unibo.it/images/search.gif" 
			  	title="${r"#"}{msg['button.tooltip.search']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	action="search${model_name}" />
		
			</ice:panelGrid>
		</ice:panelGrid>
		
			</h:form>
		</ui:fragment>
		
		<ui:fragment id="${field(mbean_list_name)}ListFragment">
			<ice:form>
	
		
		<ice:dataTable id="${field(mbean_list_name)}" var="item"
				rows="${r"#{"}pattern.pageSize}"
				rowClasses="rigaPari,rigaDispari"
				value="${r"#{"}list}">
		
			<ice:columns value="${r"#{"}columns}" var="column">
					<f:facet name="header">
						<ice:outputText value="${r"#{"}msg[column.label]}" />
					</f:facet>
					<ice:outputText value="${r"#{"}list.cellValue}" />
				</ice:columns>

				<ice:column>

					<f:facet name="header">
						<ice:outputText value="" />
					</f:facet>
				  
				   <ice:commandLink value="Edita"
				  		styleClass="buttonNew"
				  		action="select${model_name}" />				  

  				  <ice:commandLink value="Cancella"
					  styleClass="buttonNew"
					  partialSubmit="true"
					  action="cancel${model_name}" />
				  

				</ice:column>
				
			</ice:dataTable>
			
		
			<!-- e.g. [|<] [<] [>] [>|] (i bottoni di scorrimento) -->
			<ice:panelGrid columns="4">
					
				 <ice:panelGrid columns="4">
					   <ice:commandButton value="first" image="./xmlhttp/css/xp/css-images/arrow-first.gif"
						  partialSubmit="true"   action="moveFirst" />
						<ice:commandButton value="first" image="./xmlhttp/css/xp/css-images/arrow-previous.gif"
						  partialSubmit="true"   action="movePrevious" />
						<ice:commandButton value="first" image="./xmlhttp/css/xp/css-images/arrow-next.gif"
						  partialSubmit="true"   action="moveNext" />
						<ice:commandButton value="first" image="./xmlhttp/css/xp/css-images/arrow-last.gif"
						  partialSubmit="true"   action="moveLast" />
				  </ice:panelGrid>
	
				<!-- e.g. Pagina 1 di 2 -->
				<ice:outputText value="Pagina ${r"#{"}pattern.currentPage + 1} di ${r"#{"}pattern.lastPage+1}" />
			
				<!-- e.g. Righe per pagina [20] -->
				<ice:outputText value="${r"#{"}msg['paginator.pageSize']}" />
                <sf:ajaxEvent event="onchange"  processIds="*">
	                <ice:selectOneMenu value="${r"#{"}pattern.pageSize}"  partialSubmit="true">
	                    <f:selectItem itemValue="10" itemLabel="10"/>
	                    <f:selectItem itemValue="20" itemLabel="20"/>
	                    <f:selectItem itemValue="30" itemLabel="30"/>
	                    <f:selectItem itemValue="40" itemLabel="40"/>
	                    <f:selectItem itemValue="50" itemLabel="50"/>
	                </ice:selectOneMenu>
                </sf:ajaxEvent>
			
				
			</ice:panelGrid>
		
		</ice:form>
		</ui:fragment>
	</ui:define>
</ui:composition>

