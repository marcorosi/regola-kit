<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

Esempio di portlet con Regola kit, Spring MVC e Webflow
<br>

<portlet:actionURL var="actionUrl">
		<portlet:param name="execution" value="${r"${"}flowExecutionKey}" />
		<portlet:param name="_eventId" value="finish" />
</portlet:actionURL>

<a href="${r"${"}actionUrl}">esempio di link</a>
