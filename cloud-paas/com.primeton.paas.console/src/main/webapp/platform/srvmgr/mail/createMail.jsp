<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.service.MailService"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();	
	String instNumsJSON = SystemVariable.getServiceInstNumJSON(MailService.TYPE);
	String workNumsJSON = SystemVariable.getMaxMailWorkerNumsJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createMail">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >新增Mail服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_MAIL %>" class="nui-hidden" />
		        <table border="0">
		            <tr>
	                    <td style="width:20%;" align="right">独占主机：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[0].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*服务独占主机或者使用共享主机.</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">服务名称：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue"  class="nui-textbox" onvalidation="onNullValidation" value="<%=MailService.TYPE %>-default" width="90%"/>
	                    </td>
	                    <td align="left">*资源库服务名称，仅用于展示。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">主机套餐：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[2].attrValue"  class="nui-combobox" data='<%=hostTemplatesJSON %>' value="20130517J001" width="90%"/>
	                    </td>
	                    <td align="left">*选择需要创建的主机机型。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最大实例数：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_CLUSTER_MAX_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue"  class="nui-combobox" data='<%=instNumsJSON %>' value="1" width="90%"/>
	                    </td>
	                    <td align="left">*集群所允许的最大实例数。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最大邮件转发线程数：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_MAIL_MAX_MAILWORKER_NUM%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[4].attrValue"  class="nui-combobox" data='<%=workNumsJSON %>' value="1" width="90%"/>
	                    </td>
	                    <td align="left"></td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">数据库连接：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_JDBC_URL%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[5].attrValue"  class="nui-textbox" value="jdbc:mysql://127.0.0.1:3306/paas?autoReconnect=true&autoReconnectForPools=true" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left"></td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">数据库驱动：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_JDBC_DRIVER%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[6].attrValue"  class="nui-textbox" value="com.mysql.jdbc.Driver" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left"></td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">数据库用户名：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_JDBC_USER%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[7].attrValue"  class="nui-textbox" value="root" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left"></td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">数据库密码：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_JDBC_PASSWORD%>" class="nui-hidden" />
	                    	 <input id="mail_db_password" name="order.itemList[0].attrList[8].attrValue"  class="nui-password" vtype="rangeLength:4,20;" value="000000" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left"></td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">密码确认：</td>
	                    <td style="width:50%;">    
	                    	 <input id="mail_db_password2" class="nui-password" onblur="validatePassword()" value="000000" width="90%"/>
	                    </td>
	                    <td align="left">*密码确认</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最小连接数：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_JDBC_MIN_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input id="jdbc_min_pool_size" name="order.itemList[0].attrList[9].attrValue"  class="nui-textbox" value="5" vtype="range:1,10" rangeErrorText="数字必须在1~10之间" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*数据库连接池数</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最大连接数：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[10].attrName" value="<%=OrderItemAttr.ATTR_JDBC_MAX_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input id="jdbc_max_pool_size" name="order.itemList[0].attrList[10].attrValue"  class="nui-textbox" value="100" vtype="range:5,20" rangeErrorText="数字必须在5~20之间" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*数据库连接池数</td>
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
        
        //密码确认
        function validatePassword() {
        	var pwd1 = nui.get("mail_db_password").getValue();
    	    var pwd2 = nui.get("mail_db_password2").getValue();
    	    if (pwd1 == null || pwd1.length == 0) {
    	    	nui.get("mail_db_password").focus();
    	        return false;
    	    }
    	
    	    if (pwd2 == null || pwd2.length == 0) {
    	    	nui.get("mail_db_password2").focus();
    	        return false;
    	    }
    	
    	    if (pwd1 != pwd2) {
    	        nui.alert("两次输入的密码不相同，请重新输入!");
    	        //nui.get("mail_db_password").setValue("");
    	        nui.get("mail_db_password2").setValue("");
    	        nui.get("mail_db_password").focus();
    	        return false;
    	    }
    	    return true;
        }
        
        function validateJdbcPoolSize() {
        	var min = nui.get("jdbc_min_pool_size").getValue();
        	var max = nui.get("jdbc_max_pool_size").getValue();
        	if (parseInt(min) > parseInt(max)) {
        		return false;
        	}
        	return true;
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
            if (form.isValid() == false) return;
            
        	if(!validateJdbcPoolSize()){
       			nui.alert("连接数设置错误,最大连接数不能小于最小连接数 ！");
       			return;
        	}
        	if(!validatePassword()){
        		return;
        	}
        	
        	if (!confirm("你确定要提交Mail服务创建申请？")) {
    	        return false;
    	    }
            var data = form.getData().order;
            var json = nui.encode(data);
            
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/createMail",
				contentType: "application/json; charset=utf-8",
				data:json,
				type:"PUT",
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