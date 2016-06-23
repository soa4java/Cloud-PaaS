<%@page pageEncoding="UTF-8"%>

<%
	System.out.println("showMemcachedHitsInfo");
	String ctxPath = request.getContextPath();
	// String hits = request.getAttribute("hits").toString();
	// String misses = request.getAttribute("misses").toString();
	String hits = request.getParameter("hits");
	String misses = request.getParameter("misses");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" href="style.css" type="text/css">
        <script src="../../../common/amcharts/amcharts.js" type="text/javascript"></script>	
        <script type="text/javascript">
        
			var hits = parseFloat('<%=hits %>');
			var misses = parseFloat('<%=misses %>');
			
            var chartData = [{
                country: "hits",
                value: hits
            }, {
                country: "misses",
                value: misses
            }];

            AmCharts.ready(function () {
                // PIE CHART
                chart = new AmCharts.AmPieChart();
                chart.dataProvider = chartData;
                chart.titleField = "country";
                chart.valueField = "value";
                chart.outlineColor = "#FFFFFF";
                chart.outlineAlpha = 0.8;
                chart.outlineThickness = 2;
                // this makes the chart 3D
                chart.depth3D = 15;
                chart.angle = 30;

                // WRITE
                chart.write("chartdiv");
            });
        </script>
    </head>
    
    <body>
        <div id="chartdiv" style="width: 100%; height: 200px;"></div>
    </body>
</html>