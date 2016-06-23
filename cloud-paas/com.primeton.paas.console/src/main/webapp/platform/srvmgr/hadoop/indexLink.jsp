<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>

<% 
String url = SystemVariable.getHadoopManagementProtal();
if (null == url) {
	url = "#";
} else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
	url = "http://" + url;
} else if (url.charAt(url.length() - 1) == '/') {
	url = url.substring(0, url.length() - 1);
}
String user = SystemVariable.getHadoopManagementUser();
if (null == user) {
	user = "admin";
}
String password = SystemVariable.getHadoopManagementPassword();
if (null == password) {
	password = "admin";
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
</head>
<body>         
	<form id="loginForm" action="<%=url %>/j_spring_security_check" method="post">
		<input name="j_username" value="<%=user %>" type="hidden"/>
		<input name="j_password" value="<%=password %>" type="hidden"/>
	</form>
	<script type="text/javascript">
		document.getElementById("loginForm").submit();
	</script>
</body>
</html>