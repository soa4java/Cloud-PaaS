<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <link href="../../common/nui//themes/blue/skin.css" rel="stylesheet" type="text/css" />
</head>
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
<body>
	<form id="form1" method="post" style="font-size:12px; "  action="<%=request.getContextPath() %>/srv/appDeploy/deploy" enctype="multipart/form-data"> 
       <fieldset style="border:solid 1px #aaa;padding:3px;">
            <legend >部署应用</legend>
        <div style="padding:5px;">
	        <table>
	            <tr>
	                <td style="width:100px;">上传应用包:</td>
	                <td style="width:300px;">    
						<input id="uploadFile" name="uploadFile" type="file" style="width: 312px;"/>
	                </td>
	                <td><font color="red">	* 注：上传war格式的应用包。</font></td>
	            </tr>
	            <tr>
	                <td style="width:100px;">目标应用:</td>
	                <td style="width:300px;">    
							<input id="selAppList" name="selAppList" class="nui-combobox" url="<%=request.getContextPath() %>/srv/appSetting/listApp" valueField="name" textField="name" valueFromSelect="true" emptyText="请选择" nullItemText="请选择"  onvaluechanged="selValueChanged()"/>
	                </td>
	                <td></td>
	            </tr>
	            <tr>
	                <td style="width:100px;">&nbsp;</td>
	                <td style="width:600px;" colspan="2">    
							<fieldset style="border:solid 1px #aaa;padding:3px;">
								<legend >应用版本信息</legend>
								 <table>
						            <tr>
						                <td style="width:100px;">
						                	<input id="dfRadWay" name="radWay" value="D" type="radio" checked="checked" onclick="onchangeCoverVs()"/>
						                	默认方式:
						                </td>
						                <td style="width:400px;">
						                 	<font color="red">	* 注：当已经存在应用时覆盖最新的版本，不存在则创建新版本。</font>
						                </td>
						            </tr>
						            <tr>
						                <td style="width:100px;">
						               	  <input id="coverVsTag" name="radWay" value="O" type="radio" onclick="onchangeCoverVs(this)" onblur="onchangeCoverVs()"/>
						                	覆盖已有版本:
						                </td>
						                <td style="width:300px;"> 
						                   <fieldset style="border:solid 1px #aaa;padding:10px;" id="fsVsList" disabled = 'true'>
						                	</fieldset>
						                </td>
						            </tr>
						            <tr>
						                <td style="width:100px;">
						                	<input name="radWay" value="N" type="radio" onclick="onchangeCoverVs()"/>
						                		创建新版本:
						                </td>
						                <td style="width:300px;"></td>
						            </tr>
						            <tr>
						                <td style="width:100px;">
						                	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;描述:
						                </td>
						                <td style="width:300px;">
						                	<input name="des" class="nui-textarea"  style="width:400px;height:50px;"/>
						                </td>
						            </tr>
	            				</table>
							</fieldset>
	                </td>
	            </tr>
	        </table>            
	   </div>
	   <input id="dTag" name="deployTag" type="hidden">
	 </fieldset>
	 
	  <span id="showText"></span>
	   	 
	  <div style="text-align:center;padding:10px;">               
           <a class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">部署</a>            
           <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
       </div>   
	</form>

    <script type="text/javascript">
    // parse
    nui.parse();
	
    function submitForm(){
    	 if (uploadValidate()) {
	    	 var tagTime = new Date().getTime();   
	    	 var appName =  nui.get("selAppList").getValue()
	    	 tagTime = appName + tagTime;
	    	 document.getElementById("dTag").value = tagTime;
           	 nui.mask({
                 el: document.body,
                 cls: 'mini-mask-loading',
                 html: '正在部署，请稍后...'
             });
	    	document.getElementById("form1").submit();  
	    	window.setTimeout("deployMsg()",5000); 
    	 }
    }
    
    function deployMsg(){
    	tagTime = document.getElementById("dTag").value;
        $.ajax({
            url: "<%=request.getContextPath() %>/srv/appDeploy/getDeployMsg/" + tagTime,
            cache: false,
            success: function (text) {     
                	if (text) {
                		nui.alert('部署成功!');
			       		nui.unmask(document.body);
		    			CloseWindow("cancel");
                	} else {
                		nui.alert('部署失败!');
                		nui.unmask(document.body);
                	}
            }
        });
    }

    $(document).ready(function() {
       // nui.getbyName("selAppList").select(0);
       // selValueChanged();
        
	});
    
    function onCancel(e) {
        CloseWindow("cancel");
    }
    
    function CloseWindow(action) {            
        if (action == "close" && form.isChanged()) {
            if (confirm("数据被修改了，是否先保存？")) {
                return false;
            }
        }
        if (window.CloseOwnerWindow) {
        	return window.CloseOwnerWindow(action);
        } else {
        	window.close();            
        }
    }
    
    function selValueChanged(){
    	var appName =  nui.get("selAppList").getValue();
    	if (appName != null && appName != '') {
	        $.ajax({
	            url: "<%=request.getContextPath() %>/srv/appDeploy/listAppVersion/" + appName,
	            cache: false,
	            success: function (text) {     
	            	document.getElementById("dfRadWay").checked = true;
	            	document.getElementById("fsVsList").disabled = true;
	            	document.getElementById("coverVsTag").disabled = false;
	            	var v = nui.decode(text);
	            	var o = v.data;
	            	appHtml = "";
	            	if (null != o) {
	            		if (o.length ==0 ) {
	            			document.getElementById("coverVsTag").disabled = true;
	            		}
                    	for (i =0 ; i < o.length ; i++) {
                    		obj = o[i];
                    		appHtmlHead = '';
                    		if (i==0) {
                    			appHtmlHead  = '<input name="vsName" value="'+ obj.warVersion + '" type="radio" ';
                    		} else {
                    			if ( i%5 !=1 || i== 1) {
                    				appHtmlHead = '&nbsp;&nbsp;&nbsp;&nbsp;';
                    			}
                    			appHtmlHead  += '<input name="vsName" value="'+ obj.warVersion + '" type="radio" ';
                    		}
                    		appHtmlHeadBody = '';
                    		if (i==0) {
                    			appHtmlHeadBody  = ' checked="checked" ';
                    		}
                    		appHtmlHeadEnd = ' />' + obj.warVersion;
                    		if (i!=0 && i%5==0) {
                    			appHtmlHeadEnd += '<br>';
                    		}
                    		
                    		appHtml += appHtmlHead + appHtmlHeadBody + appHtmlHeadEnd;
                    	}
	            	}
	            	document.getElementById("fsVsList").innerHTML  = appHtml;
	            }
	        });
    	}
    }
    
    function onchangeCoverVs(){
    	var fsVsList = document.getElementById("fsVsList");
    	var coverVsTag = document.getElementById("coverVsTag");
    	if (coverVsTag.checked) {
    		fsVsList.disabled = false;
    	} else {
    		fsVsList.disabled = true;
    	}
    }
    
    function uploadValidate() {
    	   var file = document.getElementById("uploadFile").value;
    	   if (file == null || file.length == 0) {
    	      nui.alert('请选择war或者ear格式的应用包！');
    	      return false;
    	   }
    	   
    	   var selValue = nui.get("selAppList").getValue();
    	   if (selValue == null || selValue=='') {
     	      nui.alert('请选择目标应用！');
     	      return false;
     	   }
    	   
    	   var index = file.lastIndexOf(".")+1;
    	   var suffixName = file.substring(index).toLowerCase();
    	   if (suffixName != "war" && suffixName != "ear") {
    	      nui.alert('应用包格式不正确，只允许上传 war 或 ear 格式的应用包！');
    	      return false;
    	   }
    	   return true;
    }
	</script>
	
</body>
</html>
