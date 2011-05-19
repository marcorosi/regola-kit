<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://portals.apache.org/pluto" prefix="pluto" %>
<html>
<head>
    <title>Prova</title>
    <style type="text/css" title="currentStyle" media="screen">
        @import "<c:out value="${pageContext.request.contextPath}"/>/pluto.css";
        @import "<c:out value="${pageContext.request.contextPath}"/>/portlet-spec-1.0.css";
    </style>
    <script type="text/javascript" src="<c:out value="${pageContext.request.contextPath}"/>/pluto.js"></script>
</head>
    <body>
         <h1>Questa pagina include la portlet con nome MyPortlet</h1>
         <pluto:portlet portletId="/homes.MyPortlet">
      			<pluto:render/>
          </pluto:portlet>
   </body>
</html>
