<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
</head>
<body>
	<div style="font-weight: bold;">
		<span>连接Redis哨兵服务,并获取某个Redis集群的Master服务器;</span><br/>
		<span>(1) 获取Redis集群别名AliasName:从PAAS管理控制台或面向个人的业务门户获取;</span><br/>
		<span style="color: blue;">业务门户Redis信息截图,如下所示:</span><br/>
		<br/> <img src="<%=request.getContextPath() %>/platform/srvmgr/sentinel/app.png" alt="image" width="450" height="250" /> <br/>
		
		<span style="color: blue;">管理控制台Redis信息截图,如下所示:</span><br/>
		<br/> <img src="<%=request.getContextPath() %>/platform/srvmgr/sentinel/platform.png" alt="image" width="450" height="182" /> <br/>
		<span>(2) 命令:<span style="color: blue;">SENTINEL get-master-addr-by-name ${AliasName}</span> (用于测试), 程序请使用客户端API获取</span><br/>
		<span>(3) 更多信息请参考Redis官方文档  <a href="http://redis.io/topics/sentinel" target="_blank">http://redis.io/topics/sentinel</a></span>
	</div>	
    <script type="text/javascript">        
        nui.parse();
    </script>
</body>
</html>