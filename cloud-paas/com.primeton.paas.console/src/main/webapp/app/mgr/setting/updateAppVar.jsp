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
	                <td style="width:300px;" disabled="disabled">    
	                	<input id="SLID"  name="var.name"  class="nui-textbox" />
	                </td>
	            </tr>
	            <tr>
	                <td >变量的值:</td>
	                <td >    
	                    <input id="varValue" name="var.value"  class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >变量的值:</td>
	                <td >    
	                    <input id="varDesc" name="var.desc" class="nui-textarea"  style="width:200px;height:50px;"/>
	                </td>
	            </tr>            
	        </table>            
        </div>
        </fieldset>
   	  
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">修改</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>
   </form>
</body>
    <script type="text/javascript">
    	// parse
        nui.parse();
        var form = new nui.Form("form1");
        
        var appName ;
        var varName ;
        
        function SetData(data) {
            if (data.action == "details") {
            	appName = data.appName;
            	varName = data.varName;
            	varValue = data.varValue;
            	varDesc = data.varDesc;
            	nui.get("SLID").setValue(varName);
            	nui.get("varValue").setValue(varValue);
            	nui.get("varDesc").setValue(varDesc);
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
    
            var data = formSub.getData().var;      //获取表单多个控件的数据
            var json = nui.encode(data);   		//序列化成JSON
                    
            formSub.validate();
            if (formSub.isValid() == false) {
            	return;
            }

            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/appSetting/updateAppVar/"+appName,
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('修改成功!');
                   		CloseWindow("cancel"); 
                	} else {
                		nui.alert('修改失败!');
                	}
                } 
            });
        }
        
        function checkVarName(domain) {
        	var obj= nui.get("SLID");
            if (!f_check_domainPrefix(obj)) {
            	return false;
            }
           domain = domain.trim(); 

           nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在效验，请稍后...'});
           $.ajax({
           	   type: "get",
               url: "<%=request.getContextPath() %>/srv/appSetting/isExistVarName/" + appName + "&" + domain  ,
               success: function (text) {
            	   var o = nui.decode(text);
            	   if (o) {
	                    nui.alert("应用的变量名称“" + domain + "”已经存在，请重新输入！");
	                    obj.setValue(""); 
	                    obj.focus();
	             	    nui.unmask();
	                    return false;
            	   }
               } 
           });
           
           nui.unmask(); 
           return true;
        }  
        
        function f_check_domainPrefix(obj) {
      	   if (obj == null ||obj.value == null || obj.value.trim().length == 0) {
      	      nui.alert("变量名称必须输入");
      	      obj.focus();
      	      return false;
      	   } else if (obj.value.length < 2 || obj.value.length > 20) {
      	      nui.alert("变量名称长度2-20");
      	      obj.focus();
      	      return false;
      	   } else {
      	      var reg = new RegExp("^[a-z][a-z0-9_]{1,19}$");
      	      if (!reg.test(obj.value)) {
      	         nui.alert("变量名称以小写字母开头，允许小写字母、数字和下划线");
      	         obj.focus();
      	         return false;
      	      }
      	   }
      	   return true;
      	}
      
     </script>
</html>