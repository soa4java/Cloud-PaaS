<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<div style="float:left;width:50%;height:100%" id="ipSegments">
		<div style="width:100%;">
	        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
	            <table style="width:100%;">
	                <tr>
	                    <td style="width:100%;">
	                    	<a class="nui-button" iconCls="icon-add" onclick="addSegment()">增加</a>
	                    	<a class="nui-button" iconCls="icon-edit" onclick="editSegment()">修改</a>
	                    	<a class="nui-button" iconCls="icon-remove" onclick="removeSegment()">删除</a>
	                    </td>
	                </tr>
	            </table>           
	        </div>
	    </div>
		<div id="segmentsgrid" class="nui-datagrid" style="width:100%;height:300px;" url="<%=request.getContextPath() %>/srv/vipMgr/vipSegmentsList"  idField="id" onselectionchanged="onSelectionChanged" 
        	selectOnLoad="true"	>
	        <div property="columns">       
	        	<div type="checkcolumn" ></div>     
	            <div name="segment" width="60%" headerAlign="center"  align ="center" renderer="onSegmentRenderer">虚拟IP段</div>                                        
	            <div field="netmask" width="30%" headerAlign="center" align ="center" >子网掩码</div>          
	        </div>
    	</div> 
	</div>
	<div style="float:right;width:50%;height:100%" id="usedIPs">
		<div style="width:100%;">
	        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
	            <table style="width:100%;">
	                <tr>
	                    <td style="width:100%;">
	                    	<a class="nui-button" iconCls="icon-remove" onclick="removeUsedIP()">删除</a>
	                    </td>
	                </tr>
	            </table>           
	        </div>
	    </div>
		<div id="usedipsgrid" class="nui-datagrid" multiSelect="true" style="width:100%;height:300px;" url="<%=request.getContextPath() %>/srv/vipMgr/getUsedVIPsBySegmentId">
	        <div property="columns">            
	            <div type="checkcolumn" ></div>        
	            <div field="id" width="100" headerAlign="center"  align ="center">段内已使用的IP</div>
	        </div>
    	</div>     
	</div>
    <script type="text/javascript">     
    	nui.parse();
    	
    	var segmentsgrid = nui.get("segmentsgrid");
        var usedipsgrid = nui.get("usedipsgrid");
        segmentsgrid.load();
    	
    	function onSegmentRenderer(e) {
    		var grid = e.sender;
            var record = e.record;
            return record.begin + "-" + record.end;
    	}
        
    	function onSelectionChanged(e) {
            var grid = e.sender;
            var record = grid.getSelected();
            var segmentId = record.id;
            if (record) {
            	usedipsgrid.load({segmentId:segmentId});
            }
        }
    	/**增加ip段*/
    	function addSegment(){
    		nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/vip/addVIPSegment.jsp",
                title: "新增虚拟ip段", width: 600, height: 260,
                onload: function () {
                    //nothing
                },
                ondestroy: function (action) {
                	if (action == 'ok') {
                		segmentsgrid.reload();
                	}
                }
            });
    	}
    	
    	/**修改ip段*/
    	function editSegment() {
            var row = segmentsgrid.getSelected();
            nui.open({
                url: "<%=request.getContextPath() %>/platform/resourcemgr/vip/editVIPSegment.jsp",
                title: "修改虚拟ip段", width: 600, height: 260,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { row:row };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                	if (action == 'ok') {
                		segmentsgrid.reload();
                	}
                }
            });
    	}
    	
    	/**删除ip段*/
    	function removeSegment() {
            var record = segmentsgrid.getSelected();
            var segmentId = record.id;
            nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除IP段，请稍后...'});
    		$.ajax({
	            url: "<%=request.getContextPath() %>/srv/vipMgr/delVIPSegment/" + segmentId,
	            success: function (text) {
	            	nui.unmask();
	            	var o = nui.decode(text);
	            	if (o.result == true) {
	            		nui.alert("删除虚拟IP段成功！");
	            		segmentsgrid.reload();
	            	} else {
	            		nui.alert("删除虚拟IP段失败,请稍后再试！");
	            	}
	            },
	            error: function (jqXHR, textStatus, errorThrown) {
	            	nui.unmask();
	                nui.alert(jqXHR.responseText);
	                segmentsgrid.reload();
	            }
	        });
    	}
    	
    	/**删除已使用ip*/
    	function removeUsedIP() {
    		var rows = usedipsgrid.getSelecteds();
    		if (rows.length > 0) {
                if (confirm("确定删除选中记录？")) {
                    var vipIds = [];
                    for (var i = 0, l = rows.length; i < l; i++) {
                        var r = rows[i];
                        vipIds.push(r.id);
                    }
                    var ips = vipIds.join(',');
                    nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除，请稍后...'});
                    $.ajax({
        	            url: "<%=request.getContextPath() %>/srv/vipMgr/deleteVIP/" + ips,
        	            success: function (text) {
        	            	nui.unmask();
        	            	var o = nui.decode(text);
        	            	if (o.result==true) {
        	            		nui.alert("删除虚拟IP成功！");
        	            		segmentsgrid.reload();
        	            	} else {
        	            		nui.alert("删除虚拟IP失败,请稍后再试！");
        	            	}
        	            },
        	            error: function (jqXHR, textStatus, errorThrown) {
        	            	nui.unmask();
        	                nui.alert(jqXHR.responseText);
        	                segmentsgrid.reload();
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
 