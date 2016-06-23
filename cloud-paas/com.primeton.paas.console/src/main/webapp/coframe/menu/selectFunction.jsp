<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
    <style type="text/css">
    body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
    </style>
</head>
<body>    
    <div id="selForm" class="nui-toolbar" style="text-align:center;line-height:20px;" borderStyle="border:0;">
    	<table style="width:100%;">
    		 <tr>
                    <td style="width:80px;">功能标识：</td>
                    <td style="width:150px;"><input id="userId" name="funcId" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:80px;">功能名称：</td>
                    <td style="width:150px;"><input id="funcName" name="funcName" class="nui-textbox"  onenter="onKeyEnter"/></td>
             </tr>
              <tr>
                	 <td align="center" colspan="4">
                	 	<a class="nui-button" style="width:60px;" onclick="selForm">查询</a>
                	 </td>
              </tr>
         </table>
    </div>
    <div class="nui-fit">
        <div id="grid1" class="nui-datagrid" style="width:100%;height:100%;" idField="id" allowResize="true"
            borderStyle="border-left:0;border-right:0;" onrowdblclick="onRowDblClick" url="<%=request.getContextPath() %>/srv/authfunction/funclistForSelect"
        >
            <div property="columns">
                <div type="indexcolumn" ></div>
                <div field="funcId" width="30%" headerAlign="center" allowSort="true">功能标识</div>    
                <div field="funcName" width="30%" headerAlign="center" allowSort="true">功能名称</div>                                            
                <div field="funcType" width="30%" headerAlign="center" allowSort="true">功能类型</div>                
                <div field="funcAction" width="30%" headerAlign="center" allowSort="true">调用入口</div>                
            </div>
        </div>
    </div>                
    <div class="nui-toolbar" style="text-align:center;line-height:20px;padding-top:8px;padding-bottom:8px;" borderStyle="border:0;">
        <a class="nui-button" style="width:60px;" onclick="onOk()">确定</a>
        <span style="display:inline-block;width:25px;"></span>
        <a class="nui-button" style="width:60px;" onclick="onCancel()">取消</a>
    </div>

    <script type="text/javascript">
        nui.parse();
        var grid = new nui.get("grid1");
        grid.load();
        grid.sortBy("funcId", "asc");

    	//动态设置URL
    	//grid.setUrl("../data/AjaxService.jsp?method=SearchEmployees");
    	//也可以动态设置列 grid.setColumns([]);
    
    	function GetData() {
        	var row = grid.getSelected();
        	return row;
    	}
    	
    	function selForm() {
    		var form = new nui.Form("#selForm");         
        	var data = form.getData();      //获取表单多个控件的数据
        	var json = nui.encode(data);   
    		grid.load({keyData:json});
   	 	}
    	
    	function onKeyEnter(e) {
    		selForm();
    	}
    	
    	function onRowDblClick(e) {
        	onOk();
    	}
    	
    	function CloseWindow(action) {
        	if (window.CloseOwnerWindow) 
        		return window.CloseOwnerWindow(action);
        	else 
        		window.close();
    	}

    	function onOk() {
        	CloseWindow("ok");
    	}
    	
    	function onCancel() {
        	CloseWindow("cancel");
    	}
    </script>
</body>
</html>
