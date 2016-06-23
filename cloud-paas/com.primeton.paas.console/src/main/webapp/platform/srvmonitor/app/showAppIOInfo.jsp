<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page	import="com.primeton.paas.console.common.monitor.IOInfo"%>
<%@page	import="com.primeton.paas.console.common.monitor.AppMonitorUtil"%>

<%
	long CEP_ONEMINUTESAGO = AppMonitorUtil.getCEP_ONEMINUTESAGO();
	String appName = request.getParameter("appName");
	String timeInterval = request.getParameter("timeInterval");
	int minutes = Integer.parseInt(request.getParameter("minutes"));

	//历史数据
	IOInfo obj = AppMonitorUtil.getIOMonitorInfo(appName, minutes);
	String inputStr = "";
	String outputStr = "";
	String timeStr = "";
	if (obj != null && obj instanceof IOInfo) {
		IOInfo info = (IOInfo) obj;
		inputStr = info.getInputs();
		outputStr = info.getOutputs();
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
		var clicks = new Array();
		var clickValue;
		var times = new Array();
		var timeValue;
		
		var timeLastValue;
		var visitLastValue;
		var clickLastValue;

		//初始，历史数据
		function generateChartData() {
		 	var timeArray = '<%=timeStr%>'.split(",");
            var visits = '<%=inputStr%>'.split(",");
            var clicks = '<%=outputStr%>'.split(",");
            for (var i=0; i < timeArray.length; i++) {
                var newDate = new Date();
                newDate.setTime(parseInt(timeArray[i]));
                var visit = parseInt(visits[i]);
                var click = parseInt(clicks[i])
                chartData.push({
                    date: newDate,
                    visits: visit,
                    clicks: click
                });
            }
            timeLastValue=parseInt(timeArray[i-1]);
            visitLastValue = parseInt(visits[i-1]);
            clickLastValue = parseInt(clicks[i-1]);
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
            valueAxis.title = "IO Data 单位:kb/s";
			//valueAxis.minimum = 0;
			//valueAxis.maximum = 100;
		    chart.addValueAxis(valueAxis);

		    // GRAPH
		    var graph = new AmCharts.AmGraph();
			graph.type = "smoothedLine";
		    graph.valueField = "visits";
			
			graph.lineAlpha = 1;
            graph.lineColor = "#B87754";
            graph.fillAlphas = 0.2;

		    chart.addGraph(graph);
			
			var graph = new AmCharts.AmGraph();
			//graph.type = "smoothedLine";
			graph.type = "line";
		    graph.valueField = "clicks";
			graph.lineAlpha = 1;
            graph.lineColor = "#71462F";
            graph.fillAlphas = 0.2;
		    chart.addGraph(graph);
			
		    // CURSOR
		    chartCursor = new AmCharts.ChartCursor();
		    chartCursor.cursorPosition = "mouse";
		    chartCursor.cursorColor = "#B87754";
		    chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD  JJ:NN:SS";
		    chart.addChartCursor(chartCursor);

		    // WRITE
		    chart.write("chartdiv");
    		document.getElementById("inputing").innerHTML = Number(visitLastValue);
		    document .getElementById("outputing").innerHTML = Number(clickLastValue);
    		//每次刷新获取新数据
		    setInterval(function () {
		    	//移动旧数据
		        //chart.dataProvider.shift();
		       
				/**begin */
	    		$.ajax({
    				url: "<%=request.getContextPath() %>/srv/monitor/getAppIOInfo",
                    data: { appName:appName,timeInterval:timeInterval},
                    async: false,
                    type: "post",
                    success: function (text) {
                    	var o = nui.decode(text);
                     	visitValue = o.inputs;
                     	clickValue = o.outputs;
                    	timeValue = o.times;
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	console.log("error");
                        //nui.alert(jqXHR.responseText);
                    }
                });
				/**end*/
				
	    		var visit;
	    		var click;
	    		//获取到数据填入
	    		if (visitValue != "" && visitValue != null) {
	    			visits = visitValue.split(",");
	    			clicks = clickValue.split(",");
	    			times = timeValue.split(",");
    				document .getElementById("inputing").innerHTML = Number(visits[visits.length-1]);
    				document .getElementById("outputing").innerHTML = Number(clicks[clicks.length-1]);
    				
	    			for (var i=0; i<visits.length; i++) {
	    				var newdate = new Date();
                		newdate.setTime(parseInt(times[i]));
                		visit = Number(visits[i]);
                		click = Number(clicks[i]);
                		chart.dataProvider.shift();
                		chartData.push({
                    		date: newdate,
                    		visits: visit,
                    		clicks: click
                		});
                		timeLastValue = newdate.getTime();
                		visitLastValue = visit;
                		clickLastValue = click;
            		}
		         } else {//没获取到任何数据，取上次获取的数据
                	var newdate = new Date();
                	timeLastValue=timeLastValue+timeInterval;
                	visitLastValue=Number(visitLastValue/2).toFixed(1);
                	clickLastValue=Number(clickLastValue/2).toFixed(1);
                	if (visitLastValue == null || visitLastValue == "0.1") {
                		visitLastValue=0;
                	}
                	if (clickLastValue == null || clickLastValue == "0.1") {
                		clickLastValue=0;
                	}
                	newdate.setTime(timeLastValue);
                	chart.dataProvider.shift();
                	chartData.push({
                    	date: newdate,
                    	visits: visitLastValue,
                    	clicks: clickLastValue
                	});
		         	document.getElementById("inputing").innerHTML = Number(visitLastValue);
		         	document .getElementById("outputing").innerHTML = Number(clickLastValue);
		         }
		         chart.validateData();
			}, timeInterval);//刷新时间 s
		});
	</script>
</head>
<body>
	<div id="chartdiv" style="width:100%;height:90%"></div>
    <div style="margin-left:10px">
    	IO读写 当前读出速率:<span id="inputing" style="color: #B87754;width:15px">0</span>kb/s&nbsp;&nbsp;&nbsp;写入速率:<span id="outputing" style="color: #71462F;width:15px">0</span>kb/s&nbsp;&nbsp;&nbsp;<span style="color: #B97A57;">*注:Y坐标轴表示IO读写速率，单位:块/秒</span>
    </div>
</body>
</html>