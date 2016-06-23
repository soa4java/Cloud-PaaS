<%@page pageEncoding="UTF-8"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.primeton.paas.manage.api.service.MemcachedService"%>
<%@page import="com.primeton.paas.console.common.monitor.CacheMonitorUtil"%>
<%@page import="com.primeton.paas.console.common.monitor.HostMonitorUtil"%>

<%
	Object clusterId = request.getAttribute("clusterId");
	Object mServiceId = request.getAttribute("mServiceId");
	if (clusterId == null) {
		clusterId = request.getParameter("clusterId");
	}
	if (mServiceId == null) {
		mServiceId = request.getParameter("mServiceId");
	}
	int mState = -1;
	Map info = new HashMap();
	List<MemcachedService> mServiceList = new ArrayList<MemcachedService>();
	String url = "";
	if (null != clusterId && !clusterId.toString().equals("0")) {
		mServiceList = CacheMonitorUtil.getMemcachedServices(clusterId
				.toString());
		if (null != mServiceId && !mServiceId.toString().equals("0")) {
			MemcachedService service = CacheMonitorUtil
					.getMemcachedService(mServiceId.toString());
			mState = service.getState();
			if (mState == 1) {
				url = service.getIp() + ":" + service.getPort();
				info = CacheMonitorUtil.getServiceMonitor(url);
				if (info == null) {
					info = new HashMap();
				}
			}
		}
	}

	System.out.println("clusterId=" + clusterId + "&mServiceId="
			+ mServiceId);

	DecimalFormat df = new DecimalFormat("##.0");

	double bytes = 0;
	double usageFree = 0;
	int usageFreeToInt = 0;
	double maxbytes = 0;
	double usageFreeTemp = 0;
	double bytesTemp = 0;

	double hits = 0;
	double misses = 0;
	double maxHis$Miss = 0;
	double hitsTemp = 0;
	double missesTemp = 0;

	double freeToM = 0;
	double bytesToM = 0;
	double maxbytesToM = 0;

	String startTime = "";
	String uptimeStr = "";

	if (null != info && info.keySet().size() > 0 && mState != 0) {
		long time = Long.parseLong(info.get("time").toString().trim());
		int uptime = Integer.parseInt(info.get("uptime").toString()
				.trim());

		long afterTime = time * 1000;
		Date afterDate = new Date();
		afterDate.setTime(afterTime);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");

		startTime = format.format(afterDate);
		uptimeStr = CacheMonitorUtil.secToTime(uptime);

		bytes = this.parseDouble(info.get("bytes").toString().trim());
		maxbytes = this.parseDouble(info.get("limit_maxbytes")
				.toString().trim());
		hits = this.parseDouble(info.get("get_hits").toString().trim());
		misses = this.parseDouble(info.get("get_misses").toString()
				.trim());

		usageFree = maxbytes - bytes;
		usageFreeToInt = this.parseInt(usageFree);
		usageFreeTemp = usageFree * 100 / maxbytes;
		bytesTemp = bytes * 100 / maxbytes;

		if (usageFreeTemp == 100 && bytesTemp == 0 && bytes != 0) {
			bytesTemp = 0.1;
			usageFreeTemp = usageFreeTemp - bytesTemp;
		}

		usageFreeTemp = this.parseDouble(df.format(usageFreeTemp));
		bytesTemp = this.parseDouble(df.format(bytesTemp));
		maxHis$Miss = hits + misses;
		hitsTemp = hits * 100 / maxHis$Miss;
		missesTemp = misses * 100 / maxHis$Miss;
		hitsTemp = this.parseDouble(df.format(hitsTemp));
		missesTemp = this.parseDouble(df.format(missesTemp));

		bytesToM = bytes / 1024 / 1024;
		maxbytesToM = maxbytes / 1024 / 1024;
		freeToM = maxbytesToM - bytesToM;
	}
	Integer[] refreshTimes = HostMonitorUtil.getRefreshTime();
	request.setAttribute("refreshTimes", refreshTimes);
%>

<%!
	double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
		}
		return 0;
	}

	int parseInt(Double s) {
		try {
			return Integer.parseInt(new java.text.DecimalFormat("0").format(s));
		} catch (Exception e) {
		}
		return 0;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<script src="<%=request.getContextPath() %>/common/amcharts/amcharts.js" type="text/javascript"></script>	
	<script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
    <fieldset style="width:100%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>缓存监控</legend>
	<div
		style="margin-left:15px; position:relative; margin-right: 15px; padding-top: 5px; padding-bottom: 0px">

		 <input id="clusterId" name="clusterId" class="nui-combobox" url="<%=request.getContextPath() %>/srv/monitor/getCacheClusters" valueField=id textField="name" valueFromSelect="true" style="width:300px;" onvaluechanged="onChangeData()"
		 		emptyText="———————  选择Memcached集群 ——————— " nullItemText="———————  选择Memcached集群 ——————— " showNullItem="true"/>
		 
		&nbsp;&nbsp;&nbsp; 
		
		<select id="selServiceId" name="mServiceId"
			 value="${mServiceId}" onchange="onServiceChangeData(this)">
			<option value="0">————— 选择服务 —————</option>
			<%
			for (MemcachedService ms : mServiceList) {
			%>
			<option value="<%=ms.getId() %>"
				<% if (mServiceId !=null && mServiceId.equals(ms.getId())) { %> selected="selected" <%} %>>
				<%=ms.getName()%></option>
			<%
			}
			%>
		</select> 
<%-- 		&nbsp;&nbsp;&nbsp; 状态：<%=mState==0?"<span style='font-weight:bold;color:red'>停止</span>":"<span style='font-weight:bold;color:green'>运行</span>" %>&nbsp;&nbsp;&nbsp;  --%>
		&nbsp;&nbsp;&nbsp; 状态：
		<%
		 if (mState == 0) {
			 %><span style='font-weight:bold;color:red'>停止</span><%
		 } else if (mState == 1) {
		  	%><span style='font-weight:bold;color:green'>运行</span><%
		 } else {
		 %> <span style='font-weight:bold;color:blue'>待检测</span><% 
		 }%>&nbsp;&nbsp;&nbsp; 
		<a id="bt_refresh" class="nui-button" iconCls="icon-reload" onclick="reload()" text="刷新"></a>
	</div>

	<div style="margin-left:15px; position:relative; margin-right: 15px; padding-top: 5px; padding-bottom: 0px ;">
			<table align="center" border="0" width="100%"  style="margin-top: 2px ;font-size:12px">
<!-- 				<tr> -->
<!-- 					<td colspan="2"><img alt="" -->
<%-- 						src="<%=request.getContextPath()%>/images/muen6.png" --%>
<!-- 						style="height: 20px;"> <span style="font-weight: bold;">&nbsp;General -->
<!-- 					Cache Information</span></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<td >Memcached Host： <%=url%></td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<td >Total Memcache Cache： <%=maxbytesToM%> M --%>
<!-- 					</td> -->
<!-- 				</tr> -->
		
				<tr>
					<td colspan="2"><img alt=""
						src="<%=request.getContextPath()%>/images/muen6.png"
						style="height: 20px;"> <span style="font-weight: bold;">&nbsp;Memcache
					Server Information</span></td>
				</tr>
				<tr>
					<td >Host： <%=url%></td>
				</tr>
				<tr>
					<td >Pid: <%=info.get("pid")%></td>
				</tr>
				<tr>
					<td >Start Time： <%=startTime%></td>
				</tr>
				<tr>
					<td >Uptime: <%=info.get("uptime") %> s</td>
				</tr>
				<tr>
					<td >Memcached Server Version: <%=info.get("version")%>
					</td>
				</tr>
<!-- 				<tr> -->
<%-- 					<td >Used Cache Size: <%=bytesToM%> M</td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<td >Total Cache Size: <%=maxbytesToM%> M</td> --%>
<!-- 				</tr> -->
				<tr>
					<td >Used Cache Size: <%=info.get("bytes") %> Byte</td>
				</tr>
				<tr>
					<td >Total Cache Size: <%=info.get("limit_maxbytes") %> Byte</td>
				</tr>
		
				<tr>
					<td colspan="2"><img alt=""
						src="<%=request.getContextPath()%>/images/muen7.png"
						style="height: 20px;"> <span style="font-weight: bold;">&nbsp;Host
					Status Diagrams</span></td>
				</tr>
				<tr>
					<td>
						<div style="width:100%;height:180px;" id="Monitor_INF_Frame">
							<table width="100%" height="100%" border="0">
								<tr>
									<td width="50%" height="50%"><iframe id="freeAndBytesView" name="freeAndBytesView"
										id="freeView" src="" width="100%" height="100%" frameborder="no"
										border="0" marginwidth="0" marginheight="0" scrolling="no"
										allowtransparency="true"></iframe></td>
									<td width="50%" height="50%"><iframe id="hitsAndMissesView" name="hitsAndMissesView"
										id="bytesView" src="" width="100%" height="100%" frameborder="no"
										border="0" marginwidth="0" marginheight="0" scrolling="no"
										allowtransparency="true"></iframe></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
<!-- 				<tr> -->
<%-- 					<td >Cache Usage Free： <%=freeToM%> M (<%=usageFreeTemp%>%) --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<td >Cache Usage Used： <%=bytesToM%> M (<%=bytesTemp%>%) --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td >Cache Usage Free： <%=usageFreeToInt%> Byte  (<%=usageFreeTemp%>%)
					</td>
				</tr>
				<tr>
					<td >Cache Usage Used： <%=info.get("bytes")%> Byte (<%=bytesTemp%>%)
					</td>
				</tr>
		
				<tr>
					<td >Hits & Misses Hits： <%=info.get("get_hits")%>(<%=hitsTemp%>%)
					</td>
				</tr>
		
				<tr>
					<td >Hits & Misses Misses： <%=info.get("get_misses")%>(<%=missesTemp%>%)
					</td>
				</tr>
		
				<tr>
					<td colspan="2"><img alt=""
						src="<%=request.getContextPath()%>/images/muen7.png"
						style="height: 20px;"> <span style="font-weight: bold;">&nbsp;Cache
					Information</span></td>
				</tr>
				<tr>
					<td >Current Items： <%=info.get("curr_items")%>
					</td>
				</tr>
				<tr>
					<td >Total Items： <%=info.get("total_items")%>
					</td>
				</tr>
				<tr>
					<td >Hits： <%=info.get("get_hits")%></td>
				</tr>
				<tr>
					<td >Misses： <%=info.get("get_misses")%></td>
				</tr>
				<tr>
					<td >Curr Connections： <%=info.get("curr_connections")%></td>
				</tr>
				<tr>
					<td >Total Connections： <%=info.get("total_connections")%></td>
				</tr>
				<tr>
					<td >Evictions： <%=info.get("evictions")%></td>
				</tr>
				<tr>
					<td >Threads： <%=info.get("threads")%></td>
				</tr>
			</table>
	</div>
</fieldset>

</body>
<script type="text/javascript">

$(document).ready(function() {
    nui.getbyName("clusterId").select(0);
	var tag = "<%=clusterId%>";
	if (tag != 'null') {
		nui.getbyName("clusterId").setValue(tag);
	}
	
	clusterId = nui.get("clusterId").getValue();
	mServiceId = document.getElementById("selServiceId").value;
	if (clusterId != null && mServiceId != null 
			&& clusterId != '' && mServiceId != '' && mServiceId != '0') {
		nui.get("bt_refresh").setEnabled(true);
	} else {
		nui.get("bt_refresh").setEnabled(false);
	}
});

window.onload = init;

function onChangeData(obj) {
	var tag = "<%=clusterId%>";
	var stag = "<%=mServiceId%>";
	clusterId = nui.get("clusterId").getValue();
	if (clusterId != null) {
		clusterId = nui.get("clusterId").getValue();
		window.location="showServiceMonitor.jsp?clusterId=" + clusterId;
	}
}

function onServiceChangeData(obj) {
	clusterId = nui.get("clusterId").getValue();
	mServiceId = document.getElementById("selServiceId").value;
	if (clusterId != null && mServiceId != null){
        nui.mask({
            el: document.body,
            cls: 'mini-mask-loading',
            html: 'Loading...'
        });
		window.location="showServiceMonitor.jsp?clusterId=" + clusterId  + "&mServiceId=" + mServiceId;
	}
}

function reload() {
	onServiceChangeData();
}

function init() {
	viewMonitor();
}

function viewMonitor() {
	var free = parseFloat('<%=usageFreeTemp %>');
    var bytes = parseFloat('<%=bytesTemp %>');
    var hits = parseFloat('<%=hitsTemp %>');
    var misses = parseFloat('<%=missesTemp %>');
	var freeAndBytesFrame = document.getElementById("freeAndBytesView");
	var hitsAndMissesFrame = document.getElementById("hitsAndMissesView");
	freeAndBytesFrame.src="<%=request.getContextPath() %>/platform/srvmonitor/memcached/showMemcachedFreeInfo.jsp?free=" + free + "&bytes=" + bytes;
	hitsAndMissesFrame.src="<%=request.getContextPath() %>/platform/srvmonitor/memcached/showMemcachedHitsInfo.jsp?hits=" + hits + "&misses=" + misses;
}
</script>
</html>