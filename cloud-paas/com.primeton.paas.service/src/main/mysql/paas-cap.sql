
DROP TABLE if exists cap_function;

CREATE TABLE cap_function (
	FUNC_ID varchar(64) NOT NULL, 
	TENANT_ID varchar(64) NOT NULL, 
	FUNC_NAME varchar(128) NOT NULL, 
	FUNC_TYPE varchar(64), 
	FUNC_DESC varchar(512), 
	FUNC_ACTION varchar(255) NOT NULL, 
	ISCHECK char(1) DEFAULT '1', 
	ISMENU char(1) DEFAULT '1', 
	CREATEUSER varchar(64), 
	CREATETIME timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, 
	PRIMARY KEY (FUNC_ID), CONSTRAINT I_CAP_FUNCTION UNIQUE (TENANT_ID, FUNC_ID)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_appControl', 'default', '应用控制', 'page', '自助服务平台-应用控制', '../app/control/listApplications.jsp', '1', '1', 'sysadmin', '2014-06-16 02:04:10');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_appDeploy', 'default', '应用部署', 'page', '自助服务门户-应用部署', '../app/deploy/listApplications.jsp', '1', '1', 'sysadmin', '2014-06-16 02:01:35');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_appLog', 'default', '日志查看', 'page', '自助服务平台-日志查看', '../app/logs/appLog.jsp', '1', '1', 'sysadmin', '2014-06-16 02:05:28');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_appSetting', 'default', '应用设置', 'page', '自助服务平台-应用设置', '../app/setting/appSetting.jsp', '1', '1', 'sysadmin', '2014-06-16 02:04:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_dataManage', 'default', '数据维护', 'page', '自助服务门户-数据维护', '../app/data/mySqlAdmin.jsp', '1', '1', 'sysadmin', '2014-06-16 02:01:50');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_monitorState', 'default', '监控状态', 'page', '自助服务门户-监控状态', '../app/monitor/remark/showAppMonitor.jsp', '1', '1', 'sysadmin', '2014-06-16 02:02:04');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_myApplication', 'default', '我的应用', 'page', '自助服务门户-我的应用', '../app/application/myAppMgr.jsp', '1', '1', 'sysadmin', '2014-06-16 02:02:24');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_myOrder', 'default', '我的订单', 'page', '自助服务门户，我的订单展示页', '../app/order/myOrderMgr.jsp', '1', '1', 'sysadmin', '2014-06-03 02:16:10');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_myService', 'default', '我的服务', 'page', '自助服务平台-我的服务', '../app/service/myServicesMgr.jsp', '1', '1', 'sysadmin', '2014-06-16 02:03:25');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('App_stretchStrategy', 'default', '伸缩策略', 'page', '自助服务平台-伸缩策略', '../app/scale/appStretchStrategy.jsp', '1', '1', 'sysadmin', '2014-06-16 02:06:37');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_authManage', 'default', '授权管理', 'page', '授权管理', '../coframe/accredit/roleAuthMgr.jsp', '1', '1', null, '2014-05-28 08:08:28');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_functionManage', 'default', '功能管理', 'page', '功能管理', '../coframe/function/functionAuthMgr.jsp', '1', '1', null, '2014-05-28 08:10:53');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_infoUpdate', 'default', '资料维护', 'page', '为登录者提供资料维护、更新功能', '../platform/account/info/myAccount.jsp', '0', '1', null, '2014-07-31 19:10:29');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_menuManage', 'default', '菜单管理', 'page', '菜单管理', '../coframe/menu/menuAuthMgr.jsp', '1', '1', null, '2014-05-28 08:10:53');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_passwordChange', 'default', '密码修改', 'page', '密码修改', '../platform/account/update/updatePwd.jsp', '0', '1', null, '2014-07-31 19:11:00');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('COF_userManage', 'default', '用户管理', 'page', '用户管理', '../coframe/user/userAuthMgr.jsp', '1', '1', null, '2014-05-28 08:10:53');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_APPMonitor', 'default', '应用监控', 'page', '应用监控', '../platform/srvmonitor/app/showAppMonitor.jsp', '1', '1', 'sysadmin', '2014-05-30 06:25:30');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_AppServiceMonitor', 'default', '应用服务监控', 'page', '应用服务监控报警', '../platform/srvmonitor/appservice/serviceMonitorAndAlarm.jsp', '1', '1', 'sysadmin', '2014-12-22 14:55:28');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_CEPMgr', 'default', 'CEP服务', 'page', 'CEP服务', '../platform/srvmgr/cep/cepEngineClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:16:06');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_CollectorMgr', 'default', '日志采集服务', 'page', '日志采集服务', '../platform/srvmgr/collector/collectorClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:17:19');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_HaproxyMgr', 'default', 'Haproxy服务', 'page', 'Haproxy服务', '../platform/srvmgr/haproxy/haproxyClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:17:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_HostMgr', 'default', '主机管理', 'page', '主机管理', '../platform/resourcemgr/host/hostResMgr.jsp', '1', '1', 'sysadmin', '2014-07-03 02:22:54');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_HostMonitor', 'default', '主机监控', 'page', '主机监控', '../platform/srvmonitor/host/showHostMonitor.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_innerAppMgr', 'default', '平台应用管理', 'page', '平台应用的管理', '../platform/appmgr/inner/innerAppMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_JettyMgr', 'default', 'Jetty服务', 'page', 'Jetty集群及服务管理', '../platform/srvmgr/jetty/jettyClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_TomcatMgr', 'default', 'Tomcat服务', 'page', 'Tomcat集群及服务管理', '../platform/srvmgr/tomcat/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_RedisMgr', 'default', 'Redis服务', 'page', 'Redis集群及服务管理', '../platform/srvmgr/redis/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_RedisSentinelMgr', 'default', 'Redis哨兵', 'page', 'RedisSentinel集群及服务管理', '../platform/srvmgr/sentinel/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_GatewayMgr', 'default', '网关服务', 'page', '网关集群及服务管理', '../platform/srvmgr/gateway/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_HadoopMgr', 'default', 'Hadoop服务', 'page', 'Hadoop服务', '../platform/srvmgr/hadoop/indexLink.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_JobCtrlMgr', 'default', 'JobCtrl服务', 'page', 'JobCtrl集群及服务管理', '../platform/srvmgr/jobCtrl/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_EsbMgr', 'default', 'ESB服务', 'page', 'ESB集群及服务管理', '../platform/srvmgr/esb/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_MsgQueueMgr', 'default', '消息服务', 'page', 'MsgQueue集群及服务管理', '../platform/srvmgr/msgqueue/ClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_KeepalivedMgr', 'default', 'Keepalived服务', 'page', 'Keepalived集群和服务管理', '../platform/srvmgr/keepalived/keepalivedClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_Log', 'default', '日志下载', 'page', '日志下载', '../platform/srvmonitor/log/listLogs.jsp', '1', '1', 'sysadmin', '2014-06-25 09:33:46');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_Mail', 'default', '邮件转发服务', 'page', '邮件转发', '../platform/srvmgr/mail/mailClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_MemcachedMgr', 'default', 'Memcached服务', 'page', 'Memcached服务', '../platform/srvmgr/memcached/memcachedClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_MysqlMgr', 'default', 'Mysql服务', 'page', 'Mysql服务', '../platform/srvmgr/mysql/mysqlClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_NginxMgr', 'default', 'Nginx服务', 'page', 'Nginx服务', '../platform/srvmgr/nginx/nginxClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_OpenAPIMgr', 'default', 'OpenAPI服务', 'page', 'OpenAPI服务的管理', '../platform/srvmgr/openapi/openapiClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_orderMgr', 'default', '订单管理', 'page', '审批资源申请订单', '../platform/audit/order/orderMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_pasVarMgr', 'default', '系统变量管理', 'page', '系统变量的维护（pas_system_config 单表维护）', '../platform/srvmonitor/variables/pasVarsMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:15');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_regMgr', 'default', '用户注册审批', 'page', '审批用户注册申请', '../platform/audit/user/usrRegMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_ServiceMonitor', 'default', '缓存监控', 'page', 'memcached服务监控', '../platform/srvmonitor/memcached/showServiceMonitor.jsp', '1', '1', 'sysadmin', '2014-07-31 21:27:38');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_StorageMgr', 'default', '存储管理', 'page', '存储管理', '../platform/resourcemgr/storage/storageResMgr.jsp', '1', '1', 'sysadmin', '2014-07-03 02:23:13');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_stretchStrategy', 'default', '伸缩策略设置', 'page', '应用伸缩策略配置、全局伸缩策略', '../platform/srvmonitor/stretch/teleScopicStrategy.jsp', '1', '1', 'sysadmin', '2014-06-04 06:35:45');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_SvnMgr', 'default', 'SVN服务', 'page', '资源库服务集群管理', '../platform/srvmgr/svn/svnClusterMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_taskMonitor', 'default', '长任务监控', 'page', '长任务监控', '../platform/srvmonitor/task/taskServerMonitor.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');
