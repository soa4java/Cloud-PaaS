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
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:20%;" align="right">菜单编码：</td>
                    <td style="width:80%;">    
                         <input name="menuCode" style="width:80%;" onvalidation="onMenuCodeValidation" class="nui-textbox"/> <!-- <span id="menuCode_error" style="color:red"></span> -->
                    </td>
                </tr>
                <tr>
                    <td style="width:20%;" align="right">菜单名称：</td>
                    <td style="width:80%;">    
                         <input name="menuName" style="width:80%;" class="nui-textbox" required="true"/>
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
		
        function onMenuCodeValidation(e){
        	if (!e.value) {
        		e.errorText = "不能为空！";
        		e.isValid = false;
        	} else {
        		var menuCode = e.value;
        		//form.loading("操作中，请稍后......");
                $.ajax({
                	url: "<%=request.getContextPath() %>/srv/authmenu/checkMenuCode/" +menuCode,
                	async: false,
                    success: function (text) {
                    	//form.ummask();
                    	var o = nui.decode(text);
                    	if(o.result==false){
                    		e.errorText="菜单编码已被使用！";
                    		//errorspan.innerHTML=e.errorText;
                      		e.isValid=false;
                    	}else{
                    		//errorspan.innerHTML="";
                    		e.isValid=true;
                    	}
                    },
                    error: function () {
                    	form.ummask();
                    	e.errorText="未知错误！";
                    	e.isValid=false;
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
        
        function saveData() {
            var o = form.getData();            
            form.validate();
            if (form.isValid() == false) 
            	return;
            var json = nui.encode(o);
            form.loading();
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/authmenu/addFirstLevelCapMenu",
                type:"post",
                data: { keyData: json },
                success: function (text) {
					// form.unmask();
                	var o = nui.decode(text);
                	if(o.result==true){
                		nui.alert("新增成功");
                    	closeWindow("ok");
                	}else{
                		nui.alert("新增失败");
                    	closeWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
					// form.unmask();
                    nui.alert(jqXHR.responseText);
                    closeWindow("failed");
                }
            });
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
