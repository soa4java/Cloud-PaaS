<%@page pageEncoding="UTF-8" %>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.primeton.paas.app.example.Foo"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.primeton.paas.app.example.ExampleUtils"%>
<%
String key = (String)request.getParameter("cacheKey");
if (null != key) {
	String value = ExampleUtils.getCache(key);
	response.getWriter().write(null == value ? "null" : value);
}
%>