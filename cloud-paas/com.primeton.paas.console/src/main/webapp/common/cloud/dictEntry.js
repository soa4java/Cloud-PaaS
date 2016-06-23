/**
 * Common
 */
var YES_AND_NO = 
	[{ id: true, text: '是' , color: 'green'}
		, { id: false, text: '否' , color: 'blue'}
	];

var CLD_InstStatus = 
	[{ id: 'defaultValue', text: '全部'}
		, { id: '0', text: '停止' , color: 'red'}
		, { id: '1', text: '运行' , color: 'green'}
		, { id: '2', text: '异常' , color: 'red'}
	];


var CLD_OrderItem_Service = 
	[{ id: 'allowAccessHosts', text: '允许访问的主机列表'}
		, { id: 'appName', text: '应用名'}
		, { id: 'balance', text: '负载均衡策略'}
		, { id: 'cacheSize', text: '缓存大小/M'}
		, { id: 'characterSet', text: '数据库字符集'}
		, { id: 'checkUri', text: '心跳检测URI'}
		, { id: 'clientMaxBodySize', text: '限制HTTP请求发送的数据大小(MB)'}
		, { id: 'clusterId', text: '集群标识'}
		, { id: 'connTimeOut', text: '连接超时时间'}
		, { id: 'desc', text: '描述'}
		, { id: 'destFilePath', text: 'CardBin数据文件存放目录'}
		, { id: 'displayName', text: '显示名称'}
		, { id: 'domain', text: '域名'}
		, { id: 'isEnableDomain', text: '是否启用域名'}
		, { id: 'hostNum', text: '主机个数'}
		, { id: 'hostPkgId', text: '主机套餐编号'}
		, { id: 'hostType', text: '主机机型'}
		, { id: 'isBackUp', text: '是否主备'}
		, { id: 'isStandalone', text: '是否独占主机'}
		, { id: 'isSync', text: '是否启用数据文件同步'}
		, { id: 'jdbcDriver', text: '数据库驱动'}
		, { id: 'jdbcMaxPoolSize', text: '数据库连接池最大数'}
		, { id: 'jdbcMinPoolSize', text: '数据库连接池最小数'}
		, { id: 'jdbcPassword', text: '数据库连接密码'}
		, { id: 'jdbcUrl', text: '数据库连接字符串'}
		, { id: 'jdbcUser', text: '数据库用户名'}
		, { id: 'keepaliveTimeout', text: '长连接超时时间'}
		, { id: 'maxConnectionSize', text: '最大连接数'}
		, { id: 'maxMailWorkerNum', text: '邮件转发工作者最大数	'}
		, { id: 'maxMemorySize', text: '最大内存'}
		, { id: 'maxNum', text: '最大实例数	'}
		, { id: 'maxPermMemorySize', text: '最大Perm内存'}
		, { id: 'maxSize', text: '最大实例数'}
		, { id: 'member', text: '集群成员列表'}
		, { id: 'memorySize', text: '内存大小'}
		, { id: 'minMemorySize', text: '最小内存'}
		, { id: 'minPermMemorySize', text: '最小Perm内存'}
		, { id: 'minSize', text: '最小实例数'}
		, { id: 'password', text: '密码'}
		, { id: 'protocol', text: '负载均衡协议'}
		, { id: 'relServiceId', text: '关联服务标识'}
		, { id: 'relSrvType', text: '关联服务类型'}
		, { id: 'scope', text: '服务使用范围	'}
		, { id: 'serviceId', text: '服务实例标识'}
		, { id: 'serviceName', text: '服务名称'}
		, { id: 'storagePath', text: '存储路径'}
		, { id: 'storageSize', text: '存储大小/G'}
		, { id: 'svnPassword', text: '用户密码'}
		, { id: 'svnRepoName', text: '仓库名'}
		, { id: 'svnUserName', text: '用户名'}
		, { id: 'syncDay', text: 'CardBin数据同步时间（天）'}
		, { id: 'syncHour', text: 'CardBin数据同步时间（小时）'}
		, { id: 'tempFilePath', text: 'CardBin数据文件存放临时目录'}
		, { id: 'userName', text: '用户名'}
		, { id: 'vmArgs', text: 'VM启动参数'}
		, { id: 'workerConnections', text: 'Nginx工作线程连接池大小'}
		, { id: 'workerProcesses', text: 'Nginx工作线程个数'}
		, { id: 'serverType', text: '服务类型'}
		, { id: 'protocolType', text: '协议类型'}
		, { id: 'sslCertificatePath', text: 'ssl证书路径'}
		
		, { id: 'continuedTime', text: '持续时间( 分钟 )'}
		, { id: 'cpuThreshold', text: 'CPU使用率( % )'}
		, { id: 'ignoreTime', text: '忽略时间( 分钟 )'}
		, { id: 'ioThreshold', text: 'IO读写速率'}
		, { id: 'isEnable', text: '是否启用'}
		, { id: 'lbThreshold', text: '负载'}
		, { id: 'memoryThreshold', text: 'Memory使用率( % )'}
		, { id: 'networdThreshold', text: 'Network流量'}
		, { id: 'strategyName', text: '伸缩策略名称'}
		, { id: 'strategyType', text: '伸缩策略类型'}
		, { id: 'stretchSize', text: '伸缩幅度( 台 )'}
		
		, { id: 'allowedApp', text: '允许接受来自该列表中应用的消息'}
		, { id: 'messageDurable', text: '消息是否持久化'}
		, { id: 'forbiddenApp', text: '禁止接受来自该列表中应用的消息'}
		
		, { id: 'maxMessageCounts', text: '队列消息数量上限'}
		, { id: 'maxMessageSize', text: '队列单条消息大小上限 (KB)'}
		, { id: 'vhost', text: 'mq server 虚拟主机名'}
		, { id: 'username', text: 'mq server 用户名'}
	];


var CLD_OrderItem_SrvType = 
	[{ id: 'CardBin', text: 'CardBin服务'}
		, { id: 'HaProxy', text: 'HaProxy负载均衡服务'}
		, { id: 'Jetty', text: 'Jetty应用容器服务'}
		, { id: 'Mail', text: 'Mail邮件转发服务'}
		, { id: 'Memcached', text: 'Memcached缓存服务'}
		, { id: 'MySQL', text: 'MySQL数据库服务'}
		, { id: 'Nginx', text: 'Nginx负载均衡服务'}
		, { id: 'OpenAPI', text: 'OpenAPI开放接口服务'}
		, { id: 'SMS', text: 'SMS短信发送服务'}
		, { id: 'SVN', text: 'SVN资源库服务'}
		, { id: 'SVNRepository', text: 'SVNRepository服务'}
		, { id: 'Tomcat', text: 'Tomcat应用容器服务'}
		, { id: 'War', text: 'War服务'}
		, { id: 'Redis', text: 'Redis服务'}
		, { id: 'RedisSentinel', text: 'RedisSentinel服务'}
		, { id: 'Hadoop', text: 'Hadoop'}
		, { id: 'Gateway', text: '网关服务'}
		, { id: 'JobCtrl', text: 'JobCtrl服务'}
		, { id: 'ESB', text: 'ESB服务'}
		, { id: 'MsgQueue', text: '消息队列服务'}
	];


var CLD_OrderItem_Stretch = 
	[{ id: 'appName', text: '应用名称'}
		, { id: 'continuedTime', text: '持续时间'}
		, { id: 'cpuThreshold', text: 'CPU使用率'}
		, { id: 'ignoreTime', text: '忽略时间'}
		, { id: 'ioThreshold', text: 'IO读写速率'}
		, { id: 'isEnable', text: '是否启用'}
		, { id: 'lbThreshold', text: '负载'}
		, { id: 'memoryThreshold', text: 'Memory使用率	'}
		, { id: 'networdThreshold', text: 'Network流量'}
		, { id: 'strategyName', text: '伸缩策略名称'}
		, { id: 'strategyType', text: '伸缩策略类型'}
		, { id: 'stretchSize', text: '伸缩幅度'}
	];


var CLD_TaskStatus = 
	[{ id: 'defaultValue', text: '全部' }
		, { id: '1', text: '处理中'}
		, { id: '2', text: '完成'}
		, { id: '3', text: '异常'}
		, { id: '4', text: '终止'}
		, { id: '5', text: '超时'}
	];


var CLD_TaskType = 
	[{ id: 'defaultValue', text: '全部' }
		, { id: '1', text: '应用备份'}
		, { id: '2', text: '应用恢复'}
		, { id: '3', text: '服务自动伸缩'}
		, { id: '4', text: '服务手动伸缩'}
		, { id: '5', text: '服务安装'}
		, { id: '6', text: '数据同步'}
		, { id: '7', text: '销毁主机'}
		, { id: '8', text: '销毁存储'}
		, { id: '9', text: '服务报警'}
		, { id: '10', text: '升级主机'}
	];


var CLD_UserStatus = 
	[{ id: 'defaultValue', text: '全部' }
		, { id: '1', text: '待审批' , color: '#ffc000'}
		, { id: '2', text: '已审批' , color: 'blue'}
		, { id: '3', text: '已拒绝' , color: 'red'}
		, { id: '4', text: '已激活' , color: 'green'}
	];


var CLD_OrderTypes = 
	[{ id: 'defaultValue', text: '全部' }
		, { id: 'CREATE_APP', text: '平台应用开通' , icon: 'order_types_create_app'}
		, { id: 'CREATE_OUTER_APP', text: '外部应用注册' , icon: 'order_types_create_outer_app'}
		, { id: 'CREATE_SRV', text: '服务开通' , icon: 'order_types_create_srv'}
		, { id: 'DELETE_APP', text: '平台应用移除' , icon: 'order_types_delete_app'}
		, { id: 'DELETE_OUTER_APP', text: '外部应用移除' , icon: 'order_types_delete_outer_app'}
		, { id: 'DELETE_SRV', text: '服务移除' , icon: 'order_types_delete_srv'}
		, { id: 'STRETCH_STRATEGY_CONFIG', text: '伸缩策略配置' , icon: 'order_types_stretch_strategy_config'}
		, { id: 'UPDATE_APP', text: '应用配置' , icon: 'order_types_update_app'}
		, { id: 'UPDATE_SRV', text: '服务配置' , icon: 'order_types_update_srv'}
		, { id: 'SingleCreateRedis', text: '创建Redis服务' , icon: 'order_types_create_srv'}
		, { id: 'SingleDestroyService', text: '销毁服务' , icon: 'order_types_delete_srv'}
		, { id: 'DefaultDestroyService', text: '销毁服务' , icon: 'order_types_delete_srv'}
		, { id: 'CreateGateway', text: '创建网关服务' , icon: 'order_types_create_srv'}
		, { id: 'UpdateGateway', text: '更改网关服务' , icon: 'order_types_create_srv'}
		, { id: 'CreateRedisSentinel', text: '创建Redis哨兵' , icon: 'order_types_create_srv'}
		, { id: 'CreateJobCtrl', text: '创建JobCtrl服务' , icon: 'order_types_create_srv'}
		, { id: 'CreateESB', text: '创建ESB服务' , icon: 'order_types_create_srv'}
		, { id: 'SingleCreateMsgQueue', text: '创建消息队列服务' , icon: 'order_types_create_srv'}
	];

	
var CLD_OrderStatus = 
	[{ id: 'defaultValue',  text: '全部' }
		, { id: '1', text: '已提交', icon: 'order_status_submit'}
		, { id: '2', text: '已撤销', icon: 'order_status_repeal'}
		, { id: '3', text: '已审批', icon: 'order_status_approve'}
		, { id: '4', text: '已拒绝', icon: 'order_status_reject'}
		, { id: '5', text: '处理成功', icon: 'order_status_success'}
		, { id: '6', text: '处理失败', icon: 'order_status_fail'}
	];


var CLD_FuncType = 
	[{ id:'defaultValue',text:'全部'}
		, { id: 'page', text: '页面'}
		, { id: 'other', text: '其他'}
	];


var CLD_FuncIsCheck = 
	[{ id:'defaultValue',text:'全部'}
		, { id: '0', text: '否'}
		, { id: '1', text: '是'}
	];


var CLD_FuncIsMenu = 
	[{ id:'defaultValue',text:'全部'}
		, { id: '0', text: '否'}
		, { id: '1', text: '是'}
	];


var CLD_AppStatus = 
	[{ id: '0', text: '开通中' , color: 'blue'}
		, { id: '1', text: '已开通' , color: 'green'}
		, { id: '2', text: '待移除' , color: '#ffc000'}
	];


var CLD_ChargeStatus = 
	[{ id: 'charge', text: '收费'}
		, { id: 'free', text: '免费'}
	];


var CLD_ContainerTypes = 
	[{ id: 'Tomcat', text: 'Tomcat'}
		, { id: 'Jetty', text: 'Jetty'}
	];


var CLD_SrvStatus = 
	[{ id: '0', text: '停止' , color: 'red'}
		, { id: '1', text: '运行' , color: 'green'}
	];


var CLD_DomainStatus = 
	[{ id: 'Y', text: '启用' ,color: 'green'}
		, { id: 'N', text: '停用', color: 'blue'}
	];


var CLD_NetProxyStatus = 
	[{ id: '1', text: '启用' ,color: 'green'}
		, { id: '0', text: '停用', color: 'blue'}
	];
		

var CLD_LogLevels = 
	[{ id: 'DEBUG', text: 'DEBUG'}
		, { id: 'INFO', text: 'INFO'}
		, { id: 'WARN', text: 'WARN'}
		, { id: 'ERROR', text: 'ERROR'}
	];


var CLD_DeployVersion = 
	[{ id: 'true', text: '已部署' , color: 'green'}
		, { id: 'false', text: '未部署' , color: '#ffc000'}
	];


var CLD_Host_Standalone = 
	[{ id: true, text: '是' , color: 'green'}
		, { id: false, text: '否' , color: 'blue'}
	];

var CLD_Host_Controlable = 
	[{ id: true, text: '在线' , color: 'green'}
		, { id: false, text: '离线' , color: 'red'}
	];


var CLD_StorageStatus = 
	[{ id: '0', text: '否' , color: 'blue'}
		, { id: '1', text: '是' , color: 'green'}
	];

var CLD_SslLevel = 
	[{ id: '1', text: '1' }
		, { id: '2', text: '2' }
		, { id: '3', text: '3' }
		, { id: '4', text: '4' }
		, { id: '5', text: '5' }
		, { id: '6', text: '6' }
		, { id: '7', text: '7' }
		, { id: '8', text: '8' }
		, { id: '9', text: '9' }
		, { id: '10', text: '10' }
	];

function onYesAndNoRenderer(e) {
	var length = YES_AND_NO.length;
	for (var i = 0; i < length; i++) {
		var g = YES_AND_NO[i];
	    if (g.id == e.value) { 
	    	return '<font color="' + g.color + '">' + g.text + '</font>';
	    }
	}
	return e.value;
}

/**
 * 服务状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onServiceStateRenderer(e) {
	for (var i = 0, l = CLD_InstStatus.length; i < l; i++) {
		var g = CLD_InstStatus[i];
	    if (g.id == e.value) { 
	    	return '<font color="' + g.color + '">' + g.text + '</font>';
	    }
	}
	return e.value;
}

/**
 * 任务状态. <br>
 * 
 * @param e
 * @returns
 */
function onTaskStatusRenderer(e) {
	for (var i = 0, l = CLD_TaskStatus.length; i < l; i++) {
        var g = CLD_TaskStatus[i];
        if (g.id == e.value) { 
        	return g.text;
        }
    }
    return e.value;
}

/**
 * 任务类型. <br>
 * 
 * @param e
 * @returns
 */
function onTaskTypeRenderer(e) {
	for (var i = 0, l = CLD_TaskType.length; i < l; i++) {
        var g = CLD_TaskType[i];
        if (g.id == e.value) { 
        	return g.text;
        }
    }
    return e.value;
}

/**
 * 订单类型. <br>
 * 
 * @param e
 * @returns {String}
 */
function onOrderTypesRenderer(e) {
    for (var i = 0, l = CLD_OrderTypes.length; i < l; i++) {
        var g = CLD_OrderTypes[i];
        if (g.id == e.value) { 
        	return '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="../../../images/icons/' 
        		+ g.icon + '.png" align="middle">&nbsp;' + g.text;
        }
    }
    return e.value;
}

/**
 * 订单状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onOrderStatusRenderer(e) {
    for (var i = 0, l = CLD_OrderStatus.length; i < l; i++) {
        var g = CLD_OrderStatus[i];
        if (g.id == e.value) { 
        	return '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="../../../images/icons/'
        		+ g.icon + '.png" align="middle">&nbsp;' + g.text;
        }
    }
    return e.value;
}

/**
 * 日期格式化. <br>
 * 
 * @param e
 * @returns
 */
function onDateRenderer(e) {
    var value = e.value;
    if (value) {
    	var value = new Date(value);   
    	return nui.formatDate(value, 'yyyy-MM-dd HH:mm:ss');
    } 
    return e.value;
}

/**
 * 应用状态. <br>
 * 
 * @param e
 * @returns
 */
function onAppStatusRenderer(e) {
    for (var i = 0, l = CLD_AppStatus.length; i < l; i++) {
        var g = CLD_AppStatus[i];
        if (g.id == e.value) { 
        	return '<font color="'+g.color+'">'+g.text+'</font>';
        }
    }
    return e.value;
}

/**
 * 收费状态. <br>
 * 
 * @param e
 * @returns
 */
function onChargeStatusRenderer(e) {
    for (var i = 0, l = CLD_ChargeStatus.length; i < l; i++) {
        var g = CLD_ChargeStatus[i];
        if (g.id == e.value) { 
        	return g.text;
        }
    }
    return e.value;
}

/**
 * 用户状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onUserStatusRenderer(e){
	for (var i = 0, l = CLD_UserStatus.length; i < l; i++) {
        var g = CLD_UserStatus[i];
        if (g.id == e.value) { 
        	return '<font color="' + g.color + '">' + g.text + '</font>';
        }
    }
    return e.value;
}

/**
 * 功能类型. <br>
 * 
 * @param e
 * @returns
 */
function onFuncTypeRenderer(e) {
	for (var i = 0, l = CLD_FuncType.length; i < l; i++) {
		var g = CLD_FuncType[i];
	    if (g.id == e.value) { 
	    	return g.text;
	    }
	}
	return e.value;
}

/**
 * 功能页面检查/拦截. <br>
 * 
 * @param e
 * @returns
 */
function onFuncIsCheckRenderer(e) {
	for (var i = 0, l = CLD_FuncIsCheck.length; i < l; i++) {
		var g = CLD_FuncIsCheck[i];
	    if (g.id == e.value) { 
	    	return g.text;
	    }
	}
	return e.value;
}

/**
 * 是否可以定义菜单. <br>
 * 
 * @param e
 * @returns
 */
function onFuncIsMenuRenderer(e) {
	for (var i = 0, l = CLD_FuncIsMenu.length; i < l; i++) {
		var g = CLD_FuncIsMenu[i];
	    if (g.id == e.value) { 
	    	return g.text;
	    }
	}
	return e.value;
}

/**
 * 服务状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onSrvStatusRenderer(e) {
    for (var i = 0, l = CLD_SrvStatus.length; i < l; i++) {
        var g = CLD_SrvStatus[i];
        if (g.id == e.value){ 
        	return '<font color="' + g.color+'">' + g.text + '</font>';
        }
    }
    return e.value;
}

/**
 * 部署版本. <br>
 * 
 * @param e
 * @returns {String}
 */
function onDeployVersionRenderer(e) {
    for (var i = 0, l = CLD_DeployVersion.length; i < l; i++) {
        var g = CLD_DeployVersion[i];
        if (g.id == e.value) { 
        	return '<font color="' + g.color+'">' + g.text + '</font>';
        }
    }
    return e.value;
}

/**
 * 功能类型. <br>
 * 
 * @param e
 * @returns
 */
function onFuncTypeRenderer(e) {
	for (var i = 0, l = CLD_FuncType.length; i < l; i++) {
		var g = CLD_FuncType[i];
	    if (g.id == e.value) { 
	    	return g.text;
	    }
	}
	return e.value;
}

/**
 * 
 * @param e
 * @returns {String}
 */
function onHostStandaloneRenderer(e) {
	for (var i = 0, l = CLD_Host_Standalone.length; i < l; i++) {
		var g = CLD_Host_Standalone[i];
	    if (g.id == e.value) { 
	    	return '<font color="' + g.color + '">' + g.text + '</font>';
	    }
	}
	return e.value;
}

/**
 * 主机是否可控/在线. <br>
 * 
 * @param e
 * @returns {String}
 */
function onHostControlableRenderer(e) {
	for (var i = 0, l = CLD_Host_Controlable.length; i < l; i++) {
		var g = CLD_Host_Controlable[i];
	    if (g.id == e.value) { 
	    	return '<font color="' + g.color + '">' + g.text + '</font>';
	    }
	}
	return e.value;
}

/**
 * 存储状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onStorageStatusRenderer(e) {
	for (var i = 0, l = CLD_StorageStatus.length; i < l; i++) {
		var g = CLD_StorageStatus[i];
	    if (g.id == e.value) { 
	    	return '<font color="' + g.color+'">' + g.text + '</font>';
	    }
	}
	return e.value;
}

/**
 * 错误跳转. <br>
 * 
 * @param e
 */
function onLoadErrorRenderer(e) {
	var obj = window.location; 
	// var contextPath = obj.pathname.split("/")[1]; 
	var basePath = obj.protocol + "//" + obj.host; 
	// top.location.href = basePath + "/frame/error.html";
	top.location.href = basePath + "/login.jsp";
}

/**
 * 域名状态. <br>
 * 
 * @param e
 * @returns {String}
 */
function onDomainStatusRenderer(e) {
	for (var i = 0, l = CLD_DomainStatus.length; i < l; i++) {
        var g = CLD_DomainStatus[i];
        if (g.id == e.value) { 
        	return '<font color="' + g.color+'">' + g.text + '</font>';
        }
    }
    return e.value;
}


/**
 * 服务操作Action. <br>
 * 
 * @param e
 * @returns {String}
 */
function onServiceActionRenderer(e) {
	var grid = e.sender;
    var record = e.record;
	var serviceId = record.id;
	var state = record.state;
	var type = record.type;
    if (state == 0) {
    	return ' <a href="javascript:startService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">启动</a> '
    		+ ' <a href="javascript:restartService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">重启</a> ';
    } else if (state == 1) {
    	return ' <a href="javascript:restartService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">重启</a> ' 
        	+ ' <a href="javascript:stopService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">停止</a> ';
    }
    return ' <a href="javascript:startService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">启动</a> '
		+ ' <a href="javascript:stopService(\'' + serviceId + '\', \'' + type + '\', \'' + state + '\')">停止</a> ';
}

