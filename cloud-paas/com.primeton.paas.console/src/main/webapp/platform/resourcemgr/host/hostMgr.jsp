<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:65%">         
    <fieldset style="border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>查询条件</legend>
        <div id="selForm" style="padding:5px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:80px;" align="right">主机IP：</td>
                    <td style="width:150px;"><input id="ip" name="ip" class="nui-textbox" onenter="onKeyEnter" style="width:150px;"/></td>
                    <td style="width:80px;" align="right">服务类型：</td>
                    <td style="width:150px;">
                   		<input id="type" name="type" class="nui-combobox" url="<%=request.getContextPath() %>/srv/hostMgr/getServiceTypes" valueField="value" textField="value" emptyText="全部" nullItemText="全部" showNullItem="true"  style="width:150px;"/>
                    </td>
                </tr>
                <tr>
                	<td colspan="6" align="center">
                		<a class="nui-button" style="width:60px; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    <br/>
   <div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a class="nui-button" iconCls="icon-add" onclick="applyHost()">新增主机</a>
                        <a class="nui-button" iconCls="icon-collapse" onclick="servicesMgr()">服务管理</a>
                        <!-- 
                        <a class="nui-button" iconCls="icon-upgrade-host" onclick="upgradeHost()">配置升级</a>
                         -->
                        <a class="nui-button" iconCls="icon-no" onclick="releaseHosts()">释放</a>    
                        <a class="nui-button" iconCls="icon-remove" onclick="removeHost()">销毁</a>       
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/hostMgr/list" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="ip" width="80"    headerAlign="center" align ="center" allowSort="true"  renderer="onActionRenderer">IP</div>    
            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">HOSTNAME</div>    
			<div field="types" width="200" headerAlign="center" align ="center" allowSort="true">类型</div>    
			<div field="packageId" width="80" headerAlign="center" align ="center" allowSort="true">套餐标识</div>    
			<div field="exts.packageName" width="150" headerAlign="center" align ="center" allowSort="true">套餐名称</div>
			<div field="standalone" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onHostStandaloneRenderer">独占</div>    
			<div field="controlable" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onHostControlableRenderer">状态</div>                     
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
         
	    var grid = nui.get("datagrid1");
        grid.load();
    
        function selForm() {
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
        function onKeyEnter(e) {
    		selForm();
    	}
        
        function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var ip = record.ip
            return '<a href="javascript:showServices(\'' + ip + '\')">' + ip + '</a> ';
        }
        

        function applyHost() {
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/host/applyHost.jsp",
                title: "新增主机", width: 600, height: 240,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details" };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
        
        function servicesMgr() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var ip = row.ip;
            var name = row.name;
            var types = row.types;
        	
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/host/servicesMgr.jsp?ip=" + ip,
                title: "服务管理", width: 600, height: 350,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details", ip:ip , name:name , types:types };
                    iframe.contentWindow.SetData(data);
                    
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
      
        function showServices() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var ip = row.ip;
            var name = row.name;
            var types = row.types;
        	
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/host/showServices.jsp?ip=" + ip,
                title: "主机服务详情", width: 880, height: 675,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details", ip:ip, name:name, types:types };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
  
        function releaseHosts() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中1行记录!');
            	return;
            }
            if (!rows[0].controlable) {
            	nui.alert('该主机是离线状态，不能释放!');
            	return;
            }
            var rlist = rows[0].ip;
            for (i= 1 ; i< rows.length ;i++) {
            	rlist += ',' + rows[i].ip;
            }
            
            if (!confirm("确定释放该主机 ?")) {
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在释放，请稍后...'
            });
            
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/hostMgr/releaseHosts",
                contentType: "application/json; charset=utf-8",
                data: rlist,
                success: function (text) {
                	if (text) {
                		nui.alert('释放主机请求已发送!');
                		grid.load();
                	} else {
                		nui.alert('释放主机请求发送失败，请检测是否存在服务实例，稍后重试!');
                	}
                	nui.unmask(document.body);
                } 
            });
        }        
        
        function removeHost() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中1行记录!');
            	return;
            }
            var rlist = rows[0].ip;
            for(i= 1 ; i< rows.length ;i++){
            	rlist += ',' + rows[i].ip;
            }
            
            if (!confirm("确定销毁该主机 ?")) {
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在销毁，请稍后...'
            });
            
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/hostPoolMgr/delHosts",
                contentType: "application/json; charset=utf-8",
                data: rlist,
                success: function (text) {
                	if (text) {
                		nui.alert('销毁成功!');
                		grid.load();
                	} else {
                		nui.alert('销毁失败!');
                	}
                	nui.unmask(document.body);
                } 
            });
        }
        
        function upgradeHost() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var ip = row.ip;
            var name = row.name;
            var types = row.types;
            var packageId = row.packageId;
            var model = row.exts.model;
            var packageName = row.packageName;
        	
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/host/upgradeHost.jsp?ip="+ip,
                title: "主机配置升级", width: 350, height: 300,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details", ip:ip , name:name , types:types ,packageId:packageId ,model:model,packageName:packageName};
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                  //  grid.reload();
                }
            });
        }
        
    </script>
</body>
</html>