<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;height:120px">
            <legend >填写日志信息</legend>
        <div style="padding:5px;">
	        <table>
	            <tr>
	                <td style="width:100px;" align="right">日志名称:</td>
	                <td style="width:300px;">    
	                	<input id="logType" name="logmodel.type" class="nui-textbox" required="true" requiredErrorText="此项为必填项" onvalidation="onLogTypeValidation" width="60%"/>
	                </td>
	            </tr>
	            <tr>
	                <td align="right">日志级别:</td>
	                <td >    
	                    <input name="logmodel.level"  class="nui-combobox" data="CLD_LogLevels" value="INFO" width="60%"/>
	                </td>
	            </tr>   
	        </table>            
        </div>
        </fieldset>
   	  
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">确定</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>
	</form>
   
	<script type="text/javascript">
        nui.parse();
        
        var form = new nui.Form("form1");
        
        var appName ;
        
        function SetData(data) {
        	data = nui.clone(data);
            appName = data.appName;
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
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            //提交表单数据
            var data = form.getData().logmodel;     
            var json = nui.encode(data);   		
            form.loading("正在操作，请稍后。。。");
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appSetting/addUserLog/" + appName,
                contentType: "application/json; charset=utf-8",
            	type: "PUT",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert('添加成功!');
                   		CloseWindow("ok"); 
                	} else {
                		nui.alert('添加失败!');
                		CloseWindow("failed"); 
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.alert("系统繁忙，请稍候重试！"+jqXHR.responseText);
                    CloseWindow("failed");
                }
            });
        }
        
        function onLogTypeValidation(e) {
        	if (!e.value) {
        		e.errorText = "不能为空！";
        		e.isValid = false;
        	} else {
        		if (e.value == "system") {
        			e.errorText="已有该类型！"
        			e.isValid=false;
        			nui.alert("已有该类型！");
        		}
        		var logType = e.value;
        		//其他格式验证
        		var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,9}$")
        		if (!reg.test(logType)) {
        			e.errorText = "格式不正确！4-10位 字母、数字或下划线组合。";
            		e.isValid = false;
            		return;
        		}
                $.ajax({
                	url: "<%=request.getContextPath() %>/srv/appSetting/checkLogType/" + appName + "&" + logType,
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == false) {
                    		e.errorText = "已有该类型！";
                      		e.isValid = false;
                    	} else {
                    		e.isValid = true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText = "未知错误！";
                    	e.isValid = false;
                    }
                 });
        	}
        }  
	</script>
</body>
</html>