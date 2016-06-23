<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    
</head>
<body style="height:90%">         
    <div id="appgrid" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
        url="<%=request.getContextPath() %>/srv/appLog/listApp"  idField="id" 
       >
        <div property="columns">            
            <div field="name" width="100" align ="center" headerAlign="center" >应用标识</div>                                        
            <div field="displayName" width="100" align ="center" headerAlign="center" >应用名称</div>
            <div field="secondaryDomain" width="100"  headerAlign="center" align ="center" >域名</div>
            <div name="action" width="100"  headerAlign="center" align ="center" renderer="onActionRenderer" >查看下载</div>
        </div>
    </div> 

    <script type="text/javascript">
        nui.parse();

        var appgrid = nui.get("appgrid");
        appgrid.load();
        
        function onActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var appName = record.name;
        	var s = '<a href="<%=request.getContextPath() %>/srv/appLog/multidownload/'+appName+'.null'+'">全部下载</a>'+' || '+
        			'<a class="Edit_Button" href="javascript:downLoadLog(\'' + appName + '\')">分类下载</a> ';
        	return s;
        }
        
        function downLoadLog(appName) {
        	//check 是否只有user一种
	        	nui.open({
	                url: bootPATH + "../../app/monitor/logs/logList.jsp",
	                title: "日志下载", width: 800, height: 250,
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