<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
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
	<form id="form1" method="post" style="display:none">
		<fieldset id="dataFieldset" style="border:solid 1px #aaa;padding:3px;" >
			<legend >Haproxy服务配置详情</legend>	      
				<input name="id" class="nui-hidden" />
				        <div style="padding-left:11px;padding-bottom:5px;">
				            <table style="table-layout:fixed;">
				                <tr>
				                    <td ><font size="-1">是否主备：</font></td>
				                    <td >    
				                        <input name="attributes.isBackup" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>
				               
				                <tr>
				                    <td ><font size="-1">服务名称：</font></td>
				                    <td >    
				                        <input name="name" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>       
				                
				         		<tr>
				                    <td ><font size="-1">集群实例数：</font></td>
				                    <td >    
				                        <input name="attributes.size" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr> 
				                
				                <tr>
				                    <td ><font size="-1">主机类型：</font></td>
				                    <td >    
				                        <input name="attributes.hostType" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>   
 
				                <tr>
				                    <td ><font size="-1">健康检查页面 ：</font></td>
				                    <td >    
				                        <input name="attributes.healthUrl" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>     
				                
				          		<tr>
				                    <td ><font size="-1">访问路径：</font></td>
				                    <td >    
				                        <input name="attributes.accessUrl" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>     
				                
				         		<tr>
				                    <td ><font size="-1">协议类型：</font></td>
				                    <td >    
				                        <input name="attributes.protocal" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>   
				                
				         		<tr>
				                    <td ><font size="-1">负载均衡策略：</font></td>
				                    <td >    
				                        <input name="attributes.balance" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>   
				                				                
				         		<tr>
				                    <td ><font size="-1">最大连接数：</font></td>
				                    <td >    
				                        <input name="attributes.maxConnSize" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>   				                
				         		<tr>
				                    <td ><font size="-1">连接超时时间：</font></td>
				                    <td >    
				                        <input name="attributes.connTimeout" class="nui-textbox asLabel" style="width:200px;" />   
				                    </td>
				                </tr>   
				                				                
				         		<tr>
				                    <td ><font size="-1">已绑定外部应用：</font></td>
				                    <td >    
				                        <input name="attributes.appName" class="nui-textbox asLabel" style="width:200px;" />  
				                    </td>
				                </tr>   		
				                		                
					         	<tr>
				                    <td ><font size="-1">集群成员列表：</font></td>
				                    <td >    
				                        <span id="infoUrl"></span> 
				                    </td>
				                </tr> 			                 
				            </table>
			</div>     
		</fieldset>
	</form>

    <script type="text/javascript">
	    // parse DOM
    	nui.parse();
	    
	    var form = new nui.Form("form1");
	    
	    var clusterId ;
	    
	    function SetData(data) {
	        if (data.action == "details") {
	        	clusterId = data.id;
	            $.ajax({
	                url: "<%=request.getContextPath() %>/srv/myService/getOuterHaproxyDetail/" + clusterId,
	                async: false,
	                cache: false,
	                success: function (text) {
	                	var o = nui.decode(text);
	                	var info = o.attributes.members;
	                	document.getElementById("infoUrl").innerHTML = info;
                        form.setData(o);
                        form.setChanged(false);
                        document.getElementById("form1").style.display="block";
		            }
		        });
	        }
	    }
    </script>
</body>
</html>