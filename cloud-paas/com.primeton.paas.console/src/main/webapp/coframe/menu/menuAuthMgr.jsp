<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=utf-8" %>
<head>
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
    body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }    
    </style> 
</head>
<body>
<div class="nui-splitter" style="width:100%;height:100%;">
    <div size="240" showCollapseButton="true">
    	<div class="nui-toolbar" style="padding:2px;border-top:0;border-left:0;border-right:0;">                
            <a class="nui-button" style="width:100%" plain="true" onclick="showAllMenuRoot()">菜单树</a>                  
        </div>
        <div class="nui-fit">
        	<!--capMenu.txt-->
        	<ul id="tree1" class="nui-tree" url="<%=request.getContextPath() %>/srv/authmenu/getMenuTreeForEdit" style="width:200px;padding:5px;" 
       			showTreeIcon="true" textField="menuName" idField="menuId" parentField="parentMenuId" resultAsTree="false" expandOnDblClick="true"         
        		contextMenu="#treeMenu" expandOnLoad="false" >
    		</ul>

			<ul id="treeMenu" class="nui-contextmenu"  onbeforeopen="onBeforeOpen">        
    			<li name="add" iconCls="icon-add" onclick="onAddNode">新增菜单</li>
				<li name="edit" iconCls="icon-edit" onclick="onEditNode">编辑菜单</li>
				<li name="remove" iconCls="icon-remove" onclick="onRemoveNode">删除菜单</li>        
			</ul>
        </div>
    </div>
    <div showCollapseButton="true">
    	<div class="nui-toolbar" style="padding:2px;border-top:0;border-left:0;border-right:0;">                
            	菜单详情                
        </div>
        <div id="addButton" class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
						<a class="nui-button" iconCls="icon-add" onclick="addFirstLevelMenu()">增加主菜单</a>
                    </td>
                </tr>
            </table>           
        </div>
        <div class="nui-fit" id="menuRootDetail">
        	<div id="menuRootgrid" class="nui-datagrid" style="width:99%;height:350px;" allowResize="true"
         		idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/authmenu/getFirstLevelMenu" >
        		<div property="columns">
            	<div field="menuCode" width="150" headerAlign="center" align ="center" allowSort="true">菜单编码</div>    
            	<div field="menuName" width="100" headerAlign="center" align ="center" allowSort="true">菜单名称</div>    
				<div field="createuser" width="50" headerAlign="center" align ="center" allowSort="true">创建者</div>    
				<div field="createtime" width="150" headerAlign="center" align ="center"  allowSort="true" renderer="onDateRenderer">创建时间</div>    
        		</div>
    		</div>
        </div>
        
        <div class="nui-fit" id="menuDetail" style="display:none">
        <form id="menuDetailForm" action="">
            <table style="table-layout:fixed;width:100%,height:100%" border="0">
            	<!--
            	<tr>
                    <td style="width:10%;" align="right">菜单id：</td>
                    <td style="width:60%;">    
                         <input name="menuId" class="nui-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">父菜单id：</td>
                    <td style="width:60%;">    
                         <input name="parentMenuId" class="nui-textbox"/>
                    </td>
                </tr>
                -->
                
                <tr>
                    <td style="width:10%;" align="right">菜单编码：</td>
                    <td style="width:60%;">    
                         <input name="menuCode" style="width:50%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">菜单名称：</td>
                    <td style="width:60%;">    
                         <input name="menuName" style="width:50%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">图标样式：</td>
                    <td style="width:60%;">    
                         <input name="imagepath" style="width:50%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接类型：</td>
                    <td style="width:60%;">    
                         <input name="linkType" style="width:50%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接资源：</td>
                    <td style="width:60%;">    
                         <input name="linkRes" style="width:100%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">链接路径：</td>
                    <td style="width:60%;">    
                         <input name="linkAction" style="width:100%;" class="nui-textbox asLabel"/>
                    </td>
                </tr>
            </table>
           </form>
        </div>
    </div>        
</div>

    <script type="text/javascript">
    	/////////////////////////////////////////////////
    	nui.parse();
    	var tree = nui.get("tree1");
    	var grid = nui.get("menuRootgrid");
    	grid.load();
        var menuDetailform = new nui.Form("menuDetailForm");
        var addButton = document.getElementById("addButton");
        var menuRootDetail =  document.getElementById("menuRootDetail");
        var menuDetail =  document.getElementById("menuDetail");
        
		tree.on("nodeselect", function (e) {
          	menuRootDetail.style.display='none';
          	addButton.style.display='none';
          	menuDetail.style.display='block';
            menuDetailform.setData(e.node);
        });    
    
		
		/////////////////////////////////////////////////
		function showAllMenuRoot(){
			menuRootDetail.style.display='block';
			addButton.style.display='block';
          	menuDetail.style.display='none';
          	grid.reload();
		}
    
    	/////////////////////////////////////////////////
        function onAddNode(e) {
            var tree = nui.get("tree1");
            var parentNode = tree.getSelectedNode();
            if(parentNode.menuLevel>=4){
            	alert("无法再增加下级菜单目录！");
            	return;
            }
            nui.open({
                url: bootPATH + "../../coframe/menu/addMenu.jsp",
                title: "新增菜单", width: 650, height: 300,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { parentNode: parentNode};
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if(action=="ok"){
                    	tree.load();
                	}
                }
            });
        }
        function addFirstLevelMenu(){
        	nui.open({
                url: bootPATH + "../../coframe/menu/addFirstLevelMenu.jsp",
                title: "新增菜单", width: 400, height: 150,
                onload: function () {
                	//nothing
                },
                ondestroy: function (action) {
                	if(action=="ok"){
                    	tree.load();
                    	var grid = nui.get("menuRootgrid");
                    	grid.reload();
                	}
                }
            });
        }
        function onEditNode(e) {
            var tree = nui.get("tree1");
            var node = tree.getSelectedNode();
            nui.open({
                url: bootPATH + "../../coframe/menu/editMenu.jsp",
                title: "修改菜单信息", width: 650, height: 300,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { node: node };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if(action=="ok"){
                		tree.load();
                	}
                }
            });
        }
        function onRemoveNode(e) {
        	var tree = nui.get("tree1");
            var node = tree.getSelectedNode();
            if (node) {
                if (confirm("确定删除选中菜单节点?")) {
                	var menuId = node.menuId;
                	nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除，请稍后...'});
                	$.ajax({
                        url: "<%=request.getContextPath() %>/srv/authmenu/deleteCapMenu/"+menuId,
                        success: function (text) {
                        	nui.unmask();
                        	var o = nui.decode(text);
                        	if(o.result==true){
                        		nui.alert("删除成功！");
                        		tree.removeNode(node);
                        	} else {
                        		nui.alert("删除失败，请稍后重试！");
                        	}
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        	nui.unmask();
                            alert(jqXHR.responseText);
                        }
                    });
                }
            }
        }
        
        function onMoveNode(e) {
            var tree = nui.get("tree1");
            var node = tree.getSelectedNode();
            nui.alert("moveNode" + node);
        }
        
		function onBeforeOpen(e) {
    		var menu = e.sender;
    		var tree = nui.get("tree1");
    		var node = tree.getSelectedNode();
    		if (!node) {
        		e.cancel = true;
       	 		return;
    		}
    		if (node.menuLevel >= 2) {
    		}
		}
    </script>

</body>
</html>