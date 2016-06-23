<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>    
    <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/audit/rejectCapUsersReg">
    	<input name="ids" id="ids" class="nui-hidden" />
       	<div style="padding-left:5px;padding-bottom:5px;width:100%;height:70%">
            <table style="width:90%;height:100%;table-layout:fixed;" border="0">
                <tr>
                    <td style="width:20%;" align="right">备注信息：</td>
                    <td style="width:80%;">    
                         <input name="notes" id="notes" class="nui-textarea"  vtype="rangeLength:0,255" width="100%"/>
                    </td>
                </tr>
            </table>
        </div>
        <div style="text-align:center;padding:10px;">               
            <a id="agreeButton" class="nui-button" style="width:10%; " onclick="onOk">确定</a>  &nbsp;           
        	<a id="cancelButton" class="nui-button" style="width:10%; " onclick="onCancel">取消</a>       
        </div>        
    </form>
    <script type="text/javascript">
        nui.parse();

        var form = new nui.Form("form1");

        function SetData(data) {
       		data = nui.clone(data);
            var form = new nui.Form("form1");
            form.setData(data);
            form.setChanged(false);
        }
        
        function saveData() {
            form.loading("审批中，请稍后......");
            var ids = nui.get("ids").value;
            var notes = nui.get("notes").value;
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/audit/rejectCapUsersReg/",
                data:{ids:ids,notes:notes},
                type:"post",
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result==true) {
                		nui.alert("完成审批！");
                		closeWindow("ok");
                	} else {
                		nui.alert("审批失败！");
                		closeWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(jqXHR.responseText);
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
            if (window.CloseOwnerWindow) 
            	return window.CloseOwnerWindow(action);
            else 
            	window.close();            
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
