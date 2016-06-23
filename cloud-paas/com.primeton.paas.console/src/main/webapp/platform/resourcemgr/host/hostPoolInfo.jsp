<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
	String id = request.getParameter("id");
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
            <legend >主机池基本详情</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td align="right">主机IP:</td>
	                <td >    
	                    <input id="id" name="id" class="nui-textbox asLabel"  style="width:100px;"/>
	                </td>
	            	<td align="right">主机池套餐:</td>
	            	<td >
	            		<input id="hostPackageName" name="hostPackageName" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	            	<td align="right">是否启用池:</td>
	            	<td >
	            		<input id="isEnable" name="isEnable" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	              </tr>
	           	  <tr>
	                <td align="right">最大主机数:</td>
	                <td >    
	                    <input id="maxSize" name="maxSize" class="nui-textbox asLabel"  style="width:100px;"/>
	                </td>
	            	<td align="right">主机最小数量:</td>
	            	<td >
	            		<input id="minSize" name="minSize" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	            	<td align="right">机型:</td>
	            	<td >
	            		<input id="model" name="model" class="nui-textbox asLabel"  style="width:100px;"/>
	            	</td>
	            </tr>
	        </table>            
	   </div>
	 </fieldset>
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >池内当前主机列表</legend>
        <div style="padding:5px;">
               <div>
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
				            <table style="width:100%;">
				                <tr>
				                    <td>
				                        <a class="nui-button" iconCls="icon-add" onclick="addHost()">新增主机</a>
				                        <a class="nui-button" iconCls="icon-goto" onclick="applyHost()">分配</a>
				                        <a class="nui-button" iconCls="icon-close" onclick="removeHost()">销毁</a>
				                        <a class="nui-button" iconCls="icon-reload" onclick="refreshHosts()">刷新</a>
<!-- 				                        <a class="nui-button" iconCls="icon-add" onclick="edit()">返回</a> -->
				                    </td>
				                </tr>
				            </table>           
				        </div>
				    </div>
	              <div id="datagrid1" class="nui-datagrid" style="width:100%;height:150px;" allowResize="true"
				         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/hostPoolMgr/list/<%=id %>" showPager="false" pageSize="100">
				        <div property="columns">
				            <div type="checkcolumn" ></div>        
				            <div field="ip" width="80"    headerAlign="center" align ="center" allowSort="true" >主机IP</div>    
				            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">主机名</div>    
<!-- 							<div field="types" width="50" headerAlign="center" align ="center" allowSort="true">主机类型</div>     -->
							<div field="controlable" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onHostControlableRenderer">主机状态</div>   
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

    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            var name = data.name;
            var hostPackageName = data.hostPackageName;
            var size = data.size;
            var isEnable = data.isEnable;
            var maxSize = data.maxSize;
            var minSize = data.minSize;
            var model = data.model;
            nui.get("id").setValue(id);
            nui.get("hostPackageName").setValue(hostPackageName);
            nui.get("isEnable").setValue(isEnable);
            nui.get("maxSize").setValue(maxSize);
            nui.get("minSize").setValue(minSize);
            nui.get("model").setValue(model);
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
        if (window.CloseOwnerWindow) 
        	return window.CloseOwnerWindow(action);
        else 
        	window.close();            
    }
     
    function applyHost() {
        var rows = grid.getSelecteds();
        var count = rows.length;
        if (count < 1) {
        	nui.alert('请选中记录!');
        	return;
        }
        var rlist = rows[0].ip;
        for(i= 1 ; i< rows.length ;i++){
        	rlist += ',' + rows[i].ip;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在分配，请稍后...'
        });
        
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/hostPoolMgr/applyHosts",
            contentType: "application/json; charset=utf-8",
            data: rlist,
            success: function (text) {
            	if(text){
            		nui.alert('分配成功!');
            		grid.load();
            	} else {
            		nui.alert('分配失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
    
    
    function removeHost() {
        var rows = grid.getSelecteds();
        var count = rows.length;
        if (count < 1) {
        	nui.alert('请选中记录!');
        	return;
        }
        var rlist = rows[0].ip;
        for(i= 1 ; i< rows.length ;i++){
        	rlist += ',' + rows[i].ip;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在销毁，请稍后...'
        });
        
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/hostPoolMgr/delHosts",
            contentType: "application/json; charset=utf-8",
            data: rlist,
            success: function (text) {
            	if(text){
            		nui.alert('销毁成功!');
            		grid.load();
            	} else {
            		nui.alert('销毁失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
    
    function refreshHosts(){
    	grid.load();
    }
    
    function addHost() {
        nui.open({
            url: "<%=request.getContextPath() %>/platform/resourcemgr/host/addHost.jsp",
            title: "增加主机", width: 250, height: 145,
            onload: function () {
                var iframe = this.getIFrameEl();
                var data = { action: "details", id:id };
                iframe.contentWindow.SetData(data);
            },
            ondestroy: function (action) {
                grid.reload();
                
            }
        });
    }     
    
    
     </script>
</html>
