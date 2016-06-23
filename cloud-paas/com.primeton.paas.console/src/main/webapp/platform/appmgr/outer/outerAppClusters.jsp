<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
	String appName = request.getParameter("appName");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	应用名：[<%=appName %>]
                    </td>
                    <td style="white-space:nowrap;">
						<a class="nui-button" iconCls="icon-upgrade" onclick="goBack()">返回</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>
	<div id="appclustersgrid" class="nui-datagrid" style="width:100%;height:400px" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appMgr/showRelClusters/<%=appName %>" showPager="false" >
        <div property="columns">
            <!-- <div type="checkcolumn" ></div> -->        
            <div field="id" width="15%" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">集群标识</div>    
            <div field="name" width="20%" headerAlign="center" align ="center" allowSort="true">集群名称</div>    
            <div field="type" width="15%" headerAlign="center" align ="center" allowSort="true">集群类型</div>    
            <div field="attributes.size" width="10%" headerAlign="center" align ="center" allowSort="true">集群实例数</div>    
            <div name="instsize" width="10%" headerAlign="center" align="center" renderer="onInstSizeActionRenderer" cellStyle="padding:0;">实例数范围</div>
            <div field="owner" width="10%" headerAlign="center" align ="center" allowSort="true">所有者</div>    
			<div name="action" width="25%" headerAlign="center" align="center" renderer="onDoActionRenderer" cellStyle="padding:0;">操作</div>    
        </div>
    </div>
    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("appclustersgrid");
        
        var appName='<%=appName%>';
        
        grid.load();
        
        function goBack(){
        	window.location="outerAppMgr.jsp";
        }
        
        function onActionRenderer(e){
        	var grid = e.sender;
            var record = e.record;
            var clusterId = record.id;
            var type = record.type;
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + clusterId +'\'' +','+'\''+type+ '\')">'+clusterId+'</a> ';
            return s;
        }
        
        function onInstSizeActionRenderer(e){
        	var grid = e.sender;
            var record = e.record;
            var minSize = record.minSize;
            var maxSize = record.maxSize;
            var s = minSize +'-'+maxSize;
            return s;
        }
        
        function onDoActionRenderer(e){
        	var grid = e.sender;
            var record = e.record;
			var clusterId = record.id;
			var type = record.type;
            var s = ' <a class="New_Button" href="javascript:detailsRow(\'' + clusterId + '\'' +','+'\''+type+ '\')">查看服务实例</a> ';
            return s;
        }
        
        function detailsRow(clusterId,type){	
			window.location="outerAppServices.jsp?clusterId="+clusterId+"&&type="+type+"&&appName="+appName;
        }
    </script>
</body>
</html>
 