<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
	String id = request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >主机池当前配置</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td >启用监视器:</td>
	                <td >    
	                    <div id="isEnable" name="isEnable" trueValue="1" falseValue="0" class="nui-checkbox" checked="true" readOnly="false" onvaluechanged="onValueChanged"></div>
	                </td>
	            </tr> 
	            <tr>
	            	<td>套餐:</td>
	            	<td id="selId">
	            		<input id="id" name="id" class="nui-combobox"  style="width:150px;"
	            			textField="templateName" valueField="templateId"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getFilterHostPackages"
	            		/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>增加幅度:</td>
	            	<td >
	            		<input id="increaseSize" name="increaseSize" class="nui-combobox"  style="width:150px;"
	            			textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPoolIncreaseSize"
	            		/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>减少幅度:</td>
	            	<td >
	            		<input id="decreaseSize" name="decreaseSize" class="nui-combobox"  style="width:150px;"
	            			textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPoolDecreaseSize"	            		
	            		/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>最大数量:</td>
	            	<td >
	            		<input id="maxSize" name="maxSize" class="nui-combobox"  style="width:150px;"
	            			textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPoolMaxSize"	
						    onvaluechanged = "selChangeMax()"
	            		/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>最小数量:</td>
	            	<td >
	            		<input id="minSize" name="minSize" class="nui-combobox"  style="width:150px;"
	            			textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPoolMinSize"	
						    onvaluechanged = "selChangeMin()"
	            		/>
	            	</td>
	            </tr>	            
	            <tr>
	            	<td>轮询间隔(s):</td>
	            	<td >
	            		<input id="timeInterval" name="timeInterval" class="nui-combobox"  style="width:150px;"
	            			textField="value" valueField="value"  
						    url="<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPoolTimeInterval"	
	            		/>
	            	</td>
	            </tr>	
	            <tr>
	            	<td>重试次数:</td>
	            	<td >
	            		<input id="retrySize" name="retrySize"  size="3" maxlength="3" value="5"  class="nui-textbox"   vtype="int" style="width:50px;" required="true" requiredErrorText="此项为必填项"/>
	            	</td>
	            </tr>	
	           	<tr>
	            	<td>备注信息:</td>
	            	<td >
	            		<input id="remarks" name="remarks" class="nui-textarea"  style="width:200px;height:50px;"/>
	            	</td>
	            </tr>	
	            	            
	        </table>            
	   </div>
	 </fieldset>
	   	 
	  <div style="text-align:center;padding:10px;">      
	  	   <span id="changeInfo-Add">         
	       		<a class="nui-button" onclick="addHostPool" style="width:60px;margin-right:20px;">添加</a>     
				<a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>      
	       </span>      
	  	   <span id="changeInfo-Update" style="display:none">         
	       		<a class="nui-button" onclick="updateHostPool" style="width:60px;margin-right:20px;">修改</a>      
	       		<a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>      
	       </span>      
       </div>   
  </form>

</body>
<script type="text/javascript">
    
    nui.parse();
    
	var id ;
	
    $(document).ready(function() {
        nui.getbyName("id").select(0);
        nui.getbyName("increaseSize").select(0);
        nui.getbyName("decreaseSize").select(0);
        nui.getbyName("maxSize").select(0);
        nui.getbyName("minSize").select(0);
        nui.getbyName("timeInterval").select(0);
	});

    // 标准方法接口定义
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            if (id != null) {
            	nui.getbyName("id").setUrl('<%=request.getContextPath() %>/srv/hostPoolMgr/getHostPackages');
            	document.getElementById("selId").disabled = true;
            	
            	document.getElementById("changeInfo-Add").style.display="none";
            	document.getElementById("changeInfo-Update").style.display="block";
            	
                nui.getbyName("id").setValue(id);
                nui.getbyName("increaseSize").setValue(data.increaseSize);
                nui.getbyName("decreaseSize").setValue(data.decreaseSize);
                nui.getbyName("maxSize").setValue(data.maxSize);
                nui.getbyName("minSize").setValue(data.minSize);
                nui.getbyName("timeInterval").setValue(data.timeInterval);
                nui.get("retrySize").setValue(data.retrySize);
                nui.get("remarks").setValue(data.remarks);
                nui.get("isEnable").setValue(data.isEnable);
            } else {
            	nui.getbyName("id").setUrl('<%=request.getContextPath() %>/srv/hostPoolMgr/getFilterHostPackages');
            }
        }
    }
    
    
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
    
    
    
    function addHostPool() {
   	   var form = new nui.Form("#form1"); 
       var data = form.getData();      //获取表单多个控件的数据
       var json = nui.encode(data);   //序列化成JSON
       
       if (form.isValid() == false) 
    	   return;
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在新增，请稍后...'
        });
		$.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/hostPoolMgr/addHostPool",
            contentType: "application/json; charset=utf-8",
            data: json,
            success: function (text) {
            	if(text){
            		alert('添加成功!');
            		CloseWindow("cancel");
            	}else{
            		alert('添加失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
   
    function updateHostPool() {
		var form = new nui.Form("#form1"); 
        var data = form.getData();      //获取表单多个控件的数据
        var json = nui.encode(data);   //序列化成JSON
        
        if (form.isValid() == false) 
     	   return;
         
		nui.mask({
             el: document.body,
             cls: 'mini-mask-loading',
             html: '正在修改，请稍后...'
		});
		$.ajax({
         	type: "PUT",
             url: "<%=request.getContextPath() %>/srv/hostPoolMgr/updateHostPool",
             contentType: "application/json; charset=utf-8",
             data: json,
             success: function (text) {
             	if (text) {
             		nui.alert('修改成功!');
             		CloseWindow("cancel");
             	} else {
             		nui.alert('修改失败!');
             	}
             	nui.unmask(document.body);
             } 
         });
     }
        
    function selChangeMax() {
    	var maxSize = parseInt(nui.get("maxSize").getValue());
    	var minSize = parseInt(nui.get("minSize").getValue());
    	if (maxSize < minSize) {
    		nui.alert("主机最大数量不能小于主机最小数量");
    		nui.get("maxSize").setValue(minSize);
    	}
    }
    
    function selChangeMin() {
    	var maxSize = parseInt(nui.get("maxSize").getValue());
    	var minSize = parseInt(nui.get("minSize").getValue());
    	if (maxSize < minSize) {
    		nui.alert("主机最小数量不能大于主机最大数量");
    		nui.get("minSize").setValue(maxSize);
    	}
    }
</script>
</html>
