<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
	String ip = request.getParameter("ip");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/common.js" type="text/javascript"></script>
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
            <legend >主机信息</legend>
        <div style="padding:5px;">
	       	  <table>
	            <tr>
	                <td >主机IP:</td>
	                <td >    
	                    <input id="ip" name="ip" class="nui-textbox asLabel"  style="width:200px;"/>
	                </td>
	            </tr> 
	            <tr>
	            	<td>主机名称:</td>
	            	<td >
	            		<input id="name" name="name" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            <tr>
	            	<td>主机类型:</td>
	            	<td >
	            		<input id="types" name="types" class="nui-textbox asLabel"  style="width:200px;"/>
	            	</td>
	            </tr>
	            
	        </table>            
	   </div>
	 </fieldset>
	 
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
	 	<legend >关联服务列表</legend>
        <div>
		    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:150px;" allowResize="true"
		         idField="id" multiSelect="true"  showPager="false" pageSize="20"
		         url="<%=request.getContextPath() %>/srv/hostMgr/getServices/<%=ip %>" >
		        <div property="columns">   
		            <div field="id" width="50" headerAlign="center" align ="center" allowSort="true" >标识 </div>    
		            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">服务名称</div>    
					<div field="type" width="100" headerAlign="center" align ="center" allowSort="true">服务类型</div>    
					<div field="parentId" width="100" headerAlign="center" align ="center" allowSort="true">父实例标识</div>    
					<div field="ip" width="100" headerAlign="center" align ="center" allowSort="true">IP</div>    
					<div field="port" width="50" headerAlign="center" align ="center" allowSort="true">端口</div>    
					<div field="state" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onSrvStatusRenderer">状态</div>    
					<div field="owner" width="50" headerAlign="center" align ="center" allowSort="true">所有者</div>    
					<div field="state" width="100" headerAlign="center" align ="center" allowSort="true" renderer="onServiceActionRenderer">操作</div>    
		        </div>
		    </div>
	   </div>
	 </fieldset>
	 
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
	 	<legend >关联存储列表</legend>
	 	<div>
	        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
	            <table style="width:100%;">
	                <tr>
	                    <td style="width:100%;">
	                        <a class="nui-button" iconCls="icon-close" onclick="umountStorage()">卸载</a>
	                        <a class="nui-button" iconCls="icon-add" onclick="mountStorage()">挂载</a>
	                        &nbsp;&nbsp;&nbsp;&nbsp;
	                        /storage/<input id="newMount" name="newMount" class="nui-textbox" emptyText="请输入挂载地址" style="width:150px;" />  
	                    </td>
	                </tr>
	            </table>           
	        </div>
	    	<div id="datagrid2" class="nui-datagrid" style="width:100%;height:130px;" allowResize="true"
		         idField="id" multiSelect="true"  showPager="false" pageSize="20"
		         url="<%=request.getContextPath() %>/srv/hostMgr/getStorages/<%=ip %>"
		         onrowclick = "onChkChange()" >
		        <div property="columns">   	
		        	<div type="checkcolumn" ></div>  
		            <div field="id" width="100" headerAlign="center" align ="center" allowSort="true" >存储标识 </div>    
		            <div field="name" width="100"  headerAlign="center" align ="center" allowSort="true">存储名称</div>    
					<div field="path" width="200" headerAlign="center" align ="center" allowSort="true">存储路径</div>    
					<div field="mount" width="150" headerAlign="center" align ="center" allowSort="true">本地挂载点</div>    
					<div field="size" width="80" headerAlign="center" align ="center" allowSort="true">存储大小(G)</div>       
					<div field="status" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onStorageStatusRenderer">是否挂载</div>       
		        </div>
			</div>
	 	</div>  
	 </fieldset>
	 <fieldset style="border:solid 1px #aaa;padding:3px;">
	 	<legend >磁盘信息</legend>
	 	<div>
	    	<div id="datagrid3" class="nui-datagrid" style="width:100%;height:100px;" allowResize="true"
		         idField="id" multiSelect="true"  showPager="false" pageSize="50"
		         url="<%=request.getContextPath() %>/srv/hostMgr/getDisks/<%=ip %>"
		     >
		        <div property="columns">   	
		            <div field="filesystem" width="200" headerAlign="center" align ="center" allowSort="true" >文件系统名称</div>    
		            <div field="size" width="50"  headerAlign="center" align ="center" allowSort="true">大小</div>    
					<div field="used" width="50" headerAlign="center" align ="center" allowSort="true">已使用</div>    
					<div field="avail" width="50" headerAlign="center" align ="center" allowSort="true">可使用</div>    
					<div field="use" width="50" headerAlign="center" align ="center" allowSort="true">已使用</div>       
					<div field="mounted" width="200" headerAlign="center" align ="center" allowSort="true">目录</div>       
		        </div>
			</div>
		</div>
	</fieldset>
	   	 
  </form>

</body>
<script type="text/javascript">
    
    nui.parse();
    
    var grid = nui.get("datagrid1");
    var grid2 = nui.get("datagrid2");
    var grid3 = nui.get("datagrid3");
    
    grid.load();
    grid2.load();
    grid3.load();
    
	var ip ;
	
	var typeList = nui.get("typeList");
	var typeListAdvance = nui.get("typeListAdvance");

    function SetData(data) {
        if (data.action == "details") {
            // 跨页面传递的数据对象，克隆后才可以安全使用
            ip = data.ip;
            var name = data.name;
            var types = data.types;
            nui.get("ip").setValue(ip);
            nui.get("name").setValue(name);
            nui.get("types").setValue(types);
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
    
    
    function onChkChange() {
        var rows = grid2.getSelecteds();
        var count = rows.length;
        if (count == 1){
        	 var mount = rows[0].mount;
        	 mount = mount.replace('/storage/', '');
        	 nui.get("newMount").setValue(mount);
        } else {
        	nui.get("newMount").setValue('');
        }
    }
    
    
	function mountStorage() {
        var rows = grid2.getSelecteds();
        var count = rows.length;
        if (count != 1) {
        	nui.alert('请选中1行记录!');
        	return;
        }
        var id = rows[0].id;
		sMount = nui.get("newMount").getValue();
		if (sMount == '') {
			nui.alert("请输入本地挂载点!");
			return ;			
		}
		if (!chk(sMount)) {
			nui.alert("输入值包含特殊字符!");   
			return;
		}
		if (!confirm("确定进行更改?")) {
			return;
		}
			
		sMount = "/storage/" + sMount;
		
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在挂载，请稍后...'
        });
		
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/storageMgr/updateMountPoint/" +id + "&" + ip,
            contentType: "application/json; charset=utf-8",
            data: sMount,
            success: function (text) {
            	// var o = nui.decode(text);
            	if (text) {
            		nui.alert('挂载成功!');
            		grid2.load();
            	} else {
            		nui.alert('挂载失败!');
            	}
            	nui.unmask(document.body);
            }
        });
		
	}
	
	function umountStorage() {
        var rows = grid2.getSelecteds();
        var count = rows.length;
        if (count != 1) {
        	nui.alert('请选中1行记录!');
        	return;
        }
        var id = rows[0].id;
		
		sMount = "";
		
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: '正在卸载，请稍后...'
        });
		
        $.ajax({
        	type: "PUT",
            url: "<%=request.getContextPath() %>/srv/storageMgr/updateMountPoint/" +id + "&" + ip,
            contentType: "application/json; charset=utf-8",
            data: sMount,
            success: function (text) {
            	// var o = nui.decode(text);
            	if (text) {
            		nui.alert('卸载成功!');
            		grid2.load();
            	} else {
            		nui.alert('卸载失败!');
            	}
            	nui.unmask(document.body);
            }
        });
		
	}
	
	function chk(str) {
		var bForbidden = false;
		var strForbidden = new Array("<",">","*","?","|",'"');          //罗列所有被禁止的方法字符
		var ch;
	    for (var i=0;i<strForbidden.length;i++){        //遍历用户输入的数据
	    	for (var j=0;j<str.length;j++) {
	      		ch=str.substr(j,1);
	      		if (ch==strForbidden[i]) {
	      			bForbidden = true;                  //设置此变量为true
	      		}
	     	}    
	    }
	    if (bForbidden) {
	       return false;
	    }
	    return true;
	}
        
</script>
</html>
