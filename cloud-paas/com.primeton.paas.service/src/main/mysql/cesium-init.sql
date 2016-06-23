INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('CLD_HOST_USED', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('CLD_INSTANCE_SERVICE', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('CLD_CONFIG_MODULE', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('CLD_CONFIG_ITEM', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('CLD_CLUSTER', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('PAS_USER', 20000, 1000, 20000);
INSERT INTO `cld_id_table` (`TABLE_NAME`, `START_ID`, `INCREMENT`, `NEXT_ID`) VALUES ('PAS_ORDER', 20000, 1000, 20000);


INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Jetty', 'Jetty', 'Jetty', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('MySQL', 'MySQL', 'MySQL', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Memcached', 'Memcached', 'Memcached', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('SVN', 'SVN', 'SVN', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('SVNRepository', 'SVNRepository', 'SVN', 'LOGIC');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Nginx', 'Nginx', 'Nginx', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('HaProxy', 'HaProxy', 'HaProxy', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('CardBin', 'CardBin', 'CardBin', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('War', 'War', 'War', 'LOGIC');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('OpenAPI', 'OpenAPI', '', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Collector', 'Collector', '', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Keepalived', 'Keepalived', '', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Mail', 'Mail', '', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('SMS', 'SMS', 'SMS', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('CEPEngine', 'CEPEngine', 'CEPEngine', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Tomcat', 'Tomcat', 'Tomcat', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Redis', 'Redis', 'Redis', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('RedisSentinel', 'RedisSentinel', 'RedisSentinel', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Hadoop', 'Hadoop', 'Hadoop', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Gateway', 'Gateway', 'Gateway', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('JobCtrl', 'JobCtrl', 'JobCtrl', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('Esb', 'Esb', 'Esb', 'LOGIC');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('EsbServer', 'EsbServer', 'EsbServer', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('EsbSSM', 'EsbSSM', 'EsbSSM', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('EsbSAM', 'EsbSAM', 'EsbSAM', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('EsbConsole', 'EsbConsole', 'EsbConsole', 'PHYSICAL');
INSERT INTO `cld_service` (`SERVICE_NAME`, `SERVICE_DISPLAY_NAME`, `SERVICE_DESC`, `SERVICE_MODE`) VALUES ('MsgQueue', 'MsgQueue', 'MsgQueue', 'PHYSICAL');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('HOST', 'packageId', '套餐标识', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('HOST', 'id', '虚拟机唯一标识', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('HOST', 'assigned', '是已分配', 'boolean', 'true:已分配 | false：未分配');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('HOST', 'standalone', '独占', 'boolean', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('APP', 'type', '付费类型', 'String', 'charge | free');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('APP', 'state', '应用状态', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('APP', 'secondaryDomain', '二级域名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('APP', 'personalizedDomain', '个性化域名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('APP', 'appType', '应用类型', 'String', 'inner | outer');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'minSize', '最小实例个数', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'maxSize', '最大实例个数', 'int', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'version', '部署版本', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'storageSize', '共享存储大小', 'int', 'GB');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'storagePath', '共享存储路径', 'String', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'user', '用户名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'password', '密码', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'schema', '数据库名称', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'jdbcDriver', '数据库驱动', 'String', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'currentWarVersion', '当前部署War的版本', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'revision', '应用包资源库版本', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'repoFile', '应用包文件名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'description', '服务描述', 'String', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'domain', '域名', 'String', '如：erp.unionpay.com');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'members', '集群成员列表', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'protocolType', 'HTTP/HTTPS/HTTP-HTTPS', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'sslCertificate', 'SSL Certificate file name', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'sslCertificateKey', 'SSL Certificate Key file name', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'sslCaCertificate', 'SSL Ca Certificate file name', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'sslLevel', 'SSL Level', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'isEnableDomain', '是否启用域名', 'String', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('CLUSTER', 'memcachedSize', '缓存大小', 'int', '');


INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'mode', 'MODE', 'String', 'PHSICAL | LOGIC');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'user', '用户名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'password', '密码', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'storagePath', '存储路径', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'storageSize', '存储大小', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'state', '开通状态', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'haMode', '高可用模式', 'String', 'master | slave | cluster | block');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'createdBy', '创建者', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'createdDate', '创建时间', 'Date', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'maxConnectionSize', '最大连接数', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'minMemory', '最小内存大小', 'int', '1/8 主机物理内存');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'maxMemory', '最大内存大小', 'int', '1/4 主机物理内存');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'minPermSize', '最小Perm内存大小', 'int', '1/16 主机物理内存');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'maxPermSize', '最大Perm内存大小', 'int', '1/8 主机物理内存');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcDriver', 'JDBC 驱动', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcUrl', 'JDBC 连接Url', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcUser', 'JDBC 用户名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcPassword', 'JDBC 密码', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcMinPoolSize', 'JDBC 连接池最小数', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'jdbcMaxPoolSize', 'JDBC 连接池最大数', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'packageId', '套餐标识', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'storageId', '共享存储标识', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'isStandalone', '独占', 'boolean', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'worker_processes', 'worker_processes', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'worker_connections', 'worker_connections', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'keepalive_timeout', 'keepalive_timeout', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'types_hash_max_size', 'types_hash_max_size', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'allow_access_hosts', 'allow_access_hosts', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'client_max_body_size', 'client_max_body_size', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'sslCertificatePath', 'sslCertificatePath', 'String', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'appName', '应用名', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'vmArgs', 'Jetty的VM启动参数', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'enableSession', '是否启用分布式会话服务', 'Boolean', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'minThreadSize', '线程池最小值', 'Integer', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'maxThreadSize', '线程池最大值', 'Integer', '');

 INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'schema', '数据库名称', 'String', '');
 INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'characterSet', 'mysql字符集', 'String', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'memorySize', '内存大小', 'int', 'MB');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'repoRoot', '仓库根目录', 'String', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'repoName', '仓库名称', 'String', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'balance', '负载均衡策略', 'String', 'roundrobin | leastconn | source');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'httpCheck', 'Http检查地址', 'String', 'Http连接检查URI地址');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'timeout', 'Http连接超时', 'long', 'Http连接超时时间间隔');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'urlHealthCheckInterval', 'URL健康检查时间间隔', 'long', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'healthUrl', '健康检查的URL', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'sessionTimeout', '会话超时时间(m)', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'connTimeout', '连接超时时间', 'long', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'protocal', '协议类型', 'String', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'warVersion', 'War版本', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'submitTime', '提交时间', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'submitBy', '提交人', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'isDeployVersion', '是否已部署', 'boolean', '');

INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'notificationEmail', 'notificationEmail', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'notificationEmailFrom', 'notificationEmailFrom', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'smtpServer', 'smtpServer', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'smtpConnectTimeout', 'smtpConnectTimeout', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'routerId', 'routerId', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'vrrpScriptPath', 'vrrpScriptPath', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'vrrpScriptInterval', 'vrrpScriptInterval', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'vrrpScriptWeight', 'vrrpScriptWeight', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'vrrpState', 'vrrpState', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'interface', 'interface', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'virtualRouterId', 'virtualRouterId', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'mcastSrcIp', 'mcastSrcIp', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'priority', 'priority', 'int', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'advertInt', 'advertInt', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'authType', 'authType', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'authPass', 'authPass', 'String', '');
INSERT INTO `cld_attr` (`ATTR_TYPE`, `ATTR_NAME`, `ATTR_DISPLAY_NAME`, `DATA_TYPE`, `ATTR_DESC`) VALUES ('SERVICE', 'virtualIpAddress', 'virtualIpAddress', 'String', '');

INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'groupName', '引擎所在组的组名', 'java.lang.String', 'groupName');
INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'mqServer', '消息服务器名称', 'java.lang.String', 'mqServer');
INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'mqDests', '消息目的地：队列/路由名称', 'java.lang.String', 'mqDests');
INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'mqTypes', '消息目的地类型：队列/路由', 'java.lang.String', 'mqTypes');


INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'maxMailWorkerNum', 'maxMailWorkerNum', 'int', '最大邮件转发工作线程数');


INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'logRoot', '日志存放路径', 'java.lang.String', '日志存放路径');
INSERT INTO cld_attr (ATTR_TYPE, ATTR_NAME, ATTR_DISPLAY_NAME, DATA_TYPE, ATTR_DESC) VALUES ('SERVICE', 'appenderBuffer', 'log4j Appender Buffer size (B)', 'int', 'appenderBuffer');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'storageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'storagePath');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'storageSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'appName');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'maxMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'minMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'minPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'maxPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'vmArgs');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Jetty', 'enableSession');

INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'storagePath');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'storageSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'schema');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'user');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'password');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'characterSet');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('MySQL', 'isStandalone');

INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'memorySize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'maxConnectionSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Memcached', 'scope');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVN', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVN', 'repoRoot');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVN', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVN', 'isStandalone');

INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVNRepository', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVNRepository', 'repoName');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVNRepository', 'user');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVNRepository', 'password');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('SVNRepository', 'isStandalone');

INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'scope');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'worker_processes');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'worker_connections');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'keepalive_timeout');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'types_hash_max_size');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'allow_access_hosts');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'client_max_body_size');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('NginX', 'sslCertificatePath');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'urlHealthCheckInterval');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'healthUrl');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'maxConnectionSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'scope');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'connTimeout');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('HaProxy', 'protocal');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'warVersion');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'submitTime');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'submitBy');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'isDeployVersion');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('War', 'isStandalone');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'appName');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'maxMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'minMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'minPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'maxPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('OpenAPI', 'isStandalone');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'notificationEmail');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'smtpServer');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'smtpConnectTimeout');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'routerId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'vrrpScriptPath');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'vrrpScriptInterval');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'vrrpScriptWeight');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'vrrpState');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'interface');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'virtualRouterId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'mcastSrcIp');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'priority');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'advertInt');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'authType');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'authPass');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'virtualIpAddress');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'notificationEmailFrom');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Keepalived', 'isStandalone');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'maxMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'minMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'minPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'maxPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcDriver');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcUrl');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcUser');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcPassword');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcMinPoolSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'jdbcMaxPoolSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'maxMailWorkerNum');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Mail', 'isStandalone');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'maxMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'minMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'maxPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'groupName');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'mqServer');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'mqDests');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('CEPEngine', 'mqTypes');


INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'mode');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'maxMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'minMemory');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'maxPermSize');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'packageId');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'isStandalone');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'groupName');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'mqServer');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'mqDests');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'mqTypes');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'logRoot');
INSERT INTO `cld_service_attr_rel` (`SERVICE_NAME`, `ATTR_NAME`) VALUES ('Collector', 'appenderBuffer');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'SCRIPT_HOME', '/primeton/paas/bin', '服务脚本的根目录');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('DEFAULT', 'waiting_agent_response_timeout', '300000', '等待消息响应结果超时时间(ms)');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('DEFAULT', 'enable_check_ext_property', 'false', '是否启用检查扩展属性合法性(true|false)');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('DEFAULT', 'illegal_ext_property_action', 'warn', '检查扩展属性不合法处理动作(warn|exception)');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Nginx', '80-81', 'Nginx服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_MySQL', '3306-3320', 'MySQL服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_MsgQueue', '5672-5680', 'MsgQueue服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_HaProxy', '7000-7200', 'HaProxy服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Jetty', '7200-7300', 'Jetty服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Tomcat', '7300-7400', 'Tomcat服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Hadoop', '7400-7500', 'Hadoop服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_OpenAPI', '7500-7520', 'OpenAPI服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_JobCtrl', '7520-7550', 'JobCtrl服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Redis', '8000-8100', 'Redis服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_RedisSentinel', '8100-8120', 'RedisSentinel服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Gateway', '8120-8150', 'Gateway服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_EsbServer', '8150-8170', 'EsbServer服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_EsbSSM', '8170-8180', 'EsbSSM服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_EsbSAM', '8180-8190', 'EsbSAM服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_EsbConsole', '8190-8200', 'EsbConsole服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_Memcached', '11000-12000', 'Memcached服务端口段');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('HOST', 'PORT_RANGE_SVN', '18880-18890', 'SVN服务端口段');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'mqServer', '{"data":{"attrs":{"username":"paas","vhost":"/paas/agent","password":"000000","timeout":"15000","url":"30.10.2.65:5672"}},"type":"org.gocom.cloud.cesium.manage.runtime.api.model.MQServer"}', 'agent');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.LOG.QueueGroup', '10000,10001,10002,10003,10004', '日志采集消息队列组');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.LOG.MQServer', '{"data":{"username":"paas","vhost":"/paas/log","password":"000000","timeout":"15000","url":"30.10.2.65:5672"},"type":"java.util.HashMap"}', 'log');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.Interval', '5000', '采集时间间隔(ms)');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.MQServer', '{"data":{"username":"paas","vhost":"/paas/monitor","password":"000000","timeout":"15000","url":"30.10.2.65:5672"},"type":"java.util.HashMap"}', 'monitor');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.Status', 'true', 'true | false');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.CEPInputType', 'exchange', 'exchange | queue');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.CEPInputter', 'EventEntrance', 'exchange or queue name');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MONITOR.Exchange', 'agentMonitorExchange', 'Agent monitor data entrance');

INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MAIL.MQServer', '{"data":{"username":"paas","vhost":"/paas/mail","password":"000000","timeout":"15000","url":"30.10.2.65:5672"},"type":"java.util.HashMap"}', 'mail');
INSERT INTO `cld_variable` (`VARIABLE_TYPE`, `VARIABLE_NAME`, `VARIABLE_VALUE`, `VARIABLE_DESC`) 
	VALUES ('RUNTIME', 'PAAS.MAIL.QueueGroup', '30000,30001,30002', '邮件转发消息队列组');
