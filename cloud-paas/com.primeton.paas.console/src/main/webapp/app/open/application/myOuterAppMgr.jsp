<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	        <fieldset style="border:solid 1px #aaa;padding:3px;">
            	<legend >[<b><span id="appName"></span></b>]应用已绑定集群</legend>	      
            	<div style="width:800px;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="unBindCluster()">解绑</a>
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div> 		
	            <div id="datagridBindClusters" class="nui-datagrid" style="width:800px;height:200px;" allowResize="true"
         					idField="id" multiSelect="true"  showPager="false" pageSize="20">
			        <div property="columns">
			        	<div type="checkcolumn" ></div>   
			            <div field="id" width="60" headerAlign="center" align ="center" allowSort="true" renderer='onActionRendererOuter'>集群标识</div>    
			            <div field="type" width="100"  headerAlign="center" align ="center" allowSort="true">类型</div>    
			            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">显示名称</div>    
<!-- 			            <div field="size" width="80"  headerAlign="center" align ="center"  allowSort="true">集群实例数</div>   -->
				        <div header="实例数范围" headerAlign="center" align ="center" >
			                <div property="columns" headerAlign="center" align ="center">
			                    <div field="minSize" width="50" headerAlign="center" align ="center">最小</div>
			                    <div field="maxSize" width="50" headerAlign="center" align ="center">最大</div>
			                </div>
				         </div>						
						<div field="attributes.state" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态 </div>  
			        </div>
               </div>
               </fieldset>
               
               <fieldset style="border:solid 1px #aaa;padding:3px;">
            	<legend >[<b><span id="appName2"></span></b>]应用可绑定集群</legend>	       		
	       		<div style="width:800px;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="bindCluster()">绑定</a>
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
	            <div id="datagridUnBindClusters" class="nui-datagrid" style="width:800px;height:200px;" allowResize="true"
         					idField="id" multiSelect="true"  showPager="false" pageSize="20">
			        <div property="columns">
			            <div type="checkcolumn" ></div>   
			            <div field="id" width="60" headerAlign="center" align ="center" allowSort="true" renderer='onActionRendererOuter'>集群标识</div>    
			            <div field="type" width="100"  headerAlign="center" align ="center" allowSort="true">类型</div>    
			            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">显示名称</div>    
<!-- 			            <div field="size" width="80"  headerAlign="center" align ="center"  allowSort="true">集群实例数</div>   -->
				        <div header="实例数范围" headerAlign="center" align ="center" >
			                <div property="columns" headerAlign="center" align ="center">
			                    <div field="minSize" width="50" headerAlign="center" align ="center">最小</div>
			                    <div field="maxSize" width="50" headerAlign="center" align ="center">最大</div>
			                </div>
				         </div>						
						<div field="attributes.state" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态 </div>  
			        </div>
               </div>
               </fieldset>
            
    

    <script type="text/javascript">
	    nui.parse();
	    
		///////////////////////////////////////////////////////
	
	    var datagridBindClusters = nui.get("datagridBindClusters");
	    var datagridUnBindClusters = nui.get("datagridUnBindClusters");
	
	    var appName ;
	    ////////////////////
	    function SetData(data) {
	        if (data.action == "details") {
	        	var name = data.name;
	        	appName = name;
	        	document.getElementById("appName").innerHTML = name;
	        	document.getElementById("appName2").innerHTML = name;
	        	
	        	datagridBindClusters.setUrl("<%=request.getContextPath() %>/srv/myApp/getBindClusters/" +data.name);
	            datagridBindClusters.load();
	            datagridUnBindClusters.setUrl("<%=request.getContextPath() %>/srv/myApp/getUnBindClusters/" +data.name);
	            datagridUnBindClusters.load();
	        }
	    }
	    
	    
	    function bindCluster(){
            var rows = datagridUnBindClusters.getSelecteds();
            var count = rows.length;
            if(count != 1){
            	alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在绑定，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/myApp/bindCluster/" + appName + "-" + row.id,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if(o){
                		alert('绑定成功!');
                		datagridBindClusters.load();
                		datagridUnBindClusters.load();
                	}else{
                		alert('绑定失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
            
	    }

	    function unBindCluster(){
            var rows = datagridBindClusters.getSelecteds();
            var count = rows.length;
            if(count != 1){
            	alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在解绑，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/myApp/unBindCluster/" + appName + "-" + row.id,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if(o){
                		alert('解绑成功!');
                		datagridBindClusters.load();
                		datagridUnBindClusters.load();
                	}else{
                		alert('解绑失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
            
	    }
	    
	    
        function deployApp() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if(count != 1){
            	alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            if(row.attributes.isDeployVersion == 'true'){
            	alert('此版本已经是部署状态!');
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
                	if(o.deployStatus == 'S'){
                		alert('部署成功!');
                		grid.load();
                	}else{
                		alert('部署失败!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }
        
        function onActionRendererOuter(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var id = record.id;
            var type = record.type;
            var s ;
            if(type == 'HaProxy'){
           	 	s = '<a href="javascript:openOuterHaproxy(\''+id+'\')">'+id+'</a>';
            }else{
            	s = '<a href="javascript:openOuterMemcached(\''+id+'\')">'+id+'</a>';
            }
            return s;
        }
        function openOuterHaproxy(id){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/service/myOuterHaproxy.jsp",
	            title: "我的服务信息", width: 600, height: 400,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details", id: id };
	                iframe.contentWindow.SetData(data);
	                
	            },
	            ondestroy: function (action) {
	                grid.reload();
	                
	            }
	        });
        }
        function openOuterMemcached(id){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/service/myOuterMemcached.jsp",
	            title: "我的服务信息", width: 600, height: 300,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details", id: id };
	                iframe.contentWindow.SetData(data);
	                
	            },
	            ondestroy: function (action) {
	                grid.reload();
	            }
	        });
        }
    </script>
</body>
</html>