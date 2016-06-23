<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

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
        <legend>
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	修改个人资料：
        </legend>	
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" align="center">
                <tr>
                    <td style="width:150px;"><font size="-1">用户名：</font></td>
                    <td style="width:300px;"> 
                    	<input name="userId"  class="nui-textbox  asLabel" style="width:200px;" />   
                    </td>
                    <td>唯一标识，不可修改</td>
                </tr>
                <tr>
                    <td ><font size="-1">姓名：</font></td>
                    <td >    
                        <input id="userName" name="userName" class="nui-textbox" style="width:200px;" required="true" />   
                    </td>
                    <td>* 强烈建议使用真实姓名</td>
                </tr>
                <tr>
                    <td style="width:150px;"><font size="-1">手机号：</font></td>
                    <td style="width:300px;">    
                        <input name="phone" class="nui-textbox" style="width:200px;" required="true" onvalidation="onPhoneValidation"/>   
                    </td>
                    <td>* 手机号码 </td>
                </tr>     
                <tr>
                    <td ><font size="-1">邮箱：</font></td>
                    <td >    
                        <input name="email" class="nui-textbox" style="width:200px;" required="true" vtype="email;rangeLength:5,30;" onvalidation="onUserEmailValidation"/>   
                    </td>
                    <td>* 强烈建议使用工作邮箱 </td>
                </tr>      
                 <tr>
	                <td><font size="-1">性别：</font></td>
	                <td>    
	                    <input name="gender" class="nui-combobox" valueField="id" textField="text" data="Genders" style="width:200px;"/>
	                </td>
               	 </tr>
                <tr>
                    <td ><font size="-1">固定电话：</font></td>
                    <td >    
                        <input name="tel" class="nui-textbox" style="width:200px;" onvalidation="onTelValidation" />   
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">地址：</font></td>
                    <td >    
                        <input name="address" class="nui-textarea"  style="width:200px;height:50px;"  vtype="rangeLength:0,255"/> 
                    </td>
                    <td>0-255字</td>
                </tr> 
                <tr>
                    <td style="width:150px;"><font size="-1">备注：</font></td>
                    <td style="width:300px;">    
                        <input name="notes" class="nui-textarea"  style="width:200px;height:50px;"  vtype="rangeLength:0,100" /> 
                    </td>
                    <td>0-100字 </td>
                </tr> 
            </table>
        </div>
        </fieldset>
        <div style="text-align:center;padding:10px;">               
            <a id="btnRevoke" class="nui-button" onclick="save()" style="width:60px;margin-right:20px;">保存</a>       
            <a id="btCancel" class="nui-button" onclick="cancel()" style="width:60px;margin-right:20px;">取消</a>       
        </div>        
    </form>
    <script type="text/javascript">
    	var Genders = [{ id:"m", text: '男' }, { id: "f", text: '女'}];
    
        nui.parse();
        
        var form = new nui.Form("form1"); 
        
        $(document).ready(function() {
        	loadData();
        });

        function loadData() {
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/myInfo/details",
                cache: false,
                async: false,
                success: function (text) {
                    var o = nui.decode(text);
                    o.createtime = new Date(o.createtime);
                    nui.parse();
                    form.setData(o);
                    form.setChanged(false);
                    document.getElementById("form1").style.display = "block";
                }
            });
        }
        
        function save() {
        	saveData();
        }
        
        //验证手机号
        function onPhoneValidation(e) {
        	if (e.isValid) {
                var pattern = /\d*/;
                if (e.value.length != 11 || pattern.test(e.value) == false) {
                    e.errorText = "请输入11位手机号！";
                    e.isValid = false;
                    return;
                }
            }
        }
        //邮箱验证
        function onUserEmailValidation(e) {
        }
        
        //验证电话
        function onTelValidation(e){
        	if (e.value != null && e.value != '' && e.isValid) {
                var pattern = /\d*/;
                if (e.value.length < 7 || e.value.length > 11 
                		|| pattern.test(e.value) == false) {
                    e.errorText = "请输入正确的电话号码！";
                    e.isValid = false;
                }
            }
        }
        
        function saveData() {
        	var data = form.getData();      
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            var json = nui.encode(data);   
            form.loading("操作中，请稍后......");
			$.ajax({
				type: "PUT",
			    url: "<%=request.getContextPath() %>/srv/myInfo/update",
			    contentType: "application/json; charset=utf-8",
			    data: json,
			    success: function (text) {
                	var o = text;
                	if (null != o  && o == true) {
                		nui.alert("用户修改成功！");
                		cancel();
                	 } else {
                		nui.alert("用户修改失败！");
                	}
			    } 
			});
        }
        
        function cancel(){
        	location.href = "myAccount.jsp" ;
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

    </script>
</body>
</html>
