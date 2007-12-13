<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<body>
<ui:composition template="common/anonimo.xhtml">
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
		<ice:form>
		
		<ice:panelGrid columns="2">
		
			<ice:outputLink value="${field(model_name)}-form.html" styleClass="buttonNew">
			   <ice:outputText value="${r"#{"}msg['${field(model_name)}.title']}" />
			</ice:outputLink>
			
			<ice:panelGrid styleClass="ptoolbar" columns="100">
			  <ice:commandButton image="images/option.gif" 
			  	title="${r"#"}{msg['button.tooltip.option']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	actionListener="${r"#{"}${field(mbean_list_name)}.listPage.doColumnsDlg}" />
			  <ice:commandButton image="images/order.gif" 
			  	title="${r"#"}{msg['button.tooltip.order']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	actionListener="${r"#{"}${field(mbean_list_name)}.listPage.doOrderDlg}" />
			  
			  <!--TODO: aggiungi i filtri rapidi -->
			  <ice:outputText value="${r"#"}{msg['${field(model_name)}.column.id']}" />
 			  <ice:inputText value="${r"#"}{${field(mbean_list_name)}.listPage.filter.id}" />
			  
			  <ice:commandButton image="images/search.gif" 
			  	title="${r"#"}{msg['button.tooltip.search']}" 
			  	styleClass="toolbar" 
			  	partialSubmit="true" 
			  	actionListener="${r"#"}{${field(mbean_list_name)}.listPage.refresh}" />
		
			</ice:panelGrid>
		</ice:panelGrid>
		
		<ice:dataTable id="${field(mbean_list_name)}" var="item"
				rows="${r"#{"}${field(mbean_list_name)}.listPage.filter.pageSize}"
				rowClasses="rigaPari,rigaDispari"
				value="${r"#{"}${field(mbean_list_name)}.listPage.rowDataModel}">
		
			<ice:columns value="${r"#{"}${field(mbean_list_name)}.listPage.columnDataModel}" var="column">
					<f:facet name="header">
						<ice:outputText value="${r"#{"}msg[column.label]}" />
					</f:facet>
					<!-- il prossimo outputText fissa un baco delle icefaces -->
					<ice:outputText value="${r"#{"}${field(mbean_list_name)}.listPage.currentCulumnCallback[column.name]}" />
					<ice:outputText value="${r"#{"}${field(mbean_list_name)}.listPage.cellValue}" />
				</ice:columns>

				<ice:column>

					<f:facet name="header">
						<ice:outputText value="" />
					</f:facet>
				  
				  <ice:commandLink value="Edita"
				  		styleClass="buttonNew"
				  		action="edit"
				  		actionListener="${r"#{"}${field(mbean_list_name)}.listPage.storeId}" />				  

  				  <ice:commandLink value="Cancella"
					  styleClass="buttonNew"
					  partialSubmit="true"
					  actionListener="${r"#{"}${field(mbean_list_name)}.listPage.remove}" />

				</ice:column>
				
			</ice:dataTable>
			
			<ice:panelGrid columns="4">
		
					<ice:dataPaginator for="${field(mbean_list_name)}" fastStep="3"
					actionListener="${r"#{"}${field(mbean_list_name)}.listPage.paginatorListener}"
					pageCountVar="pageCount" pageIndexVar="pageIndex" paginator="true"
					paginatorMaxPages="4" vertical="false" rendered="true"
					styleClass="formBorderHighlight">
					<f:facet name="first">
						<ice:graphicImage url="./xmlhttp/css/xp/css-images/arrow-first.gif"
							style="border:none;" title="${r"#{"}msg['paginator.first']}" />
					</f:facet>
					<f:facet name="last">
						<ice:graphicImage url="./xmlhttp/css/xp/css-images/arrow-last.gif"
							style="border:none;" title="${r"#{"}msg['paginator.last']}" />
					</f:facet>
					<f:facet name="previous">
						<ice:graphicImage
							url="./xmlhttp/css/xp/css-images/arrow-previous.gif"
							style="border:none;" title="${r"#{"}msg['paginator.previous']}" />
					</f:facet>
					<f:facet name="next">
						<ice:graphicImage url="./xmlhttp/css/xp/css-images/arrow-next.gif"
							style="border:none;" title="${r"#{"}msg['paginator.next']}" />
					</f:facet>
				</ice:dataPaginator>
	
				<ice:dataPaginator for="${field(mbean_list_name)}" rowsCountVar="rowsCount"
					displayedRowsCountVar="displayedRowsCountVar"
					firstRowIndexVar="firstRowIndex" lastRowIndexVar="lastRowIndex"
					pageCountVar="pageCount" pageIndexVar="pageIndex">
	
					<ice:outputFormat
						value="${r"#{"}msg['paginator.state']}"
						styleClass="standard">
						<f:param value="${r"#{"}firstRowIndex}" />
						<f:param value="${r"#{"}lastRowIndex}" />
						<f:param value="${r"#{"}rowsCount}" />
						<f:param value="${r"#{"}pageIndex}" />
						<f:param value="${r"#{"}pageCount}" />
					</ice:outputFormat>
				</ice:dataPaginator>

				<ice:outputText value="${r"#{"}msg['paginator.pageSize']}" />
                <ice:selectOneMenu value="${r"#{"}${field(mbean_list_name)}.listPage.filter.pageSize}"
					               valueChangeListener="${r"#{"}${field(mbean_list_name)}.listPage.refresh}"
					               partialSubmit="true">
                    <f:selectItem itemValue="10" itemLabel="10"/>
                    <f:selectItem itemValue="20" itemLabel="20"/>
                    <f:selectItem itemValue="30" itemLabel="30"/>
                    <f:selectItem itemValue="40" itemLabel="40"/>
                    <f:selectItem itemValue="50" itemLabel="50"/>
                </ice:selectOneMenu>
			</ice:panelGrid>
		
		</ice:form>
	</ui:define>
</ui:composition>
</body>
</html>
