<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ice="http://www.icesoft.com/icefaces/component">
<body>
<ui:composition template="/common/anonimo.xhtml">
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
		<ice:inputText id="originalId" value="${r"#{"}${field(mbean_form_name)}.originalId}" visible="false" />
		<ice:outputConnectionStatus style="align: right;"/>
			<ice:panelGroup>
				<ice:panelGrid columns="4">
          <#list modelProperties as proprieta >
			
					<!-- ${same(proprieta.name)} -->
					<ice:outputText value="${r"#{"}msg['${field(model_name)}.column.${same(proprieta.name)}']}" />
					<ice:inputText  value="${r"#{"}${field(mbean_form_name)}.model.${same(proprieta.name)}}">
					</ice:inputText>
					<ice:outputText value=" " />
					<ice:outputText value="${r"#{"}${field(mbean_form_name)}.errors['${same(proprieta.name)}']}" />
		  </#list>
		  
		           <ice:outputText value="${r"#{"}${field(mbean_form_name)}.errore}" />
					<ice:outputText value=" " />
					<ice:outputText value=" " />
					<ice:outputText value=" " />

					<ice:commandButton value="Salva"
						styleClass="buttonNew"
						action="${r"#{"}${field(mbean_form_name)}.save}" />
					<ice:commandButton value="Annulla"
						styleClass="buttonNew"
						immediate="true"
						action="${r"#{"}${field(mbean_form_name)}.cancel}" />
				</ice:panelGrid>
			</ice:panelGroup>
		</ice:form>
	</ui:define>
</ui:composition>
</body>
</html>