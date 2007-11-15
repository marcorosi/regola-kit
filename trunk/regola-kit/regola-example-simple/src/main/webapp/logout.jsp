<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
if (request.getSession(false) != null) {
    session.invalidate();
    Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
    terminate.setMaxAge(0);
    response.addCookie(terminate);
}
%>

<c:redirect url="/welcome.jsp"/>