<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.primeton.paas.manage.api.app.StretchStrategy" %>
<%@page import="com.primeton.paas.console.common.stretch.StrategyItemInfo" %>
<%@page import="com.primeton.paas.console.common.monitor.AppMonitorUtil" %>
<%@page import="com.primeton.paas.console.platform.service.monitor.TelescopicStrategyUtil" %>

<%
	int nomal = AppMonitorUtil.APP_NOMAL;
	int stoped = AppMonitorUtil.APP_STOPED;
	int exception = AppMonitorUtil.APP_EXCEPTION;
	StrategyItemInfo itemInfo = TelescopicStrategyUtil.getItemInfo();
	// 	Integer[] stretchSizeArr = itemInfo.getStretchScaleArr();//伸缩幅度
	Integer[] ignorTimeArr = itemInfo.getIgnoreTimeArr();
	Integer[] continueTimeArr = itemInfo.getDurationArr();//持续时间
	Integer[] cpuArr = itemInfo.getCpuUsageArr();//cpu
	Integer[] memArr = itemInfo.getMemUsageArr();//mem
	String[] loadAverageArr = itemInfo.getLbArr();//one loadaverage

	Map<String, Object> temp = null;

	//持续时间
	List<Object> continueTimeSizeLists = new ArrayList<Object>();
	for (Integer time : continueTimeArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", time);
		temp.put("text", time + " 分钟");
		continueTimeSizeLists.add(temp);
	}
	Object continueTimeSizes = JSONArray
			.fromObject(continueTimeSizeLists);

	//休眠时间
	List<Object> ignorTimeSizeLists = new ArrayList<Object>();
	for (Integer time : ignorTimeArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", time);
		temp.put("text", time + " 分钟");
		ignorTimeSizeLists.add(temp);
	}
	Object ignorTimeSizes = JSONArray.fromObject(ignorTimeSizeLists);
	//CPU
	List<Object> cpuSizeLists = new ArrayList<Object>();
	for (Integer cpu : cpuArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", cpu);
		temp.put("text", cpu + "%");
		cpuSizeLists.add(temp);
	}
	Object cpuSizes = JSONArray.fromObject(cpuSizeLists);
	//MEM
	List<Object> memSizeLists = new ArrayList<Object>();
	for (Integer mem : memArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", mem);
		temp.put("text", mem + "%");
		memSizeLists.add(temp);
	}
	Object memSizes = JSONArray.fromObject(memSizeLists);
	//LoadAverage
	List<Object> lbSizeLists = new ArrayList<Object>();
	for (String lb : loadAverageArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", lb);
		temp.put("text", lb);
		lbSizeLists.add(temp);
	}
	Object lbSizes = JSONArray.fromObject(lbSizeLists);

	String clusterId = request.getParameter("clusterId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
    <style type="text/css">
    	html, body{
        	margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    	}
    </style>
    <script type="text/javascript">
    	var ContinuedTimeComboBoxData = <%=continueTimeSizes%>;
    	var IgnoreTimeComboBoxData = <%=ignorTimeSizes%>;
		var CpuThresholdComboBoxData = <%=cpuSizes%>;
		var MemThresholdComboBoxData = <%=memSizes%>;
		var LbThresholdComboBoxData = <%=lbSizes%>;
		var ReminderTypeComboBoxData = [{'id':'SMS','text':'短信'},{'id':'MAIL','text':'邮件'}];
	</script>
</head>
<body>
<div class="nui-layout" style="width:100%;height:100%;">
<div style="border:0;height:100%">
        	<div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
		    	选择应用:<input id="appCombox" class="nui-combobox" style="width:25%;" textField="displayName" valueField="name" 
		        onvaluechanged="onAppChanged" url="<%=request.getContextPath() %>/srv/appSS/getApps" emptyText="---请选择目标应用---" nullItemText="---请选择目标应用---"
		        showNullItem="true"
		         />&nbsp;&nbsp;应用状态: <span id="appState" style="font-weight:bold"></span>
		         <a id="enableServiceMonitor" class="nui-button" onclick="enableServiceMonitor();" style="float:right;margin-right:5px;display:none">启用服务监控</a>
		         <a id="disableServiceMonitor" class="nui-button" onclick="disableServiceMonitor();" style="float:right;margin-right:5px;display:none">禁用服务监控</a>
		         <a id="refresh" class="nui-button" onclick="refresh();" iconCls="icon-reload" style="float:right;margin-right:10px"></a>
		    </div>
		    
		    <fieldset id="HaProxy_field" style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;display:none">
		        <legend>HaProxy服务</legend>
		        <div style="width:100%;height:170px;margin-top:10px;" id="HaProxy_Monitor_INF_Frame" >
			   		<iframe name="HaProxy" id="HaProxy" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
				</div>
		    </fieldset>
		    <fieldset id="Memcached_field" style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;display:none">
		        <legend>Memcached服务</legend>
		        <div style="width:100%;height:170px;margin-top:10px;" id="Memcached_Monitor_INF_Frame" >
			   		<iframe name="Memcached" id="Memcached" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
				</div>
		    </fieldset>
		    <fieldset id="MySQL_field" style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;display:none">
		        <legend>MySQL服务</legend>
		        <div style="width:100%;height:170px;margin-top:10px;" id="MySQL_Monitor_INF_Frame" >
			   		<iframe name="MySQL" id="MySQL" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
				</div>
		    </fieldset>
		    <fieldset id="Jetty_field" style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;display:none">
		        <legend>Jetty服务</legend>
		        <div style="width:100%;height:170px;margin-top:10px;" id="Jetty_Monitor_INF_Frame" >
			   		<iframe name="Jetty" id="Jetty" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
				</div>
		    </fieldset>
		</div>
	</div>

	<script type="text/javascript">        
        nui.parse();
        
        var appCombox = nui.get("appCombox");
        
        var appState = document.getElementById("appState");
        var state_nomal = "<%=nomal %>";
        
    	var state_stop = "<%=stoped %>";
    	var state_exception = "<%=exception %>";
    	
    	var haproxyField = document.getElementById("HaProxy_field");
    	var jettyField = document.getElementById("Jetty_field");
    	var memcachedField = document.getElementById("Memcached_field");
    	var mysqlField = document.getElementById("MySQL_field");
    	
 		init();
        
        function init() {
        	if (appCombox.data.length > 1) {
        		appCombox.select(1);
        	} else {
        		nui.alert("目前没有应用！");
        	}
        }
		
        function onAppChanged(e) {
	    	var appName = appCombox.getValue();
	    	noneDisplayAll();
	    	if (!appName) {
	    		infoFrame.src = "";
	    		appState.innerHTML = "";
	    		nui.alert("请选择应用！");
	    		return;
	    	} else {
	    		displayEnableButton(appName);
	    		writeAppState(appName);
	    		getClusterInfo(appName);
	    	}
    	}
        //隐藏所有div
        function noneDisplayAll() {
        	haproxyField.style.display = "none";
        	jettyField.style.display = "none";
        	memcachedField.style.display = "none";
        	mysqlField.style.display = "none";
        	document.getElementById("enableServiceMonitor").style.display = "none";
        }
        
        function getClusterInfo(appName) {
        	//查询应用关联集群，确定是否显示对应div
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getClusterTypeByApp/" + appName,
	        	async: false,
	        	success: function (text) {
	        		var o = nui.decode(text);
	         		var data = o.data;
	         		selectDisplay(data,appName);
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
	        		console.log("error");
	        	}
	    	});
        }
        
        function selectDisplay(data, appName) {
        	haproxyEnable = data.HaProxy;
     		jettyEnable = data.Jetty;
     		memEnable = data.Memcached;
     		mysqlEnable = data.MySQL;
     		
     		if (haproxyEnable) {
     			haproxyClusterId = data.HaProxyClusterId;
     			haproxyField.style.display = "block";
				document.getElementById("HaProxy").src = "showServiceMonitor.jsp?appName="+appName+"&&clusterId="+haproxyClusterId;
     		}
     		if (jettyEnable) {
     			jettyClusterId = data.JettyClusterId;
     			jettyField.style.display = "block";
				document.getElementById("Jetty").src = "showServiceMonitor.jsp?appName="+appName+"&&clusterId="+jettyClusterId;
     		}
     		if (memEnable) {
     			memClusterId = data.MemcachedClusterId;
     			memcachedField.style.display = "block";
				document.getElementById("Memcached").src = "showServiceMonitor.jsp?appName="+appName+"&&clusterId="+memClusterId;
     		}
     		if (mysqlEnable) {
     			mysqlClusterId = data.MySQLClusterId;
     			mysqlField.style.display = "block";
				document.getElementById("MySQL").src = "showServiceMonitor.jsp?appName="+appName+"&&clusterId="+mysqlClusterId;
     		}
        }
        
        function writeAppState(appName) {
    		//应用状态   0normal 1stop 2exception
    		$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getAppState/" + appName,
                success: function (text) {
                	var o = nui.decode(text);
                	var state = o.state;
                	if (state == state_nomal) {
                		appState.style.color = "green";
                		appState.innerHTML = "正常"; //nomal
                    } else if (state == state_stop) {
                    	appState.style.color = "red";
                    	appState.innerHTML = "停止"; //STOP
                    } else if (state == state_exception) {
                    	appState.style.color = "red";
                    	appState.innerHTML = "异常"; //EXCEPTION
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	console.log("error");
                }
            });
    	}
        
        function displayEnableButton(appName) {
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/queryIfAppEnableServiceMonitor/" + appName,
                success: function (text) {
                	var o = nui.decode(text);
                	var result = o.result;//false 要显示   true 不显示
                	if (result == true) {
                		document.getElementById("enableServiceMonitor").style.display = "none";
                		document.getElementById("disableServiceMonitor").style.display = "block";
                    } else {
                		document.getElementById("enableServiceMonitor").style.display = "block";
                		document.getElementById("disableServiceMonitor").style.display = "none";
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	console.log("error");
                }
            });
        }
        
        function refresh() {
        	onAppChanged(null);
        }
        
        function disableServiceMonitor() {
        	//禁用服务监控
        	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在禁用，请稍后...'});
        	var appName = appCombox.getValue();
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/disableServiceMonitor/" + appName,
	        	async: true,
	        	success: function (text) {
	        		var o = nui.decode(text);
					nui.unmask();
					nui.alert("禁用成功！");
					onAppChanged(null);
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
					nui.unmask();
	        		console.log("error");
	        	}
	    	});
        }
        
        function enableServiceMonitor() {
        	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在启用，请稍后...'});
        	var appName = appCombox.getValue();
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/enableServiceMonitor/" + appName,
	        	async: true,
	        	success: function (text) {
	        		var o = nui.decode(text);
					nui.unmask();
					nui.alert("启用成功！", '系统提示');
					onAppChanged(null);
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
					nui.unmask();
	        		nui.alert(jqXHR.responseText, '系统提示');
	        	}
	    	});
        }
        
    </script>
</body>
</html>