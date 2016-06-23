<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

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
		<input id="packageId" name="packageId" class="nui-hidden" />
		<table>
			<tr>
				<td >主机IP:</td>
				<td >    
					<input id="ip" name="ip" class="nui-textbox" required="true" style="width:150px;"/>
				</td>
			</tr> 
			<tr>
				<td>主机名:</td>
				<td >
					<input name="name" class="nui-textbox" required="true" style="width:150px;"/>
				</td>
			</tr>
		</table>            
		<span id="showText"></span>
	   	 
		<div style="text-align:center;padding:10px;">               
			<a class="nui-button" onclick="addHost" style="width:60px;margin-right:20px;">新增</a>            
			<a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
		</div>   
	</form>
</body>
<script type="text/javascript">
    
	nui.parse();
    
	var id ;
	
	// var grid = nui.get("datagrid1");

    //标准方法接口定义
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            nui.get("packageId").setValue(id);
            // grid.load();
        }
    }
    
    function onCancel(e) {
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
     
    function addHost() {
  	   var form = new nui.Form("#form1"); 
       var data = form.getData();      //获取表单多个控件的数据
       var json = nui.encode(data);   //序列化成JSON
       
       if (form.isValid() == false) 
    	   return;
       
       if(!f_check_hostIP(data.ip)){
    	   return;
       }
       
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在添加，请稍后...'
        });
        
        $.ajax({
        	url: "<%=request.getContextPath() %>/srv/hostPoolMgr/isExistHostIP/"+data.ip,
            cache: false,
            success: function (text) {
            	if(!text){
            		$.ajax({
                    	type: "PUT",
                        url: "<%=request.getContextPath() %>/srv/hostPoolMgr/addHost",
                        contentType: "application/json; charset=utf-8",
                        data: json,
                        success: function (text) {
                        	if (text) {
                        		nui.alert('添加成功!');
                        		CloseWindow("cancel");
                        	} else {
                        		nui.alert('添加失败!');
                        	}
                        	nui.unmask(document.body);
                        } 
                    });
            	} else {
            		nui.alert('主机IP已经存在!');
            		nui.unmask(document.body);
            	}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                nui.alert(jqXHR.responseText);
                CloseWindow();
            }
        });
        
        
    }
    
	function f_check_hostIP(objValue) {
		if (objValue == '') {
			return false;
		}
		var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var reg = objValue.match(exp);
		if (reg == null) {
			nui.alert("主机IP不合法！");
			nui.get("ip").focus();
			return false;
		}
	   return true;
	}
        
	</script>
</html>
