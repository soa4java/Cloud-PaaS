<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="java.util.Date"%>
<%@page import="com.primeton.paas.manage.api.impl.util.SystemVariables"%>
<%@page import="org.gocom.cloud.cesium.manage.runtime.api.model.NodeAgentRelease"%>
<%@page import="org.gocom.cloud.cesium.manage.runtime.api.client.NodeAgentReleaseManagerFactory"%>

<%
	String type = request.getParameter("type");
	String version = request.getParameter("version");
	String downloadAddress = request.getParameter("downloadAddress");
	String upgradeStrategy = request.getParameter("upgradeStrategy");
	String changes = request.getParameter("changes");
	String description = request.getParameter("description");

	String message = "";

	if (null != version) {
		NodeAgentRelease newRelease = new NodeAgentRelease();
		newRelease.setChanges(changes);
		newRelease.setDescription(description);
		newRelease.setDownloadAddress(downloadAddress);
		newRelease.setReleaseDate(new Date());
		newRelease.setVersion(version);
		newRelease.setUpgradeStrategy(upgradeStrategy);
		System.out.println(newRelease);
		NodeAgentReleaseManagerFactory.getManager().publish(newRelease,	false);
		message = "Publish " + newRelease.toString() + " success.";
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upgrade NodeAgent</title>
</head>
<body>
	<br/>
	<div style="color: red;">
		<%=message %>
	</div>
	<br/>
	<form action="upgrade.jsp">
		<table width="90%">
			<tr>
				<td colspan="2" align="center">
					<span style="font-size: 200%; color: blue; font-weight: bold;">
						Publish NodeAgent Version </span>
				</td>
			</tr>
			<tr>
				<td width="20%"></td>
				<td width="80%"></td>
			</tr>
			<tr>
				<td align="right">Type:</td>
				<td>
					<select name="type" style="width: 200px;">
						<option value="Java">Java</option>
						<option value="NodeJs">NodeJs</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">Version:</td>
				<td>
					<input name="version" value="201508101510" style="width: 200px;" />
				</td>
			</tr>
			<tr>
				<td align="right">Download Address:</td>
				<td>
					<input name="downloadAddress" value="<%=SystemVariables.getHttpRpository() + "/agent-java/201508101510/agent-java.zip" %>" style="width: 100%" />
				</td>
			</tr>
			<tr>
				<td align="right">Upgrade Strategy:</td>
				<td>
					<select name="upgradeStrategy" style="width: 200px;">
						<option value="force">Force Upgrade</option>
						<option value="idle">Idle Upgrade</option>
						<option value="none">Not Upgrade</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">Changes:</td>
				<td>
					<textarea name="changes" rows="5" cols="100" style="width: 100%">New version features and changes.</textarea>
				</td>
			</tr>
			<tr>
				<td align="right">Description:</td>
				<td>
					<textarea name="description" rows="5" cols="100" style="width: 100%">New version description.</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" height="100px;">
					<input type="submit" value="Submit" style="font-size: 150%; width: 300px; color: blue;" />
				</td>
			</tr>
		</table>
		
	</form>
</body>
</html>