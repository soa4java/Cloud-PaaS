<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
	String clusterId = request.getParameter("clusterId");
	String type = request.getParameter("type");
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
                    	应用名：     [<%=appName %>]
                    	<span class="separator"></span>
                    	集群标识：[<%=clusterId %>]
                    	<span class="separator"></span>
                    	集群类型：[<%=type %>]
                    </td>
                    <td style="white-space:nowrap;">
						<a class="nui-button" iconCls="icon-upgrade" onclick="goBackClusters()">返回到集群</a>
						<span class="separator"></span>
						<a class="nui-button" iconCls="icon-upgrade" onclick="goBackApps()">返回到应用</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>
	<div id="appservicesgrid" class="nui-datagrid" style="width:100%;height:400px" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appMgr/showClusterServices" showPager="false" >
        <div property="columns">
            <!-- <div type="checkcolumn" ></div> -->        
             <div field="id" width="10%" headerAlign="center" align ="center" allowSort="true">服务标识</div>    
            <div field="name" width="25%" headerAlign="center" align ="center" allowSort="true">服务名称</div>    
            <div field="type" width="15%" headerAlign="center" align ="center" allowSort="true">服务类型</div>    
            <div field="ip" width="10%" headerAlign="center" align ="center" allowSort="true">主机IP</div>    
            <div field="port" width="10%" headerAlign="center" align ="center" allowSort="true">端口</div>    
            <div field="state" width="10%" headerAlign="center" align ="center" renderer="onServiceStateRenderer">运行状态</div>    
            <div field="owner" width="20%" headerAlign="center" align ="center" allowSort="true">所有者</div>    
        </div>
    </div>
    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("appservicesgrid");
        
        var appName='<%=appName%>';
        var clusterId ='<%=clusterId%>'; 
        var type='<%=type%>';
        
        grid.load({clusterId : clusterId, type : type});
        
        function goBackClusters() {
        	window.location = "innerAppClusters.jsp?appName="+appName;
        }
        
        function goBackApps() {
        	window.location="innerAppMgr.jsp";
        }
        
    </script>
</body>
</html>
 