<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
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
            <legend >填写数据源信息</legend>
        <div style="padding:5px;">
	        <table>
	            <tr>
	                <td style="width:100px;">数据源名称:</td>
	                <td style="width:300px;">    
	                	<input id="DataSourceNameID"  name="ds.dataSourceName"  class="nui-textbox" required="true" requiredErrorText="此项为必填项" onblur="validateDSName(this.value);"/>
	                </td>
	            </tr>
	            <tr>
	                <td >数据库服务名称:</td>
	                <td >    
	                    <input name="ds.dataSourceId"  id="DataSourceID" class="nui-combobox"  url="<%=request.getContextPath() %>/srv/appSetting/listDBService" valueField="id" textField="name"  required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >初始化连接数:</td>
	                <td >    
	                    <input name="ds.initialPoolSize" id="initialPoolSizeId" value="5"  class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >最小连接数:</td>
	                <td >    
	                    <input name="ds.minPoolSize" id="minPoolSizeId" value="5"  class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >最大连接数:</td>
	                <td >    
	                    <input name="ds.maxPoolSize" id="maxPoolSizeId" value="5" class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>
	                </td>
	            </tr>   
	            <tr>
	                <td >连接重试次数:</td>
	                <td >    
	                    <input name="ds.acquireRetryAttempts"  value="-1" class="nui-textbox" required="true" requiredErrorText="此项为必填项"/>&nbsp;<span style="color: #ff0000;">（-1表示无限制）</span>
	                </td>
	            </tr>   
	            <tr>
	                <td >数据源描述:</td>
	                <td >    
	                    <input name="ds.dataSourceDesc" class="nui-textarea"  style="width:200px;height:50px;"/>
	                </td>
	            </tr>     
	            <tr>
	                <td >&nbsp;</td>
	                <td >    
	                    <span style="color: #ff0000;">注意：添加或者修改数据源后，应用需要重新启动才能生效。</span>
	                </td>
	            </tr>          
	        </table>            
        </div>
        </fieldset>
   	  
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">添加</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>
        
	      <input class="nui-hidden" name="ds.acquireRetryDelay" value="1000" />
	      <input class="nui-hidden" name="ds.acquireIncrement" value="2" />
	      <input class="nui-hidden" name="ds.checkoutTimeout" value="10000" />
	</form>
	<script type="text/javascript">
        nui.parse();
        
        var form = new nui.Form("form1");
        
        var appName ;
        
        
        $(document).ready(function() {
            nui.get("DataSourceID").select(0);
		});
        
        function SetData(data) {
            if (data.action == "details") {
            	appName = data.appName;
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
            if (window.CloseOwnerWindow) {
            	return window.CloseOwnerWindow(action);
            } else {
            	window.close();            
            }
        }
        
        function submitForm() {
            var formSub = new nui.Form("#form1"); 
            var data = formSub.getData().ds;       //获取表单多个控件的数据
            var json = nui.encode(data);   		//序列化成JSON
            formSub.validate();
            if (formSub.isValid() == false) {
            	return;
            }
            if (!validateAddAppDataSource()) {
            	return;
            }
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/appSetting/addAppDataSource/" + appName,
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o) {
                		nui.alert('添加成功!');
                   		CloseWindow("cancel"); 
                	} else {
                		nui.alert('添加失败!');
                	}
                } 
            });
        }
       
        function validateAddAppDataSource() {
           if (isExsitDB() == false) {
              nui.alert("当前不存在已开通的数据库服务！");
              return false;
           }
           var obj = nui.get("DataSourceNameID");
           if (!f_check_datasource(obj)) {
              return false;
           }
           var min = nui.get("minPoolSizeId").getValue();
           var max = nui.get("maxPoolSizeId").getValue();
           var init = nui.get("initialPoolSizeId").getValue();
           if (min == null || min.length == 0 || max == null 
        		   || max.length == 0 || init == null 
        		   || init.length == 0) {
              return false;
           }
           var minNum = parseInt(min);
           var maxNum = parseInt(max);
           var initNum = parseInt(init);
           if (minNum > maxNum) {
              nui.alert("最小连接数不能大于最大连接数！");
              return false;
           }
           if (initNum > maxNum) {
              nui.alert("初始连接数不能大于最大连接数！");
              return false;
           }
           return true;
        }
     
        function isExsitDB() {
           return true;
        }
        
        function validateDSName(dataSourceName) {
        	var obj= nui.get("DataSourceNameID");
        	dataSourceName = dataSourceName.trim(); 
      		if (obj == null ||obj.value == null 
      				|| obj.value.trim().length == 0) {
       			return false;
      		}
           nui.mask({
               el: document.body,
               cls: 'mini-mask-loading',
               html: '正在效验，请稍后...'
           });
           
           $.ajax({
        	   url: "<%=request.getContextPath() %>/srv/appSetting/isExistDataSource/" + appName + "&" + dataSourceName  ,
               cache: false,
               success: function (text) {
            	   var o = nui.decode(text);
	               	if (o.result) {
	                    nui.alert("应用的变量名称“" + dataSourceName + "”已经存在，请重新输入！");
	                    obj.setValue(""); 
	                    obj.focus();
	               		nui.unmask(document.body);
	               		return false;
	               	}
	               	nui.unmask(document.body);
	            }
	        });
           return true;
        }  
        
        // RichWeb 自定义表单校验
        function f_check_datasource(obj) {
           var id = obj.getValue();
           if (id == null || id.trim().length == 0) {
              nui.alert("数据源名称必填");
              obj.focus();
              return false;
           }
           if (id.length < 4 || id.length > 30) {
              nui.alert("数据源名称长度4-30");
              obj.focus();
              return false;
           } else {
              var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,29}$");
              if (!reg.test(obj.value)) {
                 nui.alert("数据源名称以字母开头，允许数字和下划线");
                 obj.focus();
                 return false;
              }
           }
           return true;
        }
      
	</script>
</body>
</html>