<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

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
        	基本信息：
        </legend>	
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">
                <tr>
                    <td style="width:150px;"><font size="-1">用户名：</font></td>
                    <td style="width:300px;"> 
                    	<input name="userId"  class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">姓名：</font></td>
                    <td >    
                        <input id="userName" name="userName" class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>
               
                <tr>
                    <td ><font size="-1">账户状态：</font></td>
                    <td >    
                        <input name="status" class="nui-combobox asLabel" valueField="id" textField="text" data="CLD_UserStatus" style="width:200px;"/>
                    </td>
                </tr>     
                
                 <tr>
	                <td><font size="-1">性别：</font></td>
	                <td>    
	                    <input name="gender" class="nui-combobox asLabel" valueField="id" textField="text" data="Genders" style="width:200px;"/>
	                </td>
               	 </tr>  
                
                </table>  
        </div>
        </fieldset>
        <fieldset style="border:solid 1px #aaa;padding:3px;">
        <legend>
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	联系方式：
        </legend>	
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">    
                <tr>
                    <td style="width:150px;"><font size="-1">手机号：</font></td>
                    <td style="width:300px;">    
                        <input name="phone" class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>     
                <tr>
                    <td ><font size="-1">固定电话：</font></td>
                    <td >    
                        <input name="tel" class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>
                <tr>
                    <td ><font size="-1">邮箱：</font></td>
                    <td >    
                        <input name="email" class="nui-textbox asLabel" style="width:200px;" />   
                    </td>
                </tr>      
                <tr>
                    <td ><font size="-1">地址：</font></td>
                    <td >    
                        <input name="address" class="nui-textbox asLabel" style="width:200px;" /> 
                    </td>
                </tr> 
              </table>  
           </div>
        </fieldset>   
        <fieldset style="border:solid 1px #aaa;padding:3px;">
        <legend>
        	<img alt="" src="<%=request.getContextPath()%>/images/muen6.png"  style="height: 20px;"/>
        	其他：
        </legend>	
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;">          
                <tr>
                    <td style="width:150px;"><font size="-1">备注：</font></td>
                    <td style="width:300px;">    
                        <input name="notes" class="nui-textbox asLabel" style="width:200px;" /> 
                    </td>
                </tr> 
                <tr>
                    <td ><font size="-1">创建时间：</font></td>
                    <td >    
                       <input name="createtime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" style="width:200px;" />
                    </td>
                </tr> 
            </table>
        </div>
        </fieldset>
        <div style="text-align:center;padding:10px;">               
            <a id="btnRevoke" class="nui-button" onclick="toUpdate()" style="width:60px;margin-right:20px;">修改</a>       
        </div>        
    </form>
    <script type="text/javascript">
    	var Genders = [{ id:"m", text: '男' }, { id: "f", text: '女'},{id :"3", text:'未知'}];
        nui.parse();
        var form = new nui.Form("form1"); 
        
        $(document).ready(function(){
        	SetData();
        });

        function SetData() {
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/myAccount/details",
                cache: false,
                async: false,
                success: function (text) {
                    var o = nui.decode(text);
                    o.createtime = new Date(o.createtime);
                    nui.parse();
                    labelModel();
                    form.setData(o);
                    form.setChanged(false);
                    document.getElementById("form1").style.display="block";
                }
            });
        }
        
        function toUpdate(){
        	location.href = "upAccount.jsp" ;
        }

        function labelModel() {
            var fields = form.getFields();                
            for (var i = 0, l = fields.length; i < l; i++) {
                var c = fields[i];
                if (c.setReadOnly) c.setReadOnly(true);     //只读
                if (c.setIsValid) c.setIsValid(true);      //去除错误提示
            }
        }
    </script>
</body>
</html>
