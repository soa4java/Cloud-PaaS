<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@page import="com.primeton.paas.console.common.UserLoginFilter"%>
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>
<%@page import="com.primeton.paas.console.common.UserObject"%>
<%@page import="com.primeton.paas.console.common.UserLoginUtil.Result"%>

<%
	Result result = (Result) request.getAttribute("result");
	String message = "";
	if (result != null) {
		message = result.getMessage();
	}
	UserObject obj = (UserObject) session.getAttribute(UserLoginFilter.SESSION_USER_OBJECT);
	if (obj != null) {
		String homePage = SystemVariable.CONSOLE_TYPE_PLATFORM
				.equalsIgnoreCase(SystemVariable.getConsoleType()) 
				? "platform.jsp" //$NON-NLS-1$
				: "app.jsp"; //$NON-NLS-1$
		response.sendRedirect(request.getContextPath()
				+ "/frame/" + homePage); //$NON-NLS-1$
		return;
	}
	String ret = request.getParameter("ret") == null ? "value"
			: request.getParameter("ret");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title><%=NLSMessageUtils.getMessage("app.title") %></title>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/favicon.ico" type="image/x-icon"/>
<style>
body {
	margin: 0;
	padding: 0;
	border: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
	background-color: #f2f2f2;
}

.header {
	height: 45px;
}

.center {
	margin-right: auto;
	margin-left: auto;
	height: 600px;
	width: 700px;
}

.bottom {
	position: absolute;
	bottom: 0px;
	left: 0px;
	height: 40px;
	width: 100%;
	background-color: #161616;
}

.yy {
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	-webkit-box-shadow: #bbb 0px 0px 10px;
	-moz-box-shadow: #bbb 0px 0px 10px;
	box-shadow: #bbb 0px 0px 10px;
}

.radius {
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border-radius: 2px;
	border: 1px solid #ccc;
}

.y {
	-webkit-border-radius: 4px 4px 0px 0px;
	-moz-border-radius: 4px 4px 0px 0px;
	border-radius: 4px 4px 0px 0px;
	-webkit-box-shadow: #bbb 0px 0px 10px;
	-moz-box-shadow: #bbb 0px 0px 10px;
	box-shadow: #bbb 0px 0px 10px;
}

input:focus {
	outline: none;
}

.submitbutton {
	background-color: #3399ff;
}

.submitbutton:hover {
	background-color: #4ca6ff;
	cursor: pointer;
}

a {
	color: #ccc;
	text-decoration: none;
}
</style>

<script type="text/javascript">
// For redirect
if(top != window){
   top.location = window.location;
}
//初始加载
window.onload = function() {
	if (document.getElementById("loginFailure") != null) {
		document.getElementById("loginFailure").innerHTML="登录失败!";	
	}
	var username = "";
	var url = location.href;
	var begin = url.indexOf("username=");
	var end = begin;
	if (begin >= 0) {
		for (var i = begin + 9; i < url.length; i++) {
			end = i;
			if ('&' == url.charAt(i)
					|| '/' == url.charAt(i)
					|| '#' == url.charAt(i)) {
				break;
			}
		}
		username = url.substring(begin + 9, end);
		document.getElementById("paas_username").value = username;
	}
}

function validate_username() {
   var userID = document.getElementById("paas_username").value;
   var msg = document.getElementById("unMessage");
   if(userID == null || userID.length == 0) {
      msg.innerHTML = "请输入用户名";
      return false;
   } else {
      var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{0,29}$");
      if(!reg.test(userID)) {
         msg.innerHTML = "用户名格式不正确";
         return false;
      }
   }
   msg.innerHTML = "&nbsp;";
   return true;
}

function validate_password() {
   var password = document.getElementById("paas_password").value;
   var msg = document.getElementById("pwMessage");
   if(password == null || password.length == 0) {
      msg.innerHTML = "请输入密码";
      return false;
   } else if( password.length <6 || password.length >25) {
      msg.innerHTML = "密码长度必须为6-25位";
      return false;
   } else {
      msg.innerHTML = "&nbsp;";
   }
   return true;
}

function validate_onsubmit() {
	if(document.getElementById("loginFailure")!=null)
		document.getElementById("loginFailure").innerHTML="";		
   var usernameOK = validate_username();
   if(usernameOK == false) {
      return false;
   }
   var passwordOK = validate_password();
   if(passwordOK == false) {
      return false;
   }
   
   if(usernameOK && passwordOK) {
      document.getElementById("LoginForm").submit();
      return true;
   }
   return false;
}

function jump(event){
	 var event = event || window.event;
	 if(event.keyCode == 13){
		 validate_onsubmit();
	 }
}

</script>

</head>

<body onkeyup="jump()">


<div class="header">
<img src="<%=request.getContextPath() %>/images/login/logo.png" style="padding-left:10%;margin-top:20px;" />
</div>
<div class="center" style="margin-top:60px;">
	<div class="y" style="margin-left:10px;font-size:36px;background-color:#7abf50;color:#ffffff;padding-left:5px;padding-top:8px;padding-bottom:4px;width:690px;">
		<%=NLSMessageUtils.getMessage("app.title") %>
	</div>
	<div class="yy" style="width:700px;height:300px;background-color:#ffffff;padding:6px;">
		<div style="height:300px;width:400px;backgroud-color:red;float:left;">
			<a href="<%=request.getContextPath() %>/srv/myDownload/sdk" target="_blank"><img src="<%=request.getContextPath() %>/images/login/bg123.png"></a>
		</div>
		<div style="height:300px;width:300px;backgroud-color:yellow;float:right;">
			<form id="LoginForm" action="<%=request.getContextPath()%>/login.action" method="post" >
				<input type="hidden" name="gateTag" value="app" />
				<div class="radius" style="height:25px;width:260px;margin-top:44px;padding:4px 4px 4px 0px;">
					<input id="paas_username" name="username" value="" style="width:200px;height:25px;border:none;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="text" class="radius" >
					<img style="float:right" src="<%=request.getContextPath() %>/images/login/acbg.png">
				</div>


				<div style="width:80px; border:0px solid #333">    	
					<div style="float:left; height:20px; width:300px;">
						&nbsp;&nbsp;<font id="unMessage" color="red"></font>
					</div>   
				</div>

				<div class="radius" style="height:25px;width:260px;margin-top:44px;padding:4px 4px 4px 0px;">
					<input id="paas_password" name="password" value="" style="width:200px;height:25px;border:none;line-height:25px;margin-left:10px;font-size:18px;color:#161616" type="password" class="radius" >
					<img style="float:right" src="<%=request.getContextPath() %>/images/login/pdbg.png">
				</div>
				
				<div style="width:80px; border:0px solid #333">    	
					<div style="float:left; height:20px; width:300px;">
						&nbsp;&nbsp;<font id="pwMessage" color="red"></font>
						<%
							if (ret != null){
								if (ret.equals("false")) {
									out.println("<font id='loginFailure' color='red'></font>");
								}
							}
						%>
					</div>   
				</div>
				
				<div style="margin-top:34px;font-size:13px;color:#bbb;width:265px;margin-bottom:5px;">
					<span><a href="findPwd.jsp">忘记密码?</a></span>
					<span style="float:right"><a  href="register.jsp">用户注册</a></span>
				</div>
				<div onclick="javascript:validate_onsubmit(); return false;" class="radius submitbutton" style="height:40px;width:265px;text-align:center;color:#ffffff;line-height:40px;border:none;font-size:20px;">
					login
				</div>
			</form>
		</div>
	</div>

</div>
<div class="bottom">
	<div style="color:#a1a4a3;line-height: 40px;text-align:center">
		<%=NLSMessageUtils.getMessage("product.copyright")%>
	</div>
</div>

</body>

</html>