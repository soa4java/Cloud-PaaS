<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
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
</head>
<body>
  <form id="form1" method="post" style="font-size:12px; "  action="<%=request.getContextPath() %>/srv/myApp/uploadFile" enctype="multipart/form-data"> 
    		<fieldset style="border:solid 1px #aaa">
	            	<legend >上传证书</legend>
	            	<table>
			            <tr>
			                <td style="width:150px;">SSL安全证书压缩包(zip)：</td>
			                <td style="width:300px;">    
			                	<input id="uploadFile" name="uploadFile" type="file" style="width: 312px;"/>
			                </td>
			                <td><span id="msg"></span></td>
			            </tr>
			            <tr>
			            	<td colspan="3">
			            	    <span style="margin-bottom: 15px;">
							        <span style="margin-left: 15px; margin-bottom: 15px;color:#FF0000;"> 
							        	证书命名规范，单向：server.crt | server.key ； 双向：server.crt | server.key | ca.crt 
							       	</span>
						       </span>
			            	</td>
			            </tr>
			      </table>
	      </fieldset>
	      
	  <input id="dTag" name="deployTag" type="hidden">
	   	 
	  <div style="text-align:center;padding:10px;">               
           <a id="bt_upload" class="nui-button" onclick="submitForm" style="width:60px;margin-right:20px;">上传</a>      
           <a class="nui-button" onclick="rtnForm" style="width:60px;margin-right:20px;">确定</a>                  
           <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
       </div>   
  </form>

</body>
    <script type="text/javascript">
    nui.parse();
    
    var isUpload = false;
    var uploadPath ;
	
    function submitForm() {
    	 if(uploadValidate()){
	    	 var tagTime = new Date().getTime();   
	    	 document.getElementById("dTag").value = tagTime;
           	 nui.mask({
                 el: document.body,
                 cls: 'mini-mask-loading',
                 html: '正在上传，请稍后...'
             });
	    	document.getElementById("form1").submit();  
	    	window.setTimeout("deployMsg()",5000); 
    	 }
    }
    
    function deployMsg() {
    	tagTime = document.getElementById("dTag").value;
        $.ajax({
            url: "<%=request.getContextPath() %>/srv/myApp/getDeployMsg/" +tagTime,
            cache: false,
            success: function (text) {     
                	var o = nui.decode(text);
                	if (null != o && null != o.result && o.result.split(',')[0]=='S') {
                		uploadPath = o.result.split(',')[1];
                		nui.alert('上传成功!');
                		document.getElementById("msg").value = "上传成功!";
                		nui.get("bt_upload").setEnabled(false);
                		isUpload = true;
                	} else {
                		nui.alert('上传失败!');
                	}
		       		 nui.unmask(document.body);
	    			// CloseWindow("cancel");
            }
        });
    }
    
    function rtnForm() {
    	if (!isUpload) {
    		nui.alert('请先上传证书压缩包!');
    	} else {
    		CloseWindow(uploadPath);
    	}
    }
    

    
    function onCancel(e) {
        CloseWindow("");
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
    

    
    function uploadValidate() {
    	   var file = document.getElementById("uploadFile").value;
    	   if (file == null || file.length==0) {
    	      nui.alert('请选择zip格式的应用包！');
    	      return false;
    	   }
    	   var index = file.lastIndexOf(".")+1;
    	   var suffixName = file.substring(index).toLowerCase();
    	   if (suffixName != "zip") {
    	      nui.alert('应用包格式不正确，只允许上传 zip 格式的应用包！');
    	      return false;
    	   }
    	   return true;
    }
</script>
</html>
