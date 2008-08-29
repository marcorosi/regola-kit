<%@ page isErrorPage="true" import="java.io.*"%>
<html>
<head>
<title>Anomalia</title>
<style>
body,p {
	font-family: Tahoma;
	font-size: 10pt;
	padding-left: 30;
}

pre {
	font-size: 8pt;
}
</style>
</head>
<body>
<%-- Exception Handler --%>
<font color="black"> <%=exception.toString()%><br>
</font>
<%
	out.println("<pre>");
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	exception.printStackTrace(pw);
	out.print(sw);
	sw.close();
	pw.close();
	out.println("</pre>");
%>
</body>
</html>

