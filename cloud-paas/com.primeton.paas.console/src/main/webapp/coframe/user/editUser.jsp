<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
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
     <script type="text/javascript">
        var Genders = [{ id: 1, text: '男' }, { id: 2, text: '女'},{id :3, text:'未知'}];
    </script>
</head>
<body>    
     
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/authuser/updateUser">
        <!-- <input name="id" class="nui-hidden" /> -->
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:15%;" align="right">用户标识：</td>
                    <td style="width:65%;">    
                         <input name="userId" id="userId" class="nui-textbox asLabel" style="width:100%"/>
                    </td>
                </tr>
                 
                <tr>
                    <td style="width:10%;"  align="right">用户名：</td>
                    <td style="width:45%;">    
                         <input name="userName" class="nui-textbox" required="true" width="80%"/> 
                    </td>
                    <td> *建议使用真实姓名</td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">电子邮箱：</td>
                    <td style="width:45%;">    
                         <input name="email" class="nui-textbox" required="true" vtype="email;rangeLength:5,30;" onvalidation="onUserEmailValidation" width="80%"/> 
                    </td>
                    <td> *建议使用工作邮箱 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">手机：</td>
                    <td style="width:45%;">    
                         <input name="phone" class="nui-textbox" required="true" onvalidation="onPhoneValidation" width="80%"/> 
                    </td>
                    <td> *手机号码 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">固定电话：</td>
                    <td style="width:45%;">    
                         <input name="tel" class="nui-textbox" onvalidation="onTelValidation" width="80%"/> 
                    </td>
                    <td> *固定电话</td>
                </tr>
                <tr>
                	<td style="width:10%;"  align="right">性别：</td>
                    <td style="width:45%;">    
                         <input name="gender" class="nui-combobox" textField="text" data="Genders" width="80%"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">联系地址：</td>
                    <td style="width:45%;">    
                         <input name="address" class="nui-textarea" vtype="rangeLength:0,255" width="95%"/> 
                    </td>
                    <td>*0-255字 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">备注信息：</td>
                    <td style="width:45%;">    
                         <input name="notes" class="nui-textarea" vtype="rangeLength:0,100" width="95%"/> 
                    </td>
                    <td>*0-100字 </td>
                </tr>
            </table>
        </div>
       
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
    </form>
    <script type="text/javascript">
        nui.parse();

        var form = new nui.Form("form1");

        //验证手机号
        function onPhoneValidation(e){
        	if(e.isValid) {
        		var userId = nui.get("userId").value;
                var pattern = /\d*/;
                if (e.value.length != 11 || pattern.test(e.value) == false) {
                    e.errorText = "请输入11位手机号！";
                    e.isValid = false;
                }
                var phone = e.value;
                $ .ajax({
                	url: "<%=request.getContextPath() %>/srv/authuser/checkUserPhone/",
                	data:{userId:userId,phone:phone},
                	type:"post",
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if(o.result==false){
                    		e.errorText="手机号已被使用！";
                      		e.isValid=false;
                    	}else{
                    		e.isValid=true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText="未知错误！";
                    	e.isValid=false;
                    }
                 });
            }
        }
        
      	//邮箱验证
        function onUserEmailValidation(e){
        	if (e.isValid) {
        		var userId = nui.get("userId").value;
        		var email = e.value;
        		$ .ajax({
                	url: "<%=request.getContextPath() %>/srv/authuser/checkUserEamil/",
                	data:{userId:userId,email:email},
                	type:"post",
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if(o.result==false){
                    		e.errorText="邮箱已被使用！";
                      		e.isValid=false;
                    	}else{
                    		e.isValid=true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText="未知错误！";
                    	e.isValid=false;
                    }
                 });
        	}
        }
        
        //验证电话
        function onTelValidation(e){
        	if (e.isValid) {
                var pattern = /\d*/;
                if (e.value.length <7 || e.value.length>11 || pattern.test(e.value) == false) {
                    e.errorText = "请输入正确的电话号码！";
                    e.isValid = false;
                }
            }
        }
        
        function SetData(data) {
       		data = nui.clone(data);
			var resultData = data.row;
            if (resultData.gender=="m") {
               resultData.gender=1;
            } else if (resultData.gender=="f") {
               resultData.gender=2;
            } else {
               resultData.gender=3;
            }
            var form = new nui.Form("form1");
            form.setData(resultData);
            form.setChanged(false);
        }
        
        function saveData() {
            var data = form.getData();            
            form.validate();
            if (form.isValid() == false) return;
            var json = nui.encode(data);
            form.loading("操作中，请稍后......");
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/authuser/updateUser",
                type: "post",
                data: { keyData: json },
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result==true) {
                		nui.alert("保存成功");
                    	closeWindow("ok");
                	} else {
                		nui.alert("保存失败");
                    	closeWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.alert(jqXHR.responseText);
                    closeWindow("failed");
                }
            });
        }
        
        function closeWindow(action) {            
            if (action == "close" && form.isChanged()) {
                if (confirm("数据被修改了，是否先保存？")) {
                    return false;
                }
            }
            if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
            else window.close();            
        }
        
        function onOk(e) {
            saveData();
        }
        
        function onCancel(e) {
            closeWindow("cancel");
        }
    </script>
</body>
</html>
