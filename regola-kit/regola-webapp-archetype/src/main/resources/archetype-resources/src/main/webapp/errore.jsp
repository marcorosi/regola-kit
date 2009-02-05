<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html"%>
<%@ page isErrorPage = "true" %>
<%@ page import="java.io.*"%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="-1" />
	<title>Anomalia applicativa</title>
  </head>
<body>
	<h3>Regola kit module</h3>
	<b>L&#39;applicazione ha rilevato un errore inatteso</b>
	<br/>

	<h4 style="margin-top: 5px; margin-bottom: 5px">Stack trace:</h4>
	<pre>
	<%
	java.io.StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	exception.printStackTrace(pw);
	out.print(sw);
	sw.close();
	pw.close();
	%>
	</pre>
</body>
</html>
   
