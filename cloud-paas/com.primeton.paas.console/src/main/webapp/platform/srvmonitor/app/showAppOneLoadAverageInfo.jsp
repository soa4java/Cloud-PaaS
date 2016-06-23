<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page	import="com.primeton.paas.console.common.monitor.LoadAverageInfo"%>
<%@page	import="com.primeton.paas.console.common.monitor.AppMonitorUtil"%>

<%
	long CEP_ONEMINUTESAGO = AppMonitorUtil.getCEP_ONEMINUTESAGO();
	String appName = request.getParameter("appName");
	String timeInterval = request.getParameter("timeInterval");
	int minutes = Integer.parseInt(request.getParameter("minutes"));

	LoadAverageInfo obj = AppMonitorUtil.getLoadAverageMonitorInfo(
			appName, minutes);
	String loadAverageStr = "";
	String timeStr = "";
	if (obj != null && obj instanceof LoadAverageInfo) {
		LoadAverageInfo info = (LoadAverageInfo) obj;
		loadAverageStr = info.getLoadAverages();
		timeStr = info.getTimes();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<script src="../../../common/amcharts/amcharts.js" type="text/javascript"></script>	
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
		var chart;
		var chartData = [];
		var chartCursor;
		var oneMinuteago = parseInt('<%=CEP_ONEMINUTESAGO %>');
		var timeInterval = parseInt('<%=timeInterval %>');
		var appName='<%=appName%>';
		
		var visits = new Array();
		var visitValue;
		var times = new Array();
		var timeValue;
		
		var timeLastValue;
		var visitLastValue;

		//初始，历史数据
		function generateChartData() {
		 	var timeArray = '<%=timeStr%>'.split(",");
            var dataArray = '<%=loadAverageStr%>'.split(",");
            for (var i=0; i < timeArray.length; i++) {
                var newDate = new Date();
                newDate.setTime(parseInt(timeArray[i]));
                var visits = Number(dataArray[i]);
                chartData.push({
                    date: newDate,
                    visits: visits
                });
            }
            timeLastValue=parseInt(timeArray[i-1]);
            visitLastValue = Number(dataArray[i-1]);
		}
		
		// create chart
		AmCharts.ready(function() {
			// generate some data first
		   	generateChartData();
		   	// SERIAL CHART    
		   	chart = new AmCharts.AmSerialChart();
		   	chart.marginTop = 5;
		   	chart.marginRight = 10;
		   	chart.autoMarginOffset = 5;
		   	chart.zoomOutButton = {
		    	backgroundColor: '#000000',
		    	backgroundAlpha: 0.15
		   	};
		   	chart.dataProvider = chartData;
		   	chart.categoryField = "date";

		   	// AXES
		   	// category
		   	var categoryAxis = chart.categoryAxis;
		   	categoryAxis.parseDates = true;
		   	categoryAxis.minPeriod = "ss";
		   	categoryAxis.dashLength = 1;
		   	categoryAxis.gridAlpha = 0.15;
		   	categoryAxis.axisColor = "#DADADA";
				
		   	// value                
		   	var valueAxis = new AmCharts.ValueAxis();
		   	valueAxis.axisAlpha = 0.07;
		   	//valueAxis.dashLength = 1;
           	valueAxis.title = "One minute LoadAverage Data";
		   	valueAxis.minimum = 0;
		   	valueAxis.maximum = 2.0;
		   	chart.addValueAxis(valueAxis);	

		    // GRAPH
		    var graph = new AmCharts.AmGraph();
			graph.type = "smoothedLine";
		    graph.valueField = "visits";
			
			graph.lineAlpha = 1;
            graph.lineColor = "#4DA60C";//#FF9393
            graph.fillAlphas = 0.2;

		    chart.addGraph(graph);

		    // CURSOR
		    chartCursor = new AmCharts.ChartCursor();
		    chartCursor.cursorPosition = "mouse";
		    chartCursor.cursorColor = "#4DA60C";
		    chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD  JJ:NN:SS";
		    chart.addChartCursor(chartCursor);

		    // WRITE
		    chart.write("chartdiv");
    		document.getElementById("loadAverage").innerHTML = Number(visitLastValue);
    		//每次刷新获取新数据
		    setInterval(function () {
		    	//移动旧数据
		        //chart.dataProvider.shift();
		       
				/**begin */
	    		$.ajax({
    				url: "<%=request.getContextPath() %>/srv/monitor/getAppLoadAverageInfo",
                    data: { appName:appName,timeInterval:timeInterval},
                    async: false,
                    type: "post",
                    success: function (text) {
                    	var o = nui.decode(text);
                     	visitValue = o.loadAverages;
                    	timeValue = o.times;
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	console.log("error");
                        //nui.alert(jqXHR.responseText);
                    }
                });
				/**end*/
				
	    		var visit;
	    		//获取到数据填入
	    		if (visitValue != "" && visitValue != null) {
	    			visits = visitValue.split(",");
	    			times = timeValue.split(",");
    				document.getElementById("loadAverage").innerHTML = Number(visits[visits.length-1]);
	    			for (var i=0; i<visits.length; i++) {
	    				var newdate = new Date();
                		newdate.setTime(parseInt(times[i]));
                		visit = Number(visits[i]);
                		chart.dataProvider.shift();
                		chartData.push({
                    		date: newdate,
                    		visits: visit
                		});
                		timeLastValue = newdate.getTime();
                		visitLastValue = visit;
            		}
		         } else {//没获取到任何数据，取上次获取的数据
                	var newdate = new Date();
                	timeLastValue = timeLastValue+timeInterval;
                	visitLastValue = Number(visitLastValue/2).toFixed(2);
                	if (visitLastValue == null || visitLastValue == "0.01") {
                		visitLastValue=0;
                	}
                	newdate.setTime(timeLastValue);
                	chart.dataProvider.shift();
                	chartData.push({
                    	date: newdate,
                    	visits: visitLastValue
                	});
    				document.getElementById("loadAverage").innerHTML = Number(visitLastValue);
		         }
		         chart.validateData();
				}, timeInterval);//刷新时间 s
			});
	</script>
</head>
<body>
	<div id="chartdiv" style="width:100%;height:90%"></div>
    <div style="margin-left:10px">
    	OneLoadAverage 一分钟系统负载:<span id="loadAverage" style="color: #4DA60C;width:15px">0</span>&nbsp;&nbsp;&nbsp;<span style="color: #4DA60C;">*注：一分钟系统负载，即系统一分钟内任务队列的平均长度</span>
    </div>
</body>
</html>