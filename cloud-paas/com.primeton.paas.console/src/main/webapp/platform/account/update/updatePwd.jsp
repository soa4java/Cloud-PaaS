<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>    
    <form id="form1" method="post">
      	<fieldset style="border:solid 1px #aaa;padding:3px;">
        <legend>
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	修改密码：
        </legend>	
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" align="center">
                <tr>
                    <td style="width:150px;"><font size="-1">输入旧密码：</font></td>
                    <td style="width:300px;"> 
                    	<input id="oldPwd" name="oldPwd"  class="nui-password" style="width:200px;"  required="true" />   
                    </td>
                </tr>
                <tr>
                    <td style="width:150px;"><font size="-1">输入新密码：</font></td>
                    <td style="width:300px;"> 
                    	<input id="newPwd" name="newPwd"  class="nui-password" style="width:200px;"  required="true" />   
                    </td>
                </tr>
                <tr>
                    <td style="width:150px;"><font size="-1">确定密码：</font></td>
                    <td style="width:300px;"> 
                    	<input id="newPwd2" name="newPwd2"  class="nui-password" style="width:200px;" required="true" />   
                    </td>
                </tr>
          </table>  
        </div>

        <div style="text-align:center;padding:10px;">               
            <a id="btnRevoke" class="nui-button" onclick="save()" style="width:60px;margin-right:20px;">保存</a>       
        </div>  
        </fieldset>      
    </form>
    
    <script type="text/javascript">
        nui.parse();
        var form = new nui.Form("form1"); 
        
        function validatePassword(){
        	var pwd1 = nui.get("newPwd").getValue();
    	    var pwd2 = nui.get("newPwd2").getValue();
    	    if (pwd1 != pwd2) {
    	    	nui.alert("两次输入的密码不相同，请重新输入!");
    	        return false;
    	    }
    	    if (pwd1.trim().length < 6 || pwd1.trim().length>25) {
    	    	nui.alert("密码为长度为6-25位的英文字母、数字和特殊符号的组合，请重新输入!");
    	        return false;
    	    }
    	    return true;
        }
        
        function save(){
        	form.validate();
            if (form.isValid() == false) return;
            if(!validatePassword()) return;
        	var oldPwd = nui.get("oldPwd").getValue();
        	var newPwd = nui.get("newPwd").getValue();
        	
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/myAccount/validatePwd/"+oldPwd,
                cache: false,
                async: false,
                success: function (text) {
                   if (text !=null && text == true) {
                       $.ajax({
                           url: "<%=request.getContextPath() %>/srv/myAccount/resetPwd/"+newPwd,
                           cache: false,
                           async: false,
                           success: function (o) {
                              if (o !=null && o == true) {
                            	  nui.alert("密码修改成功!");
                              } else {
                            	  nui.alert("密码修改失败!");
                              }
                           }
                       });
                   } else {
                	   nui.alert("输入的旧密码不正确!");
                   }
                }
            });
        }
        
    </script>
</body>
</html>
