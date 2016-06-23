<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <style type="text/css">
    	html, body{
        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    	}
    </style>
</head>
<body>
	<div id="tabs1" class="nui-tabs" activeIndex="0" tabPosition="top" style="width:100%;height:520px;" plain="true">
	    <div title="平台日志">
			<iframe name="PlatformLog" id="PlatformLog" src="<%=request.getContextPath() %>/platform/srvmonitor/log/listPlatformLog.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="应用日志">
			<iframe name="AppLog" id="AppLog" src="<%=request.getContextPath() %>/platform/srvmonitor/log/listAppLog.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	</div>
    

    <script type="text/javascript">
	    nui.parse();
    </script>
</body>
</html>