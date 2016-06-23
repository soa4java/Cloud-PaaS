<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%@page import="com.primeton.paas.console.common.SystemVariable"%>
<%@page import="com.primeton.paas.manage.api.cluster.HaProxyCluster"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%
String[] protocals = SystemVariable.getSslProtocalTypes();
request.setAttribute("protocals",protocals);
List<Object> protocalsList = new ArrayList<Object>();
for (String st:protocals) {
	Map<String,String> protocalsMap = new HashMap<String,String>();
	String id = st;
	String text = st;
	if (st.equals(HaProxyCluster.PROTOCOL_HTTPS) 
			|| st.equals(HaProxyCluster.PROTOCOL_HTTP_HTTPS)) {
		text = text + " (单向)";
	}
	if (st.equals(HaProxyCluster.PROTOCOL_MUTUAL_HTTPS) 
			|| st.equals(HaProxyCluster.PROTOCOL_MUTUAL_HTTP_HTTPS)) {
		text = text + " (双向)";
	}
	protocalsMap.put("id", id);
	protocalsMap.put("text", text);
	protocalsList.add(protocalsMap);
}
Object protocalsData = JSONArray.fromObject(protocalsList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>

<body>
     <div id="selForm" style="padding:5px;">
       	<table>
         <tr>
             <td style="width:100px;" align="right">选择目标应用：</td>
             <td colspan="2" style="width:800px;">    
    			<input id="appName" name="appName" class="nui-combobox" url="<%=request.getContextPath() %>/srv/appSetting/listApp" valueField="name" textField="name" valueFromSelect="true" style="width:200px;" onvaluechanged="selValueChanged()" emptyText="请选择" />
             </td>
         </tr>
         <tr>
             <td align="right">客户端访问方式：</td>
             <td>    
    			<input id="protocolId" name="protocolName" class="nui-combobox" required="true" data='<%=protocalsData %>' value="HTTP"  emptyText="请选择..." style="width:200px;" onvaluechanged="selProtocol"/>
             </td>
             <td></td>
         </tr>
         <tr>
             <td  align="right" align="right">已有证书：</td>
             <td colspan="2">    
       			<span id="sslTagInfo">&nbsp;</span>&nbsp;
             </td>
         </tr>
         <tr>
             <td style="width:100%;" colspan="3">
             	 <fieldset style="border:solid 1px #aaa">
			            	<legend >证书配置变更</legend>
			            	<div style="padding:5px;" id="sslFileId">
				            	<table>
						            <tr>
						                <td style="width:120px;" align="right">安全证书：</td>
						                <td style="width:300px;">    
											<input id="uploadFile" name="sslPath" class="nui-textbox" style="width: 200px;" />
											<a id="btn_upload" class="nui-button" onclick="uploadFile" style="width:60px;margin-right:20px;">上传</a>       
						                </td>
						                <td style="width:500px;">SSL安全证书压缩包(修改生效后，上传的证书包将覆盖原有证书包)。</td>
						            </tr>
						            <tr>
							             <td  align="right">证书深度：</td>
							             <td colspan="1">    
											<input id="sslLevel" name="sslLevel" class="nui-combobox" style="width:200px;" textField="text" valueField="id"  
											    data="CLD_SslLevel" value="1"  allowInput="true"/>  
							             </td>
							             <td></td>
         							</tr>
						      </table>
					      </div>
			      </fieldset>
             </td>
         </tr>
         <tr>
             <td style="width:100%;" colspan="3" align="center">
             	<br>
                 <a class="nui-button" iconCls="icon-edit" onclick="updateAppConfig()">修改</a>       
             </td>
         </tr>
   		</table>
   	</div>
    
    <script type="text/javascript">
	    nui.parse();
	    
        $(document).ready(function() {
            //nui.getbyName("appName").select(0);
            //selProtocol();
		});
        
        function selValueChanged(){
        	var appName = nui.getbyName("appName").getValue();
        	
          	 nui.mask({
                 el: document.body,
                 cls: 'mini-mask-loading',
                 html: 'Loading...'
             });
        	
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/appCert/queryAppConfig/" +appName,
                cache: false,
                success: function (text) {     
                    	var o = nui.decode(text);
                    	if (null != o  && null != o.result) {
                    		var appName = o.result.appName;
                    		var protocolType = o.result.protocolType;
                    		var sslLevel = o.result.sslLevel;
                    		sslTag = o.result.sslTag;
                    		nui.get("protocolId").setValue(protocolType);
                    		nui.get("sslLevel").setValue(sslLevel);
        		       		var sslTagValue = "";
        		       		if (sslTag == 0) {
        		       			sslTagValue = "否";
        		       		} else if (sslTag == 2) {
        		       			sslTagValue = "是(HTTPS/HTTP-HTTPS)";
        		       		} else if (sslTag == 3) {
        		       			sslTagValue = "是(MUTUAL-HTTPS/MUTUAL-HTTP-HTTPS)";
        		       		}
        		       		document.getElementById("sslTagInfo").innerHTML = sslTagValue;
        		       		
        		       		selProtocol();
                    	} else {
                    		nui.alert("应用无法识别!");
                    	}
                    	nui.unmask(document.body);
                }
            });
        }
        
        function uploadFile(){
	        nui.open({
	            url: "<%=request.getContextPath() %>/app/open/application/uploadFile.jsp",
	            title: "上传SSL安全证书压缩包", width: 650, height: 180,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                var data = { action: "details"};
	                iframe.contentWindow.SetData(data);
	                
	            },
	            ondestroy: function (action) {
	            	nui.get("uploadFile").setValue(action);
	            }
	        });
       }

       function updateSsl() {
           //提交表单数据
           var form = new nui.Form("#selForm");            
           var data = form.getData();      //获取表单多个控件的数据
           var json = nui.encode(data);   
	       nui.mask({
	                el: document.body,
	                cls: 'mini-mask-loading',
	                html: 'Loading...'
	       });
	       $.ajax({
	       	    url: "<%=request.getContextPath() %>/srv/appCert/updateAppConfig",
	       	    type: "post",
	       	    data: json ,
	            dataType : 'json',
	            contentType:'application/json',
	       	    success: function (text) {
	                if (text) {
	                    nui.alert("修改成功！");
	                 } else {
	                    nui.alert("修改失败，请稍后重试！");
	                 }
	                nui.unmask(document.body);
	       	    }
	       	});
       }
       
       
       /* Sava Cert setting */
       var sslTag = "";
       
       function updateAppConfig() {
          var appName = nui.getbyName("appName").getValue();
          var protocolName = nui.getbyName("protocolName").getValue();
          var sslPath = nui.getbyName("sslPath").getValue();
          if (appName == null || appName == "") {
             nui.alert("请选择目标应用！");
             return false;
          }
          if (protocolName == "HTTPS" || protocolName == "HTTP-HTTPS") {
          	  if (sslPath =="" && sslTag == 0){
          		 nui.alert("请上传证书！");
                 return false;
          	  }
          }
          if (protocolName == "MUTUAL-HTTPS" || protocolName == "MUTUAL-HTTP-HTTPS") {
             if (sslPath == "" && sslTag == 0){
          		 nui.alert("请上传证书!");
                 return false;
          	  }
          	  if(sslPath =="" && sslTag != 3){
          		  nui.alert("缺少证书，请上传证书!");
                 return false;
          	  }
          }
          var ok = confirm("你确定要修改？");
          if (ok) {
        	  updateSsl();
          }
       }
       
       function selProtocol() {
    	   var value = nui.get("protocolId").getValue();
    	   if (value == 'HTTPS' || value == 'HTTP-HTTPS' 
    			   || value == 'MUTUAL-HTTPS' || value == 'MUTUAL-HTTP-HTTPS' ) {
    		   document.getElementById("sslFileId").disabled = false;
    		   nui.get("btn_upload").setEnabled(true);
    		  // nui.get("uploadFile").setRequired(true);
    	   } else {
    		   document.getElementById("sslFileId").disabled = true;
    		   nui.get("btn_upload").setEnabled(false);
    		   //  nui.get("uploadFile").setRequired(false);
    		   isSSl = false;
    	   }
       }
    </script>
</body>
</html>