<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
    <style type="text/css">
	    html,body
	    {
	        width:100%;
	        height:85%;
	        border:0;
	        margin:0;
	        padding:0;
	        overflow:visible;
	    }
    </style>
</head>
<body>         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                	<td style="width:10%" align="right">应用标识:</td>
                    <td style="width:20%;"><input id="name" name="name" class="nui-textbox" onenter="onKeyEnter" /></td>
                	<td style="width:10%" align="right">应用显示名称:</td>
                    <td style="width:20%;"><input id="displayName" name="displayName" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">所有者:</td>
                    <td style="width:20%;"><input id="owner" name="owner" class="nui-textbox" onenter="onKeyEnter"/></td>
                    <td style="width:10%;" align="right">运行状态:</td>
                    <td style="width:20%;"><input id="appStatus" name="appStatus" class="nui-combobox" data="CLD_InstStatus" value="defaultValue"/></td>
                </tr>
                <tr>
                	<td colspan="8" align="center">
                		<a class="nui-button" iconCls="icon-search" style="width:8%; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    <br/>
    <div style="width:99%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<!-- 
                    	<a class="nui-button" iconCls="icon-remove" onclick="remove()">删除</a>
                    	 -->
                    </td>
                </tr>
            </table>           
        </div>
    </div>
    <div id="innerAppgrid" class="nui-datagrid" style="width:99%;height:100%" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appMgr/innerAppList" showPager="true" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="name" width="120" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">标识</div>    
            <div field="displayName" width="120" headerAlign="center" align ="center" allowSort="false">显示名称</div>    
            <div field="secondaryDomain" width="200" headerAlign="center" align ="center" allowSort="false">二级域名</div>    
            <div name="url" width="200" headerAlign="center" align="center" renderer="onURLActionRenderer" cellStyle="padding:0;">访问路径</div>
            <div name="allstate" width="80" headerAlign="center" align ="center" renderer="onStateRenderer" allowSort="false">状态</div>
            <div field="owner" width="80" headerAlign="center" align ="center">所有者</div>    
			<div name="action" width="200" headerAlign="center" align="center" renderer="onDoActionRenderer" cellStyle="padding:0;">操作</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        var grid = nui.get("innerAppgrid");
        grid.load();
        
        function selForm() {
            //提交表单数据
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
		function onKeyEnter(e) {
    		selForm();
    	}
        
		function onStateRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var state = record.state;//开通状态
            var appStatus = record.attributes.appStatus;//运行状态
			for (var i = 0, l = CLD_AppStatus.length; i < l; i++) {
				var g = CLD_AppStatus[i];
	    		if (g.id == state) { 
	    			state = '<font color="'+g.color+'">'+g.text+'</font>';
	    		}
			}

			for (var i = 0, l = CLD_InstStatus.length; i < l; i++) {
				var g = CLD_InstStatus[i];
	    		if (g.id == appStatus) { 
	    			appStatus = '<font color="'+g.color+'">'+g.text+'</font>';
	    		}
			}
			
            var s = state+"/"+appStatus;
            return s;
		}
		
		function onURLActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var domain = record.secondaryDomain;
            var url = 'http://'+domain;
            var s = '<a  target="_blank" class="Edit_Button" href='+url+'>'+url+'</a> ';
            return s;
		}
		
        function onActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
            var appName = record.name;
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + appName + '\')">'+appName+'</a> ';
            return s;
        }
        
        function detailsRow(appName) {	
			window.location="innerAppClusters.jsp?appName="+appName;
        }
        
        function onDoActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
			var appName = record.name;
			var state = record.attributes.appStatus;
            var s = '<a class="New_Button" href="javascript:detailsRow(\'' + appName + '\')">查看服务</a> ' + '||'+' <a class="New_Button" href="javascript:removeApp(\'' + appName +'\')">删除</a> '+'||'
            + ' <a class="Edit_Button" href="javascript:startApp(\'' + appName+'\'' +','+'\''+state+ '\')">启动</a> ' + '||'
            + ' <a class="Delete_Button" href="javascript:stopApp(\'' + appName+'\'' +','+'\''+state+ '\')">停止</a> ';
            return s;
        }
        
        function removeApp(appName) {
        	if (!confirm("确定删除该集群及集群内所有服务?")) {
        		return;
        	}
        	nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在移除，请稍后...'});
        	$ .ajax({
            	url: "<%=request.getContextPath() %>/srv/appMgr/removeApp/" + appName,
                success: function (text) {
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.alert("系统错误！请稍后重试！"+jqXHR.responseText);
                }
             });
        	nui.alert("删除应用订单已提交，请在订单管理中查看！");
        	nui.unmask();
        }
        
		function startApp(appName, state) {
			if (state==1) {
				nui.alert("已是启动状态，无需再启动!");
        		return;
        	}
        	nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在启动，请稍后...'});
        	$ .ajax({
            	url: "<%=request.getContextPath() %>/srv/appMgr/startApp/" + appName,
                success: function (text) {
                	nui.unmask();
                	var o = nui.decode(text);
    	              if (o.result) {
    	            	  nui.alert("启动命令已发送！");
    	              } else {
    	            	  nui.alert("删除失败!");
    	              }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.unmask();
                	nui.alert("系统错误！请稍后重试！"+jqXHR.responseText);
                }
             });
        }
		
		function stopApp(appName,state) {
			if (state == 0) {
				nui.alert("已是停止状态，无需再停止!");
        		return;
        	}
        	nui.mask({el: document.body,cls: 'mini-mask-loading', html: '正在停止，请稍后...'});
        	$.ajax({
            	url: "<%=request.getContextPath() %>/srv/appMgr/stopApp/"+appName,
                success: function (text) {
                	nui.unmask();
                	var o = nui.decode(text);
    	              if (o.result) {
    	            	  nui.alert("停止命令已发送！");
    	              } else {
    	            	  nui.alert("停止失败!");
    	              }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.unmask();
                	nui.alert("系统错误！请稍后重试！"+jqXHR.responseText);
                }
             });
		}
    </script>
</body>
</html>