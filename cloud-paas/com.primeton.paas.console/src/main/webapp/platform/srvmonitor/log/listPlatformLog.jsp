<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    
</head>
<body>         
	<br />
	<div id="loggrid" class="nui-datagrid" style="width:100%;height:auto;"  showPager="false" pageSize="10" pager="#pager1" >
            <div property="columns" >
                <div field="type" width="80" headerAlign="center" align ="center" visible="false">日志类型 </div>
                <div field="displayname" width="80" headerAlign="center" align ="center" >日志类型 </div>
                <div id="downloadLink" name="downloadLink" width="80" allowSort="true" renderer="onActionRenderer" align="center" headerAlign="center">下载
                </div>            
            </div>
    </div>    

    <script type="text/javascript">
        nui.parse();
        
		var grid = nui.get("loggrid");
		
		init();
		
		function init() {
			var rows = [{'type':'console-platform', 'displayname':'运营管理平台'}, {'type':'console-app', 'displayname':'自助服务平台'}];  
			grid.addRows(rows,0)
		}
		
		function onActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var type = record.type;
            
			//var s = '<a class="Edit_Button" href="javascript:download(\'' + type +'\''+','+'\''+'manage'+ '\')">'+'Manager 日志</a> ' + '||'
			//	+ ' <a class="Delete_Button" href="javascript:stopService(\'' + type +'\''+','+'\''+'cesium'+ '\')">Cesium 日志</a> '+ '||'
			//	+ ' <a class="New_Button" href="javascript:configCardBinService(\'' + type +'\''+','+'\''+'console'+ '\')">Console 日志</a> ';
            var s = '<a class="Edit_Button" href="<%=request.getContextPath() %>/srv/log/downloadConsoleLog/'+type+'.'+'manage'+'">Manager 日志</a>' + '||'
            		+ ' <a class="Edit_Button" href="<%=request.getContextPath() %>/srv/log/downloadConsoleLog/'+type+'.'+'cesium'+'">Cesium 日志</a> '+ '||'
            		+ ' <a class="Edit_Button" href="<%=request.getContextPath() %>/srv/log/downloadConsoleLog/'+type+'.'+'console'+'">Console 日志</a> ';
            return s;
		}
		
		
		function download(consoleType, logType) {
			nui.alert("consoleType:" + consoleType + ", logType:" + logType);
		}
    </script>
    
</body>
</html>