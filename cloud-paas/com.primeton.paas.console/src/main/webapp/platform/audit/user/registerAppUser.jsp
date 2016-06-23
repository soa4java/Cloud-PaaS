<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><%=NLSMessageUtils.getMessage("user.register") %></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
    <script type="text/javascript">
        var Genders = [{ id: 1, text: '男' }, { id: 2, text: '女'},{id :3, text:'未知'}];
    </script>
</head>
<body>    
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/audit/registerAppUser">
        <!-- <input name="id" class="nui-hidden" /> -->
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:15%;" align="right">用户标识：</td>
                    <td style="width:40%;">    
                         <input name="userId" class="nui-textbox" onvalidation="onUserIdValidation" width="80%"/> 
                    </td>
                    <td>* 长度为4-15位，以字母开头、允许数字和下划线</td>
                </tr>
                 <tr>
                    <td style="width:10%;"  align="right">用户名：</td>
                    <td style="width:40%;">    
                         <input name="userName" class="nui-textbox" required="true" width="80%"/> 
                    </td>
                    <td>*建议使用真实姓名</td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">登录密码：</td>
                    <td style="width:40%;">    
                         <input name="password" vtype="rangeLength:6,25;" class="nui-password" required="true" vtype="rangeLength:6,25" width="80%"/>
                    </td>
                    <td>*6-25位的英文字母、数字和特殊符号的组合</td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">电子邮箱：</td>
                    <td style="width:40%;">    
                         <input name="email" class="nui-textbox" required="true" vtype="email;rangeLength:5,30;" onvalidation="onUserEmailValidation" width="80%"/> 
                    </td>
                    <td>*建议使用工作邮箱 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">手机：</td>
                    <td style="width:40%;">    
                         <input name="phone" class="nui-textbox" required="true" onvalidation="onPhoneValidation" width="80%"/> 
                    </td>
                    <td>*手机号码 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">固定电话：</td>
                    <td style="width:40%;">    
                         <input name="tel" class="nui-textbox" onvalidation="onTelValidation" width="80%"/> 
                    </td>
                    <td>*固定电话</td>
                </tr>
                <tr>
                	<td style="width:10%;"  align="right">性别：</td>
                    <td style="width:40%;">    
                         <input name="gender" class="nui-combobox" textField="text" data="Genders"  value="1" width="80%"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">联系地址：</td>
                    <td style="width:40%;">    
                         <input name="address" class="nui-textarea" vtype="rangeLength:0,255" width="100%"/> 
                    </td>
                    <td>*0-255字 </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">备注信息：</td>
                    <td style="width:40%;">    
                         <input name="notes" class="nui-textarea" vtype="rangeLength:0,100" width="100%"/>
                    </td>
                    <td> *0-100字 </td>
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

        function onUserIdValidation(e){
        	if(!e.value){
        		e.errorText="不能为空！";
        		e.isValid=false;
        	}else{
        		var userId = e.value;
        		//其他格式验证
        		var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,19}$")
        		if(!reg.test(userId)){
        			e.errorText="格式不正确！";
            		e.isValid=false;
            		return;
        		}
                $.ajax({
                	url: "<%=request.getContextPath() %>/srv/audit/checkUserId/" + userId,
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == true) {
                    		e.errorText = "帐号已被使用！";
                      		e.isValid = false;
                    	} else {
                    		e.isValid = true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText = "未知错误！";
                    	e.isValid = false;
                    }
                 });
        	}
        }
        
        function onPhoneValidation(e){
        	if (e.isValid) {
                var pattern = /\d*/;
                if (e.value.length != 11 || pattern.test(e.value) == false) {
                    e.errorText = "请输入11位手机号！";
                    e.isValid = false;
                    return;
                }
                var phone = e.value;
                $.ajax({
                	url: "<%=request.getContextPath() %>/srv/audit/checkUserPhone/",
                	data:{phone:phone},
                	type:"post",
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == true){
                    		e.errorText = "手机号已被使用！";
                      		e.isValid = false;
                    	} else {
                    		e.isValid = true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText = "系统错误！请稍后重试！";
                    	e.isValid = false;
                    }
                 });
            }
        }
        
        function onUserEmailValidation(e) {
        	if (e.isValid) {
        		var email = e.value;
        		$ .ajax({
                	url: "<%=request.getContextPath() %>/srv/audit/checkUserEamil/",
                	data:{email:email},
                	type:"post",
                	async: false,
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == true) {
                    		e.errorText = "邮箱已被使用！";
                      		e.isValid = false;
                    	} else {
                    		e.isValid = true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText = "系统错误！请稍后重试！";
                    	e.isValid = false;
                    }
                 });
        	}
        }
        
        function onTelValidation(e){
        	if (e.isValid) {
                var pattern = /\d*/;
                if (e.value.length <7 || e.value.length>11 || pattern.test(e.value) == false) {
                    e.errorText = "请输入正确的电话号码！";
                    e.isValid = false;
                }
            }
        }
        
        function CloseWindow(action) {
            if (window.CloseOwnerWindow) {
            	window.CloseOwnerWindow(action);
            } else {
            	window.close();
            }
        }
        
        function saveData() {
        	var data = form.getData();      //获取表单多个控件的数据
        	form.validate();
            if (form.isValid() == false) {
            	return;
            }
            var json = nui.encode(data);   //序列化成JSON
            form.loading("操作中，请稍后......");
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/audit/registerAppUser",
                type: "post",
                data: { keyData: json },
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("用户注册成功，等待审批！");
                		CloseWindow("ok");
                	} else {
                		nui.alert("用户注册失败,请稍后再试！");
                		CloseWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    nui.alert(jqXHR.responseText);
                    CloseWindow("failed");
                }
            });
            
        }
        
        function onOk(e) {
            saveData();
        }
        
        function onCancel(e) {
            CloseWindow("cancel");
        }
    </script>
</body>
</html>
