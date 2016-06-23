<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:65%">         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;">
        <legend>功能查询</legend>
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:10%;" align="right">功能标识:</td>
                    <td style="width:20%;"><input id="funcId" name="funcId" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">功能名称:</td>
                    <td style="width:20%;"><input id="funcName" name="funcName" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">功能类型:</td>
                    <td style="width:20%;"><input id="funcType" name="funcType" class="nui-combobox" data="CLD_FuncType" value="defaultValue"/></td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">调用入口:</td>
                    <td style="width:20%;"><input id="funcAction" name="funcAction" class="nui-textbox"  onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">验证权限:</td>
                    <td style="width:20%;"><input id="isCheck" name="isCheck" class="nui-combobox" data="CLD_FuncIsCheck" value="defaultValue"/></td>
                    <td style="width:10%;" align="right">可定义菜单:</td>
                    <td style="width:20%;"><input id="isMenu" name="isMenu" class="nui-combobox" data="CLD_FuncIsMenu" value="defaultValue"/></td>
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
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="funcgrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/authfunction/funclist" >
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="funcId" width="150" headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">功能标识</div>    
            <div field="funcName" width="100" headerAlign="center" align ="center" allowSort="true">功能名称</div>    
			<div field="funcType" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onFuncTypeRenderer">功能类型</div>    
			<div field="funcAction" width="150" headerAlign="center" align ="center" allowSort="true">调用入口</div>    
			<div field="isCheck" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onFuncIsCheckRenderer">验证权限</div>    
			<div field="isMenu" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onFuncIsMenuRenderer">可定义菜单</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        var grid = nui.get("funcgrid");
        grid.load();
        grid.sortBy("funcId", "asc");
        
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
		
		function add(){
            nui.open({
                url: bootPATH + "../../coframe/function/addFunction.jsp",
                title: "新增功能", width: 650, height: 370,
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
            var funcId = record.funcId
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + funcId + '\')">'+funcId+'</a> ';
            return s;
        }
		
		function detailsRow(funcId){
			nui.open({
                url: bootPATH + "../../coframe/function/editFunction.jsp",
                title: "修改功能信息", width: 650, height: 350,
                onload: function () {
                    //var iframe = this.getIFrameEl();
                    //var data = { action: "getFunctionByFuncId", funcId: funcId };
                    //iframe.contentWindow.SetData(data);
                    var row = grid.getSelected();
                    var iframe = this.getIFrameEl();
                    var data = { row:row };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if (action=='ok') {
                    	grid.reload();
                	}
                }
            });
        }
		
		function edit(){
			var rows = grid.getSelecteds();
            if (rows.length==1) {
            	var funcId = rows[0].funcId
            	detailsRow(funcId);
            } else {
            	nui.alert("请选中一条记录");
            }
		}
		function remove(){
			var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中记录？")) {
                    var funcIds = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        funcIds.push(r.funcId);
                    }
                    var ids = funcIds.join(',');
                    grid.loading("操作中，请稍后......");
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/authfunction/deleteFunctions/" +ids,
                        success: function (text) {
                        	var o = nui.decode(text);
                        	if(o.result==true){//result
                        		nui.alert("删除成功");
                        		grid.reload();
                        	}else{
                        		nui.alert("删除失败");
                        		grid.unmask();
                        	}
                        },
                        error: function () {
                        }
                    });
                }
            } else {
            	nui.alert("请选中一条记录");
            }
		}
    </script>
</body>
</html>