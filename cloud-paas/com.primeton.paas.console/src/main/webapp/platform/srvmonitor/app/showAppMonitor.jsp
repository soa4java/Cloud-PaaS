<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="com.primeton.paas.console.common.monitor.AppMonitorUtil" %>

<%
	int nomal = AppMonitorUtil.APP_NOMAL;
	int stoped = AppMonitorUtil.APP_STOPED;
	int exception = AppMonitorUtil.APP_EXCEPTION;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <style type="text/css">
	    html,body
	    {
	        width:100%;
	        height:100%;
	        border:0;
	        margin:0;
	        padding:0;
	        overflow:visible;
	    }
    </style>
    <script type="text/javascript">
		var MonitorItems = [{'id':'CPU','text':'CPU监控数据'},{'id':'Memory','text':'Memory监控数据'},{'id':'OneLoadAverage','text':'LoadAverage监控数据'},{'id':'IO','text':'IO监控数据'},{'id':'ALL','text':'全部'}];
	</script>
</head>
<body>
	<div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
    <input id="appCombox" class="nui-combobox" style="width:25%;" textField="displayName" valueField="name" 
        onvaluechanged="onAppChanged" url="<%=request.getContextPath() %>/srv/monitor/getApps" emptyText="---请选择目标应用---" nullItemText="---请选择目标应用---"
        showNullItem="true"
         />&nbsp;&nbsp;   
    <input id="monitorItemCombox" class="nui-combobox" style="width:20%;" textField="text" valueField="id" 
        onvaluechanged="viewGraph" data="MonitorItems" showNullItem="false" />
    	&nbsp;&nbsp;应用状态: <span id="appState" style="font-weight:bold"></span>&nbsp;&nbsp;
    	时间长度:
    <input id="timeLongCombox" class="nui-combobox" style="width:10%;" textField="text" valueField="id" 
        onvaluechanged="viewGraph" showNullItem="false"  url="<%=request.getContextPath() %>/srv/monitor/getMonitorTimeLengths"/>&nbsp;&nbsp;
    	刷新频率
    <input id="refreshTimeCombox" class="nui-combobox" style="width:10%;" textField="text" valueField="id" 
        onvaluechanged="viewGraph" showNullItem="false"  url="<%=request.getContextPath() %>/srv/monitor/getMonitorRefreshTimes"/>
    </div>
    <div style="width:100%;height:450px;margin-top:10px;" id="Monitor_INF_Frame" >
	   <iframe name="MonitorChartFrame" id="MonitorChartFrame" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
	</div>
    
    <script type="text/javascript">        
        nui.parse();
        var appCombox = nui.get("appCombox");
        var monitorItemCombox = nui.get("monitorItemCombox");
        var timeLongCombox = nui.get("timeLongCombox");
        var refreshTimeCombox = nui.get("refreshTimeCombox");
        var appState = document.getElementById("appState");
        var infoFrame = document.getElementById("MonitorChartFrame");
        
        var state_nomal = "<%=nomal %>";
    	var state_stop = "<%=stoped %>";
    	var state_exception = "<%=exception %>";
        
        init();
        
        function init() {
        	// 选择默认
        	if (appCombox.data.length > 1) {
        		appCombox.select(1);
        	} else {
        		nui.alert("目前没有应用！");
        	}
        	monitorItemCombox.select(0);
        	timeLongCombox.select(0);
        	refreshTimeCombox.select(0);
        	//设置应用状态显示
        }
        
    	function onAppChanged(e) {
	    	var appName = appCombox.getValue();
	    	if (!appName) {
	    		infoFrame.src = "";
	    		appState.innerHTML = "";
	    		nui.alert("请选择应用！");
	    		return;
	    	} else {
	    		viewGraph();
	    	}
    	}
    	
    	function writeAppState(appName) {
    		//appState.innerHTML="停止";
    		//ajax获取应用状态   // 0normal 1stop 2exception
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
                    //nui.alert(jqXHR.responseText);
                }
            });
    	}
    	
    	function viewGraph() {
    		var appName = appCombox.getValue();
    		if (!appName) {
    			appState.innerHTML = "";
 				//nui.alert("请选择应用！");
    			return;
    		}
    		writeAppState(appName);
    		var item = monitorItemCombox.getValue();
    		var timeInterval = refreshTimeCombox.getValue()*1000;
    		var minutes = timeLongCombox.getValue();
    		infoFrame.src = "showApp" + item + "Info.jsp?appName=" 
    				+ appName + "&&timeInterval=" + timeInterval 
    				+ "&&minutes=" + minutes;
    	}
    	
    </script>
</body>
</html>