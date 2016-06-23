<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.impl.util.SystemVariables"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
	String cacheSizesJSON = SystemVariable.getCacheSizesJSON();
	String cachesMaxConnSizesJSON = SystemVariable.getCacheMaxConnSizesJSON();

	String domainPostfix = SystemVariables.getDomainPostfix();
	request.setAttribute("domainPostfix", domainPostfix);

	String storageSizesJSON = SystemVariable.getNasStorageSizesJSON();

	String mysqlCharsetsJSON = SystemVariable.getMySQLCharacterSetsJSON();

	Object appServerSizesJSON = SystemVariable.getAppServerSizesJSON();
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
            <legend >应用基本信息(<font color="red">*必填</font>)</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_APP%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">应用标识:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_APP_NAME%>" class="nui-hidden" />
	                    <input id="SLDomainID" name="order.itemList[0].attrList[0].attrValue" class="nui-textbox"  required="true" requiredErrorText="此项为必填项" onblur="checkDomain(this.value);" style="width:250px;"/>
	                    <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_APP_DOMAIN%>" class="nui-hidden"  />
	                    <input name="order.itemList[0].attrList[1].attrValue" value="<%=domainPostfix%>" class="nui-hidden" id="appDomain"/>
	                    <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_APP_IS_ENABLE_DOMAIN%>" class="nui-hidden"  />
	                    <input name="order.itemList[0].attrList[4].attrValue" value="Y" class="nui-hidden" id="appIsEnableDomain"/>
	                </td>
	                <td>唯一标识，创建后不可修改。</td>
	            </tr>
	            <tr>
	                <td >应用名称:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[2].attrValue" class="nui-textbox" required="true" requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>应用的显示名称，与业务无关。</td>
	            </tr>   
	            <tr>
	                <td >应用描述:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_APP_DESC%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[3].attrValue" class="nui-textarea"  style="width:250px;height:50px;"/>
	                </td>
	                <td>不超过200个字符。</td>
	            </tr>      
	        </table>            
        </div>
        </fieldset>
        
        <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >应用容器服务配置(<font color="red">*必填</font>)</legend>
        <div style="padding:5px;">
        	<input id="serverTypeItemType" name="order.itemList[1].itemType" value="<%=OrderItem.ITEM_TYPE_JETTY%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">服务器类型:</td>
	                <td style="width:300px;">    
	                	<input name="order.itemList[1].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_APP_SERVER_TYPE%>" class="nui-hidden" />
	                    <input id="serverTypeCombox" name="order.itemList[1].attrList[7].attrValue" class="nui-combobox" onvaluechanged="onServerTypeChange" required="true" data='<%=SystemVariable.getServerTypesJSON() %>' value="Tomcat" style="width:250px;"/>
	                </td>
	                <td>应用容器服务类型。</td>
	            </tr>
	            <tr>
	                <td >独占主机:</td>
	                <td>    
	                    <input name="order.itemList[1].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    <div id="containerStandaloneRadio" name="order.itemList[1].attrList[0].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="Y" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
    	            <td>是否单独占用一台主机。</td>
	            </tr> 
	            <tr>
	                <td >服务名称:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[1].attrValue" name="order.itemList[1].attrList[1].attrValue" class="nui-textbox" required="true" requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
    	            <td>服务的显示名称，与业务无关。</td>
	            </tr>   
	            <tr>
	                <td >存储（G）:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_JETTY_STORAGE_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[2].attrValue" name="order.itemList[1].attrList[2].attrValue" class="nui-combobox" required="true" data='<%=storageSizesJSON %>' style="width:250px;" emptyText="请选择..." />
	                </td>
	       	        <td>存储服务空间大小，单位: G。（如果没有共享存储，该项无效）</td>
	            </tr>      
	            <tr>
	                <td >最大实例数:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_JETTY_MAX_SIZE%>" class="nui-hidden" />
	                    <input name="order.itemList[1].attrList[4].attrValue" class="nui-combobox" required="true" data='<%=appServerSizesJSON %>' value="1" style="width:250px;" id="maxSize" onvaluechanged = "selChangeMax()"/>
	                </td>
	                <td>应用能够创建的最大应用实例数。</td>
	            </tr>      
	            <tr>
	                <td >最小实例数:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_JETTY_MIN_SIZE%>" class="nui-hidden" />
	                    <input name="order.itemList[1].attrList[3].attrValue" class="nui-combobox" required="true" data='<%=appServerSizesJSON %>' value="1" style="width:250px;" id="minSize" onvaluechanged = "selChangeMin()"/>
	                </td>
	                <td>应用能够创建的最小应用实例数。</td>
	            </tr>      
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[6].attrValue" name="order.itemList[1].attrList[6].attrValue" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'   emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr>
	            <tr>
	                <td >MinPermSize:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_MIN_PERM_MEMORY%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[5].attrValue" name="order.itemList[1].attrList[5].attrValue" class="nui-textbox" required="true" value='64' requiredErrorText="此项为必填项" style="width:250px;" />
	                </td>
	                <td>JVM MinPermSize (MB)，如果是独占主机，该项无效</td>
	            </tr>
	            <tr>
	                <td >MaxPermSize:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_MAX_PERM_MEMORY%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[8].attrValue" name="order.itemList[1].attrList[8].attrValue" class="nui-textbox" required="true" value='128' requiredErrorText="此项为必填项" style="width:250px;" />
	                </td>
	                <td>JVM MaxPermSize (MB)，如果是独占主机，该项无效</td>
	            </tr>
	            <tr>
	                <td >MinMemSize:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_MIN_MEMORY%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[9].attrValue" name="order.itemList[1].attrList[9].attrValue" class="nui-textbox" required="true" value='128' requiredErrorText="此项为必填项" style="width:250px;" />
	                </td>
	                <td>JVM MinMemSize (MB)，如果是独占主机，该项无效</td>
	            </tr>
	            <tr>
	                <td >MaxMemSize:</td>
	                <td >    
	                    <input name="order.itemList[1].attrList[10].attrName" value="<%=OrderItemAttr.ATTR_MAX_MEMORY%>" class="nui-hidden" />
	                    <input id="order.itemList[1].attrList[10].attrValue" name="order.itemList[1].attrList[10].attrValue" class="nui-textbox" required="true" value='256' requiredErrorText="此项为必填项" style="width:250px;" />
	                </td>
	                <td>JVM MaxMemSize (MB)，如果是独占主机，该项无效</td>
	            </tr>  
	        </table>            
        </div>
        </fieldset>
        
        
        <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >负载均衡服务配置(<font color="red">*必填</font>)</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[2].itemType" value="<%=OrderItem.ITEM_TYPE_HAPROXY%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">是否主备:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[2].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_IS_BACKUP%>" class="nui-hidden" />
	                    <div name="order.itemList[2].attrList[2].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>是否创建主备服务。</td>
	            </tr>
	            <tr>
	                <td style="width:100px;">独占主机:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[2].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" style="width:250px;"/>
	                    	                    
	                    <div name="order.itemList[2].attrList[3].attrValue" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>服务的名称，用于展示。</td>
	            </tr>
	            <tr>
	                <td >服务名称:</td>
	                <td >    
	                    <input name="order.itemList[2].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[2].attrList[0].attrValue" name="order.itemList[2].attrList[0].attrValue" class="nui-textbox"  required="true" requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>负载均衡服务的名称，仅用于展示。</td>
	            </tr>   
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input name="order.itemList[2].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[2].attrList[1].attrValue" name="order.itemList[2].attrList[1].attrValue" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	            <tr>
	                <td >负载均衡策略:</td>
	                <td >    
	                	<input name="order.itemList[2].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_BALANCE%>" class="nui-hidden" />
	                    <input name="order.itemList[2].attrList[4].attrValue" class="nui-combobox" required="true" data='<%=SystemVariable.getHaproxyPolicyJSON() %>' value="roundrobin"  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td></td>
	            </tr>      
	            <tr>
	                <td >客户端访问方式:</td>
	                <td >    
	                	<input name="order.itemList[2].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_PROTOCOL_TYPE%>" class="nui-hidden" />
	                    <input id="protocolId" name="order.itemList[2].attrList[5].attrValue" class="nui-combobox" required="true" data='<%=SystemVariable.getSslProtocalTypesJSON() %>' value="HTTP"  emptyText="请选择..." style="width:250px;" onvaluechanged="selProtocol"/>
	                </td>
	                <td></td>
	            </tr>  
	            <tr>
		            <td colspan="3">
			            <fieldset style="border:solid 1px #aaa">
			            	<legend >上传证书</legend>
			            	<div style="padding:5px;" id="sslFileId" disabled="true">
				            	<table>
						            <tr>
						                <td style="width:100px;">安全证书：</td>
						                <td style="width:300px;">    
						                	<input name="order.itemList[2].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_HAPROXY_SSL_CERTIFICATE_PATH%>" class="nui-hidden" />
											<input id="uploadFile" name="order.itemList[2].attrList[6].attrValue" class="nui-textbox" style="width: 200px;" required="false" requiredErrorText="此项为必填项" />
											<a id="btn_upload" class="nui-button" onclick="uploadFile" style="width:60px;margin-right:20px;" Enabled="false">上传</a>       
						                </td>
						                <td>SSL安全证书压缩包。</td>
						            </tr>
						      </table>
					      </div>
			            </fieldset>
		            </td>
	            </tr>
	        </table>            
        </div>
        </fieldset>
        
       <span id="domainFieldsetTab">
       <fieldset style="border:solid 1px #aaa;padding:3px;" >
       		<legend >
       			<input id="onChKDomain" name="onChKDomain" type="checkbox" onclick="onChKDomainChanged(this)" checked="checked">
            	域名服务配置
            </legend>
            <div style="padding:5px;" id="domainFieldset">
	            <table>
		            <tr>
		                <td style="width:100px;">域名:</td>
		                <td style="width:300px;">    
		                	<span id="appDomainTag"></span>
		                </td>
		                <td>访问应用的域名。</td>
		            </tr>
		        </table>
	        </div>
       </fieldset>
       </span>
        
       <span id="sessionFieldsetTab">
       <fieldset style="border:solid 1px #aaa;padding:3px;" >
            <legend >
			<!--             
			<div id="onChKSession" name="onChKSession" readOnly="false" class="nui-checkbox"  text=""   onclick="onChKSessionChanged"></div> 
			-->
            <input id="onChKSession" name="onChKSession" type="checkbox" onclick="onChKSessionChanged(this)">
			分布式会话服务配置（使用Memcached存储应用Session）
            </legend>
        <div style="padding:5px;" id="sessionFieldset" disabled="true">
        	<input id="order.itemList[5].itemType" name="default" value="<%=OrderItem.ITEM_TYPE_SESSION%>" class="nui-hidden" />
	        <table>
	        	 <tr style="display:'';">
	                <td style="width:100px;" >独占主机:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[5].attrList[0].attrName" name="default" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	                    
	                    <div id="order.itemList[5].attrList[0].attrValue" name="default" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>是否单独占用一台主机。</td>
	            </tr>
	            <tr>
	                <td style="width:100px;">服务名称:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[5].attrList[1].attrName" name="default" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[5].attrList[1].attrValue" name="default" value="memcached-session" allowInput="false" class="nui-textbox" requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>缓存服务的名称，用于展示。</td>
	            </tr>
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input id="order.itemList[5].attrList[2].attrName"  name="default" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[5].attrList[2].attrValue" name="default" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr>   
	            <tr>
	                <td >缓存（M）:</td>
	                <td >    
	                	<input id="order.itemList[5].attrList[3].attrName"  name="default" value="<%=OrderItemAttr.ATTR_MEMCACHED_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[5].attrList[3].attrValue" name="default" class="nui-combobox" required="true" data='<%=cacheSizesJSON %>' value="128"  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>缓存空间大小，单位： MB。</td>
	            </tr>
	            <tr>
	                <td >最大连接数:</td>
	                <td >    
	                	<input id="order.itemList[5].attrList[4].attrName" name="default" value="<%=OrderItemAttr.ATTR_MEMCACHED_MAX_CONN_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[5].attrList[4].attrValue" name="default" class="nui-combobox" data='<%=cachesMaxConnSizesJSON %>'  emptyText="请选择..." value="50" style="width:250px;"/>
	                </td>
	                <td></td>
	            </tr>      
	        </table>            
        </div>
        </fieldset>
        </span>
        
       <span id="memcachedFieldsetTab">
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >
            <input id="chKMemcached" name="chKMemcached" type="checkbox" onclick="onChKMemcachedChanged(this)">
			<!--             
			<div id="chKMemcached" name="chKMemcached" readOnly="false" class="nui-checkbox"  text="" onclick="onChKMemcachedChanged"></div> 
			-->
            Memcached缓存服务配置
            </legend>
        <div style="padding:5px;" id="memcachedFieldset" disabled="true">
        	<input id="order.itemList[3].itemType" name="default" value="<%=OrderItem.ITEM_TYPE_MEMCACHED%>" class="nui-hidden" />
	        <table>
	        	<tr style="display:'';">
	                <td style="width:100px;" >独占主机:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[3].attrList[4].attrName" name="default" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	                    
	                    <div id="order.itemList[3].attrList[4].attrValue" name="default" class="nui-radiobuttonlist" 
						    textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" > </div>
	                </td>
	                <td>是否单独占用一台主机。</td>
	            </tr>
	            <tr>
	                <td style="width:100px;">服务名称:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[3].attrList[0].attrName" name="default" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[3].attrList[0].attrValue" name="default" class="nui-textbox" requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>缓存服务的名称，用于展示。</td>
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input id="order.itemList[3].attrList[1].attrName" name="default" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[3].attrList[1].attrValue" name="default" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'   emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	            <tr>
	                <td >缓存（M）:</td>
	                <td >    
	                    <input id="order.itemList[3].attrList[2].attrName" name="default" value="<%=OrderItemAttr.ATTR_MEMCACHED_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[3].attrList[2].attrValue" name="default" class="nui-combobox" required="true" data='<%=cacheSizesJSON %>' value="128"  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>缓存空间大小，单位： M</td>
	            </tr>      
	            <tr>
	                <td >最大连接数:</td>
	                <td >    
	                	<input id="order.itemList[3].attrList[3].attrName" name="default" value="<%=OrderItemAttr.ATTR_MEMCACHED_MAX_CONN_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[3].attrList[3].attrValue" name="default" class="nui-combobox" data='<%=cachesMaxConnSizesJSON %>' value="50" style="width:250px;"/>
	                </td>
	                <td>最大连接数。</td>
	            </tr>     
	        </table>            
        </div>
        </fieldset>
        </span>
        
        <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >
            <div id="chKMysql"  class="nui-checkbox" readOnly="false" text="" onvaluechanged="onChKMysqlChanged"></div>
            Mysql数据库服务配置
            </legend>
        <div style="padding:5px;" id="mysqlFieldset" disabled="true">
        	<input id="order.itemList[4].itemType" name="default" value="<%=OrderItem.ITEM_TYPE_MYSQL%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">服务名称:</td>
	                <td style="width:300px;">    
	                    <input id="order.itemList[4].attrList[0].attrName" name="default" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[0].attrValue" name="default" class="nui-textbox"   requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>数据库服务名称，用于展示。</td>
	            <tr>
	                <td >用户名:</td>
	                <td >    
	                    <input id="order.itemList[4].attrList[2].attrName" name="default" value="<%=OrderItemAttr.ATTR_MYSQL_USER_NAME%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[2].attrValue" name="default" class="nui-textbox"   requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>数据库服务访问用户名（不允许使用root）。</td>
	            </tr> 
	            <tr>
	                <td >密码:</td>
	                <td >    
	                    <input id="order.itemList[4].attrList[3].attrName" name="default" value="<%=OrderItemAttr.ATTR_MYSQL_PASSWORD%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[3].attrValue" name="default" class="nui-password"  requiredErrorText="此项为必填项" style="width:250px;"/>
	                </td>
	                <td>数据库服务访问密码.</td>
	            </tr>   
	            <tr>
	                <td >空间（G）:</td>
	                <td >    
	                	<input id="order.itemList[4].attrList[4].attrName" name="default" value="<%=OrderItemAttr.ATTR_MYSQL_STORAGE_SIZE%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[4].attrValue" name="default" class="nui-combobox" required="true" data='<%=storageSizesJSON %>'  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>数据库服务空间大小，单位： G。</td>
	            </tr>     
	            <tr>
	                <td >字符集:</td>
	                <td >    
	                	<input id="order.itemList[4].attrList[5].attrName" name="default" value="<%=OrderItemAttr.ATTR_MYSQL_CHARACTERSET%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[5].attrValue" name="default" class="nui-combobox" required="true" data='<%=mysqlCharsetsJSON %>' value="utf8"  emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>数据库字符编码类型。</td>
	            </tr>    	            
	            <tr>
	                <td >主机套餐:</td>
	                <td >    
	                    <input id="order.itemList[4].attrList[1].attrName" name="default" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    <input id="order.itemList[4].attrList[1].attrValue" name="default" class="nui-combobox" required="true" data='<%=hostTemplatesJSON %>'   emptyText="请选择..." style="width:250px;"/>
	                </td>
	                <td>选择需要创建的主机机型。</td>
	            </tr> 
	        </table>            
        </div>
        </fieldset>
        
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="insForm" style="width:60px;margin-right:20px;">继续</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>   
   </form>
   
   
   <form id="form2" method="post" style="font-size:12px;display:none " >
   	 <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend style="font-size:15px;"><b>创建应用向导：摘要</b></legend>
            <fieldset style="border:solid 1px #aaa;padding:3px;">
            	<legend >应用基本信息：</legend>
		        <div style="padding:5px;">
			        <table>
			            <tr>
			                <td style="width:100px;">应用名:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[0].attrList[0].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			           <td style="width:100px;">域名:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[0].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			           <td style="width:100px;">启用域名:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[0].attrList[4].attrValue"  class="nui-combobox asLabel" style="width:250px;"  textField="text" valueField="id" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" />   
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">应用名称:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[0].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			            <tr>
			                <td style="width:100px;">应用描述:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[0].attrList[3].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>    
			        </table>            
		        </div>
		    </fieldset>
		    <fieldset style="border:solid 1px #aaa;padding:3px;">
            	<legend >应用容器配置：</legend>
		        <div style="padding:5px;">
			        <table>
			            <tr>
			                <td style="width:100px;">应用容器类型:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[7].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>    
			            <tr>
			                <td style="width:100px;">服务名称:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			            <tr>
			                <td style="width:100px;">存储(G):</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">启动参数:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[5].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>
			            <tr>
			                <td style="width:100px;">最小实例数:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[3].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">最大实例数:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[4].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">主机套餐:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[1].attrList[6].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			        </table>            
		        </div>
		    </fieldset>
		    
		    <fieldset style="border:solid 1px #aaa;padding:3px;">
            	<legend >负载均衡服务配置：</legend>
		        <div style="padding:5px;">
			        <table>
			            <tr>
			                <td style="width:100px;">是否主备:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[2].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>    
			            <tr>
			                <td style="width:100px;">名称:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[2].attrList[0].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			            <tr>
			                <td style="width:100px;">负载均衡策略:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[2].attrList[4].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">主机套餐:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[2].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>  
			        </table>            
		        </div>
		    </fieldset>
		    
            <fieldset id="mFieldset" style="border:solid 1px #aaa;padding:3px;">
            	<legend >缓存服务配置：</legend>
		        <div style="padding:5px;">
			        <table>
			            <tr>
			                <td style="width:100px;">名称:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[3].attrList[0].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>    
			            <tr>
			                <td style="width:100px;">主机套餐:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[3].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			            <tr>
			                <td style="width:100px;">缓存大小:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[3].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:20px;" /> M
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">最大连接数:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[3].attrList[3].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>  
			        </table>            
		        </div>
		    </fieldset>
		    
		    
		    <fieldset id="sqlFieldset" style="border:solid 1px #aaa;padding:3px;">
            	<legend >数据库服务配置：</legend>
		        <div style="padding:5px;">
			        <table>
			            <tr>
			                <td style="width:100px;">服务名称:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[4].attrList[0].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>    
			            <tr>
			                <td style="width:100px;">用户名:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[4].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr> 
			            <tr>
			                <td style="width:100px;">字符类型:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[4].attrList[5].attrValue"  class="nui-textbox asLabel" style="width:250px;" /> 
			                </td>
			            </tr>   
			            <tr>
			                <td style="width:100px;">主机套餐:</td>
			                <td style="width:300px;">    
			                   <input name="order.itemList[4].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:250px;" />   
			                </td>
			            </tr>  
			        </table>            
		        </div>
		    </fieldset>
     </fieldset>
   	  
   	 <span id="showText"></span>
   		
      <div style="text-align:center;padding:10px;">               
           <a class="nui-button" onclick="rtnForm" style="width:60px;margin-right:20px;">返回</a> 
           <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">确定</a>            
           <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
       </div>   
   </form>
      
