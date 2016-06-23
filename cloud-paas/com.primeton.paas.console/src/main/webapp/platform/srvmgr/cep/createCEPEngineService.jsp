<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.service.CEPEngineService"%>

<%
	String hostTemplatesJSON = SystemVariable.getHostTemplatesJSON();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/common.js" type="text/javascript"></script>
</head>
<body>         
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/createCep">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >新增CEP服务向导</legend>
            <div style="padding:5px;">
            	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_CEPENGINE %>" class="nui-hidden" />
		        <table border="0">
		            <tr>
	                    <td style="width:20%;" align="right">是否主备：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_IS_BACKUP%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[0].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*是否主备.</td>
	                </tr>	
		            <tr>
	                    <td style="width:20%;" align="right">独占主机：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_IS_STANDALONE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[1].attrValue"  class="nui-radiobuttonlist"  textField="text" valueField="id" value="N" data=" [{ id: 'Y', text: '是'}, { id: 'N', text: '否'}]" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*服务独占主机或者使用共享主机.</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">服务名称：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[2].attrValue"  class="nui-textbox" onvalidation="onNullValidation" value="<%=CEPEngineService.TYPE %>-default" width="90%"/>
	                    </td>
	                    <td align="left">*资源库服务名称，仅用于展示。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">主机套餐：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[3].attrName" value="<%=OrderItemAttr.ATTR_HOSTPKG_ID%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[3].attrValue"  class="nui-combobox" data='<%=hostTemplatesJSON %>' value="20130517J001" width="90%"/>
	                    </td>
	                    <td align="left">*选择需要创建的主机机型。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">组名称：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[4].attrName" value="<%=OrderItemAttr.ATTR_CEP_GROUP_NAME%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[4].attrValue" id="groupName" class="nui-textbox" value="default" width="90%"/>
	                    </td>
	                    <td align="left">*组名称</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">消息服务器名称：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[5].attrName" value="<%=OrderItemAttr.ATTR_CEP_MQ_SERVER%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[5].attrValue" id="mqServerName" class="nui-textbox" value="PAAS.MONITOR.MQServer" width="90%"/>
	                    </td>
	                    <td align="left">*消息服务器名称</td>
	                </tr>
	                
		            <tr>
	                    <td style="width:15%;" align="right">消息地址：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[6].attrName" value="<%=OrderItemAttr.ATTR_CEP_MQ_DESTS%>" class="nui-hidden" />
	                    	 <input id="order.itemList[0].attrList[6].attrValue" name="order.itemList[0].attrList[6].attrValue" value="EventEntrance" class="nui-hidden"/>
	                    	 <input name="order.itemList[0].attrList[7].attrName" value="<%=OrderItemAttr.ATTR_CEP_MQ_TYPES%>" class="nui-hidden" />
	                    	 <input id="order.itemList[0].attrList[7].attrValue" name="order.itemList[0].attrList[7].attrValue" value="exchange" class="nui-hidden"/>
	                    	 <table id="mqdests" style="width:90%" border="0">
		                       <tr style="width:90%%">
		                       	   <td width="42%">名称： <input type="text" name="mqDest0" id="mqDest0" value="EventEntrance" style="width:60%"/></td>
		                  		   <td width="42%">类型：<select name="mqType0" id="mqType0">
												<option value="exchange">exchange</option>
												<option value="queue">queue</option>
			   				 				</select>
			   				 		</td>
			   				 		<td>
										<!-- 			   				 			
										<input class="nui-button" value="+" onclick="addMQServer('mqdests');"/>
										<input class="nui-button" value="-" onclick="deleteMQServer('mqdests');"/> 
										-->
			   				 			<a class="nui-button" onclick="addMQServer('mqdests');"><span style="font-size:8px;">+</span></a>
			   				 			<a class="nui-button" onclick="deleteMQServer('mqdests');"><span style="font-size:8px;">-</span></a>
			   				 		</td>
			   				 	</tr>
			   				 </table>
	                    </td>
	                    <td align="left">*消息地址类型:exchange | queue。</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最小内存：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[8].attrName" value="<%=OrderItemAttr.ATTR_JDBC_PASSWORD%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[8].attrValue"  class="nui-textbox" vtype="range:128,4096" rangeErrorText="数字必须在128~4096之间" value="128" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*JVM参数：minMemory, 单位:MB</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">最大内存：</td>
	                    <td style="width:50%;">    
	                    	 <input name="order.itemList[0].attrList[9].attrName" value="<%=OrderItemAttr.ATTR_JDBC_PASSWORD%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[9].attrValue"  class="nui-textbox" vtype="range:256,8192" rangeErrorText="数字必须在256~8192之间" value="256" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*JVM参数：maxMemory, 单位:MB</td>
	                </tr>
		            <tr>
	                    <td style="width:15%;" align="right">非堆内存大小：</td>
	                    <td style="width:50%;">    
	                         <input name="order.itemList[0].attrList[10].attrName" value="<%=OrderItemAttr.ATTR_JDBC_MIN_POOL_SIZE%>" class="nui-hidden" />
	                    	 <input name="order.itemList[0].attrList[10].attrValue"  class="nui-textbox" vtype="range:64,2048" rangeErrorText="数字必须在64~2048之间" value="64" onvalidation="onNullValidation" width="90%"/>
	                    </td>
	                    <td align="left">*JVM参数：maxPermSize, 单位:MB</td>
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
        
        var t_rownum=0; //当前添加行
    	//动态添加
    	function addMQServer(table_id){
    		var myTable = document.getElementById(table_id);
     		t_rownum++;
     		var newRow = myTable.insertRow(t_rownum);
     		//newRow.style.width="40%";
    		var col1 = newRow.insertCell(0);
    		col1.innerHTML = "名称： <input type='text' name='mqDest"+t_rownum+"' id='mqDest"+t_rownum+"' value=''  style='width:60%'/>";
    		var col2 = newRow.insertCell(1);
    		col2.innerHTML = "类型：<select name='mqType"+t_rownum+"' id='mqType"+t_rownum+"'><option value='exchange'>exchange</option><option value='queue'>queue</option></select></td>";
    		//col2.align = "right";
    		//var col3 = newRow.insertCell(2);
    		//var rowId = newRow.id
    		//col3.innerHTML = ""
    	}
    	
    	function deleteMQServer(table_id) {
    		if (t_rownum==0){
    			return;
    		}
    		var myTable = document.getElementById(table_id);
    		myTable.deleteRow(t_rownum);
    		t_rownum--;
    	}
        
    	function doMqDestInfo() {
    		//处理 mqdest mqserver/type  //获取
    	    var myTable = document.getElementById("mqdests");
    	    var mqDestStr = "";
    	    var mqTypeStr = "";
    	    var len = myTable.rows.length;
    	    
    	    //地址名称不重复
    	    for (var i=0;i<len;i++) {
    	    	var mqdest = document.getElementById("mqDest"+i).value;
    	    	var mqType = document.getElementById("mqType"+i).value;
    	    	if (mqdest == "" || mqType == "") {
    	    		nui.alert("请填写全部消息地址信息！");
    	    		return false;
    	    	}
    	    	for (var j=0;j<len;j++) {
    	    		if (i != j && document.getElementById("mqDest"+j).value == mqdest && mqdest != "") {
    	    			nui.alert("消息地址重复！" + mqdest);
    	    			document.getElementById("mqDest"+j).focus();
    	    			return false;
    	    		}
    	    	}
    	    }
    	    
    	    for(var i=0; i<len; i++) {
    	    	var mqdest = document.getElementById("mqDest"+i).value;
    	    	var mqType = document.getElementById("mqType"+i).value;
    	    	
    	    	if (mqType=="exchange") {
    	    		mqType='<%=CEPEngineService.MQ_TYPE_EXCHANGE %>';
    	    	} else {
    	    		mqType='<%=CEPEngineService.MQ_TYPE_QUEUE %>';
    	    	}
    	    	mqDestStr = mqDestStr + mqdest;
    	    	mqTypeStr = mqTypeStr + mqType;
    	    	if (i < len-1) {
    	    		mqDestStr = mqDestStr+",";
    	    		mqTypeStr = mqTypeStr+",";
    	    	}
    	    }
    	    nui.get("order.itemList[0].attrList[6].attrValue").value = mqDestStr;
    	    nui.get("order.itemList[0].attrList[7].attrValue").value = mqTypeStr;
    	    return true;
    	}
    	
        function onAdd() {
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            
        	if (!doMqDestInfo()) {
        		return;
        	}
        	if (!confirm("你确定要提交CEP服务创建申请？")) {
    	        return false;
    	    }
            var data = form.getData().order;
            var json = nui.encode(data);
            $.ajax({
				url: "<%=request.getContextPath() %>/srv/service/createCep",
				contentType: "application/json; charset=utf-8",
				data: json,
				type: "PUT",
                success: function (text) {
                }
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