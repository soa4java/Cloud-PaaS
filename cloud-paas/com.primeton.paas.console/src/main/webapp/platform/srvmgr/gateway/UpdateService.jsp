<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.service.GatewayService"%>

<%
	// String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
	// String gatewaySizeJSON = SystemVariable.getMaxGatewaySizeJSON();
	String gatewayMaxConnectionJSON = SystemVariable.getMaxGatewayConnectionJSON();
	
	String cluster = request.getParameter("cluster");
	System.out.println("cluster = " + cluster);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createGateway">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >更新网关服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.itemList[0].itemType" value="<%=GatewayService.TYPE %>" class="nui-hidden" />
            	<input name="order.orderType" value="UpdateGateway" class="nui-hidden" /> <!-- 更改配置 -->
            	<input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_CLUSTER_ID%>" class="nui-hidden" />
            	<input name="order.itemList[0].attrList[0].attrValue" value="<%=cluster %>" class="nui-hidden"/>
		        <table border="0">
		            <tr>
	                    <td width="25%" align="right">服务名称：</td>
	                    <td width="45%" >    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue" class="nui-textbox" onvalidation="onNullValidation" value="<%=GatewayService.TYPE %> Service" width="80%"/>
	                    </td>
	                    <td width="30%">*服务显示名称，与业务无关。</td>
	                </tr>
	                <tr>
	                    <td align="right">最大连接数：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_GATEWAY_MAX_CONNECTION%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[2].attrValue"  class="nui-combobox" data='<%=gatewayMaxConnectionJSON %>' value="100000" width="80%"/>
	                    </td>
	                    <td>* 最大连接数(终端数)</td>
	                </tr>
	                <tr>
	                    <td align="right">前置服务：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_GATEWAY_PRE_SERVERS%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue" class="nui-textbox" onvalidation="onNullValidation" value="127.0.0.1:8888" width="80%"/>
	                    </td>
	                    <td>*格式：ip1:port1,ip2:port2 ...</td>
	                </tr>
		        </table>            
            </div>
        </fieldset>
       
        <div style="text-align:center;padding:10px;">  
        	<a class="nui-button" style="width:8%; " onclick="onAdd">更改</a>  &nbsp;           
        	<a class="nui-button" style="width:8%; " onclick="onCancel">取消</a>             
        </div>        
    </form> 	
 	
    <script type="text/javascript">        
        nui.parse();
        
        var form = new nui.Form("form1"); 
        
        function onNullValidation(e) {
        	if (!e.value) {
        		e.errorText = "不能为空！";
        		e.isValid = false;
        	}
        }
        
        function closeWindow(action) {            
            if (window.CloseOwnerWindow) {
            	return window.CloseOwnerWindow(action);
            } else {
            	window.close();            
            }
        }
        
        function onAdd() {
        	if (!confirm("你确定要提交网关服务更改申请？")) {
    	        return false;
    	    }
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            
            var data = form.getData().order;
            var json = nui.encode(data);
            
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/updateGateway",
				contentType: "application/json; charset=utf-8",
				data: json,
				type: "PUT",
                success: function (text) {}
            });
            nui.alert("订单已提交，请到订单管理中查看处理结果！");
            closeWindow("ok");
        }
        
        function onCancel(){
        	closeWindow("cancel");
        }
       
    </script>
</body>
</html>