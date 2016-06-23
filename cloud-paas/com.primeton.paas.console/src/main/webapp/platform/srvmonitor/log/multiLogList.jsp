<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <style type="text/css">
	    .asLabel .nui-textbox-border,
	    .asLabel .nui-textbox-input,
	    .asLabel .nui-buttonedit-border,
	    .asLabel .nui-buttonedit-input,
	    .asLabel .nui-textboxlist-border
	    {
	        border:0;background:none;cursor:default;
	    }
	    .asLabel .nui-buttonedit-button,
	    .asLabel .nui-textboxlist-close
	    {
	        display:none;
	    }
	    .asLabel .nui-textboxlist-item
	    {
	        padding-right:8px;
	    }    
 	</style>
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
	<div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	当前应用：<input id="appName" name="appName" class="nui-textbox asLabel"/>
                    </td>
                    <td style="white-space:nowrap;">
						<a class="nui-button" iconCls="icon-download" onclick="onDownloadAll()">下载全部</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>
	<div id="loggrid" class="nui-datagrid"
		style="width: 100%; height: auto;" showPager="false" pageSize="10" pager="#pager1"	url="<%=request.getContextPath() %>/srv/log/getAllLogs" showColumns="true" showHGridLines="true" showVGridLines="true">
		<div property="columns">
			<div field="appName" width="100" headerAlign="center" align="center" allowSort="true" visible="false">应用名称</div>
			<div field="logType" width="80" headerAlign="center" align="center"	renderer="onTypeRenderer">日志类型</div>
			<div id="downloadLink" name="downloadLink" width="80" allowSort="true" renderer="onActionRenderer" align="center" headerAlign="center">下载</div>
		</div>
	</div>

	<script type="text/javascript">
		nui.parse();

		var loggrid = nui.get("loggrid");
		var appName;
		
		function SetData(data) {
			data = nui.clone(data);
			appName = data.appName;
			nui.get("appName").setValue(appName);
			loggrid.load({appName : appName});
		}
		
		function onActionRenderer(e) {
			var grid = e.sender;
			var record = e.record;
			var logType = record.logType;
			var appName = record.appName;
			var s = '<a href="<%=request.getContextPath() %>/srv/log/download/'
				+ appName + '.' + logType + '">download</a>';
        	return s;
        }
        
        function onTypeRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
            var logType = record.logType;
            if (logType == "user") {
            	return logType + "(system default)";
            }
            if (logType == "system") {
            	return logType + "(system default)";
            }
            return logType;
        }

        function onDownloadAll() {
        	window.location = "<%=request.getContextPath() %>/srv/log/download/" + appName + ".null";
        }
    </script>
</body>
</html>