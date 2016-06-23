<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>

<%
	String id = request.getParameter("id");
	String storageSizesJSON = SystemVariable.getNasStorageSizesJSON();
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
            <legend >存储信息</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td >存储标识:</td>
	                <td >    
	                    <input id="id" name="id" class="nui-textbox asLabel"  style="width:200px;"/>
	                </td>
	            </tr> 
	            <tr>
	            	<td>存储名称:</td>
	            	<td >
	            		<input id="name" name="name" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>存储路径:</td>
	            	<td >
	            		<input id="path" name="path" class="nui-textbox asLabel"  style="width:300px;"/>
	            	</td>
	            </tr>
	           	<tr>
	            	<td>存储大小:</td>
	            	<td >
	            		<input id="size" name="size" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>存储升级:</td>
	            	<td >
	            		<input id="newConfig" name="newConfig" class="nui-combobox" required="true" data='<%=storageSizesJSON %>'   emptyText="请选择..." style="width:200px;"/>
	            	</td>
	            </tr>
	        </table>            
	   </div>
   	    <div style="padding:5px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;" align="center">
                        <a id="bt_agree" class="nui-button" iconCls="icon-ok" onclick="upgradeStorage()" text="升级"></a>&nbsp;&nbsp;
                        <a id="bt_cancel" class="nui-button" iconCls="icon-no" onclick="cancel()" text="取消"></a>       
                    </td>
                </tr>
            </table>           
        </div>
	 </fieldset>
</body>
<script type="text/javascript">
    
    nui.parse();
    
    var grid = nui.get("datagrid1");
    grid.load();
	var id ;
	
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            var name = data.name;
            var path = data.path;
            var size = data.size;
            nui.get("id").setValue(id);
            nui.get("name").setValue(name);
            nui.get("path").setValue(path);
            nui.get("size").setValue(size+"G");
            
            var data = nui.get("newConfig").getData();
            json = eval(data);
            var arrayObj = new Array();　
            for (var i=0; i<json.length; i++) {
            	var dataId = json[i].Id;
            	var dataText = json[i].text;
            	var j = dataText.indexOf("G");
            	var dataNum = parseInt(dataText.substring(0,j));
            	if (size < dataNum) {
            		arrayObj.push(json[i]);
            	}
            }
            nui.get("newConfig").setData(arrayObj)
        }
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
	//取消
	function cancel(){
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
    
    function upgradeStorage() {
    	var pkgId = nui.get("newConfig").getValue();
        if (pkgId == "") {
        	nui.alert('请选择升级配置!');
        	return;
        }
        if (!confirm("确定升级该存储 ?")) {
        	return;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在升级，请稍后...'
        });
        $.ajax({
        	type: "GET",
            url: "<%=request.getContextPath() %>/srv/storageMgr/upgradeStorage/"+id+"&"+pkgId,
            success: function (text) {
            	if (text) {
            		nui.alert('升级成功!');
            		CloseWindow("Suc");
            	} else {
            		nui.alert('升级失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
</script>
</html>
