<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<span class="titoloPagina"><fmt:message key="${field(model_name)}.form.title" /></span>
<br>
<br>
<span class="sottotitoloPagina">
	<c:choose>
		<c:when test="${r"${"}${field(model_name)}.id != null}">
			<fmt:message key="${field(model_name)}.form.modifica.subtitle" />
		</c:when>
		<c:otherwise>
			<fmt:message key="${field(model_name)}.form.crea.subtitle" />
		</c:otherwise>
	</c:choose> 
</span>
<br>
<br>

<form:form modelAttribute="${field(model_name)}" action="${r"${"}flowExecutionUrl}">

<table class="tabellaForm">
  <#list modelProperties as proprieta >
	
	<!-- ${same(proprieta.name)} -->
	<tr>
		<td class="etichetta">
			<fmt:message  key="${field(model_name)}.column.${same(proprieta.name)}" />
		</td>
		<td class="campo">
			<form:input path="${same(proprieta.name)}"/>
			<span class="fieldError"><form:errors path="${same(proprieta.name)}" cssClass="error" /></span>
		</td>
	</tr>
	
  </#list>
	
</table>
	
<div class="pulsanti">
	<input class="button" type="submit"
				name="_eventId_cancel" value="<fmt:message key="button.cancel"/>" />
	<input class="button" type="submit"
				name="_eventId_save" value="<fmt:message key="button.save"/>" />
</div>
	
</form:form>