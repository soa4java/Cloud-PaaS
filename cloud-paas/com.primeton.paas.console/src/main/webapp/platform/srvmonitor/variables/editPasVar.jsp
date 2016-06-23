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
</head>
<body>    
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/monitor/addVar">
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:100px;" align="right">变量名：</td>
                    <td style="width:700px;">    
                         <input name="varKey" class="nui-textbox asLabel" width="60%" />
                    </td>
                </tr>
                 <tr>
                    <td style="width:100px;"  align="right">变量值：</td>
                    <td style="width:700px;">    
                         <input name="varValue" class="nui-textbox" width="80%" required="true"/> 
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

        function CloseWindow(action) {
            if (window.CloseOwnerWindow) {
            	window.CloseOwnerWindow(action);
            } else {
            	window.close();
            }
        }
        
        function SetData(data) {
       		data = nui.clone(data);
			var resultData = data.row;
            var form = new nui.Form("form1");
            form.setData(resultData);
            form.setChanged(false);
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
                url: "<%=request.getContextPath() %>/srv/monitor/updateVar",
                type: "post",
                data: { keyData: json },
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("修改变量成功！");
                		CloseWindow("ok");
                	} else {
                		nui.alert("修改变量失败,请稍后再试！");
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
