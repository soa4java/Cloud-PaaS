<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="<%=request.getContextPath() %>/common/nui/nui.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath() %>/common/cloud/dictEntry.js" type="text/javascript"></script>
</head>
<body>
	<form id="form1" method="post" action="<%=request.getContextPath() %>/srv/auth/updateUser">
        <!-- <input name="id" class="nui-hidden" /> -->
        <div style="padding-left:11px;padding-bottom:5px;">
            <table style="table-layout:fixed;" border="0">
                <tr>
                    <td style="width:90%;" colspan="2">    
                         <input name="id" id="id" class="nui-hidden" style="width:100%"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:90%;" colspan="2">    
                         	虚拟IP段起止设置（同一网段），请严格按照IPv4标准 (000.000.000.000)
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;" align="right">从：</td>
                    <td style="width:40%;">    
                         <input name="begin" id="begin" class="nui-textbox" required="true" width="60%" />
                    </td>
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">到：</td>
                    <td style="width:40%;">    
                         <input name="end" id="end" class="nui-textbox" required="true" width="60%" />
                    </td> 
                </tr>
                <tr>
                    <td style="width:10%;"  align="right">子网掩码：</td>
                    <td style="width:20%;">    
                         <input name="netmask" id="netmask" class="nui-textbox" required="true" width="40%" vtype="range:0,32" rangeErrorText="数字必须在0~32之间"/> <span style="font-size:5px;">(注：十进制子网掩码 如：255.255.255.0-->24)</span>
                    </td>
                </tr>
            </table>
        </div>
       
        <div style="text-align:center;padding:10px;">               
            <a class="nui-button" onclick="onOk" style="width:60px;margin-right:20px;">确定</a>       
            <a class="nui-button" onclick="onCancel" style="width:60px;">取消</a>       
        </div>        
    </form>

    <script type="text/javascript">     
    	nui.parse();
    	
    	var form = new nui.Form("form1");
    	
    	function onIPSegmentValidate(begin, end) {
	    	var begins= new Array(); 
			var ends=new Array();
			begins = begin.split(".");
			ends = end.split(".");
			if (begins.length != 4 || ends.length != 4) {
				nui.alert("IP段输入有误！");
				return false;
			}
			for (var i=0; i<begins.length-1; i++) {
				if (begins[i] != ends[i]) {
					nui.alert("起始应在同一网段！");
					return false;
				}
			}
			return true;
	    }

    	//标准方法接口定义
        function SetData(data) {
       		data = nui.clone(data);
			var resultData = data.row;
            var form = new nui.Form("form1");
            form.setData(resultData);
            form.setChanged(false);
        }
        
        function saveData() {
        	var begin = nui.get("begin").getValue();
	    	var end = nui.get("end").getValue();
	    	var netmask = nui.get("netmask").getValue();
	    	if (!onIPSegmentValidate(begin, end)) {
				return;
			}
	    	form.validate();
	        if (form.isValid() == false) {
	        	return;
	        }
	    	var data = form.getData(); 
	        var json = nui.encode(data); 
	        form.loading("操作中，请稍后......");
	        $.ajax({
	            url: "<%=request.getContextPath() %>/srv/vipMgr/updateVIPSegment",
	            type: "post",
	            data: { keyData: json },
	            success: function (text) {
	            	var o = nui.decode(text);
	            	if (o.result == true) {
	            		nui.alert("修改虚拟IP段成功！");
	            		CloseWindow("ok");
	            	} else {
	            		nui.alert("修改虚拟IP段失败,请稍后再试！");
	            		CloseWindow("failed");
	            	}
	            },
	            error: function (jqXHR, textStatus, errorThrown) {
	                nui.alert(jqXHR.responseText);
	                CloseWindow("failed");
	            }
	        });
        }
    	
    	function CloseWindow(action) {
	        if (window.CloseOwnerWindow) {
	        	window.CloseOwnerWindow(action);
	        } else {
	        	window.close();
	        }
	    }
	    
	    function onOk(e) {
	        saveData();
	    }
	    
	    function onCancel(e) {
	        CloseWindow("cancel");
	    }
	    
    </script>
</body>
</html>
 