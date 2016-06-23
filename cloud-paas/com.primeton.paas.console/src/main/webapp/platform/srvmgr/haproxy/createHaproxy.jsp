<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.service.HaProxyService"%>

<%
	String policysJSON = SystemVariable.getHaproxyPolicyJSON();	//负载均衡策略
	
	//协议类型  http/tcp
	//String protocolsJSON = SystemVariable.getHaproxyProtocolsJSON();//协议类型
	
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script type="text/javascript">
    	var protocolTypesDatas = [{'id':'http','text':'http'},{'id':'tcp','text':'tcp'}];
    </script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createCep">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >新增Haproxy服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_HAPROXY %>" class="nui-hidden" />
            	<input id="order.itemList[1].itemType" name="order.itemList[1].itemType" value="<%=OrderItem.ITEM_TYPE_NGINX %>" class="nui-hidden" />
		        <table border="0">
		            <tr>
	                    <td style="width:15%;" align="right">是否主备：</td>
	                    <td style="width:48%;">    
	                         <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_BACKUP%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[0].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="100%"/>
	                    </td>
	                    <td align="left">*是否主备.</td>
	                </tr>	
		            <tr>
	                    <td style="width:15%;" align="right">独占主机：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="100%"/>
	                    </td>
	                    <td align="left">*服务独占主机或者使用共享主机.</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">服务名称：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[2].attrValue"  class="nui-textbox" onvalidation="onNullValidation" value="<%=HaProxyService.TYPE %>-default" width="100%"/>
	                    </td>
	                    <td align="left">*资源库服务名称，仅用于展示。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">主机套餐：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue"  class="nui-combobox" data='<%=hostTemplatesJSON %>' value="20130517J001" width="100%"/>
	                    </td>
	                    <td align="left">*选择需要创建的主机机型。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">协议类型：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[4].attrValue"  class="nui-combobox" data="protocolTypesDatas" value="http" width="100%"/>
	                    </td>
	                    <td align="left">*负载均衡协议类型（http/tcp）。</td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">负载均衡策略：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_BALANCE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[5].attrValue"  class="nui-combobox" data='<%=policysJSON %>' value="roundrobin" width="100%"/>
	                    </td>
	                    <td align="left">*</td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">最大连接数：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_MAX_CONNNECTION%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[6].attrValue"  class="nui-textbox" onvalidation="onNullValidation" vtype="range:50,5000" rangeErrorText="数字必须在50~5000之间" value="50" width="100%"/>
	                    </td>
	                    <td align="left">*默认50个连接数.</td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">健康检查页面：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_HEALTHCHECKURI%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[7].attrValue"  class="nui-textbox" onvalidation="onNullValidation" value="/" width="100%"/>
	                    </td>
	                    <td align="left">*默认为根目录 /</td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">连接超时时间：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_CONN_TIMEOUT%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[8].attrValue"  class="nui-textbox" vtype="int" intErrorText="请输入整数" onvalidation="onNullValidation" value="3600" width="100%"/>
	                    </td>
	                    <td align="left">*超时时间，单位毫秒(ms) </td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">关联服务：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[0].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_REL_SRV_TYPE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[9].attrValue" id="srvTypeCombo" class="nui-combobox" style="width:150px;" textField="text" valueField="id" emptyText="--请选择--" nullItemText="---请选择---"
        							onvaluechanged="onSrvTypeChanged" url="<%=request.getContextPath() %>/srv/service/getRelServiceTypes" showNullItem="true" onvalidation="onNullValidation" /> 
	                         <input name="order.itemList[0].attrList[10].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_REL_SRV_ID%>" class="nui-hidden" />
	                    	 <input  name="order.itemList[0].attrList[10].attrValue" id="srvIdCombo" class="nui-combobox" style="width:150px;" textField="text" valueField="id" emptyText="--请选择--" /> 
	                    </td>
	                    <td align="left">*指定负载均衡关联的服务 </td>
	                </tr>
	                
	                <tr>
	                    <td style="width:15%;" align="right">
	                    <div id="if_Rel_Nginx" class="nui-checkbox" readOnly="false" onvaluechanged="enableRelNginx"></div>
	                    	关联Nginx：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[1].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_CLUSTER_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[1].attrList[0].attrValue" id="Haproxy_Rel_Nginx_Input" class="nui-combobox" style="width:150px;" textField="text" valueField="id"
        							url="<%=request.getContextPath() %>/srv/service/getAllNginxClusters" showNullItem="false"/>
	                    </td>
	                    <td align="left">*指定负载均衡关联的服务 </td>
	                </tr>
	                <tr>
	                    <td style="width:15%;" align="right">域名：</td>
	                    <td style="width:45%;">    
	                         <input name="order.itemList[1].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_APP_DOMAIN%>" class="nui-hidden" />
	                    	 <input id="Haproxy_Domain_Input" name="order.itemList[1].attrList[1].attrValue"  class="nui-textbox" width="100%"/>
	                    </td>
	                    <td align="left">*设定负载均衡服务的访问域名,默认以paas.com结尾   </td>
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
        
        var srvTypeCombo = nui.get("srvTypeCombo");
        //srvTypeCombo.select(0);
        var srvIdCombo = nui.get("srvIdCombo");
        var nginxCombo = nui.get("Haproxy_Rel_Nginx_Input");
        nginxCombo.select(0);
        enableRelNginx();
        
        function onSrvTypeChanged(e) {
        	var type = srvTypeCombo.getValue();
        	srvIdCombo.setValue("");
            if(type==""||type==null){
            	return;
            }
            var url = "<%=request.getContextPath() %>/srv/service/getAllRelServiceByType/" + type
            srvIdCombo.setUrl(url);
            srvIdCombo.select(0);
        }
        
        
        function enableRelNginx() {
        	var ifRel = nui.get("if_Rel_Nginx");
        	if (ifRel.checked) {
    			document.getElementById("Haproxy_Rel_Nginx_Input").disabled = false;
    			document.getElementById("Haproxy_Domain_Input").disabled = false;
    			nui.get("Haproxy_Rel_Nginx_Input").allowInput=true;
    			nui.get("Haproxy_Domain_Input").allowInput=true;
    		} else {
    			document.getElementById("Haproxy_Rel_Nginx_Input").disabled = true;	
    			document.getElementById("Haproxy_Domain_Input").disabled = true;
    			nui.get("Haproxy_Rel_Nginx_Input").allowInput=false;
    			nui.get("Haproxy_Domain_Input").allowInput=false;
    		}
        }
        
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
        
    	//新增
        function onAdd() {
        	var ifRel = nui.get("if_Rel_Nginx");
            if (ifRel.checked == false) {
            	nui.get("order.itemList[1].itemType").setValue("");
            } else {
            	var nginxId = nui.get("Haproxy_Rel_Nginx_Input").getValue();
            	if (nginxId == 'null' || nginxId == "" || nginxId == '') {
    				nui.alert("请指定关联的Nginx!")
    				return false;
    			}
            	var domain = nui.get("Haproxy_Domain_Input").getValue();
            	if (domain == 'null' || domain == "" || domain == '') {
    				nui.alert("请指定访问域名!")
    				nui.get("Haproxy_Domain_Input").focus();
    				return false;
    			}
            }
            
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            
        	if (!confirm("你确定要提交HaProxy服务创建申请？")) {
    	        return false;
    	    }
            
            var data = form.getData().order;
            var json = nui.encode(data);
            
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/createHaproxy",
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