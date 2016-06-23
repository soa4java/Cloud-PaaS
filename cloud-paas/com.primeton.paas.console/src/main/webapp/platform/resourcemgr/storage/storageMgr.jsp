<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.primeton.paas.console.common.SystemVariable"%>

<%
	Integer[] storageSizes = SystemVariable.getNasStorageSizes();
	request.setAttribute("appStorages", storageSizes);
	request.setAttribute("dbStorages", storageSizes);
	List<Object> storageSizesList = new ArrayList<Object>();
	for (Integer st : storageSizes) {
		Map<String, Integer> storageSizesMap = new HashMap<String, Integer>();
		storageSizesMap.put("id", st);
		storageSizesMap.put("text", st);
		storageSizesList.add(storageSizesMap);
	}
	Object storageSizesData = JSONArray.fromObject(storageSizesList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:65%">         
    <fieldset style="border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>查询条件</legend>
        <div id="selForm" style="padding:5px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:80px;" align="right">存储标识：</td>
                    <td style="width:150px;"><input id="id" name="id" class="nui-textbox"  style="width:150px;"/></td>
                    <td style="width:80px;" align="right">存储名称：</td>
                    <td style="width:150px;">
                   		<input id="name" name="name" class="nui-textbox"  style="width:150px;"/>
                    </td>
                    <td style="width:80px;" align="right">存储路径：</td>
                    <td style="width:150px;"><input id="path" name="path" class="nui-textbox"  style="width:150px;"/></td>
                    <td style="width:80px;" align="right">存储大小：</td>
                    <td style="width:150px;">
                   		<input id="size" name="size" class="nui-combobox" data='<%=storageSizesData %>' emptyText="全部" nullItemText="全部"   showNullItem="true"  style="width:150px;"/>
                    </td>

                </tr>
                <tr>
                	<td colspan="8" align="center">
                		<a class="nui-button" style="width:60px; " onclick="selForm">查询</a>
                	</td>
                </tr>
            </table>
        </div>
    </fieldset>
    <br/>
	<div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<a class="nui-button" iconCls="icon-upgrade-storage" onclick="upgradeStorege()">存储升级</a>
                        <a class="nui-button" iconCls="icon-no" onclick="releaseStorage()">释放</a>    
                        <a class="nui-button" iconCls="icon-remove" onclick="deleteStorage()">销毁</a>       
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/storageMgr/list" onloaderror="onLoadErrorRenderer">
        <div property="columns">
            <div type="checkcolumn" ></div>        
            <div field="id" width="80"    headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">存储标识</div>    
            <div field="name" width="150"  headerAlign="center" align ="center" allowSort="true">存储名称</div>    
			<div field="path" width="200" headerAlign="center" align ="center" allowSort="true">存储路径</div>    
			<div field="size" width="80" headerAlign="center" align ="center" allowSort="true">存储大小(G)</div>    
			<div field="mountStatus" width="80" headerAlign="center" align ="center" allowSort="true" renderer="onStorageStatusRenderer">是否挂载</div>                        
        </div>
    </div>

    <script type="text/javascript">        
        nui.parse();
         
	    var grid = nui.get("datagrid1");
        grid.load();
        
        function onActionRenderer(e) {
            var grid = e.sender;
            var record = e.record;
            var rowIndex = e.rowIndex;
            var id = record.id
            return '<a href="javascript:showMounts(\'' + rowIndex + '\')">' + id + '</a> ';
        }

        function selForm() {
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
        function remove() {
            var rows = grid.getSelecteds();
            if (rows.length > 0) {
                if (confirm("确定删除选中记录？")) {
                    var ids = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        ids.push(r.orderId);
                    }
                    var id = ids.join(',');
                    grid.loading("操作中，请稍后......");
                    $.ajax({
                        url: "<%=request.getContextPath() %>/srv/orderMgr/delete/" + id,
                        success: function (text) {
                            grid.reload();
                        },
                        error: function () {
                        }
                    });
                }
            } else {
                nui.alert("请选中一条记录");
            }
        }

        function showMounts(rowIndex) {
        	var row = grid.getSelected(rowIndex);
            var id = row.id;
            var name = row.name;
            var path = row.path;
            var size = row.size + "G";
        	
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/storage/showMounts.jsp?id=" + id,
                title: "存储详情", width: 900, height: 420,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details"
                    			, id : id
                    			, name : name
                    			, path : path
                    			, size : size
                    			};
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
        
        
        function releaseStorage() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中1行记录!');
            	return;
            }
            var rlist = rows[0].id;
            for (i= 1 ; i< rows.length ;i++) {
            	rlist += ',' + rows[i].id;
            }
            if (!confirm("确定释放该存储 ?")) {
            	return;
            }
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在释放，请稍后...'
            });
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/storageMgr/release",
                contentType: "application/json; charset=utf-8",
                data: rlist,
                success: function (text) {
                	if (text) {
                		nui.alert('释放存储请求已发送!');
                		grid.load();
                	} else {
                		nui.alert('释放存储请求发送失败，请查看白名单是否已清除，稍后重试!');
                	}
                	nui.unmask(document.body);
                } 
            });
        }        
        
        function deleteStorage() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中1行记录!');
            	return;
            }
            var rlist = rows[0].id;
            for (i= 1 ; i< rows.length ;i++) {
            	rlist += ',' + rows[i].id;
            }
            if (!confirm("确定销毁该存储 ?")) {
            	return;
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
                		nui.alert('销毁存储请求已发送!');
                		grid.load();
                	} else {
                		nui.alert('销毁存储请求发送失败，请稍后重试!');
                	}
                	nui.unmask(document.body);
                } 
            });
        }
        
        function upgradeStorege() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var id = row.id;
            var name = row.name;
            var path = row.path;
            var size = row.size;
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/storage/upgradeStorage.jsp?id="+id,
                title: "存储升级", width: 500, height: 270,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details"
                    			, id : id
                    			, name : name
                    			, path : path
                    			, size : size
                    			};
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if (action == 'Suc') {
                    	grid.reload();
                	}
                    
                }
            });
        }
        
    </script>
</body>
</html>