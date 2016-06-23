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
        <legend>长任务查询</legend>
        <div id="selForm" style="padding:0px;">
            <table style="width:100%;" border="0">
                <tr>
                    <td style="width:10%" align="right">任务号:</td>
                    <td style="width:20%;"><input id="id" name="id" class="nui-textbox" onenter="onKeyEnter" /></td>
                    <td style="width:10%;" align="right">任务状态:</td>
                    <td style="width:20%;"><input id="status" name="status" class="nui-combobox" data="CLD_TaskStatus" value="defaultValue"/></td>
                    <td style="width:10%;" align="right">任务类型:</td>
                    <td style="width:20%;"><input id="type" name="type" class="nui-combobox" data="CLD_TaskType" value="defaultValue"/></td>
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
                        <a class="nui-button" iconCls="icon-remove" onclick="remove()">删除</a>  
                    </td>
                </tr>
            </table>           
        </div>
	</div>

    <div id="vargrid" class="nui-datagrid" style="width:99%;height:100%;" allowResize="true"
         idField="idfield" multiSelect="true" url="<%=request.getContextPath() %>/srv/monitor/tasklist" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="id" width="150" headerAlign="center" align ="center" allowSort="false" renderer="onActionRenderer">任务号</div>    
            <div field="status" width="100" headerAlign="center" align ="center" allowSort="false" renderer="onTaskStatusRenderer">状态</div>    
			<div field="type" width="80" headerAlign="center" align ="center" allowSort="false" renderer="onTaskTypeRenderer">类型</div>    
			<div field="startTime" width="80" headerAlign="center" align ="center" allowSort="false" renderer="onDateRenderer">创建时间</div>    
			<div field="finishTime" width="80" headerAlign="center" align ="center" allowSort="false" renderer="onDateRenderer">结束时间</div>    
			<div field="owner" width="80" headerAlign="center" align ="center" allowSort="false">创建者</div>    
			<div name="action" width="40" headerAlign="center" align ="center" renderer="onDoActionRenderer" >操作</div>    
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
		
		function onDoActionRenderer(e) {
			var grid = e.sender;
            var record = e.record;
            var uid = record._uid;
            var rowIndex = e.rowIndex;
            var status = record.status;
            if (status == '1') {
            	//未完成 可终止
            	var s = '<input type="button" value="终止" onclick="javascript:abortTask(\'' + uid + '\')"/>';
            } else {
            	//已完成 不可终止
            	var s = '<input type="button" disabled="disabled" value="终止" onclick="javascript:abortTask(\'' + uid + '\')"/>';
            }
            return s;
		}
		
		function abortTask(row_uid) {
	        var row = grid.getRowByUID(row_uid);
	        var taskId = row.id;
	        nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在操作，请稍后...'});
	        $.ajax({
                url: "<%=request.getContextPath() %>/srv/monitor/abortTask/" + taskId,
                success: function (text) {
                	nui.unmask();
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("长任务已终止！");
                	} else {
                		nui.alert("操作,请稍后再试！");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	nui.unmask();
                    nui.alert(jqXHR.responseText);
                    CloseWindow("failed");
                }
            });
		}
		
		function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var uid = record._uid;
            var rowIndex = e.rowIndex;
            var id = record.id;
            var s = '<a class="Edit_Button" href="javascript:detailsRow(\'' + id + '\')">' + id + '</a> ';
            return s;
        }
		
		function detailsRow(id) {
			nui.open({
                url: bootPATH + "../../platform/srvmonitor/task/detailTask.jsp",
                title: "长任务详情", width: 800, height: 500,
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
		
		function remove(){
			var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中的记录？")) {
                    var ids = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        ids.push(r.id);
                    }
                    var taskIds = ids.join(',');
                    nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在删除，请稍后...'});
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/monitor/deleteTasks/" + taskIds,
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