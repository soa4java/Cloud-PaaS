<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
	
	String storageSizesJSON = SystemVariable.getNasStorageSizesJSON();

	String characterSetsJSON = SystemVariable.getMySQLCharacterSetsJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
	<style type="text/css">
    .asLabel .nui-textbox-border,
    .asLabel .nui-textbox-input,
    .asLabel .nui-buttonedit-border,
    .asLabel .nui-buttonedit-input,
    .asLabel .nui-textboxlist-border
    {
        border:0;background:none;cursor:default;
    }
    .asLabel .nui-buttonedit-button,
    .asLabel .nui-textboxlist-close
    {
        display:none;
    }
    .asLabel .nui-textboxlist-item
    {
        padding-right:8px;
    }    
	</style>
</head>
<body>
  <form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >Mysql数据库服务配置</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_MYSQL%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td >独占主机:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    <div name="order.itemList[0].attrList[0].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="Y" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
    	            <td>是否单独占用一台主机。</td>
	            </tr>
	            
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[1].attrValue" name="order.itemList[0].attrList[1].attrValue" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr>  
	            
	             <tr>
	                <td style="width:100px;">服务名称:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[0].attrList[3].attrName" name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[3].attrValue" name="order.itemList[0].attrList[3].attrValue" class="nui-textbox"  required="true"  requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>数据库服务名称，用于展示。</td>
	            <tr>
	                <td >用户名:</td>
	                <td >    
	                    <input id="order.itemList[0].attrList[4].attrName" name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_MYSQL_USER_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[4].attrValue" name="order.itemList[0].attrList[4].attrValue" class="nui-textbox" required="true"  requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>数据库服务访问用户名。（不允许root）</td>
	            </tr> 
	            <tr>
	                <td >密码:</td>
	                <td >    
	                    <input id="order.itemList[0].attrList[5].attrName" name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_MYSQL_PASSWORD%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[5].attrValue" name="order.itemList[0].attrList[5].attrValue" class="nui-password" required="true"  requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>数据库服务访问密码.</td>
	            </tr>   

	            <tr>
	                <td >空间（G）:</td>
	                <td >    
	                	<input id="order.itemList[0].attrList[6].attrName" name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_MYSQL_STORAGE_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[6].attrValue" name="order.itemList[0].attrList[6].attrValue" class="nui-combobox" required="true" data='<%=storageSizesJSON %>'  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>数据库服务空间大小，单位： G。</td>
	            </tr>     

	            <tr>
	                <td >字符类型:</td>
	                <td >    
	                	<input id="order.itemList[0].attrList[7].attrName" name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_MYSQL_CHARACTERSET%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[7].attrValue" name="order.itemList[0].attrList[7].attrValue" class="nui-combobox" required="true" data='<%=characterSetsJSON %>' value="utf8"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>数据库字符编码类型。</td>
	            </tr>    	            
	        </table>
	   </div>
	 </fieldset>
	 
	   	 <span id="showText"></span>
	   	 
	  <div style="text-align:center;padding:10px;">               
           <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">申请</a>            
           <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
       </div>   
  </form>

</body>
    <script type="text/javascript">
    
        nui.parse();
        
        $(document).ready(function() {
            nui.get("order.itemList[0].attrList[1].attrValue").select(0);
            nui.get("order.itemList[0].attrList[6].attrValue").select(0);
		});

        var form = new nui.Form("form1");
        
        function onCancel(e) {
            CloseWindow("cancel");
        }
        
        function CloseWindow(action) {            
            if (action == "close" && form.isChanged()) {
                if (confirm("数据被修改了，是否先保存？")) {
                    return false;
                }
            }
            if (window.CloseOwnerWindow) {
            	return window.CloseOwnerWindow(action);
            } else {
            	window.close();            
            }
        }

        function submitForm() {
            //提交表单数据
            var formSub = new nui.Form("#form1"); 
            
            formSub.validate();
            if (formSub.isValid() == false) 
            	return;
    
            var data = formSub.getData().order;      //获取表单多个控件的数据
            var json = nui.encode(data);   //序列化成JSON
                    
            if (json != null)  
            	json = json.replace(/{},/g,"")  
			// document.getElementById("showText").innerText= json;
            
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/myService/applyService",
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                    nui.alert("提交成功，订单号为:" + o.orderId);
                    CloseWindow("cancel"); 
                } 
            });
        }
 
     </script>
</html>
