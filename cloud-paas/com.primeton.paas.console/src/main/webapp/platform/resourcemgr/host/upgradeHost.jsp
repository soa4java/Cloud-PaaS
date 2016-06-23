<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
	String ip = request.getParameter("ip");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
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
</head>
<body>
	<form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >主机信息</legend>
        <div style="padding:10px;">
	       	  <table>
	            <tr>
	                <td >主机IP:</td>
	                <td >    
	                    <input id="ip" name="ip" class="nui-textbox asLabel"  style="width:200px;"/>
	                </td>
	            </tr> 
	            <tr>
	            	<td>主机名称:</td>
	            	<td >
	            		<input id="name" name="name" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>主机类型:</td>
	            	<td >
	            		<input id="types" name="types" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>套餐标识:</td>
	            	<td >
	            		<input id="packageId" name="packageId" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>配置信息:</td>
	            	<td >
	            		<input id="packageName" name="packageName" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>配置升级:</td>
	            	<td >
	            		<input id="newConfig" name="newConfig" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'   emptyText="请选择..." style="width:200px;"/>
	            	</td>
	            </tr>
	        </table>            
	   </div>
   	    <div style="padding:5px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;" align="center">
                        <a id="bt_agree" class="nui-button" iconCls="icon-ok" onclick="upgradeHost()" text="升级"></a>&nbsp;&nbsp;
                        <a id="bt_cancel" class="nui-button" iconCls="icon-no" onclick="cancel()" text="取消"></a>       
                    </td>
                </tr>
            </table>           
        </div>
	 </fieldset>
	</form>
</body>
<script type="text/javascript">
    
    nui.parse();
    
    var grid = nui.get("datagrid1");
    var grid2 = nui.get("datagrid2");
    var grid3 = nui.get("datagrid3");
    
    grid.load();
    grid2.load();
    grid3.load();

    var ip ;
	
	var typeList = nui.get("typeList");
	var typeListAdvance = nui.get("typeListAdvance");

    //标准方法接口定义
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            ip = data.ip;
            var name = data.name;
            var types = data.types;
            var packageId = data.packageId;
            var model = data.model;
            var packageName = data.packageName;
            nui.get("ip").setValue(ip);
            nui.get("name").setValue(name);
            nui.get("types").setValue(types);
            nui.get("packageId").setValue(packageId);
            nui.get("packageName").setValue(packageName+"("+model+")");
            
            var hostCpuNum = parseInt(model.substring(0, model.indexOf("C")));
            var data = nui.get("newConfig").getData();
            json = eval(data);
            var arrayObj = new Array();　
            for (var i=0; i<json.length; i++) {
            	var dataId = json[i].Id;
            	var dataText = json[i].text;
            	var m = dataText.indexOf("(");
            	var j = dataText.indexOf("C");
            	var dataCpuNum = parseInt(dataText.substring(m+1,j));
            	if (hostCpuNum < dataCpuNum) {
            		arrayObj.push(json[i]);
            	}
            }
            nui.get("newConfig").setData(arrayObj)
        }
    }
    
	// 取消
	function cancel() {
		CloseWindow("cancel");
    }
	
    function CloseWindow(action) {            
        if (action == "close" && form.isChanged()) {
            if (confirm("数据被修改了，是否先保存？")) {
                return false;
            }
        }
        if (window.CloseOwnerWindow) {
        	return window.CloseOwnerWindow(action);
        } else {
        	window.close();            
        }
    }
    
    function upgradeHost() {
    	var pkgId = nui.get("newConfig").getValue();
        if (pkgId == "") {
        	nui.alert('请选择升级配置!');
        	return;
        }
        if (!confirm("确定升级该主机 ?")) {
        	return;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在升级，请稍后...'
        });
        $.ajax({
        	type: "GET",
            url: "<%=request.getContextPath() %>/srv/hostMgr/upgradeHost/" + ip + "&" + pkgId,
            success: function (text) {
            	if (text) {
            		nui.alert('升级已处理，请稍后!');
            		cancel();
            	} else {
            		nui.alert('升级失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
     }
     </script>
     
</html>
