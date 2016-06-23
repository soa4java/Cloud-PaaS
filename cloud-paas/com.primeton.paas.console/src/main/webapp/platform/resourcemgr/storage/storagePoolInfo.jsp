<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
	String id = request.getParameter("id");
	String size = request.getParameter("size");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
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
	<form id="form1" method="post" style="font-size:12px; " >
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >存储池基本详情</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td align="right">存储IP:</td>
	                <td >    
	                    <input id="id" name="id" class="nui-textbox asLabel"  style="width:100px;"/>
	                </td>
	            	<td align="right">存储大小:</td>
	            	<td >
	            		<input id="storageSize" name="storageSize" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	            	<td align="right">是否启用池:</td>
	            	<td >
	            		<input id="isEnable" name="isEnable" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	              </tr>
	           	  <tr>
	                <td align="right">最大存储数:</td>
	                <td >    
	                    <input id="maxSize" name="maxSize" class="nui-textbox asLabel"  style="width:100px;"/>
	                </td>
	            	<td align="right">存储最小数量:</td>
	            	<td >
	            		<input id="minSize" name="minSize" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	            </tr>
	        </table>            
	   </div>
	 </fieldset>
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >池内当前存储列表</legend>
        <div style="padding:5px;">
               <div>
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
				            <table style="width:100%;">
				                <tr>
				                    <td>
				                        <a class="nui-button" iconCls="icon-goto" onclick="applyStroage()">分配</a>
				                        <a class="nui-button" iconCls="icon-close" onclick="removeStroage()">销毁</a>
				                        <a class="nui-button" iconCls="icon-reload" onclick="refresStroages()">刷新</a>
				                    </td>
				                </tr>
				            </table>           
				        </div>
				    </div>
	              <div id="datagrid1" class="nui-datagrid" style="width:100%;height:150px;" allowResize="true"
				         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/storagePoolMgr/listStorage/<%=size %>" showPager="false" pageSize="100">
				        <div property="columns">
				            <div type="checkcolumn" ></div>        
				            <div field="id" width="80"    headerAlign="center" align ="center" allowSort="true" >存储编号</div>    
				            <div field="name" width="100"  headerAlign="center" align ="center" allowSort="true">存储名</div>    
							<div field="path" width="150" headerAlign="center" align ="center" allowSort="true">存储路径</div>    
				        </div>
				  </div>
		 	</div>
		</fieldset>
		<div style="text-align:center;padding:10px;">                     
			<a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
		</div>   
	</form>
</body>
<script type="text/javascript">
    
    nui.parse();
    
	var id ;
	
	var grid = nui.get("datagrid1");

    //标准方法接口定义
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            var name = data.name;
            var hostPackageName = data.hostPackageName;
            var size = data.size;
            var storageSize = data.storageSize;
            var isEnable = data.isEnable;
            var maxSize = data.maxSize;
            var minSize = data.minSize;
            nui.get("id").setValue(id);
            nui.get("storageSize").setValue(storageSize);
            nui.get("isEnable").setValue(isEnable);
            nui.get("maxSize").setValue(maxSize);
            nui.get("minSize").setValue(minSize);
            grid.load();
        }
    }
    
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
    function CloseWindow(action) {            
        if (action == "close" && form.isChanged()) {
            if (confirm("数据被修改了，是否先保存？")) {
                return false;
            }
        }
        if (window.CloseOwnerWindow) {
        	return window.CloseOwnerWindow(action);
        } else {
        	window.close();            
        }
    }
     
    function applyStroage() {
        var rows = grid.getSelecteds();
        var count = rows.length;
        if (count < 1) {
        	nui.alert('请选中记录!');
        	return;
        }
        var rlist = rows[0].id;
        for (i= 1 ; i< rows.length ;i++) {
        	rlist += ',' + rows[i].id;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在分配，请稍后...'
        });
        
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/storagePoolMgr/apply",
            contentType: "application/json; charset=utf-8",
            data: rlist,
            success: function (text) {
            	if (text) {
            		nui.alert('分配成功!');
            		grid.load();
            	} else {
            		nui.alert('分配失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
    
    
    function removeStroage() {
        var rows = grid.getSelecteds();
        var count = rows.length;
        if (count < 1) {
        	nui.alert('请选中记录!');
        	return;
        }
        var rlist = rows[0].id;
        for (i= 1 ; i< rows.length ;i++) {
        	rlist += ',' + rows[i].id;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在销毁，请稍后...'
        });
        
        $.ajax({
        	type: "PUT",
        	url: "<%=request.getContextPath() %>/srv/storageMgr/remove",
            contentType: "application/json; charset=utf-8",
            data: rlist,
            success: function (text) {
            	if (text) {
            		nui.alert('销毁成功!');
            		grid.load();
            	} else {
            		nui.alert('销毁失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
    
    function refresStroages(){
    	grid.load();
    }
</script>
</html>
