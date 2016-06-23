<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

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
                    <td style="width:80px;" align="right">存储池标识：</td>
                    <td style="width:150px;"><input id="id" name="id" class="nui-textbox"  style="width:150px;"/></td>
                    <td style="width:80px;" align="right">存储大小：</td>
                    <td style="width:150px;">
                   		<input id="size" name="size" class="nui-combobox" url="<%=request.getContextPath() %>/srv/storagePoolMgr/getAppStorageSizes" valueField="value" textField="value" emptyText="全部" nullItemText="全部"  showNullItem="true"  style="width:150px;"/>
                    </td>
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
   <div style="width:100%;">
        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a class="nui-button" iconCls="icon-add" onclick="addStoragePool()">新增</a>
                        <a class="nui-button" iconCls="icon-edit" onclick="updateStoragePool()">修改</a>
                        <a class="nui-button" iconCls="icon-remove" onclick="removeStoragePools()">删除</a>    
                    </td>
                </tr>
            </table>           
        </div>
    </div>

    <div id="datagrid1" class="nui-datagrid" style="width:100%;height:100%;" allowResize="true"
         idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/storagePoolMgr/list" >
        <div property="columns">
			<!-- <div type="indexcolumn"></div>        -->
            <div type="checkcolumn" ></div>        
            <div field="id" width="100"  headerAlign="center" align ="center" allowSort="true" renderer="onActionRenderer">存储池标识</div>    
            <div field="storageSize" width="50"  headerAlign="center" align ="center" allowSort="true">存储大小(G)</div>    
			<div field="attributes.size" width="80" headerAlign="center" align ="center" allowSort="true">当前存储数量</div>   
			<div field="minSize" width="80" headerAlign="center" align ="center" allowSort="true">存储最小数量</div> 
			<div field="maxSize" width="80" headerAlign="center" align ="center" allowSort="true">存储最大数量</div>    
			<div field="isEnable" width="50" headerAlign="center" align ="center" allowSort="true" renderer="onHostStandaloneRenderer">是否启用</div>                      
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
            var id = record.id;
            return '<a class="Edit_Button" href="javascript:storagePoolInfo(\'' + rowIndex + '\')">'+id+'</a> ';
        }

        function selForm() {
            //提交表单数据
            var form = new nui.Form("#selForm");            
            var data = form.getData();      //获取表单多个控件的数据
            var json = nui.encode(data);   
        	grid.load({keyData:json});
        }
        
        function storagePoolInfo(rowIndex) {
            var row = grid.getSelected(rowIndex);
            
            var id = row.id;
            var storageSize = row.storageSize +"G";
            var size = row.attributes.size;
            var minSize = row.minSize;
            var maxSize = row.maxSize;
            var isEnable = row.isEnable == 1 ? '是':'否';
            
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/storage/storagePoolInfo.jsp?id="+row.id +"&size=" + row.storageSize,
                title: "存储池信息", width: 600, height: 410,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { action: "details"
                    			, id:id 
                    			, storageSize : storageSize 
                    			, size : size
                    			, minSize : minSize
                    			, maxSize : maxSize
                    			, isEnable : isEnable
                    			};
                    iframe.contentWindow.SetData(data);
                    
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
      
        function addStoragePool() {
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: 'Loading...'
            });
            $.ajax({
            	type: "get",
            	url: "<%=request.getContextPath() %>/srv/storagePoolMgr/getFilterAppStorageSizesNum",
                success: function (text) {
             	   nui.unmask(document.body);
             	   if (text != null && text > 0) {
                       nui.open({
                           url: "<%=request.getContextPath() %>/platform/resourcemgr/storage/addStoragePool.jsp",
                           title: "存储池配置", width: 400, height: 400,
                           onload: function () {
                               var iframe = this.getIFrameEl();
                               
                               var data = { action: "details"};
                               iframe.contentWindow.SetData(data);
                               
                           },
                           ondestroy: function (action) {
                               grid.reload();
                           }
                       });
             	   } else {
             		  nui.alert('存储池配置已经添加完成！');
             	   }
                } 
            });
        }
        
        function updateStoragePool() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count != 1) {
            	nui.alert('请选中一条记录!');
            	return;
            }
            var row = rows[0];
            var id = row.id;
            var storageSize = row.storageSize;
            var increaseSize = row.increaseSize;
            var decreaseSize = row.decreaseSize;
            var maxSize = row.maxSize;
            var minSize = row.minSize;
            var timeInterval = row.timeInterval;
            var retrySize = row.retrySize;
            var remarks = row.remarks;
            var isEnable = row.isEnable;
            
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/storage/addStoragePool.jsp?id="+row.id,
                title: "存储池配置", width: 400, height: 400,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    
                    var data = { action: "details"
                    			, id:id 
                    			, storageSize : storageSize 
                    			, increaseSize:increaseSize 
                    			, decreaseSize:decreaseSize
                    			, maxSize : maxSize
                    			, minSize : minSize
                    			, timeInterval : timeInterval
                    			, retrySize : retrySize
                    			, remarks : remarks
                    			, isEnable : isEnable
                    			};
                    iframe.contentWindow.SetData(data);
                    
                },
                ondestroy: function (action) {
                    grid.reload();
                }
            });
        }
    
        function removeStoragePools() {
            var rows = grid.getSelecteds();
            var count = rows.length;
            if (count < 1) {
            	nui.alert('请选中记录!');
            	return;
            }
            var rlist = rows[0].id;
            for(i= 1 ; i< rows.length ;i++){
            	rlist += ',' + rows[i].id;
            }
            nui.mask({
                el: document.body,
                cls: 'mini-mask-loading',
                html: '正在删除，请稍后...'
            });
            $.ajax({
            	type: "PUT",
                url: "<%=request.getContextPath() %>/srv/storagePoolMgr/delStoragePools",
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
        
    </script>
</body>
</html>