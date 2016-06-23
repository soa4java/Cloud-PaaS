<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
	String appName = request.getParameter("appName");
	String timeInterval = request.getParameter("timeInterval");
	int minutes = Integer.parseInt(request.getParameter("minutes"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<script src="../../../common/amcharts/amcharts.js" type="text/javascript"></script>	
	<script src="../../../common/nui/nui.js" type="text/javascript"></script>
	<style type="text/css">
	    html,body
	    {
	        width:100%;
	        height:100%;
	        border:0;
	        margin:0;
	        padding:0;
	        overflow:visible;
	    }
    </style>
</head>
<body>
	<table width="100%" height="100%" border="0">
		<tr>
			<td width="50%" height="50%">
				<iframe name="CPU" id="CPU" src="" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="true"></iframe>
			</td>
			<td width="50%" height="50%">
				<iframe name="Memory" id="Memory"  src="" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="true"></iframe>
			</td>
		</tr>
		<tr>
			<td width="50%" height="50%">
				<iframe name="OneLoadAverage" id="OneLoadAverage"  src="" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="true"></iframe>
			</td>
			<td width="50%" height="50%">
				<iframe name="IO" id="IO"  src="" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="true"></iframe>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		window.onload = init;
		
		function init() {
			var appName = "<%=appName%>";
			var timeInterval = "<%=timeInterval%>";
			var minutes = "<%=minutes%>";
			var cpuFrame = document.getElementById("CPU");
			var memFrame = document.getElementById("Memory");
			var loadAverageFrame = document.getElementById("OneLoadAverage");
			var ioFrame = document.getElementById("IO");
			cpuFrame.src = "showAppCPUInfo.jsp?appName=" + appName
					+ "&timeInterval=" + timeInterval + "&minutes=" + minutes;
			memFrame.src = "showAppMemoryInfo.jsp?appName=" + appName
					+ "&timeInterval=" + timeInterval + "&minutes=" + minutes;
			loadAverageFrame.src = "showAppOneLoadAverageInfo.jsp?appName="
					+ appName + "&timeInterval=" + timeInterval + "&minutes="
					+ minutes;
			ioFrame.src = "showAppIOInfo.jsp?appName=" + appName
					+ "&timeInterval=" + timeInterval + "&minutes=" + minutes;
		}
	</script>
</body>
</html>