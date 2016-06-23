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
            <legend >存储信息</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td >存储标识:</td>
	                <td >    
	                    <input id="id" name="id" class="nui-textbox asLabel"  style="width:200px;"/>
	                </td>
	            </tr> 
	            <tr>
	            	<td>存储名称:</td>
	            	<td >
	            		<input id="name" name="name" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>存储路径:</td>
	            	<td >
	            		<input id="path" name="path" class="nui-textbox asLabel"  style="width:400px;"/>
	            	</td>
	            </tr>
	           	<tr>
	            	<td>存储大小:</td>
	            	<td >
	            		<input id="size" name="size" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	        </table>            
	   </div>
	 </fieldset>
	 
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
	 	<legend >关联主机列表</legend>
	 	
        <div style="padding:5px;">
		 	<div style="width:850px;">
		        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
		            <table style="width:100%;">
		                <tr>
		                    <td style="width:100%;">
		                        <a class="nui-button" iconCls="icon-remove" onclick="delMountIp()">删除</a>
		                        <a class="nui-button" iconCls="icon-add" onclick="addMountIp()">添加挂载IP</a>
		                        <input id="hostIp" name="hostIp" class="nui-textbox" emptyText="请输入IP" style="width:150px;" />  
		                    </td>
		                </tr>
		            </table>           
		        </div>
		    </div>
		    <div id="datagrid1" class="nui-datagrid" style="width:850px;height:150px;" allowResize="true"
		         idField="id" multiSelect="true"  showPager="false" pageSize="20"
		         url="<%=request.getContextPath() %>/srv/storageMgr/getWhiteLists/<%=id %>"
		     >
		        <div property="columns">   
		        	<div type="checkcolumn" ></div>      
		            <div field="ip" width="50" headerAlign="center" align ="center" allowSort="true" >挂载主机IP </div>    
		            <div field="path" width="150"  headerAlign="center" align ="center" allowSort="true">本地挂载点</div>    
					<div field="status" width="100" headerAlign="center" align ="center" allowSort="true" renderer="onStorageStatusRenderer">是否挂载</div>    
		        </div>
		    </div>
	   </div>
	 </fieldset>
	</form>
</body>
<script type="text/javascript">
    
    nui.parse();
    
    var grid = nui.get("datagrid1");
    grid.load();

    var id ;
    
    //标准方法接口定义
    function SetData(data) {
        if (data.action == "details") {
            //跨页面传递的数据对象，克隆后才可以安全使用
            id = data.id;
            var name = data.name;
            var path = data.path;
            var size = data.size;
            nui.get("id").setValue(id);
            nui.get("name").setValue(name);
            nui.get("path").setValue(path);
            nui.get("size").setValue(size);
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
    
    function delMountIp() {
        var rows = grid.getSelecteds();
        var count = rows.length;
        if (count < 1){
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
            html: '正在删除，请稍后...'
        });
        
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/storageMgr/delMountIps/" + id,
            contentType: "application/json; charset=utf-8",
            data: rlist,
            success: function (text) {
            	if (text) {
            		nui.alert('删除成功!');
            		grid.load();
            	} else {
            		nui.alert('删除失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
    }
    
    function addMountIp() {
        var hostIp = nui.get("hostIp").getValue();
        if (hostIp == null || hostIp == '') {
        	nui.alert("请输入主机IP，进行挂载!");
        	return;
        }
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在挂载，请稍后...'
        });
        $.ajax({
            url: "<%=request.getContextPath() %>/srv/storageMgr/addMountIp/"+ id +"&" + hostIp,
            success: function (text) {
            	if (text) {
            		nui.alert('挂载成功!');
            		grid.load();
            	} else {
            		nui.alert('挂载失败!');
            	}
            	nui.unmask(document.body);
            } 
        });
     }
</script>
</html>
