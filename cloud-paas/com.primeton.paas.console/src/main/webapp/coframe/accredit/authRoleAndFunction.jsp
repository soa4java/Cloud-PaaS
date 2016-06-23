<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>角色授权管理</title>
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
 	<style type="text/css">
	    html,body
	    {
	        width:100%;
	        height:100%;
	        border:0;
	        margin:0;
	        padding:0;
	        overflow:visible;
	    }
    </style>
</head>
<body>
	当前角色:<input id="roleCode" name="roleCode" class="nui-textbox asLabel"/>  <input class="nui-hidden" id="roleId" name="roleId"/>    
    <table id="authtable" style="height:90%;width:100%" border="0">
        <tr>
            <td style="width:45%;height:100%">
                <div id="listbox1" class="nui-listbox" style="width:100%;height:100%;"
                    textField="text" valueField="id" showCheckBox="true" multiSelect="true">
                    <div property="columns">
                        <div header="功能标识" field="funcId" ></div>
                        <div header="功能名称" field="funcName" ></div>
                        <!-- <div header="调用入口" field="funcAction" ></div> -->
                    </div>
                </div>
            </td>
            <td style="width:10%;text-align:center;">
                <input type="button" value=">" onclick="add()" style="width:80%;"/><br />
                <input type="button" value=">>" onclick="addAll()" style="width:80%;"/><br />
                <input type="button" value="&lt;&lt;" onclick="removeAll()" style="width:80%;"/><br />
                <input type="button" value="&lt;" onclick="removes()" style="width:80%;"/><br />

            </td>
            <td style="width:45%;height:100%">
                <div id="listbox2" class="nui-listbox" style="width:100%%;height:100%;"                     
                     showCheckBox="true" multiSelect="true">     
                    <div property="columns">
                        <div header="功能标识" field="funcId" ></div>
                        <div header="功能名称" field="funcName" ></div>
                        <!-- <div header="调用入口" field="funcAction" ></div> -->
                    </div>
                </div>
            </td>
        </tr>
    </table>   
     
    <div style="width:100%;position:relative;">
     	<div style="position:absolute;width:50%;margin-left:43%">
			<a class="nui-button" iconCls="icon-save" onclick="onOk()">确定</a>
    		<a class="nui-button" iconCls="icon-close" onclick="onCancel()">取消</a>
    	</div>
    </div>
	
    <script type="text/javascript">        
        nui.parse();
		
        ////////////////////////////////////////////////////////
        nui.parse();
        var listbox1 = nui.get("listbox1");
        var listbox2 = nui.get("listbox2");

        function add() {
            var items = listbox1.getSelecteds();
            listbox1.removeItems(items);
            listbox2.addItems(items);
        }
        
        function addAll() {        
            var items = listbox1.getData();
            listbox1.removeItems(items);
            listbox2.addItems(items);
        }
        
        function removes() {
            var items = listbox2.getSelecteds();
            listbox2.removeItems(items);
            listbox1.addItems(items);
        }
        
        function removeAll() {
            var items = listbox2.getData();
            listbox2.removeItems(items);
            listbox1.addItems(items);
        }
        
        function SetData(data) {
			var roleId = data.roleId;
			var roleCode = data.roleCode;
			document.getElementById("roleId").value=roleId;
			document.getElementById("roleCode").innerHTML=roleCode;
			$.ajax({
		          url: "<%=request.getContextPath() %>/srv/authfunction/getInverCapFunctionByRoleId/" +roleId,
		          success: function (text) {
		              var o = nui.decode(text);
		              listbox1.loadData(o);
		          },
		          error: function () {
		          }
		    });
			$.ajax({
	          url: "<%=request.getContextPath() %>/srv/authfunction/getCapFunctionByRoleId/" +roleId,
	          success: function (text) {
	              var o = nui.decode(text);
	              listbox2.loadData(o);
	          },
	          error: function () {
	          }
	       });
        }
		
        function saveData() {
        	var roleId = document.getElementById("roleId").value;
        	var data = listbox2.getData();
            var json = nui.encode(data);
            nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在授权，请稍后...'});
            $.ajax({
  	          url: "<%=request.getContextPath() %>/srv/authrole/saveRoleAndFunctionAuth/"+roleId,
  	          type:"post",
  	          data:{keyData:json},
  	          success: function (text) {
  	              var o = nui.decode(text);
  	              if (o.result) {
  	            	nui.alert("授权成功！");
  	            	 closeWindow("ok");
  	              } else {
  	            	nui.alert("授权失败!");
 	            	 closeWindow("failed");
  	              }
  	          },
  	          error: function (jqXHR, textStatus, errorThrown) {
  	        	nui.alert("系统繁忙！请稍候重试！"+jqXHR.responseText);
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