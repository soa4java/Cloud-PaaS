<%@page pageEncoding="UTF-8"%>
<%@page import="com.primeton.paas.app.example.ExampleUtils"%>
<% 
String action = (String)request.getParameter("action");
String key = (String)request.getParameter("key");
String value = (String)request.getParameter("value");

Object v = null;
if ("write".equalsIgnoreCase(action)) {
	if (null != key) {
		session.setAttribute(key, value);
	}
} if ("read".equalsIgnoreCase(action)) {
	if (null != key) {
		v = session.getAttribute(key);
	}
}
%>
<html>
<head>
<title>Primeton PAAS Cloud Integrated Development Platform</title>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache,no-store">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/nui/nui.js"></script>
</head>
<body>
	<span><a href="session.jsp">刷新</a></span>
	<span>会话标识：<%=session.getId() %></span>
	<span>服务标识：<%=System.getProperty("paas.instId") %></span>
	<span>服务地址：<%=System.getProperty("paas.ip") %></span>
	<span>服务端口：<%=System.getProperty("paas.httpPort") %></span>
	<span>应用容器：<%=System.getProperty("paas.srvType") %></span>
	<br/>
	<h1>写入Session</h1>
	<br/>
	<form action="session.jsp">
		<input type="hidden" name="action" value="write" />
		<input type="text" name="key" value="time" />
		<input type="text" name="value" value="<%=System.currentTimeMillis() %>" />
		&nbsp;
		<button type="submit" value="Write" style="width: 20%;">Write</button>
	</form>
	<br/>
	<h1>读取Session</h1>
	<form action="session.jsp">
		<input type="hidden" name="action" value="read" />
		<input type="text" name="key" value="time"/>
		<button type="submit" value="Write">Read</button><br/>
		<span style=" color: red;">
		<% if ("read".equalsIgnoreCase(action)) { %>
		<%=key %> = <%=v %>
		<% } %>
		</span>
		&nbsp;
	</form>
	
</body>
</html>
