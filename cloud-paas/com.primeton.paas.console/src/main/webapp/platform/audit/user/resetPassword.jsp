<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>重置密码</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
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
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/audit/resetPassword">
        <!-- <input name="id" class="nui-hidden" /> -->
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:80px;" align="right">用户标识：</td>
                    <td style="width:200px;">    
                         <input id="userId" name="userId" class="nui-textbox asLabel" width="100%"/>
                    </td>
                    <td>* 用户标识</td>
                </tr>
                <tr>
                    <td style="width:80px;"  align="right">重置密码：</td>
                    <td style="width:200px;">    
                         <input id="password" name="password" vtype="rangeLength:6,25;" class="nui-password" value="000000" required="true" vtype="rangeLength:6,25" width="100%"/> 
                    </td>
                    <td>* 长度为6-25位的英文字母、数字和特殊符号  默认六个0</td>
                </tr>
                <tr>
                    <td style="width:80px;"  align="right">确认密码：</td>
                    <td style="width:200px;">    
                         <input id="repassword" name="respassword" vtype="rangeLength:6,25;" class="nui-password" onblur="validatePassword()" value="000000"  required="true" vtype="rangeLength:6,25" width="100%"/> 
                    </td>
                    <td>* 密码确认</td>
                </tr>
            </table>
        </div>
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
    </form>
    <script type="text/javascript">
        nui.parse();
        
        var form = new nui.Form("form1");
        
         //密码确认
        function validatePassword(){
        	var pwd1 = nui.get("password").getValue();
    	    var pwd2 = nui.get("repassword").getValue();
    	    if (pwd1 == null || pwd1.length == 0) {
    	    	nui.get("password").focus();
    	        return false;
    	    }
    	
    	    if (pwd2 == null || pwd2.length == 0) {
    	    	nui.get("repassword").focus();
    	        return false;
    	    }
    	
    	    if (pwd1 != pwd2) {
    	        alert("两次输入的密码不相同，请重新输入!");
    	        nui.get("password").setValue("000000");
    	        nui.get("repassword").setValue("000000");
    	        nui.get("password").focus();
    	        return false;
    	    }
    	    return true;
        }
        
        function CloseWindow(action) {
            if (window.CloseOwnerWindow) window.CloseOwnerWindow(action);
            else window.close();
        }
        
        function SetData(data) {
        	data = nui.clone(data);
        	nui.get("userId").setValue(data.userId);
        	nui.get("password").setValue("000000");
        	nui.get("repassword").setValue("000000");
        }
        
        function onOk(e) {
            saveData();
        }
        
        function saveData(){
        	if(!validatePassword()){
        		return;
        	}
        	//保存密码
        	var userId = nui.get("userId").getValue();
        	var password = nui.get("password").getValue();
        	nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在操作，请稍后...'});
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/audit/resetPassword",
				data:{userId:userId,password:password},
				type:"post",
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result) {//result
                		nui.alert("密码重置成功！");
                		CloseWindow("ok");
                	} else {
                		nui.alert("密码重置失败！");
                		CloseWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.alert("系统错误！请稍后重试!"+jqXHR.responseText);
        			CloseWindow("failed");
                }
            });
        }
        
        function onCancel(e) {
            CloseWindow("cancel");
        }
        
    </script>
</body>
</html>
