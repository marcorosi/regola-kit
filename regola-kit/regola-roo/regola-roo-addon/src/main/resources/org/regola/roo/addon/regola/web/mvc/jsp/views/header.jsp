<%@ page contentType="text/html"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<spring:url value="/resources/starc" var="starc_url" />
<div class="bgTestataExt">
	<div class="testata">
		<div id="leftColumnCell">
			<a href="http://www.unibo.it">
			<img src="https://starc.unibo.it/images/logo_unibo.gif"
				alt="<fmt:message key="webapp.portal.alt"/>" title="<fmt:message key="webapp.portal.alt"/>"/>
			</a>
		</div>
		<img src="${starc_url}/sol.png" alt="<fmt:message key="webapp.portal.testata.sol.alt"/>">		
	</div>
</div>