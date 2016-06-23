<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:95%">
	<div id="tabs1" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;" plain="false" TabPosition ="left">
	    <div title="应用" >
	       		<div style="width:100%;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="addInner()">申请应用</a>
			                        <a class="nui-button" iconCls="icon-reload" onclick="reload()">刷新</a>    
			                        <a class="nui-button" iconCls="icon-remove" onclick="removeInner('inner')">移除</a>       
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
    
	            <div id="datagrid1" class="nui-datagrid" style="width:100%;height:90%;" allowResize="true"
         					idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/myApp/myWebapps" showPager="false" pageSize="100" onloaderror="onLoadErrorRenderer">
			        <div property="columns">
			            <div type="checkcolumn" ></div>        
			            <div field="name" width="100" headerAlign="center" align ="center" allowSort="true">应用标识</div>    
			            <div field="secondaryDomain" width="200"  headerAlign="center" align ="center" allowSort="true">域名、内网地址</div>    
						<div field="attributes.protocal" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">访问方式</div>    
			            <div field="attributes.state" width="100"  headerAlign="center" align ="center"  allowSort="true" renderer="onAppStatusRenderer">状态</div>  
						<div field="attributes.serverType" width="80" headerAlign="center" align ="center" allowSort="true">容器类型</div>    
						<div field="type" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onChargeStatusRenderer">收费类型 </div>    
			        </div>
               </div>
	    </div>
	    
	    <!-- Comment by ZhongWen.Li (mailto:lizw@primeton.com)
	    <div title="外部应用">
	        	<div style="width:100%;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="addOuter()">申请外部应用</a>
			                        <a class="nui-button" iconCls="icon-reload" onclick="reload()">刷新</a>    
			                        <a class="nui-button" iconCls="icon-remove" onclick="removeOuter('outer')">移除</a>       
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
    
	            <div id="datagrid2" class="nui-datagrid" style="width:100%;height:90%;" allowResize="true"
         					idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/myApp/listOuterApp" showPager="false" pageSize="20">
			        <div property="columns">
			            <div type="checkcolumn" ></div>        
			            <div field="name" width="100" headerAlign="center" align ="center" allowSort="true"  renderer="onActionRendererNameOuter">标识</div>    
			            <div field="displayName" width="200"  headerAlign="center" align ="center" allowSort="true">显示名称</div>   
			            <div field="attributes.accessUrl" width="200"  headerAlign="center" align ="center" allowSort="true" renderer="onActionRendererOuter">访问地址</div>    
			            <div field="attributes.state" width="100"  headerAlign="center" align ="center"  allowSort="true" renderer="onAppStatusRenderer">状态</div>  
						<div field="type" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onChargeStatusRenderer">收费类型 </div>    
			        </div>
               </div>
	    </div>
	    -->
	</div>
    

    <script type="text/javascript">
	    nui.parse();
	    
	    var grid = nui.get("datagrid1");
	    grid.load();
	    
	    // var grid2 = nui.get("datagrid2");
	    // grid2.load();
	    
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
            } else if (protocal == 'MUTUAL-HTTPS'){
            	s = s2;
            } else if (protocal == 'MUTUAL-HTTP-HTTPS' || protocal == 'HTTP-HTTPS') {
            	s = s1+'&nbsp;&nbsp;,&nbsp;&nbsp;'+ s2;
            } else {
            	s = s1;
            }
            return s;
        }
        
        function onActionRendererOuter(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var accessUrl = record.attributes.accessUrl;
            var s = '<a href="'+ accessUrl+ '" target="_blank">'+accessUrl+'</a>';
            return s;
        }
        
        function onActionRendererNameOuter(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var name = record.name;
            var s = '<a href="javascript:openOuter(\''+name+'\')">'+name+'</a>';
            return s;
        }

        function addInner(){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/application/createInnerApp.jsp",
	            title: "申请应用", width: 800, height: 650,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details", id: orderId };
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                grid.reload();
	            }
	        });
        }

        function addOuter(){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/application/createOuterApp.jsp",
	            title: "申请外部应用", width: 800, height: 240,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details", id: orderId };
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                grid.reload();
	            }
	        });
        }
        
        function openOuter(name){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/application/myOuterAppMgr.jsp",
	            title: "应用集群信息", width: 850, height: 600,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details", name: name };
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                grid.reload();
	            }
	        });
        }
        
        function reload(){
    		grid.load();
    		// grid2.load();
        }
        
   		function removeInner(appType) {    		
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
          
            if (row.attributes.state == 0) {
            	nui.alert('应用处于 [开通中]状态，不允许进行移除操作！');
            	return;
            }
            if (row.attributes.state == 2) {
            	nui.alert('应用处于 [待移除]状态，不允许进行移除操作！');
            	return;
            }
    		
    		var appName = row.name;
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            
	        $.ajax({
	        	 // appName , @PathParam("appType") String  appType
	             url: "<%=request.getContextPath() %>/srv/myApp/removeApp/" + appName +"&"+appType,
	             contentType: "application/json; charset=utf-8",
	             cache: false,
	             async: false,
	             success: function (text) {
	                	var o = nui.decode(text);
	                	if(o){
	                		nui.alert('提交移除申请成功,等待管理员进行审批!');
	                		grid.load();
	                		// grid2.load();
	                	}else{
	                		nui.alert('提交移除申请失败，请确定当前应用状态为“已开通”！');
	                	}
	                	nui.unmask(document.body);
		            }
		        });
    	    
    	}

   		// 移除外部应用    	
    	function removeOuter(appType) {    		
            var rows = grid2.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if (row.attributes.state == 2) {
            	nui.alert('此应用已经是"移除"状态!');
            	return;
            }
    		
    		var appName = row.name;
	        
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            
	        $.ajax({
	        	 // appName , @PathParam("appType") String  appType
	             url: "<%=request.getContextPath() %>/srv/myApp/removeApp/" + appName +"&"+appType,
	             contentType: "application/json; charset=utf-8",
	             cache: false,
	             async: false,
	             success: function (text) {
	                	var o = nui.decode(text);
	                	if (o) {
	                		nui.alert('提交移除申请成功!');
	                		grid.load();
	                		grid2.load();
	                	} else {
	                		nui.alert('提交移除申请失败!');
	                	}
	                	nui.unmask(document.body);
		            }
		        });
    	}
    </script>
</body>
</html>