<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<%
	request.setAttribute("dbPkgs", SystemVariable.getHostTemplates());
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();

	request.setAttribute("policys", SystemVariable.getHaproxyPolicy());
	String policysJSON = SystemVariable.getHaproxyPolicyJSON();
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
            <legend >Haproxy 服务配置 [外部]</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_HAPROXY%>" class="nui-hidden" />
            <input name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_HEALTHCHECKURI%>" class="nui-hidden" />
	        <input name="order.itemList[0].attrList[7].attrValue" value="/" class="nui-hidden" />
	        	        <table>
	            <tr>
	                <td style="width:100px;">是否主备:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_BACKUP%>" class="nui-hidden" />
	                    <div name="order.itemList[0].attrList[0].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>是否创建主备服务。</td>
	            </tr>
	            <tr>
	                <td style="width:100px;">独占主机:</td>
	                <td style="width:300px;" disabled="true">    
	                    <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" style="width:200px;"/>
	                    	                    
	                    <div name="order.itemList[0].attrList[1].attrValue" class="nui-radiobuttonlist"  
						    textField="text" valueField="id" value="Y" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>服务的名称，用于展示。</td>
	            </tr>
	            <tr>
	                <td >服务名称:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[2].attrValue" class="nui-textbox"  required="true" requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>负载均衡服务的名称，仅用于展示。</td>
	            </tr>   
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[0].attrList[3].attrValue" name="order.itemList[0].attrList[3].attrValue" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>' emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	            <tr>
	                <td >协议类型:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_PROTOCOL%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[4].attrValue" class="nui-combobox" required="true" value="http" data="[{ id: 'http', text: 'http'}, { id: 'tcp', text: 'tcp'}]" value="roundrobin"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td></td>
	            </tr> 	            
	            <tr>
	                <td >负载均衡策略:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_BALANCE%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[5].attrValue" class="nui-combobox" required="true" data='<%=policysJSON %>' value="roundrobin"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td></td>
	            </tr>   
	            <tr>
	                <td >最大连接数:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_MAX_CONNNECTION%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[6].attrValue" class="nui-combobox" required="true" value="50" data="[{ id: '50', text: '50'}, { id: '100', text: '100'}]" value="roundrobin"  emptyText="请选择..." style="width:200px;"/>
	                </td>
	                <td>默认50个连接数</td>
	            </tr>	       
	                    
	            <tr>
	                <td >连接超时时间:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_CONN_TIMEOUT%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[8].attrValue" value="3600" class="nui-textbox" vtype="int"   required="true" requiredErrorText="此项为必填项" intErrorText="请输入整数" style="width:200px;"/>
	                </td>
	                <td>超时时间，单位毫秒(ms)</td>
	            </tr>  
	            
	            <tr>
	                <td >集群成员列表:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_MEMBER%>" class="nui-hidden" />
	                    <input id="txtAMember" name="order.itemList[0].attrList[9].attrValue" class="nui-textarea"  required="true" requiredErrorText="此项为必填项" style="width:200px;height:100px"/>
	                </td>
	                <td>输入成员信息（IP:Port）,多个用";"隔开</td>
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
            nui.get("order.itemList[0].attrList[3].attrValue").select(0);
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
            if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
            else window.close();            
        }

        function submitForm() {
            // 提交表单数据
            var formSub = new nui.Form("#form1"); 
            formSub.validate();
            if (formSub.isValid() == false) 
            	return;
            
            if(!validateForm()){
            	return;
            }
    
            var data = formSub.getData().order;      //获取表单多个控件的数据
            var json = nui.encode(data);   //序列化成JSON
                    
            if (json != null)  
            	json = json.replace(/{},/g,"")  
            
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
        
        function validateForm() {
        	var memberInfo = nui.get("txtAMember").getValue();
    		members = memberInfo.split(";");
    		if (members != null && members.length>0) {
    			for (i = 0 ; i<members.length ;i++) {
    				m = members[i];
    				ips = m.split(":");
    				if (ips != null && ips.length == 2) {
    					continue;
    				}
    				nui.alert("成员列表中有不合法输入，请重新验证.(注意中英文字符)");
    				return false;
    			}
    		}
        	return true;
        }
        
     </script>
</html>
