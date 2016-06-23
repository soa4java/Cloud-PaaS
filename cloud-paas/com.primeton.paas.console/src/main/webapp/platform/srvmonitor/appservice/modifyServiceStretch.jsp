<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.primeton.paas.manage.api.app.StretchStrategy" %>
<%@page import="com.primeton.paas.console.common.stretch.StrategyItemInfo" %>
<%@page import="com.primeton.paas.console.platform.service.monitor.TelescopicStrategyUtil" %>

<%
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
    <style type="text/css">
    .asLabel .nui-textbox-border,
    .asLabel .nui-textbox-input,
    .asLabel .nui-buttonedit-border,
    .asLabel .nui-buttonedit-input,
    .asLabel .nui-textboxlist-border
    {
        border:0;background:none;cursor:default;
    }
    .asLabel .nui-buttonedit-button,
    .asLabel .nui-textboxlist-close
    {
        display:none;
    }
    .asLabel .nui-textboxlist-item
    {
        padding-right:8px;
    }    
 </style>
    <script type="text/javascript">
// 		var StretchSizeComboBoxData = [{'id':'1','text':'1台'},{'id':'2','text':'2台'},{'id':'3','text':'3台'},{'id':'4','text':'4台'}];
<%-- 		var StretchSizeComboBoxData = <%=stretchSizes%>; --%>
    	var ContinuedTimeComboBoxData = <%=continueTimeSizes%>;
    	var IgnoreTimeComboBoxData = <%=ignorTimeSizes%>;
		var CpuThresholdComboBoxData = <%=cpuSizes%>;
		var MemThresholdComboBoxData = <%=memSizes%>;
		var LbThresholdComboBoxData = <%=lbSizes%>;
		var ReminderTypeComboBoxData = [{'id':'SMS','text':'短信'},{'id':'MAIL','text':'邮件'},{'id':'SMS,MAIL','text':'短信+邮件'}];
	</script>
</head>
<body>     
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/service/getHaproxyClusterDetail">
       	<fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >服务集群基本信息</legend>
            <div style="padding:5px;">
		        <table style="width:100%">
		        	<tr>
	                    <td style="width:10%;"  align="right">关联应用：</td>
	                    <td style="width:80%;">    
	                         <input name="appName" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>    
		            <tr>
	                    <td style="width:20%;" align="right">集群标识：</td>
	                    <td style="width:80%;">    
	                         <input name="id" id="id" class="nui-textbox asLabel" width="100%"/>
	                    </td>
	                </tr>
	                 
	                <tr>
	                    <td style="width:10%;"  align="right">显示名称：</td>
	                    <td style="width:80%;">    
	                         <input name="name" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">集群类型：</td>
	                    <td style="width:80%;">    
	                         <input name="type" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                <tr>
	                    <td style="width:10%;"  align="right">所有者：</td>
	                    <td style="width:80%;">    
	                         <input name="owner" class="nui-textbox asLabel" width="100%"/>
	                    </td>
                	</tr>      
	                  
		        </table>            
            </div>
        </fieldset>
    </form>
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
    <div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
		<input type="radio"  name="serviceStrategyType" value="service" onclick="onStrategyTypeChange(this)"/>个性化伸缩策略配置
	</div>
    <div class="nui-fit" id="serviceDiv" style="width:100%">
    	<div id="servicegrid" class="nui-datagrid" style="width:100%;height:90px;" allowResize="false" allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
        editNextOnEnterKey="true"  editNextRowCell="true"
        	idField="id" url="<%=request.getContextPath() %>/srv/monitor/getServiceStrategyConfigInfo/<%=clusterId %>" showPager="false">
        	<div property="columns">
	            <div field="enableFlag" type="checkboxcolumn" trueValue="Y" falseValue="N" width="60" headerAlign="center">是否启用</div>
				<!-- 	            
				<div field="strategyType" width="80" headerAlign="center" align ="center" renderer="onStrategyTypeRenderer">报警方式</div> 
				-->
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
    <div id="forEdit" class="nui-toolbar" style="padding:2px;border:0;display:block">
		<input type="radio" name="serviceStrategyType" value="global" onclick="onStrategyTypeChange(this)"/>全局伸缩策略配置
	</div>
    <div class="nui-fit" id="globalDiv" style="width:100%">
		<div id="globalgrid" class="nui-datagrid" style="width:100%;height:90px;" allowResize="false" allowCellEdit="false" allowCellSelect="true" multiSelect="true" 
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
 		        	    	
    <script type="text/javascript">        
        nui.parse();
        
        var servicegrid = nui.get("servicegrid");
        var globalgrid = nui.get("globalgrid");
        var form = new nui.Form("form1");
        
        var changeFlag = -1;
        
		// servicegrid.load();
        globalgrid.load();
        var radios = document.getElementsByName("serviceStrategyType");
        checkRadioBox();

		//勾选配置类型 个性化配置 or 全局配置
        function checkRadioBox(){
        	$.ajax({
				url: "<%=request.getContextPath() %>/srv/monitor/getServiceStrategyType/" + <%=clusterId %>,
                success: function (text) {
                	var o = nui.decode(text);
                	if (o.result) {//true 全局配置
                		radios[1].checked = true;
                		servicegrid.allowCellEdit = false;
                	} else {
                		radios[0].checked = true;
                		servicegrid.allowCellEdit = true;
                	}
                	servicegrid.load({clusterId : <%=clusterId %>});
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    //nui.alert(jqXHR.responseText);
                }
            });
	    } 
		
        function onStrategyTypeChange(obj) {
        	changeFlag++;
    		if (obj.value == "service") {
    			servicegrid.allowCellEdit = true;
    		} else {
    			servicegrid.allowCellEdit = false;
    		}
    	}
        
		function SetData(data) {
			data = nui.clone(data);
			var clusterId = data.clusterId;
			var appName = data.appName;
			if (appName == null || appName == 'null') {
             	appName = "所有应用";
            }
			
        	form.loading("操作中，请稍后......");
        	$.ajax({
                url: "<%=request.getContextPath() %>/srv/service/getCluster/" + clusterId,
                success: function (text) {
                	form.unmask();
                    var o = nui.decode(text);
                    o.data.type = o.data.type + " 集群";//for display
                    o.data.appName = appName;
                    labelModel();
                    form.setData(o.data);
                    form.setChanged(false);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	form.unmask();
                    nui.alert(jqXHR.responseText);
                 }
            });
		}
		
		function validateMail(email) {
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
			if (emails=="") {
				return true;//不填，使用管理员默认邮箱
			}
			var reg = new RegExp("^[a-zA-Z][a-zA-Z0-9_-]{2,20}@[a-zA-Z0-9.]{3,20}$");
			var mails = emails.split(",");
			for (var i=0; i<mails.length; i++){
				if (!reg.test(mails[i])){
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
		
		function save(){
			if (servicegrid.allowCellEdit) {
				if (servicegrid.isChanged() || changeFlag%2 == 0) {
					changeFlag=-1;
	    			var formData = form.getData();
	    			var clusterId = formData.id;
	    			
	        		//save
	        		var data = servicegrid.getData();
	    			if (data[0].alarmType == "" || data[0].continuedTime == ""
	    					|| data[0].ignoreTime == "" || data[0].cpuThreshold == ""
	    					|| data[0].memThreshold =="" || data[0].lbThreshold == "") {
	    				nui.alert("数据不完整无法保存！请检查！");
	    				return;
	    			}
	    			if (data[0].alarmType == "MAIL" && !validateMails(data[0].alarmAddress)) {
	    				nui.alert("通讯地址-邮箱格式不正确！");
	    				return;
	    			}
	    			if (data[0].alarmType=="SMS" && !validatePhones(data[0].alarmAddress)) {
	    				nui.alert("通讯地址-手机号格式不正确！");
	    				return;
	    			}
	    			if (data[0].alarmType == "SMS,MAIL" &&! validatePhoneAndMail(data[0].alarmAddress)){
	    				nui.alert("通讯地址格式:phoneNumber1,phoneNumber2...;mail1,mail2...");
	    				return;
	    			}
	    			
	                var json = nui.encode(data)
	                
	                //保存服务报警策略信息
	                servicegrid.loading("保存服务报警策略配置...");
	    			$.ajax({
	    				url: "<%=request.getContextPath() %>/srv/monitor/updateServiceWarnStrategy",
	    				async: true,
	                    data: { keyData:json, clusterId:clusterId},
	                    type: "post",
	                    success: function (text) {
	                    	var o = nui.decode(text);
	                    	if (o.result == true) { 
	                    		nui.alert("保存成功");
	                    		servicegrid.reload();
	                    	} else {
	                    		nui.alert("保存失败");
	                    		servicegrid.unmask();
	                    	}
	                    },
	                    error: function (jqXHR, textStatus, errorThrown) {
	                    	servicegrid.unmask();
	                        nui.alert(jqXHR.responseText);
	                    }
	                });
	    		} else {
	    			nui.alert("未做改动！");
	    		}
			} else {
				changeFlag=-1;
				var formData = form.getData();
    			var clusterId = formData.id;
    			nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在设置，请稍后...'});
    			$.ajax({
    				url: "<%=request.getContextPath() %>/srv/monitor/setServiceGlobalStretchStrategy/"+clusterId,
                    success: function (text) {
                    	nui.unmask();
                    	var o = nui.decode(text);
                    	if (o.result == true) { 
                    		nui.alert("保存配置成功！");
                    	} else {
                    		nui.alert("保存配置异常！");
                    	}
                    	reset();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    	nui.unmask();
                        nui.alert(jqXHR.responseText);
                    }
                });
			}
    	}
		
    	function reset(){
    		checkRadioBox();
    		servicegrid.reload();
    	}
    	
    	function labelModel() {
            var fields = form.getFields();                
            for (var i = 0, l = fields.length; i < l; i++) {
                var c = fields[i];
                if (c.setReadOnly) {
                	c.setReadOnly(true);     //只读
                }
                if (c.setIsValid) {
                	c.setIsValid(true);      //去除错误提示
                }
            }
        }
    </script>
</body>
</html>