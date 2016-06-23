<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:95%">
	<!-- Service Table Pages -->
	<div id="tabs1" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;" plain="false" TabPosition ="left">
	    <div title="MySQL服务" >
			<iframe name="DbServiceFrame" id="DBFrame" src="<%=request.getContextPath() %>/app/open/service/dbService.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="负载均衡服务">
	    	<iframe name="HaproxyServiceFrame" id="HaproxyFrame" src="<%=request.getContextPath() %>/app/open/service/haproxyService.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="缓存服务">
	       <iframe name="MemcachedServiceFrame" id="MemcachedFrame" src="<%=request.getContextPath() %>/app/open/service/memcachedService.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="Redis服务">
	       <iframe name="RedisServiceFrame" id="RedisFrame" src="<%=request.getContextPath() %>/app/open/service/myRedisClusters.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="消息服务">
	       <iframe name="MsgQueueServiceFrame" id="MsgQueueFrame" src="<%=request.getContextPath() %>/app/open/service/myMsgQueueClusters.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	</div>
</body>
</html>