<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<% 
	String limit = request.getParameter("limit");
	String total = request.getParameter("total");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <title>Primeton PAAS Product License Warning</title>
</head>
<body>
	<h1 style="color: red;">Authorized Warning</h1>
	<br/>
	<div style="color: green; font-size: 120%;">
	当前产品License授权允许平台使用所有主机（包括物理机、虚拟机）的CPU核心总数是：
	<span style="color: red;"><%=limit %></span>。
	当前平台所有主机（包括物理机、虚拟机）的CPU核心总数是：
	<span style="color: red;"><%=total %></span>，
	已经超出了授权限制，部分功能将被禁用。预计您的PAAS规模还在继续增长，
	请联系产品供应商更换PAAS产品License，以满足PAAS运维的需要。
	</div>
	<br/><br/>
	<div style="color: gray;">
		<h3>联系方式：</h3>
		<h4>地址：上海市浦东新区张江高科技园区碧波路456号中科大上海研发中心4楼</h4>
		<h4>电话：021-50805188</h4>
		<h4>邮编：201203</h4>
		<h4>官方网站：<a target="_blank" href="http://www.primeton.com">www.primeton.com</a></h4>
		<h4>官方微博：<a target="_blank" href="http://t.sina.com.cn/primetonsoftware">t.sina.com.cn/primetonsoftware</a></h4>
		<h4>官方社区：<a target="_blank" href="http://www.gocom.cc">www.gocom.cc</a></h4>
		<h4>普元平台：<a target="_blank" href="http://p.primeton.com">p.primeton.com</a></h4>
	</div>
</body>
</html>