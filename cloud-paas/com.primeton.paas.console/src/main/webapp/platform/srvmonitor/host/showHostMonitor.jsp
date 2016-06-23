<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

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
    <input id="hostCombox" class="nui-combobox" style="width:30%;" textField="name" valueField="ip" 
        onvaluechanged="onHostChanged" url="<%=request.getContextPath() %>/srv/monitor/getHosts" emptyText="---请选择目标主机---" nullItemText="---请选择目标主机---"
        showNullItem="true"
         />&nbsp;&nbsp;   
    <input id="monitorItemCombox" class="nui-combobox" style="width:20%;" textField="text" valueField="id" 
        onvaluechanged="viewGraph" data="MonitorItems" showNullItem="false" />
    	&nbsp;&nbsp;主机状态: <span id="hostState" style="font-weight:bold"></span>&nbsp;&nbsp;
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
        
        var hostCombox = nui.get("hostCombox");
        var monitorItemCombox = nui.get("monitorItemCombox");
        var timeLongCombox = nui.get("timeLongCombox");
        var refreshTimeCombox = nui.get("refreshTimeCombox");
        var hostState = document.getElementById("hostState");
        var infoFrame = document.getElementById("MonitorChartFrame");
        
        init();
        
        function init(){
        	//选择默认
        	if (hostCombox.data.length > 1) {
        		hostCombox.select(1);
        	} else {
        		nui.alert("目前没有主机！");
        	}
        	monitorItemCombox.select(0);
        	timeLongCombox.select(0);
        	refreshTimeCombox.select(0);
        	//设置应用状态显示
        }
        
    	function onHostChanged(e) {
	    	var hostIp = hostCombox.getValue();
	    	if (!hostIp) {
	    		infoFrame.src = "";
	    		hostState.innerHTML = "";
	    		nui.alert("请选择主机！");
	    		return;
	    	} else {
	    		viewGraph();
	    	}
    	}
    	
    	function writeHostState(hostIp) {
    		// hostState.innerHTML="停止";
    		// ajax获取应用状态   // 0normal 1stop 2exception
    		$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getHostState/" + hostIp,
                success: function (text) {
                	var o = nui.decode(text);
                	var state = o.state;
                	if (state) {
                		hostState.style.color = "green";
                		hostState.innerHTML = "在线"; //nomal
                    } else {
                    	hostState.style.color = "red";
                    	hostState.innerHTML = "离线"; //STOP
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	console.log("error");
                    //nui.alert(jqXHR.responseText);
                }
            });
    	}
    	
    	function viewGraph() {
    		var hostIp = hostCombox.getValue();
    		if(!hostIp){
    			hostState.innerHTML="";
    			return;
    		}
    		writeHostState(hostIp);
    		var item = monitorItemCombox.getValue();
    		var timeInterval = refreshTimeCombox.getValue()*1000;
    		var minutes = timeLongCombox.getValue();
    		infoFrame.src = "showHost" + item + "Info.jsp?ip=" + hostIp
    				+ "&&timeInterval=" + timeInterval + "&&minutes=" + minutes;
    	}
    	
    </script>
</body>
</html>