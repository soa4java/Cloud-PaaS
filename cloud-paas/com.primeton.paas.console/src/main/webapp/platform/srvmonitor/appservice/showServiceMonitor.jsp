<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
	//获取父页面 appName,clusterId
	String appName = request.getParameter("appName");
	String clusterId = request.getParameter("clusterId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/amcharts/amcharts.js" type="text/javascript"></script>
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
		function init() {
			var cpuUse = 0;
		    var cpuFree = 100;
		    var memUse = 0;
		    var memFree = 100;
		    var ioi = 0;
		    var ioo = 0;
		    var oneload = 0;
		    
		    var serviceState=1;
		    var currentTime=0;
		    var enableFlag=true;
		    
		    var clusterId='<%=clusterId%>';
		    $.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getServiceMonitorData/" + clusterId,
	        	async: false,
	        	success: function (text) {
	        		var o = nui.decode(text);
	         		var info = o.info;
	         		cpuUse = info.cpuUse;
	         		cpuFree = info.cpuFree;
	         		memUse = info.memUse;
	         		memFree = info.memFree;
	         		oneload = info.oneload;
	         		ioi = info.ioi;
	         		ioo = info.ioo;
	         		serviceState = o.serviceState;
	         		currentTime = info.currentTime;
	         		enableFlag = info.enableFlag;
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
	        		console.log("error");
	        	}
	    	});
		    var cpuChartData = [{monitor:"Free", value:cpuFree},{monitor:"Used", value:cpuUse}];
		    var memChartData = [{monitor:"Free", value:memFree},{monitor:"Used", value:memUse}];
		    
		    AmCharts.ready(function () {
		    	// Chart1 
		        chart = new AmCharts.AmPieChart();
		        chart.dataProvider = cpuChartData;
		        chart.titleField = "monitor";
		        chart.valueField = "value";
		        chart.outlineColor = "#FFFFFF";
		        chart.outlineAlpha = 0.8;
		        chart.outlineThickness = 2;
		        chart.depth3D = 5;
		        chart.angle = 20;
				chart.labelRadius = 0;
	            chart.labelText = "[[title]]:[[percents]]%";
				chart.colors = ["#FF0F00", "#FF6600"];
		
		    	// Chart2    
		        chart2 = new AmCharts.AmPieChart();
		        chart2.dataProvider = memChartData;
		        chart2.titleField = "monitor";
		        chart2.valueField = "value";
		        chart2.outlineColor = "#FFFFFF";
		        chart2.outlineAlpha = 0.8;
		        chart2.outlineThickness = 2;
		        chart2.depth3D = 5;
		        chart2.angle = 20;
		        chart2.labelRadius = 0;
		        chart2.labelText = "[[title]]:[[percents]]%";
				chart2.colors = ["#FF9E01", "#FCD202"];
		        
		     	// WRITE
		        chart.write("cpuChartDiv");
		        chart2.write("memChartDiv");
				
				writeServiceState(serviceState);
				writeServiceEnableState(enableFlag);
				
				var d = getTimeString(currentTime);
				document.getElementById("dataTime").innerHTML = d;
				
				document.getElementById("cpuUsed").innerHTML = cpuUse;
				document.getElementById("memUsed").innerHTML = memUse;
				document.getElementById("oneload").innerHTML = oneload;
				document.getElementById("ioi").innerHTML = ioi;
				document.getElementById("ioo").innerHTML = ioo;
				
		    });	
		    
		}
		window.onload = init();
		// window.onload = refresh();
    </script>
</head>
    
