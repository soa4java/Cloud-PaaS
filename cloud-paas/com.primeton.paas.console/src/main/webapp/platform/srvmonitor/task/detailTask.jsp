<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="../../../common/nui/nui.js" type="text/javascript"></script>
    <script src="../../../common/cloud/dictEntry.js" type="text/javascript"></script>
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
    <fieldset style="width:97%;border:solid 1px #aaa;margin-top:8px;position:relative;">
        <legend>长任务详情</legend>
        <form id="form1" method="post" action="<%=request.getContextPath() %>/srv/monitor/addVar">
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:100px;" align="right">任务号：</td>
                    <td style="width:700px;">    
                         <input name="id" id="id" class="nui-textbox asLabel" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">任务状态：</td>
                    <td style="width:700px;">    
                         <input name="status" id="status" class="nui-combobox asLabel" data="CLD_TaskStatus" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">任务类型：</td>
                    <td style="width:700px;">    
                         <input name="type" id="type" class="nui-combobox asLabel" data="CLD_TaskType" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">创建者：</td>
                    <td style="width:700px;">    
                         <input name="owner" id="owner" class="nui-textbox asLabel" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">时间限制：</td>
                    <td style="width:700px;">    
                         <input name="timeout" id="timeout" class="nui-textbox asLabel" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">创建时间：</td>
                    <td style="width:700px;">    
                         <input name="startTime" id="startTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">超时时间：</td>
                    <td style="width:700px;">    
                         <input name="finalTime" id="finalTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">结束时间：</td>
                    <td style="width:700px;">    
                         <input name="finishTime" id="finishTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">异常时间：</td>
                    <td style="width:700px;">    
                         <input name="exceptionTime" id="exceptionTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">强制终止时间：</td>
                    <td style="width:700px;">    
                         <input name="abortTime" id="abortTime" class="nui-datepicker asLabel" format="yyyy-MM-dd HH:mm:ss" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">任务处理结果：</td>
                    <td style="width:700px;">    
                         <input name="handleResult" id="handleResult" class="nui-textbox asLabel" width="100%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:100px;" align="right">异常信息：</td>
                    <td style="width:700px;">    
                         <input name="exception" id="exception" class="nui-textarea asLabel" width="100%" />
                    </td>
                </tr>
            </table>
        </div>
        <div style="text-align:left;padding:10px;">               
<!--             <input id="abortButton" type="button" onclick="onAbort()" value="终止"/>      -->
<!--             <input type="button" onclick="onCancel()" value="返回"/>      -->
            
            <a id="abortButton" class="nui-button" style="width:8%; " onclick="onAbort">终止</a>&nbsp;           
        	<a id="cancelButton" class="nui-button" style="width:8%; " onclick="onCancel">取消</a>   
            <!-- 
            <a id="abortButton" name="abortButton" class="nui-button" onclick="onAbort" style="width:60px;margin-right:5px;">终止</a>  
            <a class="nui-button" onclick="onCancel" style="width:60px;">返回</a>        -->
        </div>        
    </form>
    </fieldset>
    

    <script type="text/javascript">        
    	nui.parse();
    	var form = new nui.Form("form1");

    	function CloseWindow(action) {
            if (window.CloseOwnerWindow) {
            	window.CloseOwnerWindow(action);
            } else {
            	window.close();
            }
        }
        
        function SetData(data) {
       		data = nui.clone(data);
			var resultData = data.row;
            var form = new nui.Form("form1");
            //显示时间
            if (resultData.startTime) {
	            resultData.startTime = new Date(resultData.startTime);
            }
            if (resultData.finishTime) {
            	resultData.finishTime = new Date(resultData.finishTime);
            }
            if (resultData.finalTime) {
            	resultData.finalTime = new Date(resultData.finalTime);
            }
            if (resultData.exceptionTime) {
            	resultData.exceptionTime = new Date(resultData.exceptionTime);
            }
            if (resultData.abortTime) {
            	resultData.abortTime = new Date(resultData.abortTime);
            }
            if (resultData.status != '1') {
            	//已完成或异常，不可终止
            	nui.get("abortButton").setEnabled(false);
            }
            labelModel();
            form.setData(resultData);
            form.setChanged(false);
        }
        
        function finshTask() {
        	CloseWindow("ok");
        }
        
        function onAbort(e) {
        	var data = form.getData();
        	var taskId = data.id;
            form.loading("操作中，请稍后......");
            $.ajax({
                url: "<%=request.getContextPath() %>/srv/monitor/abortTask/" + taskId,
                success: function (text) {
                	form.unmask();
                	var o = nui.decode(text);
                	if (o.result == true) {
                		nui.alert("长任务已终止！");
                		CloseWindow("ok");
                	} else {
                		nui.alert("操作失败,请稍后再试！");
                		CloseWindow("failed");
                	}
                },
                error: function (jqXHR, textStatus, errorThrown) {
                	form.unmask();
                    nui.alert(jqXHR.responseText);
                    CloseWindow("failed");
                }
            });
        }
        
        function onCancel(e) {
            CloseWindow("cancel");
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