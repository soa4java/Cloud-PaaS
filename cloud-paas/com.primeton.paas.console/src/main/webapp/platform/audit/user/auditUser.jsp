<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
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
        var Genders = [{ id:"m", text: '男' }, { id: "f", text: '女'},{id :"3", text:'未知'}];
    </script>
</head>
<body>    
     
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/audit/agreeCapUsersReg">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >基本信息</legend>
            <div style="padding:5px;">
		        <table>
		            <tr>
	                    <td style="width:20%;" align="right">用户标识：</td>
	                    <td style="width:80%;">    
	                         <input name="userId" id="userId" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>
	                 
	                <tr>
	                    <td style="width:20%;"  align="right">真实姓名：</td>
	                    <td style="width:80%;">    
	                         <input name="userName" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:20%;"  align="right">账户状态：</td>
	                    <td style="width:80%;">    
	                         <input name="status" class="nui-combobox asLabel" textField="text" data="CLD_UserStatus" width="100%"/>
	                    </td>
                	</tr>      
                	
                	<tr>
	                	<td style="width:20%;"  align="right">性别：</td>
	                    <td style="width:80%;">    
	                         <input name="gender" class="nui-combobox asLabel" textField="text" data="Genders" width="100%"/>
	                    </td>
                	</tr>  
		        </table>            
            </div>
        </fieldset>
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >联系方式</legend>
            <div style="padding:5px;">
		        <table>
		             <tr>
	                    <td style="width:20%;"  align="right">电子邮箱：</td>
	                    <td style="width:80%;">    
	                         <input name="email" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>
	                <tr>
	                    <td style="width:20%;"  align="right">固定电话：</td>
	                    <td style="width:80%;">    
	                         <input name="tel" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td style="width:20%;"  align="right">手机：</td>
	                    <td style="width:80%;">    
	                         <input name="phone" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>
	                
	                <tr>
	                    <td style="width:20%;"  align="right">联系地址：</td>
	                    <td style="width:80%;">    
	                         <input name="address" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>        
		        </table>            
            </div>
        </fieldset>
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >其他</legend>
            <div style="padding:5px;">
		        <table>
		            <tr>
	                    <td style="width:20%;"  align="right">备注信息：</td>
	                    <td style="width:80%;">    
	                         <input name="notes" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
		            <tr>
	                    <td style="width:20%;"  align="right">最后登录时间：</td>
	                    <td style="width:80%;">    
	                         <input name="lastlogin" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="100%"/>
	                    </td>
                	</tr>      
		            <tr>
	                    <td style="width:20%;"  align="right">创建时间：</td>
	                    <td style="width:80%;">    
	                         <input name="createtime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="100%"/>
	                    </td>
                	</tr>      
		            <tr>
	                    <td style="width:20%;"  align="right">审批人员：</td>
	                    <td style="width:80%;">    
	                         <input name="handler" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
		        </table>            
            </div>
        </fieldset>
       <div style="text-align:center;padding:10px;">  
        	<a id="agreeButton" class="nui-button" style="width:8%; " onclick="onAgree">同意</a>  &nbsp;           
        	<a id="refuseButton" class="nui-button" style="width:8%; " onclick="onRefuse">拒绝</a> &nbsp;         
        	<a id="deleteButton" class="nui-button" style="width:8%; " onclick="onDelete">删除</a>  &nbsp;           
        	<a id="cancelButton" class="nui-button" style="width:8%; " onclick="onCancel">取消</a>             
       </div>
    </form>
    <script type="text/javascript">
        nui.parse();

        var form = new nui.Form("form1");

        function SetData(data) {
       		data = nui.clone(data);
			var resultData = data.row;
			//状态显示
            if (resultData.gender != "f" && resultData.gender != "m" ) {
               resultData.gender=3;
            }
			//时间显示
            if (resultData.lastlogin) {
            	resultData.lastlogin = new Date(resultData.lastlogin);
            }
            if (resultData.createtime){
            	resultData.createtime = new Date(resultData.createtime);
            }
            // 根据状态调整按钮状态
            if (resultData.status!=1) {
            	nui.get("agreeButton").setEnabled(false);
            	nui.get("refuseButton").setEnabled(false);
            } else {
    			nui.get("agreeButton").setEnabled(true);
    			nui.get("refuseButton").setEnabled(true);
    		}
            var form = new nui.Form("form1");
            labelModel();
            form.setData(resultData);
            form.setChanged(false);
        }
        
        function saveData() {
            if (confirm("确定同意该注册用户？")) {
	        	var ids = nui.get("userId").value;
	            form.loading("操作中，请稍后......");
	            $.ajax({
	                url: "<%=request.getContextPath() %>/srv/audit/agreeCapUsersReg/" +ids,
	                success: function (text) {
	                	nui.unmask();
	                	var o = nui.decode(text);
	                	if (o.result == true) {//result
	                		nui.alert("完成审批！");
	                		closeWindow("ok");
	                	} else {
	                		nui.alert("审批失败！");
	                		closeWindow("failed");
	                	}
	                },
	                error: function (jqXHR, textStatus, errorThrown) {
	                	closeWindow("failed");
	                	nui.alert(jqXHR.responseText);
	                }
	            });
            }
        }
        
        function closeWindow(action) {            
            if (action == "close" && form.isChanged()) {
                if (confirm("数据被修改了，是否先保存？")) {
                    return false;
                }
            }
            if (window.CloseOwnerWindow) 
            	return window.CloseOwnerWindow(action);
            else 
            	window.close();            
        }
        
        function onAgree() {
            saveData();
        }
        
        function onRefuse(){
        	var ids = nui.get("userId").value;
        	nui.open({
	        	url: bootPATH + "../../platform/audit/user/refuse.jsp",
	            title: "拒绝用户注册申请的原因", width: 500, height: 200,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { ids:ids };
	                iframe.contentWindow.SetData(data);
	            },
	            ondestroy: function (action) {
	                if (action=='ok') {
	                	closeWindow("ok");
	                } else {
	                	closeWindow("failed");
	                }
	            }
	        });
        }
        
        function onDelete(){
        	var ids = nui.get("userId").value;
        	form.loading("操作中，请稍后......");
        	$.ajax({
                 url: "<%=request.getContextPath() %>/srv/audit/deleteUsers/" +ids,
                 success: function (text) {
                 	var o = nui.decode(text);
                 	if (o.result==true) {//result
                 		nui.alert("删除成功");
                 		closeWindow("ok");
                 	} else {
                 		nui.alert("删除失败");
                 		closeWindow("failed");
                 	}
                 },
                 error: function (jqXHR, textStatus, errorThrown) {
                 	closeWindow("failed");
                 	nui.alert(jqXHR.responseText);
                 }
             });
        }
        
        function onCancel(e) {
            closeWindow("cancel");
        }
        
        function labelModel() {
            var fields = form.getFields();                
            for (var i = 0, l = fields.length; i < l; i++) {
                var c = fields[i];
                if (c.setReadOnly) 
                	c.setReadOnly(true);     //只读
                if (c.setIsValid) 
                	c.setIsValid(true);      //去除错误提示
            }
        }
    </script>
</body>
</html>
