<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.manage.api.service.SVNRepositoryService"%>
<%@page import="com.primeton.paas.manage.api.service.HaProxyService"%>
<%@page import="com.primeton.paas.manage.api.service.MemcachedService"%>
<%@page import="com.primeton.paas.manage.api.service.MySQLService"%>
<%@page import="com.primeton.paas.manage.api.service.TomcatService"%>
<%@page import="com.primeton.paas.manage.api.service.JettyService"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    	<script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    	<script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
	</head>
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
<body>    
    <form id="form1" method="post" style="display:none">
      	<fieldset style="border:solid 1px #aaa;padding:3px;">
        <legend>
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	订单基本信息：
        </legend>	
        <input name="id" class="nui-hidden" />
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:200px;"><font size="-1">订单编号：</font></td>
                    <td style="width:400px;"> 
                    	<input name="orderId"  class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">订单类型：</font></td>
                    <td >    
                        <input name="orderType" class="nui-combobox asLabel" valueField="id" textField="text" data="CLD_OrderTypes" style="width:200px;"/>
                    </td>
                </tr>
               
                <tr>
                    <td ><font size="-1">订单状态：</font></td>
                    <td >    
                        <input name="orderStatus" class="nui-combobox asLabel" valueField="id" textField="text" data="CLD_OrderStatus" style="width:200px;"/>
                    </td>
                </tr>       
                
                <tr>
                    <td ><font size="-1">订单提交时间：</font></td>
                    <td >    
                        <input name="submitTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" style="width:200px;"/>
                    </td>
                </tr>     
                
                <tr>
                    <td ><font size="-1">处理结束时间：</font></td>
                    <td >    
                        <input name="handleTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss"  style="width:200px;"/>
                    </td>
                </tr>
                
                <tr>
                    <td ><font size="-1">订单处理时长：</font></td>
                    <td >    
                        <input name="handlePeriod" name="BirthDay" class="nui-textbox asLabel" style="width:200px;" />
                    </td>
                </tr>      
                
                <tr>
                    <td ><font size="-1">订单生效时间：</font></td>
                    <td >    
                        <input name="beginTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss"  style="width:200px;"/>
                    </td>
                </tr> 
                  
                <tr>
                    <td ><font size="-1">订单失效时间：</font></td>
                    <td >    
                        <input name="endTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" style="width:200px;" />
                    </td>
                </tr> 
                
                <tr>
                    <td ><font size="-1">订单备注：</font></td>
                    <td >    
                       <input name="notes" class="nui-textbox asLabel"  style="width:200px;"/>
                    </td>
                </tr> 
            </table>
        </div>
        </fieldset>
        <span id="appSpan" ></span>
        <span id="jettySpan" ></span>
        <span id="upjasSpan" ></span>
        <span id="sessionSpan" ></span>
        <span id="mysqlSpan" ></span>
        <span id="memcachedSpan" ></span>
        <span id="haproxySpan" ></span>
        <span id="svnSpan" ></span>
        <span id="stretchSpan" ></span>
        <span id="stretchSpan_INC" ></span>
        <span id="stretchSpan_DEC" ></span>

        <div style="text-align:center;padding:10px;">               
            <a id="btnRevoke" class="nui-button" onclick="revokeOrder()" style="width:60px;margin-right:20px;">撤销</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
    </form>
    <script type="text/javascript">
		// nui.parse();
        var form = new nui.Form("form1");
        var orderId ;
        var orderStatus ;
        var dataInfo;    

        function SetData(data) {
            if (data.action == "details") {
                //跨页面传递的数据对象，克隆后才可以安全使用
                data = nui.clone(data);
                orderId = data.id;
                dataInfo = data;

                $.ajax({
                    url: "<%=request.getContextPath() %>/srv/myOrder/details/" + data.id,
                    cache: false,
                    async: false,
                    success: function (text) {
                        var o = nui.decode(text);
                        if (o.submitTime) {
                        	o.submitTime = new Date(o.submitTime);
                        }
                        if (o.handleTime) {
                        	o.handleTime = new Date(o.handleTime);
                        }
                        if (o.beginTime) {
	                        o.beginTime = new Date(o.beginTime);
                        }
                        if (o.endTime) {
	                        o.endTime = new Date(o.endTime);
                        }
                        
                        orderStatus = o.orderStatus;
                        if (null != o && null != o.itemList) {
                        	for (i =0 ; i < o.itemList.length ; i++) {
                        		obj = o.itemList[i];
                        		//应用基本信息
                        		var srvName = "应用基本信息";
                        		var srvHtmlId = "appSpan";
                        		var df = true;
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_APP%>") {
                        			srvName = "应用基本信息";
                        			srvHtmlId = "appSpan";
                        			df = false;
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_JETTY%>") {
                        			srvName = "<%=JettyService.TYPE %>服务器配置";
                        			srvHtmlId = "jettySpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_TOMCAT%>") {
                        			srvName = "<%=TomcatService.TYPE %>服务器配置";
                        			srvHtmlId = "upjasSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_SESSION%>") {
                        			srvName = "<%=OrderItem.ITEM_TYPE_SESSION %>集成式缓存服务";
                        			srvHtmlId = "sessionSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_MYSQL%>") {
                        			srvName = "<%=MySQLService.TYPE %>数据库服务";
                        			srvHtmlId = "mysqlSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_MEMCACHED%>") {
                        			srvName = "<%=MemcachedService.TYPE %>缓存服务";
                        			srvHtmlId = "memcachedSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_HAPROXY%>") {
                        			srvName = "<%=HaProxyService.TYPE %>负载均衡服务";
                        			srvHtmlId = "haproxySpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_SVN_REPO%>") {
                        			srvName = "<%=SVNRepositoryService.TYPE %>资源库";
                        			srvHtmlId = "svnSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_WAR%>") {
                        			srvName = "War版本库服务";
                        			srvHtmlId = "stretchSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_STRETCH_STRATEGY_NAME%>") {
                        			srvName = "伸缩策略";
                        			srvHtmlId = "stretchSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_STRETCH_INC_STRATEGY%>") {
                        			srvName = "伸缩策略【伸】 配置";
                        			srvHtmlId = "stretchSpan_INC";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_STRETCH_DEC_STRATEGY%>") {
                        			srvName = "伸缩策略 【缩】 配置";
                        			srvHtmlId = "stretchSpan_DEC";
                       		    }
                        		
                        		if (srvHtmlId == "appSpan" && df) {
                        			continue;
                        		}
                        		
                       			attrList = obj.attrList;
       						    var appHtmlHead = '<fieldset style="border:solid 1px #aaa;padding:3px;">'
       								                  + '<legend >' + srvName + '</legend>'
       								                  + '<div style="padding:5px;">'
       								                  + '<table>';
       						    appHtmlBody = "";
                       			for (j = 0 ; j <attrList.length ; j++) {
                       				attr =  attrList[j];
                       				attrName = attr.attrName;
                       				attrValue = attr.attrValue;
                       				
	                        		for (var m=0; m<CLD_OrderItem_Service.length; m++) { 
	                        			var sid = CLD_OrderItem_Service[m].id;
	                        			if (attr.attrName == sid) {
	                        				attrName = CLD_OrderItem_Service[m].text; 
	                        				break;
	                        			}
	                        			if (m == CLD_OrderItem_SrvType.length-1) {
	                        				attrName  = attr.attrName;
	                        			}
	                        		}
                       				
                       				if (attrValue==null) {
                       					attrValue = "";
                       				}
       								appHtmlBody += '<tr>'+
  															'<td style="width:200px;">'
  															+'<input value="'+attrName+'" class="nui-textbox asLabel" style="width:200px;" />'
  															+'</td>'
  								                       		+'<td style="width:400px;">'
  								                        	+'<input value="'+attrValue+'" class="nui-textbox asLabel" style="width:350px;" />'
  								                        	+'</td>'
       								                +'</tr>';   
                       		     }
      							 appHtmlBody += '<tr>'+
													'<td style="width:200px;">'
													+'<img alt="" src="<%=request.getContextPath()%>/images/clock.png" style="height: 20px;"/>'
													+'<input value="订单项处理时长" class="nui-textbox asLabel" style="width:100px;" />'
													+'</td>'
						                       		+'<td style="width:400px;">'
						                        	+'<input value="'+obj.handlePeriod+'" class="nui-textbox asLabel" style="width:100px;;" />'
						                        	+'</td>'
				                				+'</tr>';   
								 var appHtmlEnd  = '</table>'           
								                    +'</div>'
								                   +'</fieldset>';
								 appHtml = appHtmlHead +appHtmlBody + appHtmlEnd;
				       			 document.getElementById(srvHtmlId).innerHTML  = appHtml;
                        	}
                        }  
                        // parse
                        nui.parse();
                        
                        labelModel();
                        form.setData(o);
                        form.setChanged(false);
                        dynamicChangeInfo();
                        document.getElementById("form1").style.display = "block";
                    }
                });
            }
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
        
        function onOk(e) {
            SaveData();
        }
        
        function onCancel(e) {
            CloseWindow("cancel");
        }
        
        function dynamicChangeInfo(){
        	//不是提交状态
        	if (orderStatus != 1) {
        		nui.get("btnRevoke").setEnabled(false);
        	}         	
        }
        
    	function revokeOrder() {
	        
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            
	        $.ajax({
	             url: "<%=request.getContextPath() %>/srv/myOrder/getOrderStatus/" + orderId,
	             contentType: "application/json; charset=utf-8",
	             cache: false,
	             success: function (text) {
	                	var o = text;
	                	if (o != 1 || o == null) {
	                		nui.alert("当前订单 ' " + orderId + " ' 已在处理中，不允许撤销.");
		                	nui.unmask(document.body);
	                		return ;
	                	} else {
	                		$.ajax({
	           	             url: "<%=request.getContextPath() %>/srv/myOrder/revokeOrders/" + orderId,
	           	             contentType: "application/json; charset=utf-8",
	           	             cache: false,
	           	             success: function (text) {
	           	                	var o = nui.decode(text);
	           	                	if (o) {
	           	                		orderStatus= 2;
	           	                		SetData(dataInfo);
	           	                		nui.alert('撤销成功!!');
	           	                	} else {
	           	                		nui.alert('撤销失败!');
	           	                	}
	           	                	nui.unmask(document.body);
	           		            }
	           		        });
	                	}
		            }
		        });
    	    
    	}

        function labelModel() {
            var fields = form.getFields();                
            for (var i = 0, l = fields.length; i < l; i++) {
                var c = fields[i];
                if (c.setReadOnly) c.setReadOnly(true);     //只读
                if (c.setIsValid) c.setIsValid(true);      //去除错误提示
				//if (c.addCls) c.addCls("asLabel");          //增加asLabel外观
            }
        }
    </script>
</body>
</html>
