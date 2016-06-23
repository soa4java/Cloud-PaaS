<%@page pageEncoding="UTF-8"%>
<html>
<head>
<title>PAAS Example Application</title>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache,no-store">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/nui/nui.js"></script>
<script type="text/javascript">
	function showWindow(url, title) {
		nui.open({
			url : url,
			title : title,
			onload : function() {
				//
			},
			ondestroy : function(action) {
				//
			}
		});
	}
</script>
</head>
<body>
	<h1>
		Example PAAS Application
	</h1>
	<br/>
	<div style="font-size: 200%">
		<a class="nui-button"
			onclick="showWindow('system.jsp', 'System environment')">System	environment &gt;&gt;</a> 
		<br /> <br /> 
		<a class="nui-button"
			onclick="showWindow('logger.jsp', 'User Logger')">User Logger &gt;&gt;</a>
		<br /> <br /> 
		<a class="nui-button"
			onclick="showWindow('connection.jsp', 'DataSource')">DATABASE CRUD &gt;&gt;</a>
		<br /> <br /> 
		<a class="nui-button"
			onclick="showWindow('memcached.jsp', 'Memcached')">Memcached CRUD &gt;&gt;</a> <br />
		<br /> <br /> 
		<a class="nui-button"
			onclick="showWindow('session.jsp', 'Session')">Share Session &gt;&gt;</a> <br />
	</div>
	
	<div style="color: gray; padding-top: 300px;">Copyright &copy; 2009 - 2015 Primeton . All Rights Reserved.</div>
</body>
</html>
