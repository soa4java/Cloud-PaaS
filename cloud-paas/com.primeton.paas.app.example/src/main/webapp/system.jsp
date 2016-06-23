<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Properties"%>
<% 
Properties system = System.getProperties();
%>
<html >
	<head>
    	<title>Primeton PAAS Cloud Integrated Development Platform</title>
    	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    	<meta http-equiv="Pragma" content="no-cache">
    	<meta http-equiv="Cache-Control" content="no-cache,no-store">
	</head>
	<body>
		<h1>JVM environment</h1>
		<table style="border-style: dashed; border-color: orange; color: blue; border-width: 1px; width: 99%;">
			<thead>
				<tr>
					<th>Key</th>
					<th>Value</th>
				</tr>
			</thead>
			<tbody>
			<% int i=0; %>
			<% for (Object key : system.keySet()) { %>
				<% if (i++%2 == 0) { %>
				<tr style="color: red;">
				<% } else { %>
				<tr>
				<% } %>
					<td><%=key %></td>
					<td><%=system.get(key) %></td>
				</tr>
			<% } %>
			</tbody>
		</table>
	</body>
</html>
