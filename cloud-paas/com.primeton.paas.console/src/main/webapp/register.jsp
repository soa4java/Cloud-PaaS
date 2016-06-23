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
	background-color: #f2f2f2;margin:0;padding:0;border:0;width:100%;height:100%;
}
.bottom{
	margin-top: 120px;
	height:40px;
	width: 100%;
	background-color: #161616;
	text-align:center
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

.required {
    color: #ff0000;
}
.required_ok {
   width:30px;
   float: right;
   margin-top: 5px;
}
</style>


<script type="text/javascript">

var imgOK = '<img src="<%=request.getContextPath() %>/images/login/accept.png" style="height:15px;" />';
var imgError = '<img src="<%=request.getContextPath() %>/images/login/block.png" style="height:15px;" />';

function validateUserId() {
	   var userID = document.getElementById("paas_user_id").value;
	   var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,19}$");
	   if(reg.test(userID) && isExistUser(userID)) {
	      return true;
	   }
	   document.getElementById("paas_user_id_msg").innerHTML = imgError;
	   return false;
}


/* check userID */
function isExistUser(userID) {
	var rtn = false;
    $.ajax({
    	url: "<%=request.getContextPath() %>/srv/audit/checkUserId/" +userID,
    	async: false,
        success: function (text) {
        	var o = text;
        	if(o!=null && o.result==true){
        		document.getElementById("paas_user_id_msg").innerHTML = imgError;
          		alert("帐号已被使用！");
          		rtn = false;
        	}else{
        		document.getElementById("paas_user_id_msg").innerHTML = imgOK;
        		rtn = true;
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	document.getElementById("paas_user_id_msg").innerHTML = imgError;
        	return false;
        }
     });
    return rtn;
}

function validateUserName() {
	   var userName = document.getElementById("paas_user_name").value;
	   if(userName == null || userName.trim().length == 0) {
	      document.getElementById("paas_user_name_msg").innerHTML = imgError;
	      return false;
	   }
	   document.getElementById("paas_user_name_msg").innerHTML = imgOK;
	   return true;
}

function validatePassword() {
	   var password = document.getElementById("paas_user_password").value;
	   if(password == null || password.trim().length == 0 || password.trim().length < 6) {
	      document.getElementById("paas_user_password_msg").innerHTML = imgError;
	      return false;
	   }
	   document.getElementById("paas_user_password_msg").innerHTML = imgOK;
	   return true;
}

function validateEmail() {
	   var email = document.getElementById("paas_user_email").value;
	   var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_-]{2,20}@[a-zA-Z0-9.]{3,20}$");
	   if(reg.test(email)) {
		  document.getElementById("paas_user_email_msg").innerHTML = imgOK;
	      return true;
	   }
	   document.getElementById("paas_user_email_msg").innerHTML = imgError;
	   return false;   
}

/** 手机号码*/
function validatePhone() {
   var phone = document.getElementById("paas_user_phone").value;
   if(phone == null || phone.trim().length == 0) {
      document.getElementById("paas_user_phone_msg").innerHTML = imgError;
      return false;
   }
   var reg = new RegExp("^[0-9][0-9-]{10,13}$");
   if(reg.test(phone)) {
	   document.getElementById("paas_user_phone_msg").innerHTML = imgOK;
      return true;
   }else{
   	  document.getElementById("paas_user_phone_msg").innerHTML = imgError;
   	  return false;  
   }
}

/** 固定电话*/
function validateTel() {
   var tel = document.getElementById("paas_user_tel").value;
   if(tel == null || tel.trim().length == 0) {
   	  document.getElementById("paas_user_tel_msg").innerHTML = imgOK;
      return true;
   }
   var reg = new RegExp("^[0-9][0-9-]{5,13}$");
   if(reg.test(tel)) {
      document.getElementById("paas_user_tel_msg").innerHTML = imgOK;
      return true;
   }
   document.getElementById("paas_user_tel_msg").innerHTML = imgError;
   return false;
}

/** 地址 */
function validateAddress() {
   var address = document.getElementById("paas_user_address").value;
   if(address != null && address.length > 280) {
      document.getElementById("paas_user_address_msg").innerHTML = imgError;
      return false;
   }
   document.getElementById("paas_user_address_msg").innerHTML = imgOK;
   return true;
}

function validateIntroduction() {
   var address = document.getElementById("paas_user_introduction").value;
   if(address != null && address.length > 500) {
      document.getElementById("paas_user_introduction_msg").innerHTML = imgError;
      return false;
   }
   document.getElementById("paas_user_introduction_msg").innerHTML = imgOK;
   return true;
}

/* Validate submit form */
function validateSubmit() {
   var v1 = validateUserId();
   var v3 = validatePassword();
   //var v4 = validatePassword1();
   var v5 = validateEmail();
   var v2 = validateUserName();
   var v6 = validatePhone();
   var v7 = validateTel();
   var v8 = validateAddress();
   var v9 = validateIntroduction();
   
   if(v1 && v2 && v3 && v5 && v6 && v7 && v8 && v9) {
       if(confirm("你确定要提交注册申请？")) {
		  saveData();
          return true;
       }
   }
   return false;
}


function saveData() {
    var jsonuserinfo = $('#form1').serializeObject();
    var json = JSON.stringify(jsonuserinfo);
    $.ajax({
        url: "<%=request.getContextPath() %>/srv/audit/registerAppUser",
        type: "post",
        data: { keyData: json },
        success: function (text) {
        	var o = text;
        	if(o!=null && o.result==true){
        		alert("用户注册成功，等待审批！");
        		location.href = "login.jsp";
        	}else{
        		alert("用户注册失败,请稍后再试！");
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
<div class="center" style="margin-top:60px;">
	<div class="y" style="margin-left:10px;font-size:22px;background-color:#7abf50;color:#ffffff;padding-left:5px;padding-top:8px;padding-bottom:4px;width:375px;">
		<%=NLSMessageUtils.getMessage("user.register") %> -- <%=NLSMessageUtils.getMessage("app.title") %>
	</div>
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/audit/registerAppUser">
	<div class="yy" style="width:500px;height:270px;background-color:#ffffff;padding:6px;">
		<div style="height:300px;width:500px;backgroud-color:yellow;float:right;">
			<form >
				<div style="text-align:center;font-size:18px;padding-top:10px;color:#bbb">必填信息</div>
				<div class="errorcode" style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block">用户名</span>
					<input name="userId" id="paas_user_id" style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616"  type="text" class="radius" onblur="validateUserId();">
					<span id="paas_user_id_msg" class="required_ok">&nbsp;&nbsp;</span>
					<div class="e">
						*长度为4-15位，以字母开头、允许数字和下划线</div>
				</div>

				<div style="margin-left:30px;padding-top:20px;">
					<span style="width:70px;display:inline-block">设置密码</span>
						<input name="password" id="paas_user_password" style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="password" class="radius" onblur="validatePassword();">
						<span id="paas_user_password_msg" class="required_ok">&nbsp;&nbsp;</span>
						<div class="e">&nbsp;*长度为6-25位英文字母、数字和特殊符号的组合</div>
				</div>

				<div style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block">邮箱</span>
						<input name="email" id="paas_user_email" style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" onblur="validateEmail();">
						<span id="paas_user_email_msg" class="required_ok">&nbsp;&nbsp;</span>
						<div class="e">&nbsp;*建议使用工作邮箱</div>
				</div>
				<div style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block">姓名</span>
						<input  name="userName" id="paas_user_name"  style="width:108px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" onblur="validateUserName();">
						<span id="paas_user_name_msg" style="width:30px;margin-top:5px;float:center;">&nbsp;&nbsp;</span>
						<span style="width:40px;display:inline-block;padding-left:13px;">手机</span>
						<input  name="phone" id="paas_user_phone"  style="width:125px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" onblur="validatePhone();">
						<span id="paas_user_phone_msg" class="required_ok">&nbsp;&nbsp;</span>
						<div class="e">&nbsp;*强烈建议使用真实姓名和手机号码</div>
				</div>
		</div>
	</div>

	<div class="yy" style="width:500px;height:320px;background-color:#ffffff;padding:6px;margin-top:40px;">
		<div style="height:300px;width:500px;backgroud-color:yellow;float:right;">
				<div style="text-align:center;font-size:18px;padding-top:10px;color:#bbb">选填信息</div>

				<div style="margin-left:30px;padding-top:25px;">
					<span style="width:80px;display:inline-block">性别</span>
						<select name="gender" style="width:130px;border: 1px solid #ccc;height:28px;line-height:28px;">
							<option value="1">男</option>
							<option value="2">女</option>
						</select>
						<span style="width:40px;display:inline-block;padding-left:13px;">固话</span>
						<input name="tel" id="paas_user_tel"  style="width:125px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" onblur="validateTel();">
						<span id="paas_user_tel_msg" class="required_ok">&nbsp;&nbsp;</span>
				</div>
				<div style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block">地址</span>
					<input name="address" id="paas_user_address" style="width:325px;height:25px;line-height:25px;margin-left:10px;font-size:18px;color:#161616"  type="text" class="radius" onblur="validateAddress();">
					<span id="paas_user_address_msg" class="required_ok">&nbsp;&nbsp;</span>
					<div class="e">&nbsp;0-255字 </div>
				</div>

				<div style="margin-left:30px;padding-top:25px;">
					<span style="width:70px;display:inline-block;">备注</span>
					<textarea name="notes" id="paas_user_introduction" style="width:325px;height:70px;line-height:25px;margin-left:10px;font-size:18px;color:#161616;resize:none" type="text" class="radius" onblur="validateIntroduction();" ></textarea>
					<span id="paas_user_introduction_msg" class="required_ok">&nbsp;&nbsp;</span>
					<div class="e">&nbsp;0-100字  </div>
				</div>
				<div style="margin-left:30px;padding-top:25px;">					
					<div onclick="javascript:validateSubmit()" class="radius submitbutton" style="height:40px;width:335px;text-align:center;color:#ffffff;line-height:40px;border:none;font-size:20px;margin-left:80px;">
					注册
				</div>
		</div>
	</div>
</div>
</form>
</div>
<div class="bottom">
	<div style="color:#a1a4a3;line-height: 40px;text-align:center">
		<%=NLSMessageUtils.getMessage("product.copyright") %>
	</div>
</div>
</body>
</html>