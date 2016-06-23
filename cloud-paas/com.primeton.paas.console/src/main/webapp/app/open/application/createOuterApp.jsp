<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.manage.api.model.OrderItem"%>
<%@page import="com.primeton.paas.manage.api.model.OrderItemAttr"%>
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
            <legend >外部应用信息</legend>
        <div style="padding:5px;">
        	<input name="order.itemList[0].itemType" value="<%=OrderItem.ITEM_TYPE_APP%>" class="nui-hidden" />
	        <table>
	            <tr>
	                <td style="width:100px;">外部应用标识:</td>
	                <td style="width:300px;">    
	                    <input name="order.itemList[0].attrList[0].attrName" value="<%=OrderItemAttr.ATTR_APP_NAME%>" class="nui-hidden" />
	                    <input id="SLDomainID" name="order.itemList[0].attrList[0].attrValue" class="nui-textbox"  required="true" requiredErrorText="此项为必填项" onblur="checkDomain(this.value);"/>
	                </td>
	                <td>唯一标识，也是二级域名前缀，创建后不可修改。</td>
	            </tr>
	            <tr>
	                <td >外部应用名称:</td>
	                <td >    
	                    <input name="order.itemList[0].attrList[1].attrName" value="<%=OrderItemAttr.ATTR_DISPLAY_NAME%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[1].attrValue" class="nui-textbox" required="true" requiredErrorText="此项为必填项" style="width:200px;"/>
	                </td>
	                <td>应用的显示名称，与业务无关。</td>
	            </tr>   
	            <tr>
	                <td >外部应用描述:</td>
	                <td >    
	                	<input name="order.itemList[0].attrList[2].attrName" value="<%=OrderItemAttr.ATTR_APP_DESC%>" class="nui-hidden" />
	                    <input name="order.itemList[0].attrList[2].attrValue" class="nui-textarea"  style="width:200px;height:50px;"/>
	                </td>
	                <td>不超过200个字符。</td>
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
	
			        <div style="padding:5px;">
				        <table>
				            <tr>
				                <td style="width:100px;">外部应用标识:</td>
				                <td style="width:300px;">    
				                   <input name="order.itemList[0].attrList[0].attrValue"  class="nui-textbox asLabel" style="width:200px;" />   
				                </td>
				            </tr>    
				            <tr>
				                <td style="width:100px;">外部应用名称:</td>
				                <td style="width:300px;">    
				                   <input name="order.itemList[0].attrList[1].attrValue"  class="nui-textbox asLabel" style="width:200px;" />   
				                </td>
				            </tr> 
				            <tr>
				                <td style="width:100px;">外部应用描述:</td>
				                <td style="width:300px;">    
				                   <input name="order.itemList[0].attrList[2].attrValue"  class="nui-textbox asLabel" style="width:200px;" />   
				                </td>
				            </tr>    
				        </table>            
			        </div>
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
    
        nui.parse();

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

        function checkDomain(domain) {
        	var obj= nui.get("SLDomainID");
           domain = domain.trim(); 

           nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在效验，请稍后...'});
           $.ajax({
           	   type: "get",
               url: "<%=request.getContextPath() %>/srv/myApp/isExistDomain/"+domain,
               success: function (text) {
            	   if(text.data){
	                    nui.alert("应用标识“" + domain + "”已经存在，请重新输入！");
	                    obj.setValue(""); 
	                    obj.focus();
	             	    nui.unmask();
	                    return false;
            	   }
               } 
           });
           
           nui.unmask(); 
           return true;
        }    
        
        function f_check_domainPrefix(obj) {
     	   if(obj == null ||obj.value == null || obj.value.trim().length == 0) {
     	      nui.alert("应用标识必须输入");
     	      //obj.focus();
     	      return false;
     	   } else if(obj.value.length < 2 || obj.value.length > 20) {
     	      nui.alert("应用标识长度2-20");
     	      //obj.focus();
     	      return false;
     	   } else {
     	      var reg = new RegExp("^[a-z][a-z0-9_]{1,19}$");
     	      if(!reg.test(obj.value)){
     	         nui.alert("应用标识以小写字母开头，允许小写字母、数字和下划线");
     	         //obj.focus();
     	         return false;
     	      }
     	   }
     	   return true;
     	}
        
        function submitForm() {
            var formSub = new nui.Form("#form1"); 
            var data = formSub.getData().order;      //获取表单多个控件的数据
            var json = nui.encode(data);   //序列化成JSON
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/myApp/applyOuterApp",
                contentType: "application/json; charset=utf-8",
                data: json,
                success: function (text) {
                	var o = nui.decode(text);
                    nui.alert("提交成功，订单号为:" + o.orderId);
                    CloseWindow("cancel"); 
                } 
            });
        }
       
       function insForm(){
           //提交表单数据
           var form = new nui.Form("#form1"); 
           form.validate();
           if (form.isValid() == false) return;
           
	       var obj= nui.get("SLDomainID");
		   if(!f_check_domainPrefix(obj)) {
			  obj.focus();
	          return false;
		   }
           
           var data = form.getData();      //获取表单多个控件的数据
           var json = nui.encode(data);   //序列化成JSON
           var form2 = new nui.Form("#form2"); 
           form2.setData(data);
           form.setChanged(false);
           
           document.getElementById("form1").style.display="none";
           document.getElementById("form2").style.display="block";
      	   
       }
       
       
       function rtnForm() {
     	   var form = new nui.Form("#form1"); 
           var data = form.getData();      //获取表单多个控件的数据
    	   document.getElementById("form2").style.display="none";
    	   document.getElementById("form1").style.display="block"; 
       }
 
     </script>
</html>