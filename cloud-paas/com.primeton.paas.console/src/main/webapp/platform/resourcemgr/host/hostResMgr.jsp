<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:97%">
	<div id="tabs1" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;" plain="false" >
	    <div title="主机管理">
			<iframe name="HostFrame" id="HostFrame" src="<%=request.getContextPath() %>/platform/resourcemgr/host/hostMgr.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="主机池管理">
			<iframe name="HostPoolFrame" id="HostPoolFrame" src="<%=request.getContextPath() %>/platform/resourcemgr/host/hostPoolMgr.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	</div>
    
    <script type="text/javascript">
	    nui.parse();
    </script>
</body>
</html>