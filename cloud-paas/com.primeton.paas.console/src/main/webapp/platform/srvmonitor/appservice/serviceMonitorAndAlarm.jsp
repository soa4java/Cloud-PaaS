<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
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
		var ReminderTypeComboBoxData = [{'id':'SMS','text':'短信'},{'id':'MAIL','text':'邮件'},{'id':'SMS+MAIL','text':'短信+邮件'}];
	</script>
</head>
<body>
<div class="nui-layout" style="width:100%;height:100%;">
<div style="border:0;height:100%">
	<div id="strategyTabs" class="nui-tabs" activeIndex="0" tabPosition="top" style="width:100%;height:100%;" onactivechanged="loadNginxMonitor" >
    	
        <div title="服务监控报警策略配置" >
        	<div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
		    	选择应用:<input id="appCombox" class="nui-combobox" style="width:25%;" textField="displayName" valueField="name" 
		        onvaluechanged="onAppChanged" url="<%=request.getContextPath() %>/srv/monitor/getApps" emptyText="---请选择目标应用---" nullItemText="---请选择目标应用---"
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
        <div title="服务全局报警策略配置" >
			<div id="forEdit2" class="nui-toolbar" style="padding:2px;border:0;display:block">
			        <table style="width:100%;">
			            <tr>
			            <td style="width:100%;">
			                <a class="nui-button" iconCls="icon-save" onclick="globalStrategySave()">保存</a>
			                <span class="separator"></span>
			                <a class="nui-button" iconCls="icon-reload" onclick="globalStrategyReset()">重置</a>
			            </td>
			        </table>
			    </div>
			    
			    <div class="nui-fit" id="globalDiv">
			    	<div id="globalgrid" class="nui-datagrid" style="width:100%;height:350px;" allowResize="false" allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
			        editNextOnEnterKey="true"  editNextRowCell="true"
			        	idField="id" url="<%=request.getContextPath() %>/srv/monitor/getGlobalServiceStrategyConfigInfo" showPager="false">
			        	<div property="columns">
				            <div field="enableFlag" type="checkboxcolumn" trueValue="Y" falseValue="N" width="60" headerAlign="center">是否启用</div>
							<div type="comboboxcolumn" autoShowPopup="true" name="alarmType" field="alarmType" width="80" allowSort=false" align="center" headerAlign="center">报警方式
			                	<input id="alarmTypeInput" property="editor" class="nui-combobox" style="width:100%;" data="ReminderTypeComboBoxData" />                
			            	</div>          
			            	<div name="alarmAddress" field="alarmAddress" headerAlign="center" allowSort="false" width="150" >通讯地址
			                	<input id="alarmAddressInput" property="editor" class="nui-textbox" width="100%" align="right"/>
			            	</div> 
			            	                    
							<div type="comboboxcolumn" autoShowPopup="true" name="continuedTime" field="continuedTime" width="100" allowSort="false" align="center" headerAlign="center">持续时间(分钟)
			                	<input id="continuedTimeInput" property="editor" class="nui-combobox" style="width:100%;" data="ContinuedTimeComboBoxData" />                
			            	</div>                              
							<div type="comboboxcolumn" autoShowPopup="true" name="ignoreTime" field="ignoreTime" width="120" allowSort="false" align="center" headerAlign="center">报警后休眠时间(分钟)
			                	<input id="ignoreTimeInput" property="editor" class="nui-combobox" style="width:100%;" data="IgnoreTimeComboBoxData" />                
			            	</div>                              
							<div type="comboboxcolumn" autoShowPopup="true" name="cpuThreshold" field="cpuThreshold" width="100" allowSort="false" align="center" headerAlign="center">CPU 利用率(%)
			                	<input id="cpuThresholdInput" property="editor" class="nui-combobox" style="width:100%;" data="CpuThresholdComboBoxData" />                
			            	</div>                              
							<div type="comboboxcolumn" autoShowPopup="true" name="memThreshold" field="memThreshold" width="100" allowSort="false" align="center" headerAlign="center">内存利用率(%)
			                	<input id="memThresholdInput" property="editor" class="nui-combobox" style="width:100%;" data="MemThresholdComboBoxData" />                
			            	</div>                              
							<div type="comboboxcolumn" autoShowPopup="true" name="lbThreshold" field="lbThreshold" width="80" allowSort="false" align="center" headerAlign="center">负载
			                	<input id="lbThresholdInput" property="editor" class="nui-combobox" style="width:100%;" data="LbThresholdComboBoxData" />                
			            	</div>          
						</div>  
			    	</div>
			    </div>
    	</div>
    	
    	<div title="Nginx服务监控" >
    		<div id="forEdit3" class="nui-toolbar" style="padding:2px;border:0;display:block">
		    	选择应用:<input id="clusterCombox" class="nui-combobox" style="width:25%;" textField="name" valueField="id" 
		        onvaluechanged="onClusterChanged" url="<%=request.getContextPath() %>/srv/monitor/getPlatformClusters" emptyText="---请选择目标服务集群---" nullItemText="---请选择目标服务集群---"
		        showNullItem="true"
		         />&nbsp;&nbsp;服务状态: <span id="clusterState" style="font-weight:bold"></span>
		         <a id="enableSingleServiceMonitor" class="nui-button" onclick="enableSingleServiceMonitor();" style="float:right;margin-right:5px;display:none">启用服务监控</a>
		         <a id="disableSingleServiceMonitor" class="nui-button" onclick="disableSingleServiceMonitor();" style="float:right;margin-right:5px;display:none">禁用服务监控</a>
				<!-- 		         
				<a id="refresh3" class="nui-button" onclick="refresh3();" iconCls="icon-reload" style="float:right;margin-right:10px"></a> 
				-->
		    </div>
    	
		    <fieldset id="Nginx服务" style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;display:block">
		        <legend>Nginx服务</legend>
		        <div style="width:100%;height:250px;margin-top:10px;" id="Nginx_Monitor_INF_Frame" >
			   		<iframe name="Nginx" id="Nginx" src="" width="100%" height="100%" frameborder="no" border="1" marginwidth="0" marginheight="0" scrolling="no"></iframe>
				</div>
		    </fieldset>
        </div>
	</div>
</div>
</div>

	<script type="text/javascript">        
        nui.parse();
        
        var appCombox = nui.get("appCombox");
        var globalgrid = nui.get("globalgrid");
        globalgrid.load();
        
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
        function noneDisplayAll(){
        	haproxyField.style.display = "none";
        	jettyField.style.display = "none";
        	memcachedField.style.display = "none";
        	mysqlField.style.display = "none";
        	document.getElementById("enableServiceMonitor").style.display="none";
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
				document.getElementById("HaProxy").src = "showServiceMonitor.jsp?appName="
						+ appName + "&&clusterId=" + haproxyClusterId;
     		}
     		if (jettyEnable) {
     			jettyClusterId = data.JettyClusterId;
     			jettyField.style.display = "block";
				document.getElementById("Jetty").src = "showServiceMonitor.jsp?appName="
						+ appName + "&&clusterId=" + jettyClusterId;
     		}
     		if (memEnable) {
     			memClusterId = data.MemcachedClusterId;
     			memcachedField.style.display = "block";
				document.getElementById("Memcached").src = "showServiceMonitor.jsp?appName="
						+ appName + "&&clusterId=" + memClusterId;
     		}
     		if (mysqlEnable) {
     			mysqlClusterId = data.MySQLClusterId;
     			mysqlField.style.display = "block";
				document.getElementById("MySQL").src = "showServiceMonitor.jsp?appName="
						+ appName + "&&clusterId=" + mysqlClusterId;
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
                	if (result==true) {
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
        
        function refresh(){
        	onAppChanged(null);
        }
        
        function disableServiceMonitor(){
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
					nui.alert("启用成功！");
					onAppChanged(null);
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
					nui.unmask();
	        		console.log("error");
	        	}
	    	});
        }
        
        
        function globalStrategyReset() {
        	globalgrid.reload();
        }
        
        function globalStrategySave() {
        	if (globalgrid.isChanged()) {
        		var data = globalgrid.getData();
    			if (data[0].alarmType == "" || data[0].continuedTime == ""
    					|| data[0].ignoreTime== "" || data[0].cpuThreshold == ""
    					|| data[0].memThreshold == "" || data[0].lbThreshold == "") {
    				nui.alert("数据不完整无法保存！请检查！");
    				return;
    			}
    			if (data[0].alarmType == "MAIL" && !validateMails(data[0].alarmAddress)) {
    				nui.alert("通讯地址-邮箱格式不正确！");
    				return;
    			}
    			if (data[0].alarmType == "SMS" && !validatePhones(data[0].alarmAddress)) {
    				nui.alert("通讯地址-手机号格式不正确！");
    				return;
    			}
    			if (data[0].alarmType=="SMS,MAIL" && !validatePhoneAndMail(data[0].alarmAddress)) {
    				nui.alert("通讯地址格式:phoneNumber1,phoneNumber2...;mail1,mail2...");
    				return;
    			}
                var json = nui.encode(data)
				
                //保存服务报警策略信息
                globalgrid.loading("保存服务全局报警策略配置...");
    			$.ajax({
    				url: "<%=request.getContextPath() %>/srv/monitor/updateGlobalServiceWarnStrategy",
    				async: true,
                    data: { keyData:json },
                    type: "post",
                    success: function (text) {
                    	var o = nui.decode(text);
                    	if (o.result == true) { 
                    		nui.alert("保存成功");
                    		globalgrid.reload();
                    	} else {
                    		nui.alert("保存失败");
                    		globalgrid.unmask();
                    	}
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	globalgrid.unmask();
                        nui.alert(jqXHR.responseText);
                    }
                });
    		} else {
    			nui.alert("未做改动！");
    		}
        }
        
        function validataMail(email) { 
			if (email == "") {
				return true;
			}
			var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_-]{2,20}@[a-zA-Z0-9.]{3,20}$");
			if (reg.test(email)) {
				return true;
			}
			return false;   
		}
		
		function validatePhone(phone) {
			var pattern = /\d*/;
            if (phone.length != 11 || pattern.test(phone) == false) {
               return false;
            }
            return true;
		}
		
		function validateMails(emails) {
			if (emails == "") {
				return true;//不填，使用管理员默认邮箱
			}
			var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_-]{2,20}@[a-zA-Z0-9.]{3,20}$");
			var mails = emails.split(",");
			for (var i=0; i<mails.length; i++) {
				if (!reg.test(mails[i])) {
					return false;
				}
			}
			return true;
		}
		
		function validatePhones(phones) {
			var phonesArray = phones.split(",");
			var pattern = /\d*/;
			for (var i=0; i<phonesArray.length; i++){
				if (phonesArray[i].length != 11 
						|| pattern.test(phonesArray[i]) == false) {
		               return false;
		        }
			}
			return true;
		}

		function validatePhoneAndMail(address) {
			var addressArray = address.split(";");
			if (addressArray.length <= 0) {
				return true;//使用管理员默认手机号，作为通讯地址
			}
			if (addressArray.length == 1) {
				//只填手机号
				return validatePhones(addressArray[0]);
			} else if (addressArray.length == 2) {
				//sms,mail
				var phones = addressArray[0];
				var mails = addressArray[1];
				return validatePhones(phones)&&validateMails(mails);
			} else {
				return false;
			}
		}
		
		function loadNginxMonitor(e) {
			var clusterCombox = nui.get("clusterCombox");
			//var tabs = e.sender;
			var activetab = e.tab;
			if (activetab._id == 3) {
				var appName=null;
				//选择服务集群，获取集群id
				var clusterId = clusterCombox.getValue();
				if (!clusterId) {
					if (clusterCombox.data.length > 1) {
						clusterCombox.select(1);
		        	} else {
		        		nui.alert("目前没有符合条件的服务集群！");
		        		return;
		        	}
				}
			}
		}
		
		function onClusterChanged() {
			var clusterCombox = nui.get("clusterCombox");
			var appName=null;
			var clusterId = clusterCombox.getValue();
			if (!clusterId) {
	    		nui.alert("请选择服务集群！");
	    		return;
			} else {
				displayClusterEnableButton(clusterId);
				document.getElementById("Nginx").src = "showServiceMonitor.jsp?appName="
						+ appName + "&&clusterId=" + clusterId;
			}
		}
		
		function displayClusterEnableButton(clusterId) {
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/queryIfClusterEnableServiceMonitor/" + clusterId,
                success: function (text) {
                	var o = nui.decode(text);
                	var result = o.result;//false 要显示   true 不显示
                	if (result==true) {
                		document.getElementById("enableSingleServiceMonitor").style.display = "none";
                		document.getElementById("disableSingleServiceMonitor").style.display = "block";
                    } else {
                		document.getElementById("enableSingleServiceMonitor").style.display = "block";
                		document.getElementById("disableSingleServiceMonitor").style.display = "none";
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	console.log("error");
                }
            });
        }
		
		//启用该集群监控
		function enableSingleServiceMonitor() {
			nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在启用，请稍后...'});
			var clusterCombox = nui.get("clusterCombox");
			var clusterId = clusterCombox.getValue();
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/enableSingleServiceMonitor/" + clusterId,
	        	async: true,
	        	success: function (text) {
	        		var o = nui.decode(text);
					nui.unmask();
					nui.alert("启用成功！");
					onClusterChanged();
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
					nui.unmask();
	        		console.log("error");
	        	}
	    	});
		}
		
		function disableSingleServiceMonitor() {
			//停止该集群监控
			nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在停用，请稍后...'});
			var clusterCombox = nui.get("clusterCombox");
			var clusterId = clusterCombox.getValue();
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/disableSingleServiceMonitor/" + clusterId,
	        	async: true,
	        	success: function (text) {
	        		var o = nui.decode(text);
					nui.unmask();
					nui.alert("停用成功！");
					onClusterChanged();
	       		},
	        	error: function (jqXHR, textStatus, errorThrown) {
					nui.unmask();
	        		console.log("error");
	        	}
	    	});
		}
    </script>
</body>
</html>