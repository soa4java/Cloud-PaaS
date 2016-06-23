<%@page pageEncoding="UTF-8"%>

<%
	System.out.println("showMemcachedFreeInfo");
	String ctxPath = request.getContextPath();
	// String free = request.getAttribute("free").toString();
	// String bytes = request.getAttribute("bytes").toString();
	String free = request.getParameter("free");
	String bytes = request.getParameter("bytes");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" href="style.css" type="text/css">
        <script src="../../../common/amcharts/amcharts.js" type="text/javascript"></script>	
        <script type="text/javascript">
			var free = parseFloat('<%=free %>');
			var bytes = parseFloat('<%=bytes %>');
			
            var chartData = [{
                country: "free",
                value: free
            }, {
                country: "used",
                value: bytes
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