INSERT INTO cap_function (FUNC_ID, TENANT_ID, FUNC_NAME, FUNC_TYPE, FUNC_DESC, FUNC_ACTION, ISCHECK, ISMENU, CREATEUSER, CREATETIME) 
	VALUES ('PAS_VIPMgr', 'default', 'IP管理', 'page', 'IP管理', '../platform/resourcemgr/vip/vipMgr.jsp', '1', '1', 'sysadmin', '2014-05-30 06:28:47');

DROP TABLE if exists cap_menu;

CREATE TABLE cap_menu (
	MENU_ID decimal NOT NULL, 
	TENANT_ID varchar(64) NOT NULL, 
	MENU_CODE varchar(32) NOT NULL, 
	MENU_NAME varchar(64) NOT NULL, 
	LINK_TYPE varchar(64), 
	LINK_RES varchar(64), 
	LINK_ACTION varchar(255), 
	MENU_LEVEL decimal, 
	MENU_SEQ varchar(512), 
	ISLEAF char(1), 
	PARENT_MENU_ID decimal, 
	IMAGEPATH varchar(100), 
	EXPANDPATH varchar(100), 
	OPENMODE varchar(16) DEFAULT '0', 
	CREATEUSER varchar(64), 
	CREATETIME timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, 
	PRIMARY KEY (MENU_ID), 
	CONSTRAINT I_CAP_MENU UNIQUE (TENANT_ID, MENU_CODE)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (2, 'default', '02_COF_authManage', '权限管理', null, null, null, 1, '2.', '0', null, null, null, '0', null, '2014-05-30 14:35:52');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (4, 'default', 'W_COF_accountManager', '个人信息', null, null, null, 1, '4.', '0', null, null, null, '0', null, '2014-06-26 11:14:39');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (21, 'default', 'COF_functionManage', '功能管理', 'function', 'COF_functionManage', '../coframe/function/functionAuthMgr.jsp', 2, '2.21.', '1', 2, 'coframe_functionAuthMgr', null, '0', null, '2014-07-14 16:44:37');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (22, 'default', 'COF_menuManage', '菜单管理', 'function', 'COF_menuManage', '../coframe/menu/menuAuthMgr.jsp', 2, '2.22.', '1', 2, 'coframe_menuAuthMgr', null, '0', null, '2014-07-14 16:44:48');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (23, 'default', 'COF_roleManage', '授权管理', 'function', 'COF_authManage', '../coframe/accredit/roleAuthMgr.jsp', 2, '2.23.', '1', 2, 'coframe_roleAuthMgr', null, '0', null, '2014-07-14 16:44:58');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (24, 'default', 'COF_userManage', '用户管理', 'function', 'COF_userManage', '../coframe/user/userAuthMgr.jsp', 2, '2.24.', '1', 2, 'coframe_userAuthMgr', null, '0', null, '2014-07-14 16:45:09');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (41, 'default', 'COF_passwordChange', '密码修改', 'function', 'COF_passwordChange', '../platform/account/update/updatePwd.jsp', 2, '4.41.', '1', 4, 'platform_updatePwd', null, '0', null, '2014-07-31 19:11:01');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (42, 'default', '06_PAS_serviceManager', '服务管理', null, null, null, 1, '42.', '0', null, null, null, '0', 'sysadmin', '2013-08-27 05:36:43');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (102, 'default', '03_PAS_serviceAudit', '服务审批', null, null, null, 1, '102.', '0', null, null, null, '0', 'sysadmin', '2014-05-29 14:58:07');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (105, 'default', 'PAS_regMgr', '用户审批', 'function', 'PAS_regMgr', '../platform/audit/user/usrRegMgr.jsp', 2, '102.105.', '1', 102, 'platform_usrRegMgr', null, '0', 'sysadmin', '2014-07-14 14:00:46');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (106, 'default', 'PAS_orderMgr', '订单管理', 'function', 'PAS_orderMgr', '../platform/audit/order/orderMgr.jsp', 2, '102.106.', '1', 102, 'platform_orderMgr', null, '0', 'sysadmin', '2014-07-14 11:39:09');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (121, 'default', 'COF_infoUpdate', '资料维护', 'function', 'COF_infoUpdate', '../platform/account/info/myAccount.jsp', 2, '4.121.', '1', 4, 'platform_myAccount', null, '0', 'sysadmin', '2014-07-31 19:10:30');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (141, 'default', '04_PAS_appManager', '应用管理', null, null, null, 1, '141.', '0', null, null, null, '0', 'sysadmin', '2013-08-27 05:47:49');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (261, 'default', 'PAS_2090_mysql', 'MySQL服务', 'function', 'PAS_MysqlMgr', '../platform/srvmgr/mysql/mysqlClusterMgr.jsp', 2, '42.261.', '1', 42, 'platform_mysqlClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:52:12');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (262, 'default', 'PAS_1050_cache', 'Memcached服务', 'function', 'PAS_MemcachedMgr', '../platform/srvmgr/memcached/memcachedClusterMgr.jsp', 2, '42.262.', '1', 42, 'platform_memcachedClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:57:25');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (281, 'default', 'PAS_1030_svn', 'SVN服务', 'function', 'PAS_SvnMgr', '../platform/srvmgr/svn/svnClusterMgr.jsp', 2, '42.281.', '1', 42, 'platform_svnClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:41:39');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (282, 'default', 'PAS_1021_jetty', 'Jetty服务', 'function', 'PAS_JettyMgr', '../platform/srvmgr/jetty/jettyClusterMgr.jsp', 2, '42.282.', '1', 42, 'platform_jettyClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (283, 'default', 'PAS_1022_tomcat', 'Tomcat服务', 'function', 'PAS_TomcatMgr', '../platform/srvmgr/tomcat/ClusterMgr.jsp', 2, '42.283.', '1', 42, 'platform_tomcatClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (284, 'default', 'PAS_1023_redis', 'Redis服务', 'function', 'PAS_RedisMgr', '../platform/srvmgr/redis/ClusterMgr.jsp', 2, '42.284.', '1', 42, 'platform_redisClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (285, 'default', 'PAS_1024_redisSentinel', 'Redis哨兵', 'function', 'PAS_RedisSentinelMgr', '../platform/srvmgr/sentinel/ClusterMgr.jsp', 2, '42.285.', '1', 42, 'platform_redisClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (286, 'default', 'PAS_1025_gateway', '网关服务', 'function', 'PAS_GatewayMgr', '../platform/srvmgr/gateway/ClusterMgr.jsp', 2, '42.286.', '1', 42, 'platform_gatewayClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (287, 'default', 'PAS_1026_hadoop', 'Hadoop服务', 'function', 'PAS_HadoopMgr', '../platform/srvmgr/hadoop/indexLink.jsp', 2, '42.287.', '1', 42, 'platform_hadoopClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (288, 'default', 'PAS_1027_jobCtrl', 'JobCtrl服务', 'function', 'PAS_JobCtrlMgr', '../platform/srvmgr/jobCtrl/ClusterMgr.jsp', 2, '42.288.', '1', 42, 'platform_jobCtrlClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (289, 'default', 'PAS_1028_esb', 'ESB服务', 'function', 'PAS_EsbMgr', '../platform/srvmgr/esb/ClusterMgr.jsp', 2, '42.289.', '1', 42, 'default', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (290, 'default', 'PAS_1029_msgQueue', '消息服务', 'function', 'PAS_MsgQueueMgr', '../platform/srvmgr/msgqueue/ClusterMgr.jsp', 2, '42.290.', '1', 42, 'default', null, '0', 'sysadmin', '2015-01-23 11:45:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (301, 'default', 'PAS_1010_haproxy', 'HaProxy服务', 'function', 'PAS_HaproxyMgr', '../platform/srvmgr/haproxy/haproxyClusterMgr.jsp', 2, '42.301.', '1', 42, 'platform_haproxyClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:33:15');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (302, 'default', 'PAS_1000_nginx', 'Nginx服务', 'function', 'PAS_NginxMgr', '../platform/srvmgr/nginx/nginxClusterMgr.jsp', 2, '42.302.', '1', 42, 'platform_nginxClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:55:09');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (321, 'default', '05_PAS_resManager', '资源管理', null, null, null, 1, '321.', '0', null, null, null, '0', 'sysadmin', '2013-08-27 05:47:56');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (322, 'default', '01PAS_host', '主机管理', 'function', 'PAS_HostMgr', '../platform/resourcemgr/host/hostResMgr.jsp', 2, '321.322.', '1', 321, 'platform_hostMgr', null, '0', 'sysadmin', '2014-07-14 11:40:09');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10002, 'default', 'PAS_1040_Keepalived', 'Keepalived服务', 'function', 'PAS_KeepalivedMgr', '../platform/srvmgr/keepalived/keepalivedClusterMgr.jsp', 2, '42.10002.', '1', 42, 'platform_keepalivedClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:57:11');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10003, 'default', 'PAS_5000_stretch', '伸缩策略', 'function', 'PAS_stretchStrategy', '../platform/srvmonitor/stretch/teleScopicStrategy.jsp', 2, '10022.10003.', '1', 10022, 'platform_strategyConfig', null, '0', 'sysadmin', '2015-01-23 14:04:26');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10004, 'default', 'PAS_3000_Log', '日志下载', 'function', 'PAS_Log', '../platform/srvmonitor/log/listLogs.jsp', 2, '10022.10004.', '1', 10022, 'platform_listLogs', null, '0', 'sysadmin', '2015-01-23 13:41:32');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10005, 'default', 'PAS_1000_APPMonitor', '应用监控', 'function', 'PAS_APPMonitor', '../platform/srvmonitor/app/showAppMonitor.jsp', 2, '10022.10005.', '1', 10022, 'platform_showAppMonitor', null, '0', 'sysadmin', '2015-01-23 14:03:57');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10006, 'default', 'PAS_1020_HostMonitor', '主机资源监控', 'function', 'PAS_HostMonitor', '../platform/srvmonitor/host/showHostMonitor.jsp', 2, '10022.10006.', '1', 10022, 'platform_showHostMonitor', null, '0', 'sysadmin', '2015-01-23 14:03:18');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10007, 'default', '03PAS_VIPMgr', 'IP管理', 'function', 'PAS_VIPMgr', '../platform/resourcemgr/vip/vipMgr.jsp', 2, '321.10007.', '1', 321, 'platform_vipMgr', null, '0', 'sysadmin', '2014-07-14 11:40:31');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10008, 'default', 'PAS_2000_CEPMgr', 'CEP服务', 'function', 'PAS_CEPMgr', '../platform/srvmgr/cep/cepEngineClusterMgr.jsp', 2, '42.10008.', '1', 42, 'platform_cepEngineClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:45:46');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10021, 'default', 'PAS_2010_openapi', 'OpenAPI服务', 'function', 'PAS_OpenAPIMgr', '../platform/srvmgr/openapi/openapiClusterMgr.jsp', 2, '42.10021.', '1', 42, 'platform_openapiClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:47:49');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10022, 'default', '07_PAS_monitor', '平台监控', null, null, null, 1, '10022.', '0', null, null, null, '0', 'sysadmin', '2013-08-27 05:33:04');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10023, 'default', 'PAS_2000_task', '长任务监控', 'function', 'PAS_taskMonitor', '../platform/srvmonitor/task/taskServerMonitor.jsp', 2, '10022.10023.', '1', 10022, 'platform_taskServerMonitor', null, '0', 'sysadmin', '2015-01-23 13:40:56');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10041, 'default', 'PAS_2001_collector', 'Collector服务', 'function', 'PAS_CollectorMgr', '../platform/srvmgr/collector/collectorClusterMgr.jsp', 2, '42.10041.', '1', 42, 'platform_collectorClusterMgr', null, '0', 'sysadmin', '2015-01-23 14:06:17');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10081, 'default', '02PAS_storage', '存储管理', 'function', 'PAS_StorageMgr', '../platform/resourcemgr/storage/storageResMgr.jsp', 2, '321.10081.', '1', 321, 'platform_storageMgr', null, '0', 'sysadmin', '2014-07-14 11:40:19');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10121, 'default', 'PAS_4000_PasVariable', '系统变量', 'function', 'PAS_pasVarMgr', '../platform/srvmonitor/variables/pasVarsMgr.jsp', 2, '10022.10121.', '1', 10022, 'platform_pasVarsMgr', null, '0', 'sysadmin', '2015-01-23 13:41:50');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10141, 'default', '01_PAS_innerApp', '平台应用', 'function', 'PAS_innerAppMgr', '../platform/appmgr/inner/innerAppMgr.jsp', 2, '141.10141.', '1', 141, 'platform_innerAppMgr', null, '0', 'sysadmin', '2014-07-14 11:39:28');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10201, 'default', 'PAS_2060_mail', 'Mail服务', 'function', 'PAS_Mail', '../platform/srvmgr/mail/mailClusterMgr.jsp', 2, '42.10201.', '1', 42, 'platform_mailClusterMgr', null, '0', 'sysadmin', '2015-01-23 11:53:55');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10203, 'default', 'App_01_AppOpen', '应用开通', null, '', '', 1, '10203.', '0', null, null, null, '0', 'sysadmin', '2014-06-03 12:36:57');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10204, 'default', '01_app_myorder', '我的订单', 'function', 'App_myOrder', '../app/order/myOrderMgr.jsp', 2, '10203.10204.', '1', 10203, null, null, '0', 'sysadmin', '2014-06-03 10:17:26');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10205, 'default', 'App_02_AppManage', '应用管理', null, null, null, 1, '10205.', '0', null, null, null, '0', 'sysadmin', '2014-06-03 10:39:02');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10206, 'default', 'App_03_AppMonitor', '应用监控', null, null, null, 1, '10206.', '0', null, null, null, '0', 'sysadmin', '2014-06-03 10:39:40');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10207, 'default', 'App_04_DataManage', '数据库管理', 'function', '', '', 1, '10207.', '0', null, null, null, '0', 'sysadmin', '2014-06-27 10:15:41');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10208, 'default', '01_app_appdeploy', '应用部署', 'function', 'App_appDeploy', '../app/deploy/listApplications.jsp', 2, '10205.10208.', '1', 10205, null, null, '0', 'sysadmin', '2014-06-16 10:01:36');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10209, 'default', '01_app_appmonitorstate', '监控状态', 'function', 'App_monitorState', '../app/monitor/remark/showAppMonitor.jsp', 2, '10206.10209.', '1', 10206, null, null, '0', 'sysadmin', '2014-06-16 10:02:05');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10210, 'default', '01_app_datamanage', '数据维护', 'function', 'App_dataManage', '../app/data/mySqlAdmin.jsp', 2, '10207.10210.', '1', 10207, null, null, '0', 'sysadmin', '2014-06-16 10:01:51');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10211, 'default', 'PAS_1010_ServiceMonitor', '缓存监控', 'function', 'PAS_ServiceMonitor', '../platform/srvmonitor/memcached/showServiceMonitor.jsp', 2, '10022.10211.', '1', 10022, 'platform_serverMonitor', null, '0', 'sysadmin', '2015-01-23 14:03:43');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10212, 'default', '02_app_myApp', '我的应用', 'function', 'App_myApplication', '../app/application/myAppMgr.jsp', 2, '10203.10212.', '1', 10203, null, null, '0', 'sysadmin', '2014-06-16 10:08:16');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10213, 'default', '03_app_myService', '我的服务', 'function', 'App_myService', '../app/service/myServicesMgr.jsp', 2, '10203.10213.', '1', 10203, null, null, '0', 'sysadmin', '2014-06-16 10:08:52');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10214, 'default', '02_app_appControl', '应用控制', 'function', 'App_appControl', '../app/control/listApplications.jsp', 2, '10205.10214.', '1', 10205, null, null, '0', 'sysadmin', '2014-06-16 10:09:35');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10215, 'default', '03_app_appSetting', '应用设置', 'function', 'App_appSetting', '../app/setting/appSetting.jsp', 2, '10205.10215.', '1', 10205, null, null, '0', 'sysadmin', '2014-06-16 10:10:03');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10216, 'default', '02_app_appLog', '日志查看', 'function', 'App_appLog', '../app/logs/appLog.jsp', 2, '10206.10216.', '1', 10206, null, null, '0', 'sysadmin', '2014-06-16 10:11:07');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10217, 'default', '03_app_stretchStrategy', '伸缩策略', 'function', 'App_stretchStrategy', '../app/scale/appStretchStrategy.jsp', 2, '10206.10217.', '1', 10206, null, null, '0', 'sysadmin', '2014-06-16 10:11:45');
INSERT INTO cap_menu (MENU_ID, TENANT_ID, MENU_CODE, MENU_NAME, LINK_TYPE, LINK_RES, LINK_ACTION, MENU_LEVEL, MENU_SEQ, ISLEAF, PARENT_MENU_ID, IMAGEPATH, EXPANDPATH, OPENMODE, CREATEUSER, CREATETIME) 
	VALUES (10224, 'default', 'PAS_1020_AppServiceMonitor', '服务监控告警', 'function', 'PAS_AppServiceMonitor', '../platform/srvmonitor/appservice/serviceMonitorAndAlarm.jsp', 2, '10022.10224.', '1', 10022, 'platform_appServiceMonitor', null, '0', 'sysadmin', '2015-01-23 13:39:11');

DROP TABLE if exists cap_partyauth;

CREATE TABLE cap_partyauth (
	TENANT_ID varchar(64) NOT NULL, 
	ROLE_ID varchar(64) NOT NULL, 
	ROLE_TYPE varchar(64) NOT NULL, 
	PARTY_ID varchar(64) NOT NULL, 
	PARTY_TYPE varchar(64) NOT NULL, 
	CREATEUSER varchar(64), 
	CREATETIME timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, 
	PRIMARY KEY (ROLE_ID, ROLE_TYPE, PARTY_TYPE, PARTY_ID)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_partyauth (TENANT_ID, ROLE_ID, ROLE_TYPE, PARTY_ID, PARTY_TYPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'sysadmin', 'user', null, '2013-01-23 08:15:12');
INSERT INTO cap_partyauth (TENANT_ID, ROLE_ID, ROLE_TYPE, PARTY_ID, PARTY_TYPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'paas', 'user', 'sysadmin', '2013-12-04 09:33:33');
INSERT INTO cap_partyauth (TENANT_ID, ROLE_ID, ROLE_TYPE, PARTY_ID, PARTY_TYPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'admin', 'user', 'sysadmin', '2013-12-04 09:33:33');

DROP TABLE if exists cap_resauth;

CREATE TABLE cap_resauth (
	TENANT_ID varchar(64) NOT NULL, 
	PARTY_ID varchar(64) NOT NULL, 
	PARTY_TYPE varchar(32) NOT NULL, 
	RES_ID varchar(255) NOT NULL, 
	RES_TYPE varchar(32) NOT NULL, 
	RES_STATE varchar(512) NOT NULL, 
	PARTY_SCOPE char(1) DEFAULT '0', 
	CREATEUSER varchar(64), 
	CREATETIME timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, 
	PRIMARY KEY (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_TYPE, RES_ID)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_authEmps', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_authFunctions', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_authManage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_authManagePage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_authUsers', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_functionAdd', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_functionEdit', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_functionManage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_functionManagePage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_infoUpdate', 'function', '1', '0', 'sysadmin', '2013-03-11 10:01:49');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuAdd', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuBindingFunc', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuBindingOther', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuEdit', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuManage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_menuManagePage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_passwordChange', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_passwordChangePage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_roleAdd', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_roleEdit', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_userAdd', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_userManage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'COF_userManagePage', 'function', '1', '0', null, '2013-01-23 07:01:26');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'InfoUpdate', 'function', '1', '0', 'sysadmin', '2013-02-25 07:12:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_empAdd', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_empEdit', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_empManage', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_empQuery', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_orgAdd', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_orgEdit', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_orgEmpManage', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_orgManage', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '1', 'role', 'Party_orgQuery', 'function', '1', '0', null, '2013-01-23 07:03:33');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'COF_infoUpdate', 'function', '1', '0', null, '2014-06-05 10:42:07');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'COF_passwordChange', 'function', '1', '0', null, '2014-06-05 10:42:07');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'PAS_innerAppMgr', 'function', '1', '0', 'sysadmin', '2013-12-07 03:51:39');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'PAS_orderMgr', 'function', '1', '0', 'sysadmin', '2013-05-07 02:30:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '101', 'role', 'PAS_regMgr', 'function', '1', '0', 'sysadmin', '2013-05-07 02:30:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'COF_infoUpdate', 'function', '1', '0', null, '2014-06-05 10:42:07');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'COF_passwordChange', 'function', '1', '0', null, '2014-06-05 10:42:07');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_apiAuthMgr', 'function', '1', '0', 'sysadmin', '2014-12-01 20:38:58');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_apiOrderMgr', 'function', '1', '0', 'sysadmin', '2014-12-01 20:38:59');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_apiSrvMgr', 'function', '1', '0', 'sysadmin', '2014-12-01 20:38:59');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_appMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_APPMonitor', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_AppServiceMonitor', 'function', '1', '0', 'sysadmin', '2014-12-30 09:26:58');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_CEPMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_CollectorMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_HaproxyMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_HostMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_HostMonitor', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_JettyMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_TomcatMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_RedisMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_RedisSentinelMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_GatewayMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_HadoopMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_JobCtrlMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_EsbMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_MsgQueueMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_KeepalivedMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_Log', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_Mail', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_MemcachedMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_MysqlMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_NginxMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_OpenAPIMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_pasVarMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_ServiceMonitor', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:28');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_StorageMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_stretchStrategy', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_SvnMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_taskMonitor', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:29');
INSERT INTO cap_resauth (TENANT_ID, PARTY_ID, PARTY_TYPE, RES_ID, RES_TYPE, RES_STATE, PARTY_SCOPE, CREATEUSER, CREATETIME) 
	VALUES ('default', '102', 'role', 'PAS_VIPMgr', 'function', '1', '0', 'sysadmin', '2014-05-30 06:50:29');

DROP TABLE if exists cap_role;

CREATE TABLE cap_role 
	(ROLE_ID varchar(64) NOT NULL, 
	TENANT_ID varchar(64) NOT NULL, 
	ROLE_CODE varchar(64) NOT NULL, 
	ROLE_NAME varchar(64), ROLE_DESC varchar(255), 
	CREATEUSER varchar(64), 
	CREATETIME timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, 
	PRIMARY KEY (ROLE_ID), CONSTRAINT I_CAP_ROLE UNIQUE (TENANT_ID, ROLE_CODE)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_role (ROLE_ID, TENANT_ID, ROLE_CODE, ROLE_NAME, ROLE_DESC, CREATEUSER, CREATETIME) 
	VALUES ('1', 'default', 'sysadmin', '系统管理员', '系统管理员', null, '2013-01-23 08:15:12');
INSERT INTO cap_role (ROLE_ID, TENANT_ID, ROLE_CODE, ROLE_NAME, ROLE_DESC, CREATEUSER, CREATETIME) 
	VALUES ('101', 'default', 'operation', '运营人员', '平台业务运营人员', 'sysadmin', '2013-12-07 05:19:21');
INSERT INTO cap_role (ROLE_ID, TENANT_ID, ROLE_CODE, ROLE_NAME, ROLE_DESC, CREATEUSER, CREATETIME) 
	VALUES ('102', 'default', 'maintenance', '运维人员', '平台系统运维人员', 'sysadmin', '2014-05-30 06:49:56');

DROP TABLE if exists cap_user;

CREATE TABLE cap_user 
	(OPERATOR_ID varchar(64) NOT NULL, 
	TENANT_ID varchar(64) NOT NULL, 
	USER_ID varchar(64) NOT NULL, 
	PASSWORD varchar(100), 
	USER_NAME varchar(64), 
	GENDER varchar(1), 
	TEL varchar(45), 
	PHONE varchar(45) NOT NULL, 
	EMAIL varchar(128) NOT NULL, 
	ADDRESS varchar(255), 
	STATUS varchar(16) NOT NULL, 
	LASTLOGIN timestamp DEFAULT '0000-00-00 00:00:00' NULL, 
	CREATETIME timestamp DEFAULT '0000-00-00 00:00:00' NULL, 
	CREATEUSER varchar(64), 
	NOTES varchar(100), 
	PRIMARY KEY (OPERATOR_ID), CONSTRAINT I_CAP_USER UNIQUE (USER_ID, TENANT_ID)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cap_user (OPERATOR_ID, TENANT_ID, USER_ID, PASSWORD, USER_NAME, GENDER, TEL, PHONE, EMAIL, ADDRESS, STATUS, LASTLOGIN, CREATETIME, CREATEUSER, NOTES) 
	VALUES ('100', 'default', 'sysadmin', 'ZwsUcorZkCrsujLiL6T2vQ==', '管理员', 'm', '1111111', '11111111111', 'sysadmin@primeton.com', 'Primeton', '4', '2014-05-30 06:42:32', '2014-05-30 06:42:32', 'sysadmin', 'Primeton');
INSERT INTO cap_user (OPERATOR_ID, TENANT_ID, USER_ID, PASSWORD, USER_NAME, GENDER, TEL, PHONE, EMAIL, ADDRESS, STATUS, LASTLOGIN, CREATETIME, CREATEUSER, NOTES) 
	VALUES ('101', 'default', 'admin', 'ZwsUcorZkCrsujLiL6T2vQ==', '运维管理员', 'f', '0000000', '00000000000', 'admin@primeton.com', 'Primeton', '4', '2014-06-03 04:38:44', '2014-06-03 04:38:44', 'sysadmin', 'Primeton');
