<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.primeton.paas.manage.api.app.StretchStrategy" %>
<%@page import="com.primeton.paas.console.common.stretch.StrategyItemInfo" %>
<%@page import="com.primeton.paas.console.platform.service.monitor.TelescopicStrategyUtil" %>

<%
	StrategyItemInfo itemInfo = TelescopicStrategyUtil.getItemInfo();
	Integer[] stretchSizeArr = itemInfo.getStretchScaleArr();//伸缩幅度
	Integer[] ignorTimeArr = itemInfo.getIgnoreTimeArr();
	Integer[] continueTimeArr = itemInfo.getDurationArr();//持续时间
	Integer[] cpuArr = itemInfo.getCpuUsageArr();//cpu
	Integer[] memArr = itemInfo.getMemUsageArr();//mem
	String[] loadAverageArr = itemInfo.getLbArr();//one loadaverage

	Map<String, Object> temp = null;
	//伸缩幅度
	List<Object> stretchSizeLists = new ArrayList<Object>();
	for (Integer size : stretchSizeArr) {
		temp = new HashMap<String, Object>();
		temp.put("id", size);
		temp.put("text", size + " 台");
		stretchSizeLists.add(temp);
	}
	Object stretchSizes = JSONArray.fromObject(stretchSizeLists);
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
		// var StretchSizeComboBoxData = [{'id':'1','text':'1台'},{'id':'2','text':'2台'},{'id':'3','text':'3台'},{'id':'4','text':'4台'}];
		var StretchSizeComboBoxData = <%=stretchSizes%>;
    	var ContinuedTimeComboBoxData = <%=continueTimeSizes%>;
    	var IgnoreTimeComboBoxData = <%=ignorTimeSizes%>;
		var CpuThresholdComboBoxData = <%=cpuSizes%>;
		var MemThresholdComboBoxData = <%=memSizes%>;
		var LbThresholdComboBoxData = <%=lbSizes%>;
	</script>
</head>
<body>     
	<div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
        <table style="width:100%;">
            <tr>
            <td style="width:100%;">
                <a class="nui-button" iconCls="icon-save" onclick="save()">保存</a>
                <span class="separator"></span>
                <a class="nui-button" iconCls="icon-reload" onclick="reset()">重置</a>
            </td>
        </table>
    </div>
    
    <div class="nui-fit" id="globalDiv">
    	<div id="globalgrid" class="nui-datagrid" style="width:100%;height:350px;" allowResize="false" allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
        editNextOnEnterKey="true"  editNextRowCell="true"
        	idField="id" url="<%=request.getContextPath() %>/srv/monitor/getGlobalStrategyConfigInfo" showPager="false">
        	<div property="columns">
	            <div field="enableFlag" type="checkboxcolumn" trueValue="Y" falseValue="N" width="60" headerAlign="center">是否启用</div>
	            <div field="strategyType" width="80" headerAlign="center" align ="center" renderer="onStrategyTypeRenderer">伸缩类型</div>    
				<div type="comboboxcolumn" autoShowPopup="false" name="stretchSize" field="stretchSize" width="100" allowSort="true" align="center" headerAlign="center">伸/缩 幅度(台)
                	<input property="editor" class="nui-combobox" style="width:100%;" data="StretchSizeComboBoxData" />                
            	</div>                              
				<div type="comboboxcolumn" autoShowPopup="true" name="continuedTime" field="continuedTime" width="100" allowSort="true" align="center" headerAlign="center">持续时间(分钟)
                	<input property="editor" class="nui-combobox" style="width:100%;" data="ContinuedTimeComboBoxData" />                
            	</div>                              
				<div type="comboboxcolumn" autoShowPopup="true" name="ignoreTime" field="ignoreTime" width="100" allowSort="true" align="center" headerAlign="center">伸/缩 后休眠时间(分钟)
                	<input property="editor" class="nui-combobox" style="width:100%;" data="IgnoreTimeComboBoxData" />                
            	</div>                              
				<div type="comboboxcolumn" autoShowPopup="true" name="cpuThreshold" field="cpuThreshold" width="100" allowSort="true" align="center" headerAlign="center">CPU 利用率(%)
                	<input property="editor" class="nui-combobox" style="width:100%;" data="CpuThresholdComboBoxData" />                
            	</div>                              
				<div type="comboboxcolumn" autoShowPopup="true" name="memThreshold" field="memThreshold" width="100" allowSort="true" align="center" headerAlign="center">内存利用率(%)
                	<input property="editor" class="nui-combobox" style="width:100%;" data="MemThresholdComboBoxData" />                
            	</div>                              
				<div type="comboboxcolumn" autoShowPopup="true" name="lbThreshold" field="lbThreshold" width="100" allowSort="true" align="center" headerAlign="center">负载
                	<input property="editor" class="nui-combobox" style="width:100%;" data="LbThresholdComboBoxData" />                
            	</div>            
			</div>  
    	</div>
    </div>
    
    <script type="text/javascript">        
        nui.parse();
        var grid = nui.get("globalgrid");
    	grid.load();
    	
    	var CLD_StrategyType = [{ id: 'INCREASE', text: '伸'} , { id: 'DECREASE', text: '缩'}];
    	
		function onStrategyTypeRenderer(e) {
			if (e.value == CLD_StrategyType[0].id) {
				return CLD_StrategyType[0].text;
			} else {
				return CLD_StrategyType[1].text;
			}
		}
    	
		function save() {
    		if (grid.isChanged()) {
    			// var rows = grid.getChanges(null,false);//获取改动的行。如果有改动
        		var data = grid.getData();
        		// var rows = grid.getChanges();
                var json = nui.encode(data)
                grid.loading("保存全局伸缩策略配置...");
    			$.ajax({
    				url: "<%=request.getContextPath() %>/srv/monitor/updateGlobalStretchStrategy/",
                    data: { keyData: json },
                    type: "post",
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == true) {
                    		nui.alert("保存成功");
                    		grid.reload();
                    	} else {
                    		nui.alert("保存失败");
                    		grid.unmask();
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	grid.unmask();
                        nui.alert(jqXHR.responseText);
                    }
                });
    		} else {
    			nui.alert("未做改动！");
    		}
    	}
		
    	function reset() {
    		grid.reload();
    	}
    </script>
</body>
</html>