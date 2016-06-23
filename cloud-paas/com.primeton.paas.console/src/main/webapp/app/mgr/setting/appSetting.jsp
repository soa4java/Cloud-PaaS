<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body style="height:90%">
	<p>
    	选择目标应用:
    	<input id="selAppList" name="selAppList" class="nui-combobox" url="<%=request.getContextPath() %>/srv/appSetting/listApp" valueField="name" textField="name" valueFromSelect="true" onvaluechanged="selValueChanged()"/>
    </p>
	<div id="tabs1" class="nui-tabs" activeIndex="0" style="width:100%;height:100%;" plain="false" TabPosition ="left">
	    <div title="变量" >
	    
	        	<div style="width:100%;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="newAppVar()">新增</a>
			                        <a class="nui-button" iconCls="icon-edit" onclick="updateAppVar()">修改</a>       
			                        <a class="nui-button" iconCls="icon-remove" onclick="removeAppVar()">删除</a>     
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
	            <div id="datagridAppVar" class="nui-datagrid" style="width:100%;height:90%;" allowResize="true"
         					idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appSetting/listAppVar"  showPager="false" pageSize="20">
			        <div property="columns">
			            <div type="checkcolumn" width="20" headerAlign="center" align ="center">选择</div>     
			            <div type="indexcolumn" width="20" headerAlign="center" align ="center">编号</div>     
			            <div field="name" width="80px" headerAlign="center" align ="center" allowSort="true">变量名称</div>        
			            <div field="value" width="80px" headerAlign="center" align ="center" allowSort="true">变量值</div>        
			            <div field="desc" width="80px" headerAlign="center" align ="center" allowSort="true">变量说明</div>        
			        </div>
               </div>
	    </div>
	    
	    <div title="日志">
				<div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
					<table style="width:100%;">
						<tr>
							<td style="width:100%;">
						    	<a class="nui-button" iconCls="icon-save" onclick="saveLog()">保存</a>
						    	<a class="nui-button" iconCls="icon-reload" onclick="resetLog()">重置</a>
						    	<span class="separator"></span>
						    	<a class="nui-button" iconCls="icon-add" onclick="addLogType()">新增</a>
						    	<a class="nui-button" iconCls="icon-remove" onclick="deleteLogType()">删除</a>
							</td>
						</tr>
					</table>
				</div>
					<div class="nui-fit" id="logDiv">
						<div id="loggrid" class="nui-datagrid" style="width:100%;height:95%;" allowResize="true" allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
						    url="<%=request.getContextPath() %>/srv/appSetting/getAppLog" editNextRowCell="true" showPager="false">
							<div property="columns">
						    	<div type="checkcolumn" width="20" headerAlign="center" align ="center">选择</div> 
						        <div field="type" width="80px" headerAlign="center" align ="center" allowSort="false" renderer="onLogTypeRenderer">日志类型</div>                                  
							    <div type="comboboxcolumn" autoShowPopup="true" name="level" field="level" width="100" allowSort="false" align="center" headerAlign="center">日志级别 (点击修改)
                					<input property="editor" class="nui-combobox" style="width:100%;" data="CLD_LogLevels" />                
            					</div>
							</div>  
					</div>
				</div>			        	
	    </div>
	    
	    <div title="数据源">
	        	<div style="width:100%;">
			        <div class="nui-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="nui-button" iconCls="icon-add" onclick="newAppDS()">新增</a>
			                        <a class="nui-button" iconCls="icon-edit" onclick="updateAppDS()">修改</a>
			                        <a class="nui-button" iconCls="icon-remove" onclick="removeAppDS()">删除</a>       
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
    
	            <div id="datagridAppDataSource" class="nui-datagrid" style="width:100%;height:90%;" allowResize="true"
         					idField="id" multiSelect="true" url="<%=request.getContextPath() %>/srv/appSetting/listAppDataSource" showPager="false" pageSize="20">
			        <div property="columns">
			            <div type="checkcolumn" ></div>        
			            <div field="dataSourceName" width="80" headerAlign="center" align ="center" allowSort="true">数据源名称</div>    
			            <div field="dbServiceName" width="80"  headerAlign="center" align ="center">数据库服务标识</div>    
			            <div field="dataSourceDesc" width="150"  headerAlign="center" align ="center"  allowSort="true">数据源说明</div>  
			            
			            <div field="dataSourceId" width="80"  headerAlign="center" align ="center" visible="false">数据库服务标识</div>    
			            <div field="initialPoolSize" width="150"  headerAlign="center" align ="center"  allowSort="true" visible="false">初始化连接数</div>  
			            <div field="minPoolSize" width="150"  headerAlign="center" align ="center"  allowSort="true" visible="false">最小连接数</div>  
			            <div field="maxPoolSize" width="150"  headerAlign="center" align ="center"  allowSort="true" visible="false">最大连接数</div>  
			            <div field="acquireRetryAttempts" width="150"  headerAlign="center" align ="center"  allowSort="true" visible="false">连接重试次数</div>  
			        </div>
               </div>
	    </div>
	    
	</div>
    
    <script type="text/javascript">
	    nui.parse();

        function newAppVar() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName ) {
		        nui.open({
		            url: "<%=request.getContextPath() %>/app/mgr/setting/newAppVar.jsp",
		            title: "新增变量", width: 600, height: 280,
		            onload: function () {
	                    var iframe = this.getIFrameEl();
	                    var data = { action: "details" , appName : appName};
	                    iframe.contentWindow.SetData(data);
		            },
		            ondestroy: function (action) {
		            	selValueChanged();
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }
        }
        
        function updateAppVar() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName ) {
	        	var datagridAppVar = nui.get("datagridAppVar");
	            var rows = datagridAppVar.getSelecteds();
	            var count = rows.length;
	            if (count != 1) {
	            	nui.alert('请选中一条记录!');
	            	return;
	            }
	            var row = rows[0];
		        nui.open({
		            url: "<%=request.getContextPath() %>/app/mgr/setting/updateAppVar.jsp",
		            title: "修改变量", width: 600, height: 280,
		            onload: function () {
	                    var iframe = this.getIFrameEl();
	                    var data = { action: "details" , appName : appName, varName :row.name , varValue:row.value , varDesc:row.desc };
	                    iframe.contentWindow.SetData(data);
		            },
		            ondestroy: function (action) {
		            	selValueChanged();
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }
        }
        
        function removeAppVar() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName) {
	        	var datagridAppVar = nui.get("datagridAppVar");
	            var rows = datagridAppVar.getSelecteds();
	            var count = rows.length;
	            if (count < 1) {
	            	nui.alert('请选中一条记录!');
	            	return;
	            }
                var ids = [];
                for (var i = 0, l = rows.length; i < l; i++) {
                    var r = rows[i];
                    ids.push(r.name);
                }
                var id = ids.join(',');
	            
	            $.ajax({
	            	url: "<%=request.getContextPath() %>/srv/appSetting/deleteAppVar/"+appName +"&"+id,
	                cache: false,
	                success: function (text) {
	                	var o = nui.decode(text);
	                	if (o.result) {
	                		nui.alert('删除成功!');
	                		selValueChanged();
	                	} else {
	                		nui.alert('删除失败!');
	                	}
	                	nui.unmask(document.body);
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }    	
        }
        
        function newAppDS() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName) {
		        nui.open({
		            url: "<%=request.getContextPath() %>/app/mgr/setting/newAppDS.jsp",
		            title: "新增数据源", width: 600, height: 360,
		            onload: function () {
	                    var iframe = this.getIFrameEl();
	                    var data = { action: "details" , appName : appName};
	                    iframe.contentWindow.SetData(data);
		            },
		            ondestroy: function (action) {
		            	selValueChanged();
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }
        }
     
        function updateAppDS() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName ) {
	        	var datagridAppDataSource = nui.get("datagridAppDataSource");
	            var rows = datagridAppDataSource.getSelecteds();
	            var count = rows.length;
	            if (count != 1) {
	            	nui.alert('请选中一条记录!');
	            	return;
	            }
	            var row = rows[0];
		        nui.open({
		            url: "<%=request.getContextPath() %>/app/mgr/setting/updateAppDS.jsp",
		            title: "修改数据源", width: 600, height: 360,
		            onload: function () {
	                    var iframe = this.getIFrameEl();
	                    var data = { action: "details" 
	                    			, appName : appName
	                    			, dataSourceName:row.dataSourceName 
	                    			, dbServiceName:row.dbServiceName 
	                    			, dataSourceId:row.dataSourceId 
	                    			, initialPoolSize:row.initialPoolSize
	                    			, minPoolSize:row.minPoolSize
	                    			, maxPoolSize:row.maxPoolSize
	                    			, acquireRetryAttempts:row.acquireRetryAttempts
	                    			, dataSourceDesc:row.dataSourceDesc };
	                    iframe.contentWindow.SetData(data);
		            },
		            ondestroy: function (action) {
		            	selValueChanged();
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }
        }
        
        function removeAppDS() {
	        var appName =  nui.get("selAppList").getValue();
	        if (null != appName && '' != appName ) {
	        	var datagridAppDataSource = nui.get("datagridAppDataSource");
	            var rows = datagridAppDataSource.getSelecteds();
	            var count = rows.length;
	            if (count != 1) {
	            	nui.alert('请选中一条记录!');
	            	return;
	            }
	            var row = rows[0];
	            $.ajax({
	            	url: "<%=request.getContextPath() %>/srv/appSetting/deleteAppDataSource/"+appName +"&"+row.dataSourceName,
	                cache: false,
	                success: function (text) {
	                	var o = nui.decode(text);
	                	if (o) {
	                		nui.alert('删除成功!');
	                		selValueChanged();
	                	} else {
	                		nui.alert('删除失败!');
	                	}
	                	nui.unmask(document.body);
		            }
		        });
	        } else {
	        	nui.alert('请选择目标应用!');
	        }    	
        }
        
        $(document).ready(function() {
            nui.getbyName("selAppList").select(0);
            selValueChanged();
            
		});
        
        function selValueChanged() {
        	var appName =  nui.get("selAppList").getValue();
        	
        	// 变量信息加载
    	    var datagridAppVar = nui.get("datagridAppVar");
    	    datagridAppVar.load({keyData:appName});
    	    
    	    // 日志信息加载
    	    var loggrid = nui.get("loggrid");
    	    loggrid.load({keyData:appName});
    	    
        	// 数据源信息加载
    	    var datagridAppDataSource = nui.get("datagridAppDataSource");
    	    datagridAppDataSource.load({keyData:appName});
        }
        
		function onLogTypeRenderer(e) {
			var grid = e.sender;
		    var record = e.record;
		    var rowIndex = e.rowIndex;
		    var logType = record.type;
		    if (logType=="user") {
				logType=logType + " (system default)";            	
		    }
		    return logType;
		}
		
		//保存日志配置
		function saveLog() {
			var appName =  nui.get("selAppList").getValue();
			var loggrid = nui.get("loggrid");
			if (loggrid.isChanged()) {
				var data = loggrid.getData();
				var json = nui.encode(data)
				loggrid.loading("保存用户日志配置...");
				$.ajax({
					url: "<%=request.getContextPath() %>/srv/appSetting/updateAppLog/"+appName,
		            data: { keyData: json },
		            type: "post",
		            success: function (text) {
		            	var o = nui.decode(text);
		            	if (o.result==true) { 
		            		nui.alert("保存成功");
		            		loggrid.reload();
		            	} else {
		            		nui.alert("保存失败");
		            		loggrid.unmask();
		            	}
		            },
		            error: function (jqXHR, textStatus, errorThrown) {
		            	loggrid.unmask();
		                nui.alert(jqXHR.responseText);
		            }
		        });
			} else {
				nui.alert("未做改动！");
			}
		}
		
		function resetLog() {
			var loggrid = nui.get("loggrid");
			loggrid.reload();
		}
		
		function addLogType() {
			var loggrid = nui.get("loggrid");
			if (loggrid.totalCount >= 10) {
				nui.alert("用户日志最多10种，已无法新增！");
				return;
			}
			var appName =  nui.get("selAppList").getValue();
		    if(null != appName && '' != appName ) {
				nui.open({
			    	url: "../app/mgr/setting/newAppLog.jsp",
			    	title: "新增日志类型", width: 450, height: 250,
			    	onload: function () {
		        		var iframe = this.getIFrameEl();
		        		var data = {appName:appName};
		        		iframe.contentWindow.SetData(data);
			    	},
			        ondestroy: function (action) {
			            if (action=="ok") {
			            	loggrid.load({keyData:appName});
			            	//loggrid.reload();
			            }
			    	}
			 	});
		     } else {
		     	alert('请选择目标应用!');
		     }
		}

		function deleteLogType() {
			var appName =  nui.get("selAppList").getValue();
			var loggrid = nui.get("loggrid");
			var rows = loggrid.getSelecteds();
			if (rows.length==1) {
				var logType = rows[0].type;
				if (logType == "user") {
					nui.alert("系统默认日志类型，不允许删除！");
					return;
				}
				nui.mask({el: document.body,cls: 'mini-mask-loading',html: '正在删除，请稍后...'});
				$.ajax({
		            url: "<%=request.getContextPath() %>/srv/appSetting/deleteUserLog/" + appName + "&" + logType,
		            success: function (text) {
		            	nui.unmask();
		            	var o = nui.decode(text);
		            	if (o.result == true) {
		            		nui.alert('删除成功!', '系统提示');
		            		loggrid.reload();
		            	} else {
		            		nui.alert('删除失败!', '系统提示');
		            	}
		            },
		            error: function (jqXHR, textStatus, errorThrown) {
		            	nui.unmask();
		            	nui.alert("系统繁忙，请稍候重" + jqXHR.responseText);
		            }
		        });
			} else {
				nui.alert("请选择一条记录！", '系统提示');
			}
		}
    </script>
</body>
</html>