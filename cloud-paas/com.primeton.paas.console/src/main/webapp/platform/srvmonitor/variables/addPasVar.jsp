<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>    
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/monitor/addVar">
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:100px;" align="right">变量名：</td>
                    <td style="width:700px;">    
                         <input name="varKey" class="nui-textbox" width="60%" onvalidation="onVarKeyValidation"/> <span id="varKey_error" style="color:red"></span>
                    </td>
                </tr>
                 <tr>
                    <td style="width:100px;"  align="right">变量值：</td>
                    <td style="width:700px;">    
                         <input name="varValue" class="nui-textbox" width="60%" required="true"/> 
                    </td>
                </tr>
                
                <tr>
                    <td style="width:100px;"  align="right">变量说明：</td>
                    <td style="width:700px;">    
                         <input name="description" class="nui-textarea" width="80%" vtype="rangeLength:0,255"/> * 0-255字 
                    </td>
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

        function onVarKeyValidation(e) {
        	var error = document.getElementById("varKey_error");
        	if (!e.value) {
        		e.errorText = "不能为空！";
        		e.isValid=false;
        	} else {
        		var varKey = e.value;
        		//其他格式验证
        		var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_]{3,512}$");
        		if (!reg.test(varKey)) {
        			e.errorText = "格式不正确！";
        			error.innerHTML = e.errorText;
            		e.isValid=false;
            		return;
        		}
        		//form.loading("操作中，请稍后......");
                $.ajax({
                	url: "<%=request.getContextPath() %>/srv/monitor/checkVarKey/" + varKey,
                    success: function (text) {
                    	//form.ummask();
                    	var o = nui.decode(text);
                    	if (o.result == false) {
                    		e.errorText = "变量名已被使用！";
                      		error.innerHTML = e.errorText;
                      		e.isValid=false;
                    	} else {
                    		error.innerHTML="";
                    		e.isValid=true;
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	e.errorText = "未知错误！";
                    	e.isValid = false;
                    }
                 });
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
                url: "<%=request.getContextPath() %>/srv/monitor/addVar",
                type: "post",
                data: { keyData: json },
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("新增变量成功！");
                		CloseWindow("ok");
                	} else {
                		nui.alert("新增变量失败,请稍后再试！");
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
