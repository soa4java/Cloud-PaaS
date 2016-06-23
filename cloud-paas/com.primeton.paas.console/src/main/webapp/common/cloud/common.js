/**
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 * 
 * @version 1.0.0
 *
 */

// Deploy as default webapp
var contextPath = "";


/**
 * 启动服务集群. <br>
 * 
 * @param clusterId 标识
 * @param type 类型
 * @param state 状态
 */
function startCluster(clusterId, type, state) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	if (state == 1) {
		nui.alert(type + "[" + clusterId + "] 已是启动状态!", "系统提示");
		return;
	}
	nui.confirm("你确定要启动 " + type + " [" + clusterId + "] 集群 ?", "系统提示", function(action) {
		if (action == "ok") {
			doStartCluster(clusterId, type);
		}
	});
}

/**
 * 启动集群. <br>
 * 
 * @param clusterId
 * @param type
 */
function doStartCluster(clusterId, type) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	nui.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '正在启动, 请稍后 ...'
	});
	$.ajax({
		url : contextPath + "/srv/service/startCluster",
		data : {
			clusterId : clusterId,
			type : type
		},
		type : "post",
		success : function(text) {
			nui.unmask();
			var o = nui.decode(text);
			if (o.result) {
				nui.alert("启动命令已发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
			} else {
				nui.alert("启动失败, 请稍后重试!", "系统提示");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			nui.unmask();
			nui.alert("系统错误, 请稍后重试！<br/>" + jqXHR.responseText);
		}
	});
}

/**
 * 关闭服务集群. <br>
 * 
 * @param clusterId 标识
 * @param type 类型
 * @param state 状态
 */
function stopCluster(clusterId, type, state) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	if (state == 0) {
		nui.alert(type + "[" + clusterId + "] 已是停止状态!", "系统提示");
		return;
	}
	nui.confirm("你确定要关闭 " + type + " [" + clusterId + "] 集群 ?", "系统提示", function(action) {
		if (action == "ok") {
			doStopCluster(clusterId, type);
		}
	});
}

/**
 * 关闭服务集群. <br>
 * 
 * @param clusterId 标识
 * @param type 类型
 */
function doStopCluster(clusterId, type) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	nui.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '正在关闭, 请稍后 ...'
	});
	$.ajax({
		url : contextPath + "/srv/service/stopCluster",
		data : {
			clusterId : clusterId,
			type : type
		},
		type : "post",
		success : function(text) {
			nui.unmask();
			var o = nui.decode(text);
			if (o.result) {
				nui.alert("关闭命令已发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
			} else {
				nui.alert("关闭失败, 请稍后重试!", "系统提示");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			nui.unmask();
			nui.alert("系统错误, 请稍后重试！<br/>" + jqXHR.responseText);
		}
	});
}


/**
 * 重新启动服务集群. <br>
 * 
 * @param clusterId 标识
 * @param type 类型
 * @param state 状态
 */
function restartCluster(clusterId, type, state) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	nui.confirm("你确定要重启 " + type + " [" + clusterId + "] 集群 ?", "系统提示", function(action) {
		if (action == "ok") {
			doRestartCluster(clusterId, type);
		}
	});
}


/**
 * 重新启动服务集群. <br>
 * 
 * @param clusterId 标识
 * @param type 类型
 */
function doRestartCluster(clusterId, type) {
	if (null == clusterId) {
		nui.alert("集群标识为空!", "系统提示");
		return;
	}
	nui.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '正在重启, 请稍后 ...'
	});
	$.ajax({
		url : contextPath + "/srv/service/restartCluster",
		data : {
			clusterId : clusterId,
			type : type
		},
		type : "post",
		success : function(text) {
			nui.unmask();
			var o = nui.decode(text);
			if (o.result) {
				nui.alert("重启命令已发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
			} else {
				nui.alert("重启失败, 请稍后重试!", "系统提示");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			nui.unmask();
			nui.alert("系统错误, 请稍后重试！<br/>" + jqXHR.responseText);
		}
	});
}


/**
 * 启动服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 * @param state 状态
 */
function startService(serviceId, type, state) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	if (state == 1) {
		nui.alert(type + " [" + serviceId + " ] 已经是启动状态!", "系统提示");
		return;
	}
	nui.confirm("你确定要启动 " + type + " [" + serviceId + "] 服务 ?", "系统提示", function(action) {
		if (action == "ok") {
			doStartService(serviceId, type);
		}
	});
}


/**
 * 启动服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 */
function doStartService(serviceId, type) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在启动, 请稍后 ...'});
	$.ajax({
		url : contextPath + "/srv/service/startService",
		data : {
			serviceId : serviceId,
			type : type
		},
		type : "post",
        success : function (text) {
        	nui.unmask();
        	var o = nui.decode(text);
        	if (o.result == true) { 
        		nui.alert("启动命令发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
        		grid.reload();
        	} else {
        		nui.alert("启动失败, 请稍后重试!", "系统提示");
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	nui.unmask();
            nui.alert("系统错误, 请稍后重试!<br/>" + jqXHR.responseText);
        }
    });
}

/**
 * 关闭服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 * @param state 状态
 */
function stopService(serviceId, type, state) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	if (state == 0) {
		nui.alert(type + " [" + serviceId + " ] 已经是停止状态!", "系统提示");
		return;
	}
	nui.confirm("你确定要关闭 " + type + " [" + serviceId + "] 服务 ?", "系统提示", function(action) {
		if (action == "ok") {
			doStopService(serviceId, type);
		}
	});
}

/**
 * 关闭服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 */
function doStopService(serviceId, type) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在关闭, 请稍后 ...'});
	$.ajax({
		url : contextPath + "/srv/service/stopService",
		data : {
			serviceId : serviceId,
			type : type
		},
		type : "post",
        success : function (text) {
        	nui.unmask();
        	var o = nui.decode(text);
        	if (o.result == true) { 
        		nui.alert("关闭命令发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
        		grid.reload();
        	} else {
        		nui.alert("关闭失败, 请稍后重试!", "系统提示");
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	nui.unmask();
            nui.alert("系统错误, 请稍后重试!<br/>" + jqXHR.responseText);
        }
    });
}

/**
 * 启动服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 * @param state 状态
 */
function restartService(serviceId, type, state) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	nui.confirm("你确定要重启 " + type + " [" + serviceId + "] 服务 ?", "系统提示", function(action) {
		if (action == "ok") {
			doRestartService(serviceId, type);
		}
	});
}

/**
 * 启动服务实例. <br>
 * 
 * @param serviceId 标识
 * @param type 类型
 */
function doRestartService(serviceId, type) {
	if (null == serviceId) {
		nui.alert("服务标识为空!", "系统提示");
		return;
	}
	nui.mask({el: document.body, cls: 'mini-mask-loading', html: '正在重启, 请稍后 ...'});
	$.ajax({
		url : contextPath + "/srv/service/restartService",
		data : {
			serviceId : serviceId,
			type : type
		},
		type : "post",
        success : function (text) {
        	nui.unmask();
        	var o = nui.decode(text);
        	if (o.result == true) { 
        		nui.alert("重新启动命令发送成功, 状态可能会有0-2秒延迟, 请稍后刷新.", "系统提示");
        		grid.reload();
        	} else {
        		nui.alert("重新启动失败, 请稍后重试!", "系统提示");
        	}
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	nui.unmask();
            nui.alert("系统错误, 请稍后重试!<br/>" + jqXHR.responseText);
        }
    });
}


/**
 * 设置NUI表单. <br>
 * 
 * @param form
 * @param readonly
 * @param clearError
 */
function setNUIForm(form, readonly, clearError) {
	if (null == form) {
		return;
	}
	var fields = form.getFields();
	var length = fields.length;
    for (var i = 0; i < length; i++) {
        var c = fields[i];
        if (readonly) {
        	if (c.setReadOnly) {
        		c.setReadOnly(true);
        	}
        }
        if (clearError) {
        	if (c.setIsValid) {
        		c.setIsValid(true);
        	}
        }
    }
}

