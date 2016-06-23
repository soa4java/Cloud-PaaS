<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
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
        <div style="padding:5px;">
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
	            	<td>已安装服务类型:</td>
	            	<td >
	            		<div id="typeList" class="nui-checkboxlist" repeatItems="4" repeatLayout="table"
						    textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostMgr/getServiceTypes/<%=ip %>"
						    >
						</div>
	            	</td>
	            </tr>
	            <tr>
	            	<td>可安装服务类型:</td>
	            	<td >
	            		<div id="typeListAdvance" class="nui-checkboxlist" repeatItems="4" repeatLayout="table"
						    textField="value" valueField="value"  onload="onLoad"
						    url="<%=request.getContextPath() %>/srv/hostMgr/getAdvanceServiceTypes/<%=ip %>"
						    >
						</div>
	            	</td>
	            </tr>
	        </table>            
	   </div>
	 </fieldset>
	   	 
	  <div style="text-align:center;padding:10px;">               
	       <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">安装</a> 
	  	   <a class="nui-button" onclick="uninstall" style="width:60px;margin-right:20px;">卸载</a>              
           <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
       </div>   
	</form>

</body>
<script type="text/javascript">
    
    nui.parse();
    
	var ip ;
	
	var typeList = nui.get("typeList");
	var typeListAdvance = nui.get("typeListAdvance");

    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            ip = data.ip;
            var name = data.name;
            var types = data.types;
            nui.get("ip").setValue(ip);
            nui.get("name").setValue(name);
            nui.get("types").setValue(types);
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
    
    function submitForm() {
        var typeListAdvance = nui.get("typeListAdvance").getValue();
        
        if( typeListAdvance ==null || typeListAdvance == '' ){
        	nui.alert('请勾选预安装服务类型！');
        	return;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在新增，请稍后...'
        });
        $.ajax({
             url: "<%=request.getContextPath() %>/srv/hostMgr/applyHostService/" + ip + "&" + typeListAdvance,
             cache: false,
             success: function (text) {
             	var o = nui.decode(text);
             	if(o != null && o.result != null){
             		var taskIds = o.result;
             		nui.alert('信息已提交，请稍后查看服务安装情况，或者查看长任务信息[' + taskIds + '].');
             		CloseWindow("cancel");
             	}
             	nui.unmask(document.body);
         	 }
     	 });
    }
    
    function uninstall() {
        var typeList = nui.get("typeList").getValue();
        if( typeList ==null || typeList == '' ){
        	nui.alert('请勾选预卸载服务类型！');
        	return;
        }
        $.ajax({
            url: "<%=request.getContextPath() %>/srv/hostMgr/uninstallService/"+ip+"&"+typeList,
            cache: false,
            success: function (text) {
        	 }
    	 });
        nui.alert('信息已提交，请稍后查看服务卸载情况信息');
        CloseWindow("cancel");
    }
        
</script>
</html>