</body>
    <script type="text/javascript">
    	// Parse DOM
        nui.parse();
        
        $(document).ready(function() {
            nui.get("order.itemList[1].attrList[2].attrValue").select(0);
            nui.get("order.itemList[1].attrList[6].attrValue").select(0);
            nui.get("order.itemList[2].attrList[1].attrValue").select(0);
            nui.get("order.itemList[5].attrList[2].attrValue").select(0);
            nui.get("order.itemList[3].attrList[1].attrValue").select(0);
            nui.get("order.itemList[4].attrList[1].attrValue").select(0);
            nui.get("order.itemList[4].attrList[4].attrValue").select(0);
            
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

        /*检验二级域名前缀是否可用*/
        function checkDomain(domain) {
        	var obj= nui.get("SLDomainID");
			domain = domain.trim(); 
			nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在效验，请稍后...'});
			$.ajax({
           	   type: "get",
               url: "<%=request.getContextPath() %>/srv/myApp/isExistDomain/" + domain,
               success: function (text) {
            	   if (text.data) {
            		    nui.alert("应用标识“" + domain + "”已经存在，请重新输入！");
	                    obj.setValue(""); 
	                    obj.focus();
	             	    nui.unmask();
	                    return false;
            	   } else {
            		   nui.get("appDomain").setValue(domain+"<%=domainPostfix%>");
            		   document.getElementById("appDomainTag").innerText = domain+"<%=domainPostfix%>";
            		   var serverType = nui.get("serverTypeCombox").getValue();
                       nui.get("order.itemList[1].attrList[1].attrValue").setValue(serverType + "-" + domain);
                       nui.get("serverTypeItemType").setValue(serverType);
                       nui.get("order.itemList[3].attrList[0].attrValue").setValue("memcached-" + domain);
                       nui.get("order.itemList[2].attrList[0].attrValue").setValue("haproxy-" + domain);
                       nui.get("order.itemList[4].attrList[0].attrValue").setValue("mysql-" + domain);
                       nui.get("order.itemList[5].attrList[1].attrValue").setValue("memcached-session-" + domain);
            	   }
               } 
           });
                 
           nui.unmask();
           return true;
        }       

        function f_check_domainPrefix(obj) {
        	   if (obj == null || obj.value == null || obj.value.trim().length == 0) {
        		   nui.alert("应用标识必须输入");
        	      //obj.focus();
        	      return false;
        	   } else if (obj.value.length < 2 || obj.value.length > 20) {
        		   nui.alert("应用标识长度2-20");
        	      //obj.focus();
        	      return false;
        	   } else {
        	      var reg = new RegExp("^[a-z][a-z0-9_]{1,19}$");
        	      if (!reg.test(obj.value)) {
        	    	  nui.alert("应用标识以小写字母开头，允许小写字母、数字和下划线");
        	         // obj.focus();
        	         return false;
        	      }
        	   }
        	   return true;
        	}
        
        
       function submitForm() {
            // 提交表单数据
            var formSub = new nui.Form("#form1"); 
    
            var data = formSub.getData().order;      //获取表单多个控件的数据
            var json = nui.encode(data);   //序列化成JSON
			// document.getElementById("showText").innerText= json;
            
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/myApp/applyApp",
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                	nui.alert("提交成功，订单号为:" + o.orderId);
                    CloseWindow("cancel"); 
                } 
            });
        }
       
       function insForm() {
           // 提交表单数据
           var form = new nui.Form("#form1"); 
           form.validate();
           if (form.isValid() == false) {
        	   return;
           }
           
	       var obj= nui.get("SLDomainID");
	       if (!f_check_domainPrefix(obj)) {
	           obj.focus();
	           return false;
	       }
           
           var data = form.getData();      //获取表单多个控件的数据
           var json = nui.encode(data);   //序列化成JSON
           var form2 = new nui.Form("#form2"); 
           form2.setData(data);
           form.setChanged(false);
           
           document.getElementById("form1").style.display = "none";
           document.getElementById("form2").style.display = "block";
           
           if (chkMemcachedValue) {
        	   document.getElementById("mFieldset").style.display = "block";
           } else {
        	   document.getElementById("mFieldset").style.display = "none";
           }
           
           if (chKMysqlValue) {
        	   document.getElementById("sqlFieldset").style.display = "block";
           } else {
        	   document.getElementById("sqlFieldset").style.display = "none";
           }
      	   
       }
       
       
       function rtnForm(){
     	   var form = new nui.Form("#form1"); 
           var data = form.getData();      //获取表单多个控件的数据
    	   
    	   document.getElementById("form2").style.display = "none";
    	   document.getElementById("form1").style.display = "block"; 
       }
       
       function onChKDomainChanged(e) {
    	   if (e.checked) {
    		   nui.get("appIsEnableDomain").setValue("Y");
	    	   document.getElementById("domainFieldset").disabled = false;
    	   } else {
    		   nui.get("appIsEnableDomain").setValue("N");
    		   document.getElementById("domainFieldset").disabled = true;
    	   }
       }
       
       var chkSessionValue = false;
       function onChKSessionChanged(e) {
    	  var sessionAttrName = nui.get("order.itemList[5].attrList[1].attrValue");
    	   
    	  var i5i  = nui.get("order.itemList[5].itemType");
    	  var i5a0n = nui.get("order.itemList[5].attrList[0].attrName");
    	  var i5a0v = nui.get("order.itemList[5].attrList[0].attrValue");
    	   
    	  var i5a1n = nui.get("order.itemList[5].attrList[1].attrName");
    	  var i5a1v = nui.get("order.itemList[5].attrList[1].attrValue");
    	   
    	  var i5a2n = nui.get("order.itemList[5].attrList[2].attrName");
    	  var i5a2v = nui.get("order.itemList[5].attrList[2].attrValue");
    	   
    	  var i5a3n = nui.get("order.itemList[5].attrList[3].attrName");
    	  var i5a3v = nui.get("order.itemList[5].attrList[3].attrValue");
    	   
    	  var i5a4n = nui.get("order.itemList[5].attrList[4].attrName");
    	  var i5a4v = nui.get("order.itemList[5].attrList[4].attrValue");

    	   
    	  if (e.checked) {
    		   document.getElementById("sessionFieldset").disabled = false;
    		   sessionAttrName.setRequired(true);
    		   chkSessionValue = true;
    		   
    		   i5i.setName(i5i.getId());
    		   
    		   i5a0n.setName(i5a0n.getId());
    		   i5a0v.setName(i5a0v.getId());
    	    	   
    		   i5a1n.setName(i5a1n.getId());
    		   i5a1v.setName(i5a1v.getId());
    	    	   
    		   i5a2n.setName(i5a2n.getId());
    		   i5a2v.setName(i5a2v.getId());
    	    	   
    		   i5a3n.setName(i5a3n.getId());
    		   i5a3v.setName(i5a3v.getId());
    	    	   
    		   i5a4n.setName(i5a4n.getId());
    		   i5a4v.setName(i5a4v.getId());
    		   
    	   } else {
  		  	   document.getElementById("sessionFieldset").disabled = true;
    		   sessionAttrName.setRequired(false);
  		  	   chkSessionValue = false;
  		  	   
    		   i5i.setName("default");
    		   
    		   i5a0n.setName("default");
    		   i5a0v.setName("default");
    		   
    		   i5a1n.setName("default");
    		   i5a1v.setName("default");
    		   
    		   i5a2n.setName("default");
    		   i5a2v.setName("default");
    		   
    		   i5a3n.setName("default");
    		   i5a3v.setName("default");
    		   
    		   i5a4n.setName("default");
    		   i5a4v.setName("default");
    	   }
       }
       
       var chkMemcachedValue = false;
       
       function onChKMemcachedChanged(e) {
    	  var memcachedAttrName = nui.get("order.itemList[3].attrList[0].attrValue");
    	   
     	  var i3i  = nui.get("order.itemList[3].itemType");
    	  var i3a0n = nui.get("order.itemList[3].attrList[0].attrName");
    	  var i3a0v = nui.get("order.itemList[3].attrList[0].attrValue");
    	  
    	  var i3a1n = nui.get("order.itemList[3].attrList[1].attrName");
    	  var i3a1v = nui.get("order.itemList[3].attrList[1].attrValue");
    	  
    	  var i3a2n = nui.get("order.itemList[3].attrList[2].attrName");
    	  var i3a2v = nui.get("order.itemList[3].attrList[2].attrValue");
    	  
    	  var i3a3n = nui.get("order.itemList[3].attrList[3].attrName");
    	  var i3a3v = nui.get("order.itemList[3].attrList[3].attrValue");
    	   
    	  var i3a4n = nui.get("order.itemList[3].attrList[4].attrName");
    	  var i3a4v = nui.get("order.itemList[3].attrList[4].attrValue");
    	   
    	  if (e.checked) {
    		   document.getElementById("memcachedFieldset").disabled = false;
    		   memcachedAttrName.setRequired(true);
    		   chkMemcachedValue = true;
    		   
    		   i3i.setName(i3i.getId());
    		   
    		   i3a0n.setName(i3a0n.getId());
    		   i3a0v.setName(i3a0v.getId());
    		   
    		   i3a1n.setName(i3a1n.getId());
    		   i3a1v.setName(i3a1v.getId());
    		   
    		   i3a2n.setName(i3a2n.getId());
    		   i3a2v.setName(i3a2v.getId());
    		   
    		   i3a3n.setName(i3a3n.getId());
    		   i3a3v.setName(i3a3v.getId());
    		   
    		   i3a4n.setName(i3a4n.getId());
    		   i3a4v.setName(i3a4v.getId());
    		   
    	   } else {
  		  	   document.getElementById("memcachedFieldset").disabled = true;
  		  	   memcachedAttrName.setRequired(false);
  		  	   chkMemcachedValue = false;
  		  	   
    		   i3i.setName("default");
    		   
    		   i3a0n.setName("default");
    		   i3a0v.setName("default");
    		   
    		   i3a1n.setName("default");
    		   i3a1v.setName("default");
    		   
    		   i3a2n.setName("default");
    		   i3a2v.setName("default");
    		   
    		   i3a3n.setName("default");
    		   i3a3v.setName("default");
    		   
    		   i3a4n.setName("default");
    		   i3a4v.setName("default");
    	   }
       }
       
       var chKMysqlValue = false;
       function onChKMysqlChanged(e) {
    	   
   	      var mysqlAttrName = nui.get("order.itemList[4].attrList[0].attrValue");
   	      var mysqlAttrUName = nui.get("order.itemList[4].attrList[2].attrValue");
   	      var mysqlAttrPwd = nui.get("order.itemList[4].attrList[3].attrValue");
    	   
      	  var i4i  = nui.get("order.itemList[4].itemType");
    	  var i4a0n = nui.get("order.itemList[4].attrList[0].attrName");
    	  var i4a0v = nui.get("order.itemList[4].attrList[0].attrValue");
    	  
    	  var i4a1n = nui.get("order.itemList[4].attrList[1].attrName");
    	  var i4a1v = nui.get("order.itemList[4].attrList[1].attrValue");
    	  
    	  var i4a2n = nui.get("order.itemList[4].attrList[2].attrName");
    	  var i4a2v = nui.get("order.itemList[4].attrList[2].attrValue");
    	  
    	  var i4a3n = nui.get("order.itemList[4].attrList[3].attrName");
    	  var i4a3v = nui.get("order.itemList[4].attrList[3].attrValue");
    	  
    	  var i4a4n = nui.get("order.itemList[4].attrList[4].attrName");
    	  var i4a4v = nui.get("order.itemList[4].attrList[4].attrValue");
    	  
    	  var i4a5n = nui.get("order.itemList[4].attrList[5].attrName");
    	  var i4a5v = nui.get("order.itemList[4].attrList[5].attrValue");
    	   
    	   
    	  if (this.getChecked()) {
    		   chKMysqlValue = true;
    		   document.getElementById("mysqlFieldset").disabled = false;
    		   mysqlAttrName.setRequired(true);
    		   mysqlAttrUName.setRequired(true);
    		   mysqlAttrPwd.setRequired(true);
    		   
    		   i4i.setName(i4i.getId());
    		   
    		   i4a0n.setName(i4a0n.getId());
    		   i4a0v.setName(i4a0v.getId());
    	    	   
    		   i4a1n.setName(i4a1n.getId());
    		   i4a1v.setName(i4a1v.getId());
    	    	   
    		   i4a2n.setName(i4a2n.getId());
    		   i4a2v.setName(i4a2v.getId());
    	    	   
    		   i4a3n.setName(i4a3n.getId());
    		   i4a3v.setName(i4a3v.getId());
    	    	   
    		   i4a4n.setName(i4a4n.getId());
    		   i4a4v.setName(i4a4v.getId());
    		   
    		   i4a5n.setName(i4a5n.getId());
    		   i4a5v.setName(i4a5v.getId());
    	   } else {
    		   chKMysqlValue = false;
  		  	   document.getElementById("mysqlFieldset").disabled = true;
  		  	   mysqlAttrName.setRequired(false);
    		   mysqlAttrUName.setRequired(false);
    		   mysqlAttrPwd.setRequired(false);
  		  	   
    		   i4i.setName("default");
    		   
    		   i4a0n.setName("default");
    		   i4a0v.setName("default");
    		   
    		   i4a1n.setName("default");
    		   i4a1v.setName("default");
    		   
    		   i4a2n.setName("default");
    		   i4a2v.setName("default");
    		   
    		   i4a3n.setName("default");
    		   i4a3v.setName("default");
    		   
    		   i4a4n.setName("default");
    		   i4a4v.setName("default");
    	   }
       }
       
       function uploadFile() {
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/application/uploadFile.jsp",
	            title: "上传SSL安全证书压缩包", width: 650, height: 180,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details"};
	                iframe.contentWindow.SetData(data);
	                
	            },
	            ondestroy: function (action) {
	            	nui.get("uploadFile").setValue(action);
	            }
	        });
       }
       
       var isSSl = false;
       
       function selProtocol() {
    	   var value = nui.get("protocolId").getValue();
    	   if (value == 'HTTPS' || value == 'HTTP-HTTPS' || value == 'MUTUAL-HTTPS' || value == 'MUTUAL-HTTP-HTTPS' ) {
    		   document.getElementById("sslFileId").disabled = false;
    		   nui.get("btn_upload").setEnabled(true);
    		   nui.get("uploadFile").setRequired(true);
    	   } else {
    		   document.getElementById("sslFileId").disabled = true;
    		   nui.get("btn_upload").setEnabled(false);
    		   nui.get("uploadFile").setRequired(false);
    		   isSSl = false;
    	   }
       }
       
       function selChangeMax() {
	       	var maxSize = parseInt(nui.get("maxSize").getValue());
	       	var minSize = parseInt(nui.get("minSize").getValue());
	       	if (maxSize<minSize) {
	       		nui.alert("容器最大实例数不能小于最小实例数");
	       		nui.get("maxSize").setValue(minSize);
	       	}
       }
       
       function selChangeMin() {
	       	var maxSize = parseInt(nui.get("maxSize").getValue());
	       	var minSize = parseInt(nui.get("minSize").getValue());
	       	if (maxSize<minSize) {
	       		nui.alert("容器最小实例数不能大于最大实例数");
	       		nui.get("minSize").setValue(maxSize);
	       	}
       }
 
       
       function onServerTypeChange() {
           var serverTypeCombox = nui.get("serverTypeCombox");
           var serverType = serverTypeCombox.getValue();
           var domain = nui.get("SLDomainID").getValue();
           if (domain) {
              nui.get("order.itemList[1].attrList[1].attrValue").setValue(serverType + "-" + domain);
              nui.get("serverTypeItemType").setValue(serverType);
           }
       }
       
     </script>
</html>