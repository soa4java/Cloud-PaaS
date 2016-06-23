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
                        <a class="nui-button"  onclick="startApp()"  iconCls="icon-ok" >启动</a>
                        <a class="nui-button"  onclick="stopApp()"  iconCls="icon-no">停止</a>
                        <a class="nui-button"  onclick="restartApp()"  iconCls="icon-reload">重启</a>
                        <a class="nui-button"  onclick="uninstallApp()"  iconCls="icon-cancel">卸载</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appControl/listApp" multiSelect="false	" onlyCheckSelection="true" showPager="false" pageSize="20" onloaderror="onLoadErrorRenderer">
        <div property="columns">   
            <div type="checkcolumn" ></div>    
            <div field="name" width="100" headerAlign="center" align ="center" allowSort="true" >应用标识</div>    
            <div field="displayName" width="100"  headerAlign="center" align ="center" allowSort="true">显示名称</div>    
            <div field="secondaryDomain" width="200"  headerAlign="center" align ="center" allowSort="true" >域名、内网地址</div> 
            <div field="attributes.protocal" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">访问方式</div>       
			<div field="attributes.state" width="80" headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态</div>    
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
            var secondaryDomain = record.secondaryDomain;
            var protocal = record.attributes.protocal;
            var s1 = '<a href="http://'+ secondaryDomain+ '" target="_blank">HTTP</a>';
            var s2 = '<a href="https://'+ secondaryDomain+ '" target="_blank">HTTPS</a>';
            var s = '';
            if (protocal == 'HTTP') {
            	s = s1;
            } else if (protocal == 'HTTPS') {
            	s = s2;
            } else if (protocal == 'MUTUAL-HTTPS') {
            	s = s2;
            } else if (protocal == 'MUTUAL-HTTP-HTTPS') {
            	s = s1+'&nbsp;&nbsp;,&nbsp;&nbsp;'+ s2;
            } else {
            	s = s1;
            }
            return s;
        }
        
        function startApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.state == '1') {
            	nui.alert('此应用已经是启动状态!');
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在启动，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appControl/startApp/" + row.name ,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('启动成功!');
                		grid.load();
                	} else {
                		nui.alert('启动失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }
        
        function stopApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.state == '0') {
            	nui.alert('此应用已经是停止状态!');
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在停止，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appControl/stopApp/" + row.name ,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('停止成功!');
                		grid.load();
                	} else {
                		nui.alert('停止失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }        
        
        function restartApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.state == '0') {
            	nui.alert('此应用是停止状态!');
            	return;
            }
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在重启，请稍后...'
            });
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appControl/restartApp/" + row.name ,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('重启成功!');
                		setTimeout(function () {
                            nui.unmask(document.body);
                        }, 2000);
                		grid.load();
                	} else {
                		nui.alert('重启失败!');
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
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在卸载，请稍后...'
            });
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appControl/uninstallApp/" + row.name ,
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
    </script>
</body>
</html>