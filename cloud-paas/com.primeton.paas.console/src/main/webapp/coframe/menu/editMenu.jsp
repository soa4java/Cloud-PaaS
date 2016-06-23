<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>    
     
    <form id="form1" method="post">
        <input id="menuId" name="menuId" class="nui-hidden" />
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:10%;" align="right">菜单编码：</td>
                    <td style="width:80%;">    
                         <input name="menuCode" style="width:50%;" class="nui-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">菜单名称：</td>
                    <td style="width:80%;">    
                         <input name="menuName" style="width:50%;" class="nui-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">图标样式：</td>
                    <td style="width:80%;">    
                         <input name="imagepath" style="width:50%;" class="nui-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接资源：</td>
                    <td style="width:80%;">    
                    	 <!-- <input name="linkRes" style="width:60%;" class="nui-textbox"/>-->
                         <input name="linkRes" id="linkRes" style="width:50%;" class="nui-buttonedit" onbuttonclick="onbuttonclick" allowInput="false"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接类型：</td>
                    <td style="width:80%;">    
                         <input id="linkType" name="linkType" style="width:50%;" class="nui-textbox" allowInput="false"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接路径：</td>
                    <td style="width:80%;">    
                         <input id="linkAction" name="linkAction" style="width:80%;" class="nui-textarea" allowInput="false"/>
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

        function SetData(data) {
          //跨页面传递的数据对象，克隆后才可以安全使用
        	data = nui.clone(data);
        	var form = new nui.Form("form1");
            form.setData(data.node);
           	nui.get("linkRes").setValue(data.node.linkRes);
           	nui.get("linkRes").setText(data.node.linkRes);
            //form.setChanged(false);
            //保留menuId
            //nui.get("menuId").setValue(data.node.menuId);
        }
        
        function saveData() {
            var o = form.getData();            

            form.validate();
            if (form.isValid() == false) 
            	return;

            var json = nui.encode(o);
            form.loading();
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/authmenu/updateCapMenu",
                type:"post",
                data: { keyData: json },
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("修改成功");
                    	closeWindow("ok");
                	} else {
                		nui.alert("修改失败");
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
        
        function onbuttonclick(){
        	var btnEdit = this;
            nui.open({
                url: bootPATH + "../../coframe/menu/selectFunction.jsp",
                title: "功能选择列表",
                width: 650,
                height: 380,
                ondestroy: function (action) {
                    //if (action == "close") return false;
                    if (action == "ok") {
                        var iframe = this.getIFrameEl();
                        var data = iframe.contentWindow.GetData();
                        data = nui.clone(data);    //必须
                        if (data) {
                        	nui.get("linkRes").setValue(data.funcId);
                           	nui.get("linkRes").setText(data.funcId);
                        	nui.get("linkType").setValue(data.funcType);
                        	nui.get("linkAction").setValue(data.funcAction);
                        }
                    }
                }
            });            
        }
    </script>
</body>
</html>