<body>
	<table style="width:100%; height:100%;" border="0">
		<tr>
			<td>服务状态:<span id="serviceState" style="font-weight:bold"></span></td> 
			<td>数据时间:<span id="dataTime" style="font-weight:bold"></span></td>
			<td style="width:25%; height:5%;" colspan="1" align="right">
				是否启用报警:<span id="enableFlag" style="font-weight:bold"></span>&nbsp;&nbsp;
				<a id="serviceSetting" class="nui-button" onclick="doModify();">服务报警设置</a>
				<a id="refresh" class="nui-button" iconCls="icon-reload" onclick="refresh();"></a>&nbsp;
			</td>
		</tr>
		<tr>
			<td align="center" style="width:35%; height:90%;"><div id="cpuChartDiv" style="width:100%; height:80%;"></div>CPU使用率:&nbsp;<span id="cpuUsed"></span>%</td>
			<td align="center" style="width:35%; height:90%;"><div id="memChartDiv" style="width:100%; height:80%;"></div>内存使用率:&nbsp;<span id="memUsed"></span>%</td>
			<td valign="bottom" style="width:30%;">
				<table>
					<tr><td align="right">一分钟内CPU负载：</td><td><span id="oneload" style="font-weight:bold"></span></td></tr>
					<tr><td align="right">io读写速率：</td><td>写入:<span id="ioi" style="font-weight:bold"></span>kb/s,写出:<span id="ioo" style="font-weight:bold"></span>kb/s</td></tr>
				</table>
			</td>
		</tr>
   </table>
   <script type="text/javascript">
   		nui.parse();
   		
   		//刷新，重填数据
   		function refresh() {
   			var cpuUse = 0;
		    var cpuFree = 100;
		    var memUse = 0;
		    var memFree = 100;
		    var ioi = 0;
		    var ioo = 0;
		    var oneload = 0;
		    var serviceState=1;
		    var currentTime;
		    var enableFlag=true;
		    
		    var clusterId='<%=clusterId%>';
		    $.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getServiceMonitorData/" + clusterId,
	        	async: false,
	        	success: function (text) {
	        		var o = nui.decode(text);
	         		var info = o.info;
	         		cpuUse = info.cpuUse;
	         		cpuFree = info.cpuFree;
	         		memUse = info.memUse;
	         		memFree = info.memFree;
	         		oneload = info.oneload;
	         		ioi = info.ioi;
	         		ioo = info.ioo;
	         		serviceState = o.serviceState;
	         		currentTime = info.currentTime;
	         		enableFlag = info.enableFlag;
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
	        		console.log("error");
	        	}
	    	});
		    var cpuChartData = [{monitor:"Free", value:cpuFree},{monitor:"Used", value:cpuUse}];
		    var memChartData = [{monitor:"Free", value:memFree},{monitor:"Used", value:memUse}];
		    
		  	// Chart1 
	        chart = new AmCharts.AmPieChart();
	        chart.dataProvider = cpuChartData;
	        chart.titleField = "monitor";
	        chart.valueField = "value";
	        chart.outlineColor = "#FFFFFF";
	        chart.outlineAlpha = 0.8;
	        chart.outlineThickness = 2;
	        chart.depth3D = 5;
	        chart.angle = 20;
			chart.labelRadius = 0;
            chart.labelText = "[[title]]:[[percents]]%";
			chart.colors = ["#FF0F00", "#FF6600"];
	
	    	// Chart2    
	        chart2 = new AmCharts.AmPieChart();
	        chart2.dataProvider = memChartData;
	        chart2.titleField = "monitor";
	        chart2.valueField = "value";
	        chart2.outlineColor = "#FFFFFF";
	        chart2.outlineAlpha = 0.8;
	        chart2.outlineThickness = 2;
	        chart2.depth3D = 5;
	        chart2.angle = 20;
	        chart2.labelRadius = 0;
	        chart2.labelText = "[[title]]:[[percents]]%";
			chart2.colors = ["#FF9E01", "#FCD202"];
			
	     	// WRITE
	        chart.write("cpuChartDiv");
	        chart2.write("memChartDiv");
			
	        writeServiceState(serviceState);
	        writeServiceEnableState(enableFlag);
			
			var d = getTimeString(currentTime);
			document.getElementById("dataTime").innerHTML = d;
			
			document.getElementById("cpuUsed").innerHTML = cpuUse;
			document.getElementById("memUsed").innerHTML = memUse;
			document.getElementById("oneload").innerHTML = oneload;
			document.getElementById("ioi").innerHTML = ioi;
			document.getElementById("ioo").innerHTML = ioo;
	    }
   		
   		//服务状态
		function writeServiceState(serviceState) {
			var serviceStateTag = document.getElementById("serviceState");
			if (serviceState == 1) {
				serviceStateTag.style.color = "green";
				serviceStateTag.innerHTML = "运行"; //nomal
	        } else if (serviceState == 0) {
	        	serviceStateTag.style.color = "red";
	        	serviceStateTag.innerHTML = "停止"; //STOP
	        } else if (serviceState == 2) {
	        	serviceStateTag.style.color = "red";
	        	serviceStateTag.innerHTML = "异常"; //EXCEPTION
	        }	
		}
   		
   		//是否启用报警
		function writeServiceEnableState(enableFlag) {
			var serviceEnableStateTag = document.getElementById("enableFlag");
			if (enableFlag == true) {
				serviceEnableStateTag.style.color = "green";
				serviceEnableStateTag.innerHTML = "是"; //nomal
	        } else{
	        	serviceEnableStateTag.style.color = "red";
	        	serviceEnableStateTag.innerHTML = "否"; //STOP
	        } 
		}
   		
   		//时间显示类型
		function getTimeString(currentTime) {
			document.getElementById("dataTime").style.color = "green";
   			var tail = "";
   			if (currentTime == null || currentTime == 0) {
   				document.getElementById("dataTime").style.color = "red";
   				return "0000/00/00 00:00:00(无数据)";
   			}
   			
			var time = new Date();
			var num = new Number(time.getTime()-currentTime);
			if (num >= 5*60*1000) {
				document.getElementById("dataTime").style.color = "red";
				tail = "(旧数据)";
			}
			
			time.setTime(currentTime);
			var year = time.getFullYear();    
			var month = time.getMonth()+1;       
			var day = time.getDate();        
			var hour = time.getHours();       
			var minute = time.getMinutes();     
			var second = time.getSeconds();
			if (month >= 0 && month < 10) {
				month = "0" + month;
			}
			if (day >=0 && day < 10) {
				day = "0" + day;
			}
			if (hour >=0 && hour < 10) {
				hour = "0" + hour;
			}
			if (minute >=0 && minute < 10) {
				minute = "0" + minute;
			}
			if (second >= 0 && second < 10) {
				second="0"+second;
			}
			return year + "/" + month + "/" + day + "," + hour + ":" + minute + ":" + second + tail;
		}
   		
   		function doModify() {
   			var clusterId='<%=clusterId%>';
   			var appName = '<%=appName%>';
   			nui.open({
   				url: bootPATH + "../../platform/srvmonitor/appservice/modifyServiceStretch.jsp?clusterId=" + clusterId,
                title: "服务报警策略设置", width: 900, height: 500,
                allowResize: false,
                onload: function () {
                    var iframe = this.getIFrameEl();
                    var data = { clusterId:clusterId, appName:appName };
                    iframe.contentWindow.SetData(data);
                },
                ondestroy: function (action) {
                }
            });   			        
   		}
   </script>
</body>
</html>