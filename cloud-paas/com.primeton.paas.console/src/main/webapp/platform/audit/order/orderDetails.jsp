<%@page import="com.primeton.paas.manage.api.service.RedisService"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.manage.api.service.TomcatService"%>
<%@page import="com.primeton.paas.manage.api.service.SVNRepositoryService"%>
<%@page import="com.primeton.paas.manage.api.service.HaProxyService"%>
<%@page import="com.primeton.paas.manage.api.service.MemcachedService"%>
<%@page import="com.primeton.paas.manage.api.service.MySQLService"%>
<%@page import="com.primeton.paas.manage.api.service.JettyService"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
    <form id="form1" method="post" style="display:none">
		<fieldset style="border:solid 1px #aaa;padding:3px;">
        <legend >
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	订单基本信息：
        </legend>	      
        <input name="id" class="nui-hidden" />
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:200px;"><font size="-1">编号：</font></td>
                    <td style="width:300px;"> 
                    	<input name="orderId"  class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">类型：</font></td>
                    <td >    
                        <input name="orderType" class="nui-combobox asLabel" valueField="id" textField="text" data="CLD_OrderTypes" style="width:200px;"/>
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">状态：</font></td>
                    <td >    
                        <input name="orderStatus" class="nui-combobox asLabel" valueField="id" textField="text" data="CLD_OrderStatus" style="width:200px;"/>
                    </td>
                </tr>       
                <tr>
                    <td ><font size="-1">提交时间：</font></td>
                    <td >    
                        <input name="submitTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" style="width:200px;"/>
                    </td>
                </tr>     
                <tr>
                    <td ><font size="-1">结束时间：</font></td>
                    <td >    
                        <input name="handleTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss"  style="width:200px;"/>
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">处理时长：</font></td>
                    <td >    
                        <input name="handlePeriod" name="BirthDay" class="nui-textbox asLabel" style="width:200px;" />
                    </td>
                </tr>      
                <tr>
                    <td ><font size="-1">生效时间：</font></td>
                    <td >    
                        <input name="beginTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss"  style="width:200px;"/>
                    </td>
                </tr> 
                <tr>
                    <td ><font size="-1">失效时间：</font></td>
                    <td >    
                        <input name="endTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" style="width:200px;" />
                    </td>
                </tr> 
                <tr>
                    <td ><font size="-1">备注信息：</font></td>
                    <td >    
                       <input name="notes" class="nui-textbox asLabel"  style="width:200px;"/>
                    </td>
                </tr> 
            </table>
        </div>
        
	    <div style="width:560px;">
			<table style="width:100%;">
				<tr>
					<td style="width:100%;">
						<a id="bt_agree" class="nui-button" iconCls="icon-ok" onclick="agreeOrder()" text="同意" enabled="false"></a>&nbsp;&nbsp;
						<a id="bt_reject" class="nui-button" iconCls="icon-no" onclick="rejectOrder()" text="拒绝"></a>&nbsp;&nbsp;
						<a id="bt_refresh" class="nui-button" iconCls="icon-reload" onclick="refreshOrder()" text="刷新"></a>&nbsp;&nbsp;
						<a id="bt_remove" class="nui-button" iconCls="icon-remove" onclick="removeOrder()" text="删除"></a>&nbsp;&nbsp;
						<a id="bt_cancel" class="nui-button" iconCls="icon-no" onclick="cancel()" text="取消"></a>       
					</td>
				</tr>
			</table>           
		</div>
		</fieldset>
        
        <span id="appSpan" ></span>
        <span id="appSpan_other" ></span>      
        <span id="haproxySpan" ></span>
        <span id="jettySpan" ></span>
        <span id="tomcatSpan" ></span>
        <span id="sessionSpan" ></span>
        <span id="mysqlSpan" ></span>
        <span id="memcachedSpan" ></span>
        <span id="redisSpan" ></span>
        <span id="svnSpan" ></span>
        <span id="warSpan" ></span>      
        <span id="stretchSpan" ></span>      
        <span id="stretchSpan_INC" ></span>      
        <span id="stretchSpan_DEC" ></span>      
    </form>
    
	<div id="editWindow" class="nui-window" title="添加拒绝原因" style="width:350px;height:200px;" 
    		showModal="true" allowResize="true" allowDrag="true" >
    	<div id="editform" class="form" >
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">备注信息：</td>
                <td style="width:150px;">
                	<input id="remark" name="remark" class="nui-textarea"  style="width:200px;height:100px;"/>
                </td>
            </tr>
        </table>
        <div style="text-align:center;padding:10px;">         
           <a class="nui-button" onclick="rejectApply()" style="width:60px;margin-right:20px;">确定</a>            
           <a class="nui-button" onclick="cancelRow()" style="width:60px;">取消</a>     
        </div>  
    	</div>
	</div>
    
    <script type="text/javascript">
		// nui.parse();
        var form = new nui.Form("form1");
        var orderId ;
        var orderStatus ;
        var dataInfo;    
        
        function dynamicChangeInfo() {
        	//已提交
        	if (orderStatus == 1) {
        		nui.get("bt_agree").setEnabled(true);
        		nui.get("bt_reject").setEnabled(true);
        	}         	
        	
        	//已撤销
        	if (orderStatus == 2) {
        		nui.get("bt_agree").setEnabled(false);
        		nui.get("bt_reject").setEnabled(false);
        	} 
        	
        	//已审批
        	if (orderStatus == 3) {
        		nui.get("bt_agree").setEnabled(false);
        		nui.get("bt_reject").setEnabled(false);
        	}   
        	
        	//已拒绝
        	if (orderStatus == 4) {
        		nui.get("bt_agree").setEnabled(false);
        		nui.get("bt_reject").setEnabled(false);
        	}        	
        	
        	//处理成功
        	if (orderStatus == 5) {
        		nui.get("bt_agree").setEnabled(false);
        		nui.get("bt_reject").setEnabled(false);
        		nui.get("bt_agree").setValue("同意");
        	}
        	
        	//处理失败
        	if (orderStatus == 6) {
        		nui.get("bt_agree").setEnabled(true);
        		nui.get("bt_agree").setText("重新执行");
        	}
        }

        
        function cancelRow() {
        	var editWindow = nui.get("editWindow");
            editWindow.hide();
        }
        
        function SetData(data) {
            if (data.action == "details") {
                //跨页面传递的数据对象，克隆后才可以安全使用
                data = nui.clone(data);
                orderId = data.id;
                dataInfo = data;
                $.ajax({
                    url: "<%=request.getContextPath() %>/srv/orderMgr/details/" + data.id,
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
                        	for(i =0 ; i < o.itemList.length ; i++) {
                        		obj = o.itemList[i];
                        		itemId = obj.itemId;
                        		
                        		//应用基本信息
                        		var srvName = "应用基本信息";
                        		var srvHtmlId = "appSpan";
                        		var itemStatus = obj.itemStatus;
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
                        			srvHtmlId = "tomcatSpan";
                       		    }
                        		if (obj.itemType == "<%=OrderItem.ITEM_TYPE_REDIS%>") {
                        			srvName = "<%=RedisService.TYPE %>服务";
                        			srvHtmlId = "redisSpan";
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
                        			srvHtmlId = "warSpan";
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
                        			srvName  = obj.itemType + "信息";
                        			srvHtmlId = "appSpan_other";
                        		}
                        		
                                var imgInfo = '';
                                var btnRollBack = '';
                        		if (itemStatus == 1) {
                        			imgInfo = '<img alt="" src="<%=request.getContextPath()%>/images/error.gif"  style="height: 20px;"/>&nbsp;';
                        		    btnRollBack = '&nbsp;<a class="nui-button" iconCls="icon-reload" onclick="redo(\''+itemId+'\')" style="width:90px;margin-right:20px;">重新执行</a>';
                        		} else if (itemStatus == 0) {
                        			imgInfo = '<img alt="" src="<%=request.getContextPath()%>/images/confirm.gif"  style="height: 20px;"/>&nbsp;';
                        		}
                        		
                       			attrList = obj.attrList;
       						    var appHtmlHead = '<fieldset style="border:solid 1px #aaa;padding:3px;">'
       						    					  + '<legend >'
       						    					  + imgInfo
       								                  + srvName
       								                  + btnRollBack
       								                  + '</legend>'
       								                  + '<div style="padding:5px;">'
       								                  + '<table>';
       						    appHtmlBody = "";
                       			for (j = 0 ; j <attrList.length ; j++) {
                       				attr =  attrList[j];
                       				attrName = attr.attrName;
                       				
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
                       				
                       				attrValue = attr.attrValue;
                       				if (attrValue == null) {
                       					attrValue = "";
                       				}
       								appHtmlBody += '<tr>'
       										+ '<td style="width:200px;">'
  											+ '<input value="' + attrName + '" class="nui-textbox asLabel" style="width:200px;" />'
  											+ '</td>' 
  											+ '<td style="width:400px;">'
  								            + '<input value="' + attrValue + '" class="nui-textbox asLabel" style="width:350px;" />'
  								            + '</td>'
       								        + '</tr>';   
                       		     }
      							 appHtmlBody += '<tr>'+
													'<td style="width:200px;">'
													+'<img alt="" src="<%=request.getContextPath()%>/images/clock.png" style="height: 20px;"/>'
													+'<input value="订单项处理时长" class="nui-textbox asLabel" style="width:100px;" />'
													+'</td>'
						                       		+'<td style="width:400px;">'
						                        	+'<input value="'+obj.handlePeriod+'" class="nui-textbox asLabel" style="width:100px;" />'
						                        	+'</td>'
				                				+'</tr>';   
								 var appHtmlEnd  = '</table>'           
								                    +'</div>'
								                   +'</fieldset>';
								 appHtml = appHtmlHead +appHtmlBody + appHtmlEnd;
				       			 document.getElementById(srvHtmlId).innerHTML  = appHtml;
                        	}
                        	
                        }  
                        nui.parse();
                        labelModel();
                        form.setData(o);
                        form.setChanged(false);
                        dynamicChangeInfo();
                        document.getElementById("form1").style.display = "block";
                        nui.unmask(document.body);
                    }
                });
            }
        }

        function labelModel() {
            var fields = form.getFields();                
            for (var i = 0, l = fields.length; i < l; i++) {
                var c = fields[i];
                if (c.setReadOnly) {
                	c.setReadOnly(true);     //只读
                }
                if (c.setIsValid) {
                	c.setIsValid(true);      //去除错误提示
                }
            }
        }

        function agreeOrder() {
        	if (!confirm(" 你确定要批准订单 [" + orderId + "] ?")) {
        		return;
        	}
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/orderMgr/approveApply/" + orderId,
                cache: false,
                success: function (text) {
	            }
	        });
            nui.alert("订单正在处理!" );
            orderStatus = 3;
            dynamicChangeInfo();
        }
        
        function rejectOrder() {
        	var editWindow = nui.get("editWindow");
        	editWindow.show();
        }
        
        function rejectApply() {
	        var remark = nui.get("remark").getValue();
	        
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            
	         $.ajax({
	        	 type: "PUT",
	             url: "<%=request.getContextPath() %>/srv/orderMgr/rejectApply/" + orderId,
	             contentType: "application/json; charset=utf-8",
	             data: remark,
	             cache: false,
	             success: function (text) {
	                	var o = nui.decode(text);
	                	if (o) {
	                		nui.alert('处理成功!!');
	                	} else {
	                		nui.alert('处理失败!');
	                	}
		       	         orderStatus = 3;
		    	         dynamicChangeInfo();
		    	         SetData(dataInfo);
		    			 cancelRow();
	                	 nui.unmask(document.body);
		            }
		        });
         	
        }

    	//重新执行订单项
    	function redo(itemId) {
    		if (!confirm("确定重做该订单项 任务?")) {
    			return;
    		}
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/orderMgr/redoItemApply/" + orderId + "&" + itemId,
                cache: false,
                success: function (text) {
	            }
	        });
            nui.alert("当前订单项' " + itemId + " ' 已提交处理申请，请稍后查看." );
            orderStatus = 3;
            dynamicChangeInfo();

    	}
        
        function removeOrder() {
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
	         $.ajax({
	             url: "<%=request.getContextPath() %>/srv/orderMgr/delete/" + orderId,
	             cache: false,
	             success: function (text) {
	                	var o = nui.decode(text);
	                	if (o) {
	                		nui.alert('删除成功!!');
	                	} else {
	                		nui.alert('删除失败!');
	                	}
	                	 nui.unmask(document.body);
	                	 CloseWindow("cancel");
		            }
		        });
        }  
        
    	//刷新页面
    	function refreshOrder() {
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
    		window.setTimeout("SetData(dataInfo)",500); 
    	}
    	
    	//取消
    	function cancel() {
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

    </script>
</body>
</html>
