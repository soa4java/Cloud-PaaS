insert into cld_config_module (MODULE_ID, MODULE_NAME, APP_TYPE, IS_GLOBAL) values (12055, 'threadPool', null, 1);
insert into cld_config_module (MODULE_ID, MODULE_NAME, APP_TYPE, IS_GLOBAL) values (12056, 'systemLog', null, 1);
insert into cld_config_module (MODULE_ID, MODULE_NAME, APP_TYPE, IS_GLOBAL) values (12057, 'userLog', 'default', 0);
insert into cld_config_module (MODULE_ID, MODULE_NAME, APP_TYPE, IS_GLOBAL) values (12058, 'dataSource', 'default', 0);
insert into cld_config_module (MODULE_ID, MODULE_NAME, APP_TYPE, IS_GLOBAL) values (12059, 'variable', 'default', 0);


insert into cld_config_item (ITEM_ID, MODULE_ID, CONFIG_KEY, CONFIG_VALUE) 
	values (12055, 12055, 'default', '{"data":{"poolName":"threadPool1","minPoolSize":10,"maxPoolSize":200,"shrinkTime":2000},"type":"com.primeton.paas.app.config.model.ThreadPoolModel"}');
insert into cld_config_item (ITEM_ID, MODULE_ID, CONFIG_KEY, CONFIG_VALUE) 
	values (12056, 12056, 'default', '{"data":{"logLevel":"INFO"},"type":"com.primeton.paas.app.config.model.SystemLogModel"}');
insert into cld_config_item (ITEM_ID, MODULE_ID, CONFIG_KEY, CONFIG_VALUE) 
	values (12057, 12057, 'default', '{"data":{"userLogs":{"user":"INFO"}},"type":"com.primeton.paas.app.config.model.UserLogModel"}');
insert into cld_config_item (ITEM_ID, MODULE_ID, CONFIG_KEY, CONFIG_VALUE) 
	values (12058, 12058, 'default', '{"data":{"dataSourceName":"default","dataSourceId":"-1","dbServiceName":"MySQL","dataSourceDesc":"template","initialPoolSize":"5","minPoolSize":"5","maxPoolSize":"10","acquireRetryAttempts":"30","acquireRetryDelay":"1000","acquireIncrement":"3","checkoutTimeout":"0","idleConnectionTestPeriod":"3600000","testSQL":null},"type":"com.primeton.paas.app.config.model.DataSourceModel"}');
insert into cld_config_item (ITEM_ID, MODULE_ID, CONFIG_KEY, CONFIG_VALUE) 
	values (12059, 12059, 'default', '{"data":{"name":"default","value":"value","valueType":"String","desc":"template"},"type":"com.primeton.paas.app.config.model.VariableModel"}');


insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J001', 'Redhat-1C2G', '2013091610295010', '2012090100000006', 'GB', 1, 2, 0, 'Redhat-Enterprise', '6.4');
insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J002', 'Redhat-2C2G', '2013091610295010', '2012090100000007', 'GB', 2, 2, 0, 'Redhat-Enterprise', '6.4');
insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J003', 'Redhat-2C4G', '2013091610295010', '2012090100000008', 'GB', 2, 4, 0, 'Redhat-Enterprise', '6.4');

insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J011', 'SUSE-1C2G', '2013091610295011', '2012090100000006', 'GB', 1, 2, 0, 'SUSE-Enterprise', '11G_SP2');
insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J012', 'SUSE-2C2G', '2013091610295011', '2012090100000007', 'GB', 2, 2, 0, 'SUSE-Enterprise', '11G_SP2');
insert into PAS_HOST_TEMPLATE (templateId, templateName, imageId, profileId, unit, cpu, memory, storage, osName, osVersion)
	values ('20130517J013', 'SUSE-2C4G', '2013091610295011', '2012090100000008', 'GB', 2, 4, 0, 'SUSE-Enterprise', '11G_SP2');


insert into pas_stretch_strategy (strategy_name, strategy_type, isEnable, stretch_size, continued_time, ignore_time) 
	values ('global_strategy', 'DECREASE', false, 1, 30, 60);
insert into pas_stretch_strategy (strategy_name, strategy_type, isEnable, stretch_size, continued_time, ignore_time) 
	values ('global_strategy', 'INCREASE', false, 1,10, 30);

insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'DECREASE', 'CPU', 20);
insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'DECREASE', 'MEMORY', 20);
insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'DECREASE', 'LB', 0.5);
insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'INCREASE', 'CPU', 80);
insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'INCREASE', 'MEMORY', 80);
insert into pas_stretch_strategy_item (strategy_name, strategy_type, item_type, threshold) values ('global_strategy', 'INCREASE', 'LB', 1.5);

insert into pas_app_stretch_strategy (app_name, strategy_name) values ('global_strategy', 'global_strategy');


insert into pas_user (ID, USER_ID, PASSWORD, USER_NAME, GENDER, TEL, PHONE, EMAIL, STATUS, ADDRESS, UNLOCKTIME, LASTLOGIN, ERRCOUNT, CREATETIME, NOTES, HANDLER) 
	VALUES (1000, 'paas', 'ZwsUcorZkCrsujLiL6T2vQ==', 'ZhongWen.Li', 'M', '15618332941', '15618332941', '15618332941@163.com', '4', '15618332941', null, '2015-05-18 17:03:02', null, '2015-11-11 11:11:11', null, null);


INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('paas_home', '/primeton/paas', 'PAAS Platform Home Path');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('wait_for_message_time', '120000', '等待Agent消息响应最大时间(ms)');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('send_message_retry_times', '3', '发送消息失败后重试次数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('http_repos_url', 'http://30.10.2.65:7399/services', 'HTTP软件仓库地址');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('clean_after_process_error', 'true', 'true|false,服务创建异常后是否需要清理');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('haproxy_conn_timeout', '3600', 'Haproxy服务连接超时时间,单位：ms');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('haproxy_health_url', '/', 'Haproxy服务的健康检查页面');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('haproxy_max_conn_size', '50', 'Haproxy服务的最大连接数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('haproxy_protocal', 'http', 'Haproxy服务的默认协议,Http协议');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('haproxy_rel_srvType', 'Jetty,Tomcat,OpenAPI,CardBin,SMS', 'HaProxy可关联服务类型');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Jetty_20130517J001_inst', '2', '小型主机最大Jetty实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Jetty_20130517J002_inst', '4', '标准主机最大Jetty实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Jetty_20130517J003_inst', '8', '大型主机最大Jetty实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MySQL_20130517J001_inst', '1', '小型主机最大MySQL实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MySQL_20130517J002_inst', '1', '标准主机最大MySQL实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MySQL_20130517J003_inst', '1', '大型主机最大MySQL实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Memcached_20130517J001_inst', '8', '小型主机最大Memcached实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Memcached_20130517J002_inst', '16', '标准主机最大Memcached实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Memcached_20130517J003_inst', '32', '大型主机最大Memcached实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SVN_20130517J001_inst', '1', '小型主机最大SVN实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SVN_20130517J002_inst', '1', '标准主机最大SVN实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SVN_20130517J003_inst', '1', '大型主机最大SVN实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Nginx_20130517J001_inst', '1', '小型主机最大Nginx实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Nginx_20130517J002_inst', '1', '标准主机最大Nginx实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Nginx_20130517J003_inst', '1', '大型主机最大Nginx实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_HaProxy_20130517J001_inst', '5', '小型主机最大HaProxy实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_HaProxy_20130517J002_inst', '10', '标准主机最大HaProxy实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_HaProxy_20130517J003_inst', '20', '大型主机最大HaProxy实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CardBin_20130517J001_inst', '1', '小型主机最大CardBin实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CardBin_20130517J002_inst', '1', '标准主机最大CardBin实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CardBin_20130517J003_inst', '1', '大型主机最大CardBin实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_OpenAPI_20130517J001_inst', '2', '小型主机最大OpenAPI实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_OpenAPI_20130517J002_inst', '4', '标准主机最大OpenAPI实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_OpenAPI_20130517J003_inst', '6', '大型主机最大OpenAPI实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Collector_20130517J001_inst', '1', '小型主机最大Collector实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Collector_20130517J002_inst', '1', '标准主机最大Collector实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Collector_20130517J003_inst', '1', '大型主机最大Collector实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Keepalived_20130517J001_inst', '1', '小型主机最大Keepalived实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Keepalived_20130517J002_inst', '1', '标准主机最大Keepalived实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Keepalived_20130517J003_inst', '1', '大型主机最大Keepalived实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Mail_20130517J001_inst', '1', '小型主机最大Mail实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Mail_20130517J002_inst', '1', '标准主机最大Mail实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Mail_20130517J003_inst', '1', '大型主机最大Mail实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SMS_20130517J001_inst', '1', '小型主机最大SMS实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SMS_20130517J002_inst', '1', '标准主机最大SMS实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_SMS_20130517J003_inst', '1', '大型主机最大SMS实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CEPEngine_20130517J001_inst', '1', '小型主机最大CEPEngine实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CEPEngine_20130517J002_inst', '1', '标准主机最大CEPEngine实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_CEPEngine_20130517J003_inst', '1', '大型主机最大CEPEngine实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Tomcat_20130517J001_inst', '2', '小型主机最大Tomcat实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Tomcat_20130517J002_inst', '4', '标准主机最大Tomcat实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Tomcat_20130517J003_inst', '8', '大型主机最大Tomcat实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Redis_20130517J001_inst', '2', '小型主机最大Redis实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Redis_20130517J002_inst', '4', '标准主机最大Redis实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_Redis_20130517J003_inst', '8', '大型主机最大Redis实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_JobCtrl_20130517J001_inst', '1', '小型主机最大JobCtrl实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_JobCtrl_20130517J002_inst', '1', '标准主机最大JobCtrl实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_JobCtrl_20130517J003_inst', '1', '大型主机最大JobCtrl实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbServer_20130517J001_inst', '1', '小型主机最大EsbServer实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbServer_20130517J002_inst', '1', '标准主机最大EsbServer实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbServer_20130517J003_inst', '1', '大型主机最大EsbServer实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSSM_20130517J001_inst', '1', '小型主机最大EsbSSM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSSM_20130517J002_inst', '1', '标准主机最大EsbSSM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSSM_20130517J003_inst', '1', '大型主机最大EsbSSM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSAM_20130517J001_inst', '1', '小型主机最大EsbSAM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSAM_20130517J002_inst', '1', '标准主机最大EsbSAM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbSAM_20130517J003_inst', '1', '大型主机最大EsbSAM实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbConsole_20130517J001_inst', '1', '小型主机最大EsbConsole实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbConsole_20130517J002_inst', '1', '标准主机最大EsbConsole实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_EsbConsole_20130517J003_inst', '1', '大型主机最大EsbConsole实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MsgQueue_20130517J001_inst', '1', '小型主机最大MsgQueue实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MsgQueue_20130517J002_inst', '1', '标准主机最大MsgQueue实例数');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('max_MsgQueue_20130517J003_inst', '1', '大型主机最大MsgQueue实例数');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Nginx_timeout', '900000', '15m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_HaProxy_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_SVN_timeout', '900000', '15m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Memcached_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Keepalived_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_CardBin_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Collector_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_OpenAPI_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Mail_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_SMS_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Jetty_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_MySQL_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Tomcat_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Redis_timeout', '300000', '5m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Hadoop_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_Gateway_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_JobCtrl_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_EsbServer_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_EsbSSM_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_EsbSAM_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_EsbConsole_timeout', '600000', '10m');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('install_MsgQueue_timeout', '600000', '10m');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('app_cache_size', '128,256,1024,2048,4096,8192', '应用缓存大小配置,单位:MB');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('app_server_cluster_max_num', '10', '应用服务器最大集群限制');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('nas_storage_size', '2,4,8,16,32,64,128,256,512,1024,2048', 'NAS存储大小范围,单位:GB');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('memcached_slice_size', '128', '缓存服务内存切片大小,单位:MB');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('strategy_duration_opts', '5,10,30,60', '触发伸缩策略持续时间,单位:分钟');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('cpu_usage_opts', '20,40,60,80', 'CPU使用率的范围,单位:%');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('memory_usage_opts', '20,40,60,80', 'Memory使用率的范围,单位:%');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('lb_balance_opts', '0.5,1.0,1.5,2.0', '负载使用的范围');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('stretch_scale_opts', '1,2,3,4', '伸缩幅度');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('ignore_time_opts', '15,30,45,60', '伸缩后伸缩策略休眠时间,单位:分钟');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('jetty_storage_path', '/storage/app', 'Jetty的存储路径配置');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('mysql_storage_path', '/storage/db', 'MySQL的存储路径配置');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('mysql_characterSets', 'utf8,gbk,gb2312', 'mysql字符集');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('phpmyadmin_url', 'http://30.10.2.66:7080/index.php', 'phpMyAdmin URL');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('paas_log_root_path', '/primeton/paas/workspace/application', 'Collector storage application logs');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('cloud_dev_tool_file', 'sdk.zip', '集成开发工具包名称');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('web_app_domain_postfix', '.paas.primeton.com', 'PAAS应用的域名后缀');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('paas_war_root_path', '/primeton/paas/workspace/wars', '应用war包根路径');


INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_web_service_url', 'http://192.168.100.1:80/default/ResourceService?wsdl', 'IAAS WS URL');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_user_name', 'kitty', '租户标识');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_user_password', '000000', '租户密码');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_biz_zone_id', '2013091610242115', '虚拟机业务区标识');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_default_group', 'PAAS_PLATFORM', '默认组名称');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_nas_zone_id', '2013122012541509', 'NAS存储业务区标识');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_enable_vm', 'false', '是否启用IaaS-VM虚拟化接口');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('iaas_enable_storage', 'false', '是否启用IaaS-Storage虚拟化接口');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_pool_decrease_size', '1,2,3,4,5,6,7,8,9,10', '缩减步长大小配置,单位:块');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_pool_increase_size', '1,2,3,4,5,6,7,8,9,10', '增长步长大小配置,单位:块');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_pool_max_size', '1,2,3,4,5,6,7,8,9,10,20,30,40,50,100,200,300,400,500', '资源上限大小配置,单位:块');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_pool_min_size', '1,2,3,4,5,6,7,8,9,10,20,30,40,50,100,200,300,400,500', '资源下限大小配置,单位:块');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_pool_time_interval', '10,30,60,90,120,150', '轮询时间间隔配置,单位:秒');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_operate_timeout', '600000', '存储操作超时时间(默认10分钟)');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('storage_destroy_timeout', '300000', '存储操作销毁时间(默认5分钟)');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_pool_decrease_size', '1,2,3,4,5,6,7,8,9,10', '缩减步长大小配置,单位:台');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_pool_increase_size', '1,2,3,4,5,6,7,8,9,10', '增长步长大小配置,单位:台');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_pool_max_size', '1,2,3,4,5,6,7,8,9,10,20,30,40,50,100,200,300,400,500', '资源上限大小配置,单位:台');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_pool_min_size', '1,2,3,4,5,6,7,8,9,10,20,30,40,50,100,200,300,400,500', '资源下限大小配置,单位:台');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_pool_time_interval', '10,30,60,90,120,150', '轮询时间间隔配置,单位:秒');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_operate_timeout', '600000', '虚拟机操作超时时间(默认10分钟)');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('vm_destroy_timeout', '300000', '虚拟机销毁超时时间(默认5分钟)');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('keep_last_time_data', '60', '保留最近一段时间的监控数据（单位：分钟）');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('app_monitor_interval', '10', 'eps语句[...time_batch(? sec)]求应用监控数据平均值（单位：秒）');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('monitor_refresh_times', '15,30,45,60', '监控刷新时间间隔');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('monitor_time_ago', '30000', '默认监控图表展示30秒前的数据');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('monitor_enable', 'true', '是否启用监控数据处理模块');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('service_stretch_timeout', '900', '服务伸缩长任务的超时时间,单位：秒');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('nginx_default_name', 'Nginx-default', '平台Nginx服务的默认名称');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('ssl_certificate_path', '/storage/ssl', 'Nginx SSL认证证书存放目录');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('ssl_certificate_temp_path', '/primeton/paas/workspace/ssl', 'Nginx SSL认证证书临时存放目录');

INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('hadoop_management_protal', 'http://192.168.0.21:7180', 'Hadoop management portal');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('hadoop_management_user', 'admin', 'Hadoop management user');
INSERT INTO pas_system_config (COL_KEY, COL_VALUE, COL_DESC) VALUES ('hadoop_management_password', 'admin', 'Hadoop management password');
