<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:70%">         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>角色查询</legend>
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                    <td style="width:10%;" align="right">角色代码:</td>
                    <td style="width:40%;"><input id="roleCode" name="roleCode" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">角色名称:</td>
                    <td style="width:40%;"><input id="roleName" name="roleName" class="nui-textbox" onenter="onKeyEnter" /></td>
                </tr>
                <tr>
                	<td colspan="6" align="center">
                		<a class="nui-button" style="width:60px; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    <br/>
   <div style="width:99%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
						<a class="nui-button" iconCls="icon-add" onclick="add()">增加</a>
                        <a class="nui-button" iconCls="icon-edit" onclick="edit()">修改</a>
                        <a class="nui-button" iconCls="icon-remove" onclick="remove()">删除</a>  
                        <a class="nui-button" iconCls="icon-edit" onclick="auth()">授权</a>     
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="rolegrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/authrole/rolelist" >
        <div property="columns">
            <div type="checkcolumn" ></div>
            <div name="roleIdColum" field="roleId" type="hidden" width="0" style="display:none"></div>    
            <div field="roleCode" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">角色代码</div>    
            <div field="roleName" width="100" headerAlign="center" align ="center" allowSort="true">角色名称</div>    
			<div field="createuser" width="80" headerAlign="center" align ="center" allowSort="true">创建者</div>    
			<div field="createtime" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onDateRenderer">创建时间</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        var grid = nui.get("rolegrid");
        grid.load();
        grid.sortBy("roleCode", "desc");
        grid.hideColumn("roleIdColum");

        function selForm() {
            //提交表单数据
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        	grid.hideColumn("roleIdColum");
        }
        
		function onKeyEnter(e) {
    		selForm();
    	}
		
		function add() {
            nui.open({
                url: bootPATH + "../../coframe/accredit/addRole.jsp",
                title: "新增角色", width: 600, height: 230,
                onload: function () {
                    //nothing
                	
                },
                ondestroy: function (action) {
                	if(action=='ok'){
                    	grid.reload();
                	}
                }
            });
        }
		
		function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var roleCode = record.roleCode
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + roleCode + '\')">'+roleCode+'</a> ';
            return s;
        }
		
		function detailsRow(roleCode){
			nui.open({
                url: bootPATH + "../../coframe/accredit/editRole.jsp",
                title: "修改角色", width: 600, height: 230,
                onload: function () {
                	var row = grid.getSelected();
                    var iframe = this.getIFrameEl();
                    var data = { row:row };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if(action=='ok'){
                    	grid.reload();
                	}
                }
            });
        }
		
		function edit(){
			var rows = grid.getSelecteds();
            if (rows.length==1) {
            	var roleCode = rows[0].roleCode
            	detailsRow(roleCode);
            } else {
                alert("请选中一条记录");
            }
		}
		
		function remove(){
			var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中记录？")) {
                    var roleIds = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        roleIds.push(r.roleId);
                    }
                    var ids = roleIds.join(',');
                    grid.loading("操作中，请稍后......");
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/authrole/deleteRoles/" +ids,
                        success: function (text) {
                        	grid.unmask();
                        	var o = nui.decode(text);
                        	if (o.result==true) {//result
                        		nui.alert("删除成功");
                        	} else {
                        		nui.alert("删除失败");
                        	}
                            grid.reload();
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        	grid.unmask();
                            alert(jqXHR.responseText);
                        }
                    });
                }
            } else {
            	nui.alert("请选中一条记录");
            }
		}
		
		function auth() {
			var rows = grid.getSelecteds();
            if (rows.length==1) {
            	var roleId = rows[0].roleId;
            	var roleCode = rows[0].roleCode;
				nui.open({
                	url: bootPATH + "../../coframe/accredit/authRoleAndFunction.jsp",
                	title: "修改角色授权信息", width: 900, height: 600,
                	onload: function () {
	                    var iframe = this.getIFrameEl();
    	                var data = { roleId: roleId ,roleCode:roleCode};
        	            iframe.contentWindow.SetData(data);
            	    },
                	ondestroy: function (action) {
                		if (action=='ok') {
                    		grid.reload();
                		}
                	}
            	});
            } else {
            	nui.alert("请选中一条记录");
            }
		}
    </script>
</body>
</html>