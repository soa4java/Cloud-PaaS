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
 	<style type="text/css">
	    html,body
	    {
	        width:100%;
	        height:100%;
	        border:0;
	        margin:0;
	        padding:0;
	        overflow:visible;
	    }
    </style>
</head>
<body>
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/getOpenAPIClusterDetail">
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
	                    <td style="width:10%;"  align="right">类型：</td>
	                    <td style="width:80%;">    
	                         <input name="type" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">所有者：</td>
	                    <td style="width:80%;">    
	                         <input name="owner" class="nui-textbox asLabel" width="100%"/>
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
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/service/openapiService/<%=clusterId %>" showPager="false" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <!-- <div type="checkcolumn" ></div> -->        
            <div field="id" width="100" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">服务标识</div>    
            <div field="name" width="150" headerAlign="center" align ="center" allowSort="false">服务名称</div>    
            <div field="ip" width="150" headerAlign="center" align ="center" allowSort="false">主机IP</div>    
            <div field="port" width="100" headerAlign="center" align ="center" allowSort="false">端口</div>    
            <div field="state" width="100" headerAlign="center" align ="center" renderer="onServiceStateRenderer">状态</div>    
            <div field="memorySize" width="100" headerAlign="center" align ="center" allowSort="false" renderer="onMemorySizeActionRenderer">内存</div>    
            <div field="owner" width="100" headerAlign="center" align ="center" allowSort="false">所有者</div>    
			<div name="action" width="300" headerAlign="center" align="center" renderer="onServiceActionRenderer" cellStyle="padding:0;">操作</div>    
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
        	form.loading("操作中，请稍后 ...");
        	$.ajax({
                url: "<%=request.getContextPath() %>/srv/service/getOpenAPIClusterDetail/" + clusterId,
                success: function (text) {
                	form.unmask();
                    var o = nui.decode(text);
                    o.data.type = o.data.type + " 集群";  
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
        
        function onMemorySizeActionRenderer(e) {
	        var grid = e.sender;
	        var record = e.record;
	        return record.minMemorySize + '-' + record.maxMemorySize;
        }
        
        function reFresh() {
        	grid.reload();
        }
        
        function goBack() {
        	window.location = "openapiClusterMgr.jsp";
        }
        
        function removeService(serviceId) {
        	if (!confirm("确定删除该服务?")) {
        		return;
        	}
        	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在移除，请稍后...'});
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/service/removeOpenAPIService/" + serviceId,
                success: function (text) {
                	nui.unmask();
                	var o = nui.decode(text);
                	if (o.result == true) { 
                		nui.alert("删除命令已发送！");
                		grid.reload();
                	} else {
                		nui.alert("删除失败！");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.unmask();
                    nui.alert("系统错误！请稍后重试!" + jqXHR.responseText);
                }
            });
        }
        
    </script>
</body>
</html>
 