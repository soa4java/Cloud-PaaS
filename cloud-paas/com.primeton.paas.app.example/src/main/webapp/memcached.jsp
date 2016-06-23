<%@page pageEncoding="UTF-8" %>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.primeton.paas.app.example.Foo"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.primeton.paas.app.example.ExampleUtils"%>
<%
String key = (String)request.getParameter("key");
String value = (String)request.getParameter("value");
String expireTime = (String)request.getParameter("expireTime");
int time = 0;
try {
	time = Integer.parseInt(expireTime);
} catch (Exception e) {
}

if (key != null) {
	ExampleUtils.putCache(key, value, time);
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
<script type="text/javascript">
function getValue() {
	var key = document.getElementById("key").value;
	$.ajax({
	    url: "handler.jsp?cacheKey=" + key,
	    type: "get",
	    success: function (text) {
	    	document.getElementById("value").value = text;
	    	nui.alert(text);
	    }
	});
}
</script>
</head>
<body>
	<h1>If you has memcached service and bind this application.</h1>
	<br/>
	<br/>
	<form action="memcached.jsp">
		Key : <input type="text" name="key" value="PAAS_VENDOR" />&nbsp;
		Value : <input type="text" name="value" value="Primeton Software" />&nbsp;
		ExpireTime : <select name="expireTime">
			<option value="-1">-1</option>
			<option value="10">10</option>
			<option value="30">30</option>
			<option value="50">50</option>
			<option value="100">100</option>
			<option value="200">200</option>
		</select>
		<input type="submit" name="submit" value="Put" />
	</form>
	<br/>
	<br/>
	<form action="memcached.jsp">
		<input type="text" name="key" value="PAAS_VENDOR" id="key" />&nbsp;
		<input type="text" name="value" value="" readonly="readonly" id="value" />&nbsp;
		<input type="button" value="Get" onclick="getValue();" />
	</form>
</body>
</html>
