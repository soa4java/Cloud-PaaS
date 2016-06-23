<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:90%">         
    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/mysqlAdmin/list" showPager="false" pageSize="20" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="indexcolumn">编号</div>       
            <div field="id" width="80"    headerAlign="center" align ="center" allowSort="true" >服务标识</div>  
            <div field="name" width="120"    headerAlign="center" align ="center" allowSort="true" >服务名称</div>   
            <div field="ip" width="100"    headerAlign="center" align ="center" allowSort="true" >主机</div>   
            <div field="port" width="80"    headerAlign="center" align ="center" allowSort="true" >端口</div>   
            <div field="schema" width="80"    headerAlign="center" align ="center" allowSort="true" >数据库名称</div>   
            <div field="user" width="100"    headerAlign="center" align ="center" allowSort="true" >用户</div>   
			<div field="state" width="50"    headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态</div>   
			<div field="attributes.phpMyAdminURL" width="50"  headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">访问</div>   
        </div>
    </div>
	
    <script type="text/javascript">
        nui.parse();
        
	    var grid = nui.get("datagrid1");
        grid.load();
        
        function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var phpMyAdminURL = record.attributes.phpMyAdminURL;
            var s = '<a href="javascript:openPhpMyAdmin(\''+phpMyAdminURL+'\')"><img alt="Login" src="<%=request.getContextPath()%>/images/direction.gif" style="height: 16px;"></a>';
            return s;
        }
        
        function openPhpMyAdmin(url) {
        	window.open(url, "phpMyAdmin", "location=no, toolbar=no, menubar=no, status=no, resizable=yes");
        }
    </script>
    
</body>
</html>