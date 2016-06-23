<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();

	String cacheSizesJSON = SystemVariable.getCacheSizesJSON();
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
            <legend >Memcached 服务配置 [外部]</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_MEMCACHED%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">独占主机:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" style="width:200px;"/>
	                    	                    
	                    <div name="order.itemList[0].attrList[0].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>服务的名称，用于展示。</td>
	            </tr>
	            <tr>
	                <td >服务名称:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[1].attrValue" class="nui-textbox"  required="true" requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>负载均衡服务的名称，仅用于展示。</td>
	            </tr>   
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[2].attrValue" name="order.itemList[0].attrList[2].attrValue" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	            <tr>
	                <td >缓存（M）:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_MEMCACHED_SIZE%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[3].attrValue" class="nui-combobox" required="true" data='<%=cacheSizesJSON %>' value="32"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>缓存空间大小，单位： M。</td>
	            </tr>	             
	            <tr>
	                <td >最大连接数:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_MAX_CONNNECTION%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[4].attrValue" class="nui-combobox" required="true" value="50" data="[{ id: '50', text: '50'}, { id: '100', text: '100'}, { id: '500', text: '500'}]" value="roundrobin"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>默认50个连接数</td>
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
            nui.get("order.itemList[0].attrList[2].attrValue").select(0);
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
            // 提交表单数据
            var formSub = new nui.Form("#form1"); 
            formSub.validate();
            if (formSub.isValid() == false) {
            	return;
            }
            var data = formSub.getData().order;      //获取表单多个控件的数据
            var json = nui.encode(data);   //序列化成JSON
                    
            if (json != null)  
            	json = json.replace(/{},/g,"")  
            // document.getElementById("showText").innerText= json;
            
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/myService/applyOuterService",
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
