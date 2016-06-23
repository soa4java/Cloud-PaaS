<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
	String clusterId = request.getParameter("clusterId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/common.js" type="text/javascript"></script>
    <style type="text/css">
    .asLabel .nui-textbox-border,
    .asLabel .nui-textbox-input,
    .asLabel .nui-buttonedit-border,
    .asLabel .nui-buttonedit-input,
    .asLabel .nui-textboxlist-border
    {
        border:0;background:none;cursor:default;
    }
    .asLabel .nui-buttonedit-button,
    .asLabel .nui-textboxlist-close
    {
        display:none;
    }
    .asLabel .nui-textboxlist-item
    {
        padding-right:8px;
    }    
 </style>
    <script type="text/javascript">
    	var datas = [{ id: 'inner', text: '平台服务'}
		, { id: 'outer', text: '外部服务'}
         ];
    </script>
</head>
<body>
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/getMysqlClusterDetail">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >集群基本信息</legend>
            <div style="padding:5px;">
		        <table style="width:100%">
		            <tr>
	                    <td style="width:10%;" align="right">集群标识：</td>
	                    <td style="width:80%;">    
	                         <input name="id" id="id" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td style="width:10%;"  align="right">显示名称：</td>
	                    <td style="width:80%;">    
	                         <input name="name" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">集群类型：</td>
	                    <td style="width:80%;">    
	                         <input name="type" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">存储路径：</td>
	                    <td style="width:80%;">    
	                         <input name="attributes.storagePath" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">存储大小：</td>
	                    <td style="width:80%;">    
	                         <input name="attributes.storageSize" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">数据库驱动：</td>
	                    <td style="width:80%;">    
	                         <input name="attributes.jdbcDriver" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
		        </table>            
            </div>
        </fieldset>
    </form>
	<br />
	<div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">				
                    </td>
                    <td style="white-space:nowrap;">
                        <a class="nui-button" iconCls="icon-reload" onclick="reFresh()">刷新</a><span class="separator"></span>
						<a class="nui-button" iconCls="icon-upgrade" onclick="goBack()">返回</a>
                    </td>
                </tr>
            </table>           
        </div>
    </div>
	<div id="datagrid" class="nui-datagrid" style="width:100%;height:120px" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/service/mysqlService/<%=clusterId %>" showPager="false" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <!-- <div type="checkcolumn" ></div> -->        
            <div field="id" width="100" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">服务标识</div>    
            <div field="name" width="150" headerAlign="center" align ="center" allowSort="false">服务名称</div>    
            <div field="ip" width="100" headerAlign="center" align ="center" allowSort="false">主机IP</div>    
            <div field="port" width="50" headerAlign="center" align ="center" allowSort="false">端口</div>    
            <div field="schema" width="100" headerAlign="center" align ="center" allowSort="false">数据库schema</div>    
            <div name="userAndPassword" width="150" headerAlign="center" align ="center" allowSort="false" renderer="onUserAndPasswordRenderer">用户名</div>    
            <div field="owner" width="100" headerAlign="center" align ="center" allowSort="false">所有者</div>    
            <div field="state" width="80" headerAlign="center" align ="center" renderer="onServiceStateRenderer">状态</div>    
			<div name="action" width="200" headerAlign="center" align="center" renderer="onServiceActionRenderer" cellStyle="padding:0;">操作</div>    
        </div>
    </div>
    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("datagrid");
        var form = new nui.Form("form1");
        initForm();
        grid.load();
        
        function initForm() {
        	var clusterId = '<%=clusterId%>';
        	form.loading("操作中，请稍后......");
        	$.ajax({
                url: "<%=request.getContextPath() %>/srv/service/getMysqlClusterDetail/" + clusterId,
                success: function (text) {
                	form.unmask();
                    var o = nui.decode(text);
                    o.data.type = o.data.type + " 集群";//for display
                    setNUIForm(form, true, true); // see common.js
                    form.setData(o.data);
                    form.setChanged(false);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	form.unmask();
                    nui.alert(jqXHR.responseText);
                 }
            });
        }
        
        function reFresh() {
        	grid.reload();
        }
        
        function goBack() {
        	window.location = "mysqlClusterMgr.jsp";
        }
        
        function onUserAndPasswordRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
            var user = record.user;
            /* 
            var password = record.password;
            var s = user +'/'+password;
             */
            return user;
        }
        
        function onDoActionRenderer(e) {
        	var grid = e.sender;
            var record = e.record;
			var serviceId = record.id;
			var state = record.state;
			var phpMyAdminUrl = record.attributes.phpmyadminUrl;
            if (state == 1) {
            	return ' <a href="javascript:restartService(\'' + serviceId +'\''+','+'\''+state+ '\')">重启</a> ' 
                	+ ' <a href="javascript:stopService(\'' + serviceId +'\''+','+'\''+state+ '\')">停止</a> ' 
                	+ ' <a href="#" onclick="openPhpmyAdmin(\'' + phpMyAdminUrl +'\')"><img alt="LoginMySql" src="<%=request.getContextPath()%>/images/direction.gif" style="height: 16px;"></a> ';
            }
            return ' <a href="javascript:startService(\'' + serviceId +'\''+','+'\''+state+ '\')">启动</a> ' 
            	+ ' <a href="javascript:stopService(\'' + serviceId +'\''+','+'\''+state+ '\')">停止</a> ' 
            	+ ' <a href="#" onclick="openPhpmyAdmin(\'' + phpMyAdminUrl +'\')"><img alt="LoginMySql" src="<%=request.getContextPath()%>/images/direction.gif" style="height: 16px;"></a> ';
        }
        
		//phpmyadmin访问数据库
		function openPhpmyAdmin(url) {
			window.open(url, "phpMyAdmin", "location=no, toolbar=no, menubar=no, status=no, resizable=yes");
		}
		
    </script>
</body>
</html>
 