<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/common.js" type="text/javascript"></script>
</head>
<body style="height:75%">         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                	<td style="width:10%" align="right">集群标识:</td>
                    <td style="width:20%;"><input id="id" name="id" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">显示名称:</td>
                    <td style="width:20%;"><input id="name" name="name" class="nui-textbox" onenter="onKeyEnter"/></td>
                    <td style="width:10%;" align="right">所有者:</td>
                    <td style="width:20%;"><input id="owner" name="owner" class="nui-textbox" onenter="onKeyEnter"/></td>
                    <td style="width:10%;" align="right">状态:</td>
                    <td style="width:20%;"><input id="status" name="status" class="nui-combobox" data="CLD_InstStatus" value="defaultValue"/></td>
                </tr>
                <tr>
                	<td colspan="8" align="center">
                		<a class="nui-button" style="width:8%; " onclick="selForm">查询</a>
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
						<a class="nui-button" iconCls="icon-add" onclick="createCluster()">新建 </a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid" class="nui-datagrid" style="width:99%;height:100%" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/service/haproxyCluster" showPager="true" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <!-- <div type="checkcolumn" ></div> -->        
            <div field="id" width="100" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">集群标识</div>    
            <div field="name" width="120" headerAlign="center" align ="center" allowSort="false">显示名称</div>    
            <div field="attributes.size" width="80" headerAlign="center" align ="center" allowSort="false">实例数量</div>    
            <div name="instsize" width="80" headerAlign="center" align="center" renderer="onInstSizeActionRenderer" cellStyle="padding:0;">实例数范围</div>
            
            <div field="attributes.url" width="180" headerAlign="center" align="center" renderer="onURLActionRenderer">访问路径</div>
            
            <div field="attributes.status" width="50" headerAlign="center" align ="center" renderer="onServiceStateRenderer">状态</div>    
            <div field="owner" width="100" headerAlign="center" align ="center" allowSort="false">所有者</div>    
			<div name="action" width="260" headerAlign="center" align="center" renderer="onDoActionRenderer" cellStyle="padding:0;">操作</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("datagrid");
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
        
		function onURLActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var url = record.attributes.url;
            return '<a  target="_blank" href=' + url + '>' + url + '</a> ';
		}
		
		function onInstSizeActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            return record.minSize + "-" + record.maxSize;
		}
		
        function onActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
            var clusterId = record.id;
            return '<a href="javascript:detailsRow(\'' + clusterId + '\')">' + clusterId + '</a> ';
        }
        
        function detailsRow(clusterId) {	
			window.location = "haproxyServiceMgr.jsp?clusterId=" + clusterId;
        }
        
        function onDoActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
			var clusterId = record.id;
			var type=record.type;
			var state=record.attributes.status;
            if (state == 1) {
            	return '<a href="javascript:detailsRow(\'' + clusterId + '\')">服务实例</a> ' 
            		+ ' <a href="javascript:removeCluster(\'' + clusterId +'\'' +','+'\''+type+ '\')">删除</a> ' 
		        	+ ' <a href="javascript:restartCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">重启</a> ' 
		        	+ ' <a href="javascript:stopCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">停止</a> ';
		   	}
            return '<a href="javascript:detailsRow(\'' + clusterId + '\')">服务实例</a> ' 
            	+ ' <a href="javascript:removeCluster(\'' + clusterId +'\'' +','+'\''+type+ '\')">删除</a> ' 
            	+ ' <a href="javascript:startCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">启动</a> ' 
            	+ ' <a href="javascript:stopCluster(\'' + clusterId+'\'' +','+'\''+type+'\''+','+'\''+state+ '\')">停止</a> ';
        }
        
        function removeCluster(clusterId, type) {
        	if (!confirm("确定删除该集群及集群内所有服务?")) {
        		return;
        	}
        	$.ajax({
            	url: "<%=request.getContextPath() %>/srv/service/removeCluster",
            	data: {clusterId:clusterId, type:type},
                type: "post",
                success: function (text) {
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.alert("系统错误！请稍后重试！" + jqXHR.responseText);
                }
             });
        	nui.alert("集群删除订单已创建，可在订单管理查看处理结果！");
        }
        
		function createCluster(){
			nui.open({
                url: "<%=request.getContextPath() %>/platform/srvmgr/haproxy/createHaproxy.jsp",
                title: "新增Haproxy服务向导", width: 850, height: 480,
                onload: function () {
                    //nothing
                },
                ondestroy: function (action) {
                	if (action=='ok') {
                    	grid.reload();
                	}
                }
            });
		}
    </script>
</body>
</html>