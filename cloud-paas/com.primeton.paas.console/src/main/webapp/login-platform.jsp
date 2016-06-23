<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@page import="com.primeton.paas.console.common.UserLoginFilter"%>
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>
<%@page import="com.primeton.paas.console.common.UserObject"%>
<%@page import="com.primeton.paas.console.common.UserLoginUtil.Result"%>
<%@page import="com.primeton.paas.console.common.LicenseManager"%>

<%
	//设置无缓存 
	response.setHeader("progma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String contextPath = request.getContextPath();

	Result result = (Result) request.getAttribute("result");

	String message = "";
	if (result != null) {
		message = result.getMessage();
	}

	UserObject user = (UserObject) session.getAttribute(UserLoginFilter.SESSION_USER_OBJECT);
	if (user != null) {
		String homePage = SystemVariable.CONSOLE_TYPE_PLATFORM
				.equalsIgnoreCase(SystemVariable.getConsoleType()) 
				? "platform.jsp" //$NON-NLS-1$
				: "app.jsp"; //$NON-NLS-1$
		response.sendRedirect(request.getContextPath()
				+ "/frame/" + homePage); //$NON-NLS-1$
	}
	
	long total = LicenseManager.getManager().getTotalCpu();
	long limit = LicenseManager.getManager().getCpuLimits();
	boolean warn = total > limit;
%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<title><%=NLSMessageUtils.getMessage("product.name")%></title>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/favicon.ico" type="image/x-icon"/>
<script type="text/javascript">

	if (top != window) {
		top.location.href = top.location.href;
		top.location.reload;
	}

	// 初始加载
	window.onload = function() {
		if ($("loginFailure") != null) {
			$("loginFailure").innerHTML = "登录失败!";
		}
		// 用户名
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
			document.getElementById("username").value = username;
		}
	}

	// 定义通过id获取页面控件的方法
	function $(id) {
		return document.getElementById(id);
	}

	// 国际化设置
	function setOptions() {
		var options = document.getElementsByTagName("option");
		for (var i = 0; i < options.length; i++) {
			var lang = options[i].id;
			options[i].text = eval('message.language.' + lang);
		}
	}

	// 登录表单空值验证
	function sub() {
		if ($("loginFailure") != null && $("loginFailure").innerHTML != "")
			$("loginFailure").innerHTML = "";
		var userName = $("username").value;
		if (userName == "") {
			$("loginMsg").innerHTML = "请输入用户名！";
			return;
		}
		var passwd = $("password").value;
		if (passwd == "") {
			$("loginMsg").innerHTML = "请输入密码！";
			return;
		}
		document.getElementById("form1").submit();
	}

	// 登录表单重置
	function reset() {
		document.getElementById("form1").reset();
	}

	// 获取键盘 Enter 键事件并响应登录
	function keyboardLogin(evt) {
		var event = evt ? evt : window.event;
		if (13 == event.keyCode) {
			sub();
		}
	}
</script>

<link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/platform/login.css" />
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/platform/css.css" />

<style type="text/css">
<!--
div#nifty{margin:0;background:#ced9e7}
p {padding:10px}
div.rtop {display:block;background:#ced9e7}
div.rtop div {display:block;height:1px;overflow:hidden;background:#9BD1FA}
div.r1{margin:0 5px}
div.r2{margin:0 3px}
div.r3{margin:0 2px}
div.rtop div.r4 {margin:0 1px;height:2px}

lis{  border:1px solid #c7d9e9; }
-->
</style>

<link href="js/DD_belatedPNG_0.0.8a.js" type="text/javascript" />
</head>

<%
	String original_url = null;
	Object objAttr = request.getAttribute("original_url");
	if (objAttr != null) {
		original_url = (String) objAttr;
	}
%>

<body style="background-color:#0d4c9b;background:url(<%=contextPath%>/images/platform/index.jpg) 0 0 no-repeat; ">
	
<% if (warn) { %>
	<marquee style="width: 100%; height: 100px" scrollamount="5" direction="left" >
		<font color="red" size="8">
			<a target="_blank" href="license.jsp?limit=<%=limit%>&total=<%=total%>">Authorized Warning (license limits), click here for details.</a>
		</font>
	</marquee>
<% } %>

<form id="form1" action="<%=request.getContextPath()%>/login.action" method="post" >

<div class="navz">
<div class="clearfloatz" style="height:5px;"></div>
<div class="nav-div-bottomz" style="margin-top:98px; margin-left: 330px;"><table width="80%" border="0" align="right" cellpadding="0" cellspacing="0" style="margin-top:10px; margin-bottom:10px; word-break:keep-all;white-space:nowrap;">
      <tr height="20px"><td></td></tr>
      <tr>
        <td height="38"><div ID="oFilterDIV" STYLE="font:12px; 
        filter:progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, 
        Color='#19498f', Positive='true')" align="right"><span class="tz1" id="nameLabel" >用户名：</span></div></td>
        <td colspan="2"><span class="t2">
          <!-- <input name="text" type="text" class="textboxz" style="height:17px;"/> -->
          <input id="username" name="username" value="" class="textboxz" style="height:17px;" tabindex="1" autocomplete="false" htmlEscape="true"/>
          
        </span></td>
      </tr>
      <tr>
        <td height="38"><div ID="oFilterDIV" STYLE="font:12px; 
        filter:progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, 
        Color='#19498f', Positive='true')" align="right"><span class="tz1" id="pswLabel" >密码：</span></div></td>
        <td width="65%">
          <!-- <input name="text" type="text" class="textboxz" style="height:17px;"/> -->
          <input type="password" id="password" value="" class="textboxz" style="height:17px;" tabindex="2" name="password" htmlEscape="true" onkeydown="keyboardLogin(event)"/>
        </span></td>
        <td width="19%"><div ID="oFilterDIV" STYLE="font:12px; 
        filter:progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, 
        Color='#19498f', Positive='true')" align="right"><span class="tz1"><!-- <a href="#">忘记密码？</a> --></span></div></td>
      </tr>
	  <tr>
        <td height="45">&nbsp;</td><input style="display:none" type="submit"/>
        <td colspan="2"><a href="#" onclick="sub();"><img src="<%=contextPath%>/images/platform/indexbuttom1.png" width="75" height="30" border="0" /></a> <a href="#" onclick="reset();"><img src="<%=contextPath%>/images/platform/indexbuttom2.png" width="75" height="30" border="0" /></a></td>
      </tr>
      
      <tr>
		<td height="15" width="230" colspan=2>
			<font id="loginMsg" color="red"></font>
			<%
				String obj = request.getParameter("ret");
				if (obj != null) {
					if (obj.equals("false")) {
						out.println("<font id='loginFailure' color='red'>登录失败</font>");
					}
				}
			%>
		</td> 
	  </tr>	
	  <tr>
        <td height="60" colspan="3" valign="bottom"><div ID="oFilterDIV" STYLE="font:12px; 
        filter:progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, 
        Color='#19498f', Positive='true')" align="right"><span class="tz1"><a href="#">&nbsp<!-- <%=NLSMessageUtils.getMessage("product.copyright") %> --></a></span></div></td>
      </tr>
	  <tr height="35" valign="bottom"><td></td></tr>
    </table>
    
	<script type="text/javascript">
		// IE
		function fnDepress() {
			var bState = oFilterDIV.filters
					.item('DXImageTransform.Microsoft.dropshadow').enabled;
			oFilterDIV.filters.item('DXImageTransform.Microsoft.dropshadow').enabled = !bState;
		}
	</script>

</div>
</div>

</form>
</body>
</html>

