<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.service.MsgQueueService"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createMsgQueue">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >新增消息队列服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.orderType" value="SingleCreateMsgQueue" class="nui-hidden" />
            	<input name="order.itemList[0].itemType" value="<%=MsgQueueService.TYPE %>" class="nui-hidden" />
		        <table border="0">
		            <tr>
	                    <td width="25%" align="right">独占主机：</td>
	                    <td width="45%">    
	                         <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[0].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="Y" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="80%"/>
	                    </td>
	                    <td width="30%">*服务独占主机或者使用共享主机.</td>
	                </tr>
		            <tr>
	                    <td align="right">服务名称：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue" class="nui-textbox" onvalidation="onNullValidation" value="<%=MsgQueueService.TYPE %> Service" width="80%"/>
	                    </td>
	                    <td>*服务显示名称，与业务无关。</td>
	                </tr>
		            <tr>
	                    <td align="right">主机套餐：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[2].attrValue"  class="nui-combobox" data='<%=hostTemplatesJSON %>' value="20130517J001" width="80%"/>
	                    </td>
	                    <td>*选择需要创建的主机机型。</td>
	                </tr>
	                <tr>
	                    <td align="right">用户名：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_MSG_QUEUE_USER%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue"  class="nui-textbox" value="paas" width="80%" />
	                    </td>
	                    <td>* 用户名，默认用户名paas</td>
	                </tr>
	                <tr>
	                    <td align="right">密码：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_MSG_QUEUE_PASSWORD%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[4].attrValue"  class="nui-password" value="paas" width="80%" />
	                    </td>
	                    <td>* 用户名，默认密码paas</td>
	                </tr>
		        </table>            
            </div>
        </fieldset>
       
        <div style="text-align:center;padding:10px;">  
        	<a class="nui-button" style="width:8%; " onclick="onAdd">新增</a>  &nbsp;           
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
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
        	if (!confirm("你确定要提交消息队列服务创建申请？")) {
    	        return false;
    	    }
            
            var data = form.getData().order;
            var json = nui.encode(data);
            
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/createMsgQueue",
				contentType: "application/json; charset=utf-8",
				data: json,
				type: "PUT",
                success: function (text) {}
            });
            nui.alert("订单已提交，请到订单管理中查看处理结果！");
            closeWindow("ok");
        }
        
        function onCancel() {
        	closeWindow("cancel");
        }
       
    </script>
</body>
</html>