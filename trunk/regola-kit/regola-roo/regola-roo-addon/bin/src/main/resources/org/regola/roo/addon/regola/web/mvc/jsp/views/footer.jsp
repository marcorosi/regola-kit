<%@ page contentType="text/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- <%@ taglib uri="http://java.sun.com/jstl/core-rt" prefix="c-rt" %> --%>

<%@page import="org.regola.webapp.listener.StartupListener"%>

<div class="tools">
&nbsp;
</div>
<div class="leftFooterColumnCell">
	<img alt="Brand Unibo.it" title="Brand Unibo.it" src="https://starc.unibo.it/images/unibo_brand.jpg"/> 
	&nbsp;
</div>


<div class="copyright">
	<p class="screenFooter">
		<fmt:message key="label.footer.copyright" /> <fmt:formatDate value="<%=new java.util.Date()%>" type="date" pattern="yyyy" />
		- <fmt:message key="label.footer.universityname"/><br/>
		<fmt:message key="label.footer.universityaddress"/><br/>
		<a
		href="<fmt:message key="label.footer.privacypolicyurl"/>"><fmt:message key="label.footer.informativaPrivacy"/></a> - 
		<a href="<fmt:message key="label.footer.identitysystemurl"/>">
			<fmt:message key="label.footer.sistemaIdentitaAteneo" />
		</a>
	</p>
	<!-- 
	[<%=application.getAttribute(StartupListener.BUILD_INFO)%> - 
	<%=application.getAttribute(StartupListener.SERVER_INFO)%>]
	 -->
</div>

<c:set var="ambiente"  value="<%= System.getProperty(\"regola.env\") %>" />
<c:if test="${ambiente != 'dev' && ambiente != 'test'}" >

	<script type="text/javascript">
	
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-22602159-1']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	</script>
</c:if>
