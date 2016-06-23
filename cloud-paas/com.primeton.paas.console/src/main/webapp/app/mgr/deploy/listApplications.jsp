<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:90%">           
   <div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a class="nui-button" iconCls="icon-add" onclick="deployApp()">部署应用</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appDeploy/listApp" showPager="false" pageSize="20" onloaderror="onLoadErrorRenderer">
        <div property="columns">   
            <div field="name" width="100" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">名称</div>    
            <div field="secondaryDomain" width="200"  headerAlign="center" align ="center" allowSort="true" visible="false">二级域名</div>    
            <div field="displayName" width="200"  headerAlign="center" align ="center" allowSort="true">显示名称</div>    
			<div field="attributes.deployVersion" width="80" headerAlign="center" align ="center" allowSort="true">部署版本</div>    
			<div field="attributes.versionNum" width="50" headerAlign="center" align ="center" allowSort="true">版本数</div>    
			<div field="attributes.latestVersion" width="50" headerAlign="center" align ="center" allowSort="true">最新版本</div>    
        </div>
    </div>

    <script type="text/javascript">
    	// parse
        nui.parse();
         
	    var grid = nui.get("datagrid1");
        grid.load();

        function deployApp(){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/mgr/deploy/deployApp.jsp",
	            title: "部署平台应用", width: 800, height: 380,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details"};
	                iframe.contentWindow.SetData(data);
	                
	            },
	            ondestroy: function (action) {
	            	grid.reload();
	                
	            }
	        });
        }
        
        function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var name = record.name;
            var displayName = record.displayName;
            var domain = record.secondaryDomain;
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + name + '\',\'' + displayName + '\',\'' + domain + '\')">'+name+'</a> ';
            return s;
        }
        
        function detailsRow(name, displayName, domain) {
        	if (null != name) {
                nui.open({
                    url: "<%=request.getContextPath() %>/app/mgr/deploy/listAppVersions.jsp",
                    title: "应用信息", width: 700, height: 450,
                    onload: function () {
                        var iframe = this.getIFrameEl();
                        var data = { action: "details", name : name , displayName:displayName , domain:domain };
                        iframe.contentWindow.SetData(data);
                    },
                    ondestroy: function (action) {
                        grid.reload();
                        
                    }
                });
        	}
        }
    </script>
    
</body>
</html>