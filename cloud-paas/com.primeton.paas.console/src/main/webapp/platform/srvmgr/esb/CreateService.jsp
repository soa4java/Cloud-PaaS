<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.cluster.EsbCluster"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<%
	// 主机套餐JSON
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
	// 数据库类型JSON
	String databaseTypesJSON = SystemVariable.getDatabaseTypesJSON();
	// ESB Server 实例数可选
	String serverSizesJSON = SystemVariable.getEsbServerSizesJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createJobCtrl">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >新增ESB服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.orderType" value="CreateESB" class="nui-hidden" />
            	<input name="order.itemList[0].itemType" value="<%=EsbCluster.TYPE %>" class="nui-hidden" />
		        <table border="0">
		            <tr>
	                    <td width="25%" align="right">Server实例：</td>
	                    <td width="45%">    
	                         <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_CLUSTER_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[0].attrValue" class="nui-combobox" data='<%=serverSizesJSON %>' value="1" width="80%"/>
	                    </td>
	                    <td width="30%">*ESB Server 实例数量（集群）</td>
	                </tr>
		            <tr>
	                    <td align="right">服务名称：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue" class="nui-textbox" onvalidation="onNullValidation" value="<%=EsbCluster.TYPE %> Service" width="80%"/>
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
	                    <td align="right">数据库类型：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_DATABASE_TYPE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue"  class="nui-combobox" data='<%=databaseTypesJSON %>' value="MySql" width="80%"/>
	                    </td>
	                    <td>* 数据库类型</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 URL：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_C3P0_URL%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[4].attrValue"  class="nui-textbox" value="" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，连接地址</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 用户名：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_C3P0_USER_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[5].attrValue"  class="nui-textbox" value="" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，用户名</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 密码：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_C3P0_PASSWORD%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[6].attrValue"  class="nui-password" value="" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，用户密码</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 连接池大小：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_C3P0_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[7].attrValue"  class="nui-textbox" value="100" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，连接池大小</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 最大连接数：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_C3P0_MAX_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[8].attrValue"  class="nui-textbox" value="200" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，连接池最大连接数</td>
	                </tr>
	                <tr>
	                    <td align="right">C3P0 最小连接数：</td>
	                    <td>    
	                         <input name="order.itemList[0].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_C3P0_MIN_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[9].attrValue"  class="nui-textbox" value="10" width="80%" required="true" onvalidation="onNullValidation" />
	                    </td>
	                    <td>* C3P0数据源配置，连接池最小连接数</td>
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
        	if (null == e) {
        		return;
        	}
        	if (!e.value) {
        		e.errorText = "不能为空！";
        		e.isValid = false;
        	} else {
        		e.errorText = "";
    			e.isValid = true;
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
        	if (!confirm("你确定要提交ESB服务创建申请？")) {
    	        return false;
    	    }
            
            var data = form.getData().order;
            var json = nui.encode(data);
            
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/createEsb",
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