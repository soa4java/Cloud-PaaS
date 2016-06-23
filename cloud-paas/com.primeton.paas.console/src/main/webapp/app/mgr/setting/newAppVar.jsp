<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >填写变量信息</legend>
        <div style="padding:5px;">
	        <table>
	            <tr>
	                <td style="width:100px;">变量名称:</td>
	                <td style="width:300px;">    
	                	<input id="SLID"  name="dvar.name"  class="nui-textbox" required="true" requiredErrorText="此项为必填项" onblur="checkVarName(this.value);"/>
	                </td>
	            </tr>
	            <tr>
	                <td >变量的值:</td>
	                <td >    
	                    <input name="dvar.value"  class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >变量的说明:</td>
	                <td >    
	                    <input name="dvar.desc" class="nui-textarea"  style="width:200px;height:50px;"/>
	                </td>
	            </tr>            
	        </table>            
        </div>
        </fieldset>
   	  
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">添加</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>
	</form>
      
	<script type="text/javascript">
        nui.parse();
        
        var form = new nui.Form("form1");
        
        var appName ;

        function SetData(data) {
            if (data.action == "details") {
            	appName = data.appName;
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
            var formSub = new nui.Form("#form1"); 
    
            var data = formSub.getData().dvar;      //获取表单多个控件的数据
            var json = nui.encode(data);   		//序列化成JSON
                    
            formSub.validate();
            if (formSub.isValid() == false) {
            	return;
            }
            
        	var obj= nui.get("SLID");
	        if (!f_check_domainPrefix(obj)) {
	        	obj.focus();
	            return false;
	        }
	        
           nui.mask({
               el: document.body,
               cls: 'mini-mask-loading',
               html: '正在添加，请稍后...'
           });

            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/appSetting/addAppVar/"+appName,
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	// var o = nui.decode(text);
                	if (text) {
                		nui.alert('添加成功!');
                		nui.unmask(document.body);
                   		CloseWindow("cancel"); 
                	} else {
                		nui.alert('添加失败!');
                		nui.unmask(document.body);
                	}
                } 
            });
        }
       
        function checkVarName(domain) {
        	var obj= nui.get("SLID");
        	domain = domain.trim(); 
        	if (obj == null || obj.value == null || obj.value.trim().length == 0) {
				return false;
			}
        	nui.mask({
               el: document.body,
               cls: 'mini-mask-loading',
               html: '正在效验，请稍后...'
			});
           
			$.ajax({
        	   url: "<%=request.getContextPath() %>/srv/appSetting/isExistVarName/" + appName + "&" + domain  ,
               cache: false,
               success: function (text) {
            	   var o = nui.decode(text);
	               if(o.result){
	                    nui.alert("应用的变量名称“" + domain + "”已经存在，请重新输入！");
	                    obj.setValue(""); 
	                    obj.focus();
	               		nui.unmask(document.body);
	               		return false;
	               	}
	               	nui.unmask(document.body);
	            }
			});
           return true;
        }  
        
        function f_check_domainPrefix(obj) {
      	   if (obj == null ||obj.value == null || obj.value.trim().length == 0) {
      	      nui.alert("变量名称必须输入");
      	      //obj.focus();
      	      return false;
      	   } else if (obj.value.length < 2 || obj.value.length > 20) {
      	      nui.alert("变量名称长度2-20");
      	      //obj.focus();
      	      return false;
      	   } else {
      	      var reg = new RegExp("^[a-z][a-z0-9_]{1,19}$");
      	      if (!reg.test(obj.value)) {
      	         nui.alert("变量名称以小写字母开头，允许小写字母、数字和下划线");
      	         //obj.focus();
      	         return false;
      	      }
      	   }
      	   return true;
      	}
      
     </script>
</body>
</html>