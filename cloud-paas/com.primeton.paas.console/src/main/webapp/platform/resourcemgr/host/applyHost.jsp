<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
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
            <legend >新增主机向导</legend>
			<div style="padding:5px;">
			<table>
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="selId" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>' emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	            <tr>
	            	<td>预安装服务类型:</td>
	            	<td colspan="2">
	            		<div id="typeList" class="nui-checkboxlist" repeatItems="4" repeatLayout="table"
						    textField="value" valueField="value"  onload="onLoad"
						    url="<%=request.getContextPath() %>/srv/hostMgr/getServiceTypes" >
						</div>
	            	</td>
	            </tr>
	        </table>            
			</div>
		</fieldset>
		<span id="showText"></span>
	   	 
		<div style="text-align:center;padding:10px;">               
			<a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">新增</a>            
			<a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
		</div>   
	</form>

    <script type="text/javascript">
    
        nui.parse();

        var form = new nui.Form("form1");
        
        $(document).ready(function() {
            nui.getbyName("selId").select(0);
		});
        
        
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

        function submitForm() {
            var sid = nui.getbyName("selId").getValue();
            var typeList = nui.get("typeList").getValue();
            
            if (typeList == null || typeList == '' ){
            	nui.alert('请勾选预安装服务类型！');
            	return;
            }
            
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在安装，请稍后...'
            });
            
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/hostMgr/getApplyIpByPid/" + sid,
                cache: false,
                success: function (text) {
                	var o = nui.decode(text);
                	if(o != null && o.result != null && o.result != ''){
                		var newIp = o.result;
                		alert('newIp='+newIp);
                        $.ajax({
                            url: "<%=request.getContextPath() %>/srv/hostMgr/applyHostService/" + newIp + "&" + typeList,
                            cache: false,
                            success: function (text) {
                            	var o = nui.decode(text);
                            	if (o != null && o.result != null) {
                            		var taskIds = o.result;
                            		nui.alert('信息已提交，请稍后查看服务安装情况，或者查看长任务信息['+taskIds+']');
                            		CloseWindow("cancel");
                            	} else {
                            		nui.alert('此项主机池没有多余主机!');
                            	}
                            	nui.unmask(document.body);
            	            }
            	        });
                		
                	} else {
                		nui.alert('此项主机池没有多余主机!');
                	}
                	nui.unmask(document.body);
	            }
	        });
        }
        
        function validateForm() {
        	return true;
        }
        
     </script>
</body>
</html>
