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
</head>
<body>     
   <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >应用基本信息</legend>
        <div style="padding:5px;">
	        <table>
	            <tr>
	                <td style="width:100px;">应用名称</td>
	                <td style="width:300px;">    
	                	<input id="name" name="name"  class="nui-textbox asLabel" style="width:200px;" />   
	                </td>
	            </tr>
	            <tr>
	                <td style="width:100px;">显示名称</td>
	                <td style="width:300px;">    
	                	<input id="displayName" name="displayName"  class="nui-textbox asLabel" style="width:200px;" />   
	                </td>
	            </tr>
	    	    <tr>
	                <td style="width:100px;">访问路径</td>
	                <td style="width:300px;">    
	                	<span id="domain"></span>
	                </td>
	            </tr>
	        </table>            
        </div>
   </fieldset>
   <br>
      
   <div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a class="nui-button" iconCls="icon-add" onclick="deployApp()">部署</a>
                        <a class="nui-button" iconCls="icon-add" onclick="uninstallApp()">卸载</a>
                        <a class="nui-button" iconCls="icon-add" onclick="deleteApp()">删除</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:220px;" allowResize="true"
         idField="id" multiSelect="true"  showPager="false" pageSize="20" onloaderror="onLoadErrorRenderer">
        <div property="columns">   
        	<div type="checkcolumn"></div> 
            <div field="id" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">标识</div>    
            <div field="warVersion" width="50"  headerAlign="center" align ="center" allowSort="true">版本号</div>    
			<div field="createdDate" width="100" headerAlign="center" align ="center" allowSort="true" renderer="onDateRenderer">创建时间</div>    
			<div field="submitTime" width="100" headerAlign="center" align ="center" allowSort="true" renderer="onDateRenderer">更新时间</div>    
			<div field="attributes.description" width="100" headerAlign="center" align ="center" allowSort="true">备注</div>    
			<div field="attributes.isDeployVersion" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onDeployVersionRenderer">部署状态</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
	
	    var grid = nui.get("datagrid1");

        function SetData(data) {
            if (data.action == "details") {
                //跨页面传递的数据对象，克隆后才可以安全使用
                nui.get("name").setValue(data.name);
                nui.get("displayName").setValue(data.displayName);
                // nui.get("domain").setValue(data.domain);
                var s = '<a href="http://'+ data.domain+ '" target="_blank">'+data.domain+'</a>';
                document.getElementById("domain").innerHTML = s;
                nui.get("datagrid1").setUrl("<%=request.getContextPath() %>/srv/appDeploy/listAppVersion/" + data.name);
                grid.load();
            }
        }
        
        function deployApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.isDeployVersion == 'true') {
            	nui.alert('此版本已经是部署状态!');
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在部署，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appDeploy/deploy/" + row.name + "-" + row.warVersion,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.deployStatus == 'S') {
                		nui.alert('部署成功!');
                		grid.load();
                	} else {
                		nui.alert('部署失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }
        
        function uninstallApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.isDeployVersion == 'false') {
            	nui.alert('此版本已经是未部署状态!');
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在卸载，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appDeploy/uninstall/" + row.name + "-" + row.warVersion,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('卸载成功!');
                		grid.load();
                	} else {
                		nui.alert('卸载失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }
        
        function deleteApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var msg = '该操作将清除"' + row.name +'"应用"' + row.warVersion + '"版本的应用包，如果该版本已经部署，将先卸载，后删除。此操作不会清除应用的数据和服务，确定要删除吗？';
            var ok = confirm(msg);
            if (ok) {
            	 nui.mask({
                     el: document.body,
                     cls: 'mini-mask-loading',
                     html: '正在删除，请稍后...'
                 });
                 
                 $.ajax({
                     url: "<%=request.getContextPath() %>/srv/appDeploy/delete/" + row.name + "-" + row.warVersion,
                     cache: false,
                     success: function (text) {
                     	var o = nui.decode(text);
                     	if (o) {
                     		nui.alert('删除成功!');
                     		grid.load();
                     	} else {
                     		nui.alert('删除失败!');
                     	}
                     	nui.unmask(document.body);
     	            }
     	        });
            	
            }
        }
        
        function loading() {
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '加载中...'
            });
            setTimeout(function () {
                nui.unmask(document.body);
            }, 2000);
        }
        
    </script>
</body>
</html>