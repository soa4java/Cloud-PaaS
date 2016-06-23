<%@ page contentType="text/html;charset=utf-8" %>

<%@page import="com.primeton.paas.console.common.UserLoginFilter"%>
<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>
<%@page import="com.primeton.paas.console.common.UserObject"%>

<%
	Object object = session.getAttribute(UserLoginFilter.SESSION_USER_OBJECT);
	UserObject user = (null != object && object instanceof UserObject) ? (UserObject)object : null;
	String userId = null == user ? "anonymous" : user.getUserId();;
	if (null == user) {
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><%=NLSMessageUtils.getMessage("app.title") %></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script> 
    <link rel="stylesheet" media="screen" href="<%=request.getContextPath() %>/css/icon.css">

    <style type="text/css">
	    body{
	        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
	    }    
	    .header
	    {
	        background:url(../images/header.gif) repeat-x 0 -1px;
	    }
	    .Node
	    {
	        background:url(../images/arrow.png) no-repeat;width:16px;height:16px;
	    }
	    
    </style>
</head>
<body>
    
<!--Layout-->
<div id="layout1" class="nui-layout" style="width:100%;height:100%;">
    <div class="header" region="north" height="65" showSplit="false" showHeader="false" style="padding: 10px;" >
        <h1 style="margin:0;padding:0px;cursor:default;font-family:微软雅黑,黑体,宋体;">
           	<div id="logo-img" style="background: url(../images/logo.png) no-repeat; width: 161px; height: 40px; float: left;">
			</div>
			&nbsp; &nbsp; &nbsp; &nbsp;   
			<!--
			<font size="+2" color="White"> -->
			<font size="+2">
			<%=NLSMessageUtils.getMessage("app.title") %>
			</font>
        </h1>
        <div style="position:absolute;top:18px;right:10px;">
           <font ><%=userId %>&nbsp;&nbsp;|
           <a class="nui-button nui-button-iconTop" iconCls="icon-close" onclick="onClose"  plain="true" >退出</a>      
           </font>  
        </div>
    </div>
    
    <div title="south" region="south" showSplit="false" showHeader="false" height="30" >
    	<!-- TODO NLS -->
        <div style="line-height:28px;text-align:center;cursor:default"><%=NLSMessageUtils.getMessage("product.copyright") %></div>
    </div>
    <div title="center" region="center" style="border:0;" bodyStyle="overflow:hidden;">
        <!--Splitter-->
        <div class="nui-splitter" style="width:100%;height:100%;" borderStyle="border:0;">
            <div size="180" maxSize="250" minSize="100" showCollapseButton="true" style="border:0;">
                <!--OutlookTree-->
                <div id="leftTree" class="nui-outlooktree" url="<%=request.getContextPath() %>/common/data/app-menu.txt" onnodeselect="onNodeSelect"
                    textField="text" idField="id" parentField="pid"   expandOnLoad="true"
                >
                </div>
                
            </div>
            <div showCollapseButton="false" style="border:0;">
                <!--Tabs-->

			   <div id="mainTabs" class="nui-tabs" activeIndex="2" style="width:100%;height:100%;"      
                     plain="false" onactivechanged="onTabsActiveChanged"  refreshOnClick="true"
                >
				<!--<div title="我的订单" url="../app/order/myOrderMgr.jsp" iconCls="app_myOrder">-->
                </div>

            </div>        
        </div>
    </div>
</div>

    

<script type="text/javascript">
   nui.parse();

   var tree = nui.get("leftTree");
   init();
   
   function init(){
	   var tabs = nui.get("mainTabs");
	   tab = {};
       tab._nodeid = 'myOrderMgr';
       tab.name = "tab$myOrderMgr";
       tab.title = '我的订单';
       tab.showCloseButton = true;
       tab.url = '<%=request.getContextPath() %>/app/open/order/myOrderMgr.jsp';
       tab.iconCls = 'app_myOrder';
       tabs.addTab(tab);
       tabs.activeTab(tab);
   }

   function showTab(node) {
       var tabs = nui.get("mainTabs");
       var id = "tab$" + node.id;
       var tab = tabs.getTab(id);
       if (!tab) {
           tab = {};
           tab._nodeid = node.id;
           tab.name = id;
           tab.title = node.text;
           tab.showCloseButton = true;
           tab.url = node.url;
           tab.iconCls = node.iconCls;
           tabs.addTab(tab);
       }
       tabs.activeTab(tab);
   }

   function onNodeSelect(e) {
       var node = e.node;
       var isLeaf = e.isLeaf;

       if (isLeaf) {
           showTab(node);
       }
   }

   function onClose(e) {
       var text = this.getText();
       window.location.href="logout.jsp";
   }
   
   function onQuickClick(e) {
       tree.expandPath("datagrid");
       tree.selectNode("datagrid");
   }

   function onTabsActiveChanged(e) {
       var tabs = e.sender;
       var tab = tabs.getActiveTab();
       if (tab && tab._nodeid) {
           
           var node = tree.getNode(tab._nodeid);
           if (node && !tree.isSelectedNode(node)) {
               tree.selectNode(node);
           }
       }
   }

</script>

</body>
</html>