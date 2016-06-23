<%@ page contentType="text/html;charset=utf-8" %>

<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script src="<%=request.getContextPath() %>/common/nui/jquery/jquery-1.9.js" type="text/javascript"></script> 
<style>

.header{
	height: 45px;
}
.center{
	margin-right: auto;
	margin-left: auto;
	height: 600px;
	width: 500px;
}
body{
	background-size:cover;
	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
}
.bottom{
	position: absolute;
	bottom: 0px;
	left:0px;
	height:40px;
	width: 100%;
	background-color: #161616;
}
.yy{
-webkit-border-radius: 4px;
-moz-border-radius: 4px;
border-radius: 4px;
-webkit-box-shadow: #bbb 0px 0px 10px;
-moz-box-shadow: #bbb 0px 0px 10px;
box-shadow: #bbb 0px 0px 10px;
}
.radius{
	-webkit-border-radius: 2px;
-moz-border-radius: 2px;
border-radius: 2px;
border: 1px solid #ccc;
}
.y{
-webkit-border-radius: 4px 4px 0px 0px;
-moz-border-radius: 4px 4px 0px 0px;
border-radius: 4px 4px 0px 0px;
-webkit-box-shadow: #bbb 0px 0px 10px;
-moz-box-shadow: #bbb 0px 0px 10px;
box-shadow: #bbb 0px 0px 10px;
}

.submitbutton{
	background-color:#3399ff;
}
.submitbutton:hover{
	background-color:#4ca6ff;
	cursor: pointer;
}
a{
	color:#ccc;
	text-decoration: none;
}
select:focus{
	outline: none;
}
input:focus+div{
	display:block;
}
.e{
	font-size:13px;color:red;margin-left:80px;margin-top:5px;float:left;
}
</style>

<script type="text/javascript">

function validateUserId() {
	   var userID = document.getElementById("userId").value;
	   var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,19}$");
	   if(reg.test(userID)) {
		  document.getElementById("paas_user_id_msg").innerHTML = "";
	      return true;
	   }
	   document.getElementById("paas_user_id_msg").innerHTML = "长度为4-15位，以字母开头、允许数字和下划线";
	   return false;
}

/** 手机号码*/
function validatePhone() {
   var phone = document.getElementById("phone").value;
   if(phone == null || phone.trim().length == 0) {
      document.getElementById("paas_user_phone_msg").innerHTML = "手机号不能空";
      return false;
   }
   var reg = new RegExp("^[0-9][0-9-]{10,13}$");
   if(reg.test(phone)) {
	   document.getElementById("paas_user_phone_msg").innerHTML = "";
      return true;
   }else{
   	  document.getElementById("paas_user_phone_msg").innerHTML = "请输入正确的手机号";
   	  return false;  
   }
}

/* Validate submit form */
function validateSubmit() {
   var v1 = validateUserId();
   var v6 = validatePhone();
   
   if(v1  && v6 ) {
       if(confirm("你确定要找回密码？")) {
		  saveData();
          return true;
       }
   }
   return false;
}

function saveData() {
    var jsonuserinfo = $('#form1').serialize();
    $.ajax({
        url: "<%=request.getContextPath() %>/srv/audit/resetForgottenPwd",
        type: "post",
        data: jsonuserinfo,
        success: function (text) {
        	var o = text;
        	if(o!=null && o.result==true){
        		alert("密码找回短信已经发送，请注意查收！");
        		location.href = "login.jsp";
        	}else{
        		alert("密码找回失败，请检查账户或者手机是否正确！");
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText);
        }
    });
}

$.fn.serializeObject = function()  
{  
   var o = {};  
   var a = this.serializeArray();  
   $.each(a, function() {  
       if (o[this.name]) {  
           if (!o[this.name].push) {  
               o[this.name] = [o[this.name]];  
           }  
           o[this.name].push(this.value || '');  
       } else {  
           o[this.name] = this.value || '';  
       }  
   });  
   return o;  
};

</script>

</head>

<body>

<div class="header">
<img src="<%=request.getContextPath() %>/images/login/logo.png" style="padding-left:10%;margin-top:20px;" />
</div>
<div class="center" style="margin-top:100px;">
	<div class="y" style="margin-left:10px;font-size:22px;background-color:#7abf50;color:#ffffff;padding-left:5px;padding-top:8px;padding-bottom:4px;width:375px;">
		<%=NLSMessageUtils.getMessage("find.password") %> -- <%=NLSMessageUtils.getMessage("platform.title") %>
	</div>
	<div class="yy" style="width:500px;height:200px;background-color:#ffffff;padding:6px;">
		<div style="height:300px;width:500px;backgroud-color:yellow;float:right;">
			<form id="form1">
				<div class="errorcode" style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block">用户名</span>
					<input  id="userId" name="userId"  style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616"  type="text" class="radius" >
					<div id="paas_user_id_msg" class="e"></div>
				</div>

				<div style="margin-left:30px;padding-top:20px;">
					<span style="width:70px;display:inline-block">手机号码</span>
						<input id="phone" name="phone" style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" >
						<div id="paas_user_phone_msg" class="e"></div>
				</div>

                <div style="margin-left:30px;padding-top:25px;">
					
						<div onclick="javascript:validateSubmit()" class="radius submitbutton" style="height:40px;width:335px;text-align:center;color:#ffffff;line-height:40px;border:none;font-size:20px;margin-left:80px;">
					找回密码
				</div>
				
			</form>
		</div>
	</div>
</div>
<div class="bottom">
	<div style="color:#a1a4a3;line-height: 40px;text-align:center">
		<%=NLSMessageUtils.getMessage("product.copyright") %>
	</div>
</div>

</body>
</html>