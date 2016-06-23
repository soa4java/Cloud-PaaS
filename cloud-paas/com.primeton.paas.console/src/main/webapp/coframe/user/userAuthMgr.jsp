<%@page import="com.primeton.paas.console.common.NLSMessageUtils"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>用户管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:65%">         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>用户查询</legend>
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:10%" align="right">用户标识:</td>
                    <td style="width:20%;"><input id="userId" name="userId" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">用户名:</td>
                    <td style="width:20%;"><input id="userName" name="userName" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">用户状态:</td>
                    <td style="width:20%;"><input id="status" name="status" class="nui-combobox" data="CLD_UserStatus" value="defaultValue"/></td>
                </tr>
                <tr>
                    <td style="width:10%" align="right">手机:</td>
                    <td style="width:20%;"><input id="phone" name="phone" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">邮箱:</td>
                    <td style="width:20%;"><input id="email" name="email" class="nui-textbox" onenter="onKeyEnter" /></td>
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
<!--                         <a class="nui-button" iconCls="icon-add" onclick="addAppUser()">增加应用用户</a> -->
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="usergrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/authuser/userlist" >
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="userId" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">用户标识</div>    
            <div field="userName" width="100" headerAlign="center" align ="center" allowSort="true">用户名</div>    
			<div field="phone" width="80" headerAlign="center" align ="center" allowSort="true">手机号</div>    
			<div field="email" width="150" headerAlign="center" align ="center" allowSort="true">用户邮箱</div>    
			<div field="status" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onUserStatusRenderer">用户状态</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        var grid = nui.get("usergrid");
        grid.load();
        grid.sortBy("userId", "desc");

        function selForm() {
            //提交表单数据
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
		function onKeyEnter(e) {
    		selForm();
    	}
		
		function add() {
            nui.open({
                url: bootPATH + "../../coframe/user/addUser.jsp",
                title: "新增用户", width: 650, height: 400,
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
		
		function addAppUser() {
            nui.open({
                url: bootPATH + "../../coframe/user/addAppUser.jsp",
                title: "<%=NLSMessageUtils.getMessage("new.user") %>(默认自助服务门户菜单权限)", width: 650, height: 400,
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
            var userId = record.userId
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + userId + '\')">'+userId+'</a> ';
            return s;
        }
		
		function detailsRow(userId){	
			nui.open({
                url: bootPATH + "../../coframe/user/editUser.jsp",
                title: "修改用户", width: 650, height: 400,
                onload: function () {
                    //var iframe = this.getIFrameEl();
                    //var data = { action: "getUserByUserId", userId: userId };
                    //iframe.contentWindow.SetData(data);
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
            	var userId = rows[0].userId
            	detailsRow(userId);
            } else {
                alert("请选中一条记录");
            }
		}
		
		function remove(){
			var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中记录？")) {
                    var userIds = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        userIds.push(r.userId);
                    }
                    var ids = userIds.join(',');
                    //grid.loading("操作中，请稍后......");
                    nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除，请稍后...'});
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/authuser/deleteUsers/" +ids,
                        success: function (text) {
                        	nui.unmask();
                        	var o = nui.decode(text);
                        	if (o.result==true) {//result
                        		nui.alert("删除成功");
                        		grid.reload();
                        	} else {
                        		nui.alert("删除失败");
                        		//grid.unmak();
                        	}
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        	//grid.unmask();
                        	nui.unmask();
                            alert(jqXHR.responseText);
                        }
                    });
                    grid.unmask();
                }
            } else {
            	nui.alert("请选中一条记录");
            }
		}
		
		function auth(){
			var rows = grid.getSelecteds();
            if (rows.length==1) {
            	var userId = rows[0].userId;
				nui.open({
                	url: bootPATH + "../../coframe/user/authUserAndRole.jsp",
                	title: "修改用户授权信息", width: 650, height: 400,
                	onload: function () {
	                    var iframe = this.getIFrameEl();
    	                var data = { userId: userId };
        	            iframe.contentWindow.SetData(data);
            	    },
                	ondestroy: function (action) {
                		if(action=='ok'){
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