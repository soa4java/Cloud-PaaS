<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:90%">
	<p>
    	选择目标应用:
    	<input id="selAppList" name="selAppList" class="nui-combobox" url="<%=request.getContextPath() %>/srv/appSetting/listApp" valueField="name" textField="name" valueFromSelect="true" onvaluechanged="selValueChanged()"/>
    </p>
	<div id="tabs1" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;" plain="false" TabPosition ="left">
	    <div title="数据源" >
			<iframe name="DataSourceFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="DAS" >
			<iframe name="DASFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="HTTP接入" >
			<iframe name="HttpAccessFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="定时器" >
			<iframe name="TimerFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="缓存" >
			<iframe name="CacheFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="业务字典" >
			<iframe name="BizDictFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="服务注册" >
			<iframe name="ServiceFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="邮件" >
			<iframe name="EmailFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="系统变量" >
			<iframe name="SystemVariableFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="服务变量" >
			<iframe name="ServiceVariableFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="HTTP安全控制" >
			<iframe name="HttpSecurityFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	    <div title="JMX安全控制" >
			<iframe name="JmxSecurityFrame" src="<%=request.getContextPath() %>/app/mgr/setting/test.jsp" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	    </div>
	</div>
</body>
</html>