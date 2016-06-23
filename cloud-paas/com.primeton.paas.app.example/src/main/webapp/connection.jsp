<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.primeton.paas.app.example.Foo"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.primeton.paas.app.example.ExampleUtils"%>
<%
String id = (String)request.getParameter("id");
String name = (String)request.getParameter("name");
if (id != null) {
	ExampleUtils.addFoo(new Foo(id, name));
}

id = (String)request.getParameter("removeId");
ExampleUtils.removeFoo(id);

List<Foo> foos = ExampleUtils.getFoos();
foos = null == foos ? new ArrayList<Foo>() : foos;
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

</script>
</head>
<body>
	<h1>Please configure your app datasource before insert data.</h1>
	<br/>
	<table style="border-style: dashed; width: 90%;">
			<thead>
				<tr>
					<th>ID</th>
					<th>NAME</th>
				</tr>
			</thead>
			<tbody>
			<% int i=0; %>
			<% for (Foo foo : foos) { %>
				<% if (i++%2 == 0) { %>
				<tr style="color: red;">
				<% } else { %>
				<tr>
				<% } %>
					<td><%=foo.getId() %></td>
					<td><%=foo.getName() %></td>
				</tr>
			<% } %>
			</tbody>
	</table>
	<br/>
	<form action="connection.jsp">
		<input type="text" name="id" value="<%=System.currentTimeMillis() %>" />&nbsp;
		<input type="text" name="name" value="<%=new Date().toString() %>" />&nbsp;
		<input type="submit" name="submit" value="submit" />
	</form>
</body>
</html>
