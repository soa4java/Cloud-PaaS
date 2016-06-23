<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:70%">         
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>变量查询</legend>
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                    <td style="width:10%;" align="right">变量名称:</td>
                    <td style="width:40%;"><input id="varKey" name="varKey" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">变量值:</td>
                    <td style="width:40%;"><input id="varValue" name="varValue" class="nui-textbox" onenter="onKeyEnter" /></td>
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
						<a class="nui-button" iconCls="icon-add" onclick="add()">新增</a>
                        <a class="nui-button" iconCls="icon-edit" onclick="edit()">更新</a>
                        &nbsp;&nbsp;&nbsp;
                        <a class="nui-button" iconCls="icon-remove" onclick="remove()">删除</a>  
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="vargrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/monitor/varlist" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="varKey" width="150" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">变量名称</div>    
            <div field="varValue" width="100" headerAlign="center" align ="center" allowSort="false">变量值</div>    
			<div field="description" width="80" headerAlign="center" align ="center" allowSort="false">变量说明</div>    
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
        
        var grid = nui.get("vargrid");
        grid.load();
        grid.sortBy("varKey", "desc");
        
		function selForm() {
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
                url: bootPATH + "../../platform/srvmonitor/variables/addPasVar.jsp",
                title: "新增变量", width: 600, height: 230,
                onload: function () {
                    //nothing
                },
                ondestroy: function (action) {
                	if (action == 'ok') {
                    	grid.reload();
                	}
                }
            });
        }
		
		function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var varKey = record.varKey
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + varKey + '\')">'+varKey+'</a> ';
            return s;
        }
		
		function detailsRow(varKey) {	
			nui.open({
                url: bootPATH + "../../platform/srvmonitor/variables/editPasVar.jsp",
                title: "修改变量", width: 600, height: 230,
                onload: function () {
                    var row = grid.getSelected();
                    var iframe = this.getIFrameEl();
                    var data = { row:row };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if (action == 'ok') {
                    	grid.reload();
                	}
                }
            });
        }
		
		function edit() {
			var rows = grid.getSelecteds();
            if (rows.length==1) {
            	var varKey = rows[0].varKey
            	detailsRow(varKey);
            } else {
                nui.alert("请选中一条记录");
            }
		}
		
		function remove() {
			var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中的变量？")) {
                    var varKeys = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        varKeys.push(r.varKey);
                    }
                    var varKeyss = varKeys.join(',');
                    //grid.loading("正在删除，请稍后......");
                    //grid.loading("正在删除，请稍后......");
                    nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除，请稍后...'});
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/monitor/deleteVars/" + varKeyss,
                        success: function (text) {
                        	nui.unmask();
                        	var o = nui.decode(text);
                        	if (o.result == true) { 
                        		nui.alert("删除成功");
                        		grid.reload();
                        	} else {
                        		nui.alert("删除失败");
                        	}
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        	nui.unmask();
                            nui.alert(jqXHR.responseText);
                        }
                    });
                    grid.unmask();
                }
            } else {
                nui.alert("请选中一条记录");
            }
		}
    </script>
</body>
</html>