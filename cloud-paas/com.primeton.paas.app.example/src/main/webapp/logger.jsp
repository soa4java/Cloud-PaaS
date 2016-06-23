<%@page pageEncoding="UTF-8"%>
<%@page import="com.primeton.paas.app.example.ExampleUtils"%>
<% 
String message = (String)request.getParameter("message");
String level = (String)request.getParameter("level");
level = (null == level) ? "info" : level;
String result = "";
if (null != message) {
	ExampleUtils.log(level, message);
	result = "Write " + message + " success.";
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
	<h1>Write log</h1>
	<br/>
	<span style="color: blue;"><%=result %></span>
	<br/>
	<form action="logger.jsp">
		<input type="text" name="message" value="<%=System.currentTimeMillis() %>" style="width: 60%;"/>
		<select name="level">
			<option value="info">info</option>
			<option value="debug">debug</option>
			<option value="warn">warn</option>
			<option value="error">error</option>
		</select> &nbsp;
		<button type="submit" value="Write">Write</button>
	</form>
</body>
</html>
