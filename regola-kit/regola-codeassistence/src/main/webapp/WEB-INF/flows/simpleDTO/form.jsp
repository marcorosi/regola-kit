<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<span class="titoloPagina"><fmt:message key="simpleDTO.form.title" /></span>
<br>
<br>
<span class="sottotitoloPagina">
	<c:choose>
		<c:when test="${simpleDTO.id != null}">
			<fmt:message key="simpleDTO.form.modifica.subtitle" />
		</c:when>
		<c:otherwise>
			<fmt:message key="simpleDTO.form.crea.subtitle" />
		</c:otherwise>
	</c:choose> 
</span>
<br>
<br>

<form:form modelAttribute="simpleDTO" action="${flowExecutionUrl}">

<table class="tabellaForm">
	
</table>
	
<div class="pulsanti">
	<input class="button" type="submit"
				name="_eventId_cancel" value="<fmt:message key="button.cancel"/>" />
	<input class="button" type="submit"
				name="_eventId_save" value="<fmt:message key="button.save"/>" />
</div>
	
</form:form>