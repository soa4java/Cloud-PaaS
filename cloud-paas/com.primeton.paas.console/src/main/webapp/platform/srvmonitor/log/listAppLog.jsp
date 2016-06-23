<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:80%">         
	<fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                	<td style="width:10%" align="right">应用标识:</td>
                    <td style="width:20%;"><input id="name" name="name" class="nui-textbox" onenter="onKeyEnter"/></td>
                	<td style="width:10%" align="right">应用显示名称:</td>
                    <td style="width:20%;"><input id="displayName" name="displayName" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">所有者:</td>
                    <td style="width:20%;"><input id="owner" name="owner" class="nui-textbox" onenter="onKeyEnter"/></td>
                </tr>
                <tr>
                	<td colspan="8" align="center">
                		<a class="nui-button" style="width:8%; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    <br />
    <div id="appgrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
        url="<%=request.getContextPath() %>/srv/log/innerAppList"  idField="id"
          onshowrowdetail="onShowRowDetail">
        <div property="columns">            
            <div field="name" width="100%" align ="center" headerAlign="center" >应用标识</div>                                        
            <div field="displayName" width="200" align ="center" headerAlign="center" >应用显示名称</div>
            <div field="secondaryDomain" width="250"  headerAlign="center" align ="center" >域名</div>
            <div field="owner" width="100" headerAlign="center" align ="center">所有者</div>          
            <div name="action1" width="300" headerAlign="center" align ="center"  renderer="onDownloadAllRenderer">下载</div>          
			<!--             
			<div name="action2" width="100" headerAlign="center" align ="center"  renderer="onDownloadRenderer">分类下载</div>           
			<div type="expandcolumn" >#下载</div>            
			-->
        </div>
    </div> 

    <div id="logForm" style="display:none;margin-left:0px;">
        <div id="loggrid" class="nui-datagrid" style="width:100%;height:auto;"  showPager="false" pageSize="10" pager="#pager1"
            url="<%=request.getContextPath() %>/srv/log/getAllLogs"   
        >
            <div property="columns">
                <div field="appName" width="100"  headerAlign="center" align ="center" allowSort="true" visible="false">应用名称</div>  
                <div field="logType" width="80" headerAlign="center" align ="center" renderer="onTypeRenderer">日志类型 </div>        
                <div id="downloadLink" name="downloadLink" width="80" allowSort="true" renderer="onActionRenderer" align="center" headerAlign="center">下载
                </div>            
            </div>
        </div>    
    </div>

    <script type="text/javascript">
        nui.parse();

        var appgrid = nui.get("appgrid");
		// var loggrid = nui.get("loggrid");
		// var logForm = document.getElementById("logForm");
        appgrid.load();
        
        function selForm() {
            var form = new nui.Form("#selForm");            
            var data = form.getData();    
            var json = nui.encode(data);   
            appgrid.load({keyData:json});
        }
        
		function onKeyEnter(e) {
    		selForm();
    	}
        
        function onActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var logType = record.logType;
            var appName = record.appName;
        	var s = '<a class="Edit_Button" href="<%=request.getContextPath() %>/srv/log/download/' + appName+'.'+logType+'">下载</a>';
        	return s;
        }
        
        function onTypeRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
            var logType = record.logType;
            if (logType == "user" || logType == "system") {
            	return logType + "(system default)";
            } else {
            	return logType;
            }
        }
        
        function onDownloadAllRenderer(e) {
        	var grid = e.sender;
			var row = e.record;
			var rowIndex = e.rowIndex;
			var appName = row.name;
            var s = '<a class="Edit_Button" href="javascript:doOnShowRowDetail(\'' + appName + '\')">用户日志下载</a>'+' || '+
					'<a href="<%=request.getContextPath() %>/srv/log/download/'+appName+'.system'+'">系统日志下载</a>';
            return s;
        }
        
        function onDownloadRenderer(e) {
        	var grid = e.sender;
			var row = e.record;
			var rowIndex = e.rowIndex;
			var appName = row.name;
            var s = '<a class="Edit_Button" href="javascript:doOnShowRowDetail(\'' + appName + '\')">分类下载</a>';
            return s;
        }
        
        function doOnShowRowDetail(appName) {
        	nui.open({
                url: bootPATH + "../../platform/srvmonitor/log/multiLogList.jsp",
                title: "日志分类下载", width: 800, height: 350,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { appName:appName };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                }
            });	        	
        }
        
    </script>
</body>
</html>