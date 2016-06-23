<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    	<script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    	<script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    	<script src="<%=request.getContextPath() %>/common/cloud/common.js" type="text/javascript"></script>
	</head>
	<body style="height:100%">         
		<fieldset style="border:solid 1px #aaa;padding:3px;height:45%">
			<legend ><b>消息队列服务</b></legend>
			<br/>
			<div style="width:99%;">
				<div class="nui-toolbar" style="border-bottom:0;padding:0px;">
					<table style="width:100%;">
						<tr>
                    		<td style="width:100%;">
								<a class="nui-button" iconCls="icon-add" onclick="newMsgQueue()">新建</a>
                    		</td>
                		</tr>
           			</table>           
        		</div>
   			</div>	       		
			<div id="datagrid" class="nui-datagrid" style="width:100%;height:90%;" allowResize="true"
					idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/myService/listMsgQueue">
			        <div property="columns">
			            <div field="id" width="60" headerAlign="center" align ="center" allowSort="true" >集群标识</div>
			            <div field="name" width="120"  headerAlign="center" align ="center" allowSort="true">服务名称</div>    
			            <div field="attributes.size" width="80"  headerAlign="center" align ="center"  allowSort="true">实例数</div>  
			            <div header="实例范围" headerAlign="center" align ="center" >
			                <div property="columns" headerAlign="center" align ="center">
			                    <div field="minSize" width="50" headerAlign="center" align ="center">最小</div>
			                    <div field="maxSize" width="50" headerAlign="center" align ="center">最大</div>
			                </div>
			            </div>						
						<div field="attributes.state" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态 </div>  
						<div field="desc" width="150" headerAlign="center" align ="center" allowSort="true" >描述 </div>   
						<div name="action" width="150" headerAlign="center" align="center" renderer="onDoActionRenderer" cellStyle="padding:0;">操作</div>
			        </div>
			</div>
		</fieldset>
    	<script type="text/javascript">
    	// parse DOM
        nui.parse();
        
	    var datagrid = nui.get("datagrid");
	    datagrid.load();
	    
	    function onDoActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
			var clusterId = record.id;
			var type = record.type;
			var state = record.attributes.status;
            if (state == 1) {
		   		return '<a href="javascript:detailsRow(\'' + clusterId + '\')">服务实例</a> '
		   			+ ' <a href="javascript:removeCluster(\'' + clusterId + '\'' + ',' + '\'' + type + '\')">删除</a> '
		        	+ ' <a href="javascript:restartCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">重启</a> '
		        	+ ' <a href="javascript:stopCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">停止</a> ';
		   	} else if (state == 0) {
		   		return '<a href="javascript:detailsRow(\'' + clusterId + '\')">服务实例</a> ' 
		   			+ ' <a href="javascript:removeCluster(\'' + clusterId + '\'' + ',' + '\''+ type + '\')">删除</a> ' 
               		+ ' <a href="javascript:startCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">启动</a> ' 
                	+ ' <a href="javascript:stopCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">停止</a> ';
		   	}
            return '<a href="javascript:detailsRow(\'' + clusterId + '\')">服务实例</a> ' 
            	+ ' <a href="javascript:removeCluster(\'' + clusterId + '\'' + ',' + '\''+ type + '\')">删除</a> ' 
            	+ ' <a href="javascript:startCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">启动</a> ' 
            	+ ' <a href="javascript:stopCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">停止</a> ';;
        }
	    
	    function detailsRow(clusterId) {	
			window.location="msgQueueServices.jsp?clusterId=" + clusterId;
        }
	    
        function removeCluster(clusterId, type) {
        	if (!confirm("确定删除该集群及集群内所有服务?")) {
        		return;
        	}
        	$.ajax({
            	url : "<%=request.getContextPath() %>/srv/service/removeCluster",
            	data : {clusterId : clusterId, type : type},
                type : "post",
                success : function (text) {
                },
                error : function (jqXHR, textStatus, errorThrown) {
                	nui.alert("系统错误！请稍后重试！<br/>" + jqXHR.responseText);
                }
             });
        	nui.alert("集群删除订单已提交，请到订单管理查看处理结果！");
        }
        
		function newMsgQueue() {
			nui.open({
                url: "<%=request.getContextPath() %>/app/open/service/createMsgQueueService.jsp",
                title: "新增消息队列服务向导", width: 800, height: 300,
                onload: function () {
                    //nothing
                },
                ondestroy: function (action) {
                	if (action == 'ok') {
                		datagrid.reload();
                	}
                }
            });
		}
        
   		function remove(gName) {    		
            var rows = nui.get(gName).getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            
            if (row.attributes.state == 2) {
            	nui.alert('此服务已经是移除状态!');
            	return;
            }
    		
    		var id = row.id;
	        
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            
	        $.ajax({
	        	 // appName , @PathParam("appType") String  appType
	             url: "<%=request.getContextPath() %>/srv/myService/removeService/" + id,
	             contentType: "application/json; charset=utf-8",
	             cache: false,
	             async: false,
	             success: function (text) {
	                	var o = nui.decode(text);
	                	if (o) {
	                		nui.alert('提交移除申请成功!');
	                		reload();
	                	} else {
	                		nui.alert('提交移除申请失败!');
	                	}
	                	nui.unmask(document.body);
		            }
		        });
    	    
    	}
       	
        function reload() {
        	datagrid.load();
        }

    </script>
</body>
</html>