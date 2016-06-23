<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:80%">         
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
                </tr>
                <tr>
                	<td colspan="8" align="center">
                		<a class="nui-button" style="width:8%; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    </br>
    <div id="outerAppgrid" class="nui-datagrid" style="width:99%;height:100%" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appMgr/outerAppList" showPager="true" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="name" width="150" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">应用标识</div>    
            <div field="displayName" width="120" headerAlign="center" align ="center" allowSort="false">应用显示名称</div>    
            <div field="attributes.accessUrl" width="200" headerAlign="center" align="center" renderer="onURLActionRenderer" cellStyle="padding:0;">访问路径</div>
            <div field="state" width="100" headerAlign="center" align ="center" renderer="onStateRenderer" allowSort="false">状态</div>
            <div field="owner" width="80" headerAlign="center" align ="center">所有者</div>    
			<div name="action" width="200" headerAlign="center" align="center" renderer="onDoActionRenderer" cellStyle="padding:0;">操作</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("outerAppgrid");
        grid.load();
        
        function selForm() {
            // 提交表单数据
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
		function onKeyEnter(e) {
    		selForm();
    	}
		
		function onStateRenderer(e){
			var grid = e.sender;
        	var record = e.record;
           	var state = record.state;//开通状态
			for (var i = 0, l = CLD_AppStatus.length; i < l; i++) {
				var g = CLD_AppStatus[i];
	    		if (g.id == state){ 
	    			state = '<font color="'+g.color+'">'+g.text+'</font>';
	    		}
			}
           	return state;
		}
		
		function onURLActionRenderer(e){
			var grid = e.sender;
           	var record = e.record;
           	var url = record.attributes.accessUrl;
           	var s = '<a  target="_blank" class="Edit_Button" href='+url+'>'+url+'</a> ';
           	return s;
		}
		
		function onActionRenderer(e){
       		var grid = e.sender;
           	var record = e.record;
           	var appName = record.name;
           	var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + appName + '\')">'+appName+'</a> ';
           	return s;
       }
       
		function detailsRow(appName){	
			window.location="outerAppClusters.jsp?appName="+appName;
       }
       
       function onDoActionRenderer(e){
       		var grid = e.sender;
           	var record = e.record;
			var appName=record.name;
			var state=record.attributes.appStatus;
           	var s = '<a class="New_Button" href="javascript:detailsRow(\'' + appName + '\')">查看服务</a> ' + '||'
           	   +'<a class="New_Button" href="javascript:removeOuterApp(\'' + appName +'\')">删除</a> ';
           	return s;
       }
       
       function removeOuterApp(appName){
          	if (!confirm("确定删除该集群及集群内所有服务?")) {
          		return;
          	}
          	nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在移除，请稍后...'});
          	$.ajax({ 
              	url: "<%=request.getContextPath() %>/srv/appMgr/removeOuterApp/" + appName,
                  success: function (text) {
                  	nui.unmask();
                  	var o = nui.decode(text);
                  	if (o.result == true) {
                  		nui.alert("删除成功！");
                  	} else {
                  		nui.alert("删除失败！");
                  	}
                  },
                  error: function (jqXHR, textStatus, errorThrown) {
					nui.alert("系统错误！请稍后重试！"+jqXHR.responseText);
                  }
               });
       }
    </script>
</body>
</html>