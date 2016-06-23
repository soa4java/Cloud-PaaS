CREATE TABLE
    PAS_USER
    (
        ID bigint NOT NULL AUTO_INCREMENT,
        USER_ID VARCHAR(64) NOT NULL,
        PASSWORD VARCHAR(100),
        USER_NAME VARCHAR(64),
        GENDER VARCHAR(1),
        TEL VARCHAR(45),
        PHONE VARCHAR(45) NOT NULL,
        EMAIL VARCHAR(128) NOT NULL,
        STATUS VARCHAR(16) NOT NULL,
        ADDRESS VARCHAR(255),
        UNLOCKTIME TIMESTAMP NULL,
        LASTLOGIN TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON
    UPDATE
        CURRENT_TIMESTAMP NULL,
        ERRCOUNT DECIMAL,
        CREATETIME TIMESTAMP DEFAULT '0000-00-00 00:00:00' NULL,
        NOTES VARCHAR(100),
        HANDLER VARCHAR(64),
        PRIMARY KEY (ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_order
    (
        ORDER_ID VARCHAR(64) NOT NULL,
        ORDER_TYPE VARCHAR(64) NOT NULL,
        ORDER_STATUS VARCHAR(64) DEFAULT '0' NOT NULL,
        SUBMIT_TIME TIMESTAMP NULL,
        HANDLE_TIME TIMESTAMP NULL,
        FINISH_TIME TIMESTAMP NULL,
        OWNER VARCHAR(64),
        HANDLER VARCHAR(64),
        BEGIN_TIME TIMESTAMP NULL,
        END_TIME TIMESTAMP NULL,
        NOTES VARCHAR(512),
        PRIMARY KEY (ORDER_ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    PAS_ORDER_ITEM
    (
        ITEM_ID VARCHAR(64) NOT NULL,
        ORDER_ID VARCHAR(64) NOT NULL,
        ITEM_TYPE VARCHAR(64),
        ITEM_STATUS VARCHAR(16) DEFAULT '-1',
        HANDLE_TIME TIMESTAMP NULL,
        FINISH_TIME TIMESTAMP NULL,
        PRIMARY KEY (ITEM_ID),
        CONSTRAINT pas_order_item_fk1 FOREIGN KEY (ORDER_ID) REFERENCES pas_order (ORDER_ID),
        INDEX fk_order_item_idx (ORDER_ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
        
CREATE TABLE
    PAS_ITEM_ATTR
    (
        ID VARCHAR(64) NOT NULL,
        ITEM_ID VARCHAR(64) NOT NULL,
        ATTR_NAME VARCHAR(64),
        ATTR_VALUE VARCHAR(256),
        DESCRIPTION VARCHAR(512),
        PRIMARY KEY (ID),
        CONSTRAINT pas_item_attr_fk1 FOREIGN KEY (ITEM_ID) REFERENCES pas_order_item (ITEM_ID),
        INDEX fk_item_attr_idx (ITEM_ID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE 
     PAS_SYSTEM_CONFIG 
     ( 
        COL_KEY VARCHAR(200) NOT NULL, 
        COL_VALUE VARCHAR(512), 
        COL_DESC VARCHAR(512), 
        PRIMARY KEY (COL_KEY) 
     ) 
     ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    PAS_HOST_TEMPLATE
    (
        templateId VARCHAR(64) NOT NULL,
        templateName VARCHAR(256),
        imageId VARCHAR(128),
        profileId VARCHAR(128),
        unit VARCHAR(16),
        cpu INT(12),
        memory INT(12),
        storage INT(12),
        osName VARCHAR(256),
        osVersion VARCHAR(64),
        PRIMARY KEY (templateId)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE 
	`pas_shared_storage` 
	(
		`id` varchar(64) NOT NULL,
		`name` varchar(64) DEFAULT NULL,
		`path` varchar(256) DEFAULT NULL,
		`size` int(12) DEFAULT '0',
		`isAssigned` int(12) DEFAULT '0',
		PRIMARY KEY (`id`)
	) 
	ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `pas_storage_mounts` 
	(
		`id` varchar(64) NOT NULL,
		`ip` varchar(64) NOT NULL,
		`path` varchar(512) DEFAULT NULL,
 		`status` int(1) DEFAULT '0',
		PRIMARY KEY (`id`,`ip`)
	) 
	ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pas_task` 
	(
        `id` VARCHAR(64) NOT NULL,
        `type` VARCHAR(64) NOT NULL,
        `status` VARCHAR(20) NOT NULL,
        `timeout` bigint(13),
        `startTime` TIMESTAMP NULL,
        `finalTime` TIMESTAMP NULL,
        `finishTime` TIMESTAMP NULL,
        `exceptionTime` TIMESTAMP NULL,
        `abortTime` TIMESTAMP NULL,
        `handleResult` VARCHAR(512),
        `exception` text,
        `owner` VARCHAR(64),
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

    
CREATE TABLE `pas_vm_pool` 
	(
		`id` varchar(64) NOT NULL,
		`name` varchar(64) DEFAULT NULL,
		`minSize` int(10) DEFAULT '0',
		`maxSize` int(10) DEFAULT '0',
		`increaseSize` int(10) DEFAULT '0',
		`decreaseSize` int(10) DEFAULT '0',
		`retrySize` int(10) DEFAULT '0',
		`timeInterval` bigint(13) DEFAULT '0',
		`waitIncreaseTime` bigint(13) DEFAULT '0',
		`waitDecreaseTime` bigint(13) DEFAULT '0',
		`createTimeout` bigint(13) DEFAULT '0',
		`destroyTimeout` bigint(13) DEFAULT '0',
		`isEnable` int(10) DEFAULT '0',
		`remarks` varchar(512) DEFAULT NULL,
		PRIMARY KEY (`id`)
	) 
	ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pas_storage_pool` 
	(
		`id` varchar(64) NOT NULL COMMENT '唯一标识',
		`name` varchar(64) DEFAULT NULL COMMENT '显示名称',
		`storageSize` int(10) DEFAULT NULL COMMENT '存储配额大小',
		`minSize` int(10) DEFAULT NULL COMMENT '资源下限',
		`maxSize` int(10) DEFAULT NULL COMMENT '资源上限',
		`increaseSize` int(10) DEFAULT NULL COMMENT '增长步长',
		`decreaseSize` int(10) DEFAULT NULL COMMENT '缩减步长',
		`retrySize` int(10) DEFAULT '0',
		`timeInterval` bigint(13) DEFAULT NULL,
		`waitIncreaseTime` bigint(13) DEFAULT NULL COMMENT '等待创建存储时间',
		`waitDecreaseTime` bigint(13) DEFAULT NULL COMMENT '等待销毁存储时间',
		`createTimeout` bigint(13) DEFAULT NULL COMMENT '创建存储超时时间',
		`destroyTimeout` bigint(13) DEFAULT NULL COMMENT '销毁存储超时时间',
		`isEnable` int(11) DEFAULT NULL COMMENT '否启用资源池(默认是0未启动，1为启动)',
		`remarks` varchar(256) DEFAULT NULL COMMENT '备注',
		PRIMARY KEY (`id`)
	) 
	ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE
    pas_stretch_strategy
    (
        strategy_name VARCHAR(64) NOT NULL,
        strategy_type VARCHAR(64) NOT NULL,
        isEnable TINYINT(1) NOT NULL,
        stretch_size INT(10) NOT NULL,
        continued_time bigint(13),
        ignore_time bigint(13),
        PRIMARY KEY (strategy_name, strategy_type)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_stretch_strategy_item
    (
        strategy_name VARCHAR(64) NOT NULL,
        strategy_type VARCHAR(64) NOT NULL,
        item_type VARCHAR(64) NOT NULL,
        threshold VARCHAR(8),
        PRIMARY KEY (strategy_name, strategy_type, item_type)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_app_stretch_strategy
    (
        app_name VARCHAR(64) NOT NULL,
        strategy_name VARCHAR(64) NOT NULL,
        PRIMARY KEY (app_name)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 
	pas_host_monitor 
	(
		id varchar(64) NOT NULL, 
		ip varchar(32), 
		occur_time bigint(13) DEFAULT '0', 
		cpu_us float(4,1) DEFAULT '0.0', 
		cpu_sy float(4,1) DEFAULT '0.0', 
		cpu_ni float(4,1) DEFAULT '0.0', 
		cpu_id float(4,1) DEFAULT '0.0', 
		cpu_wa float(4,1) DEFAULT '0.0', 
		cpu_hi float(4,1) DEFAULT '0.0', 
		cpu_si float(4,1) DEFAULT '0.0', 
		cpu_st float(4,1) DEFAULT '0.0', 
		cpu_oneload float(10,2) DEFAULT '0.00', 
		cpu_fiveload float(10,2) DEFAULT '0.00', 
		cpu_fifteenload float(10,2) DEFAULT '0.00', 
		mem_total bigint DEFAULT '0', 
		mem_used bigint DEFAULT '0', 
		mem_free bigint DEFAULT '0', 
		mem_buffers bigint DEFAULT '0', 
		mem_us float(4,1) DEFAULT '0.0', 
		io_si bigint DEFAULT '0', 
		io_so bigint DEFAULT '0', 
		io_bi bigint DEFAULT '0', 
		io_bo bigint DEFAULT '0', 
		sto_filesystem varchar(500), 
		sto_size varchar(100), 
		sto_used varchar(100), 
		sto_avail varchar(100), 
		sto_use varchar(100), 
		sto_mounted varchar(500), 
		PRIMARY KEY (id)
	) 
	ENGINE=InnoDB DEFAULT CHARSET=utf8;    
    
    
CREATE TABLE
	pas_app_monitor
	(
		id VARCHAR(64) NOT NULL,
		appName VARCHAR(128),
		occur_time BIGINT(13) DEFAULT 0,
		cpu_us float(4, 1) DEFAULT 0.0,
		cpu_sy float(4, 1) DEFAULT 0.0,
		cpu_ni float(4, 1) DEFAULT 0.0,
		cpu_id float(4, 1) DEFAULT 0.0,
		cpu_wa float(4, 1) DEFAULT 0.0,
		cpu_hi float(4, 1) DEFAULT 0.0,
		cpu_si float(4, 1) DEFAULT 0.0,
		cpu_st float(4, 1) DEFAULT 0.0,
		cpu_oneload float(10, 2) DEFAULT 0.0,
		cpu_fiveload float(10, 2) DEFAULT 0.0,
		cpu_fifteenload float(10, 2) DEFAULT 0.0,
		mem_total BIGINT(20) DEFAULT 0,
		mem_used BIGINT(20) DEFAULT 0,
		mem_free BIGINT(20) DEFAULT 0,
		mem_buffers BIGINT(20) DEFAULT 0,
		mem_us float(4, 1) DEFAULT 0.0,
		io_si BIGINT(20) DEFAULT 0,
		io_so BIGINT(20) DEFAULT 0,
		io_bi BIGINT(20) DEFAULT 0,
		io_bo BIGINT(20) DEFAULT 0,
		PRIMARY KEY (id)
	)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 
	pas_service_monitor 
	(
		id varchar(64) NOT NULL, 
		clusterId varchar(64), 
		occur_time bigint(13) DEFAULT '0', 
		cpu_us float(4,1) DEFAULT '0.0', 
		cpu_sy float(4,1) DEFAULT '0.0', 
		cpu_ni float(4,1) DEFAULT '0.0', 
		cpu_id float(4,1) DEFAULT '0.0', 
		cpu_wa float(4,1) DEFAULT '0.0', 
		cpu_hi float(4,1) DEFAULT '0.0', 
		cpu_si float(4,1) DEFAULT '0.0', 
		cpu_st float(4,1) DEFAULT '0.0', 
		cpu_oneload float(10,2) DEFAULT '0.00', 
		cpu_fiveload float(10,2) DEFAULT '0.00', 
		cpu_fifteenload float(10,2) DEFAULT '0.00', 
		mem_total bigint DEFAULT '0', 
		mem_used bigint DEFAULT '0', 
		mem_free bigint DEFAULT '0', 
		mem_buffers bigint DEFAULT '0', 
		mem_us float(4,1) DEFAULT '0.0', 
		io_si bigint DEFAULT '0', 
		io_so bigint DEFAULT '0', 
		io_bi bigint DEFAULT '0', 
		io_bo bigint DEFAULT '0', 
		PRIMARY KEY (id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
    
CREATE TABLE 
	pas_app_eps_rel
	(
		app_name	VARCHAR(128),
		avg_inst_id VARCHAR(20) DEFAULT 0,
        inc_inst_id VARCHAR(20) DEFAULT 0,
        dec_inst_id VARCHAR(20) DEFAULT 0,
        PRIMARY KEY (app_name)
	)
	ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_vip_segment
    (
    id varchar(64) NOT NULL,
    name varchar(64),
	begin varchar(64) NOT NULL,
	end varchar(64) NOT NULL,
	netmask varchar(64) NOT NULL,
	remarks varchar(128),
	PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_vip_used
    (
        vip varchar(64) NOT NULL,
        segmentId varchar(64),
	PRIMARY KEY (vip)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE
    pas_mail
    (
    MAILID varchar(36) NOT NULL,
    APPNAME varchar(64) NOT NULL,
	MAILSERVERHOST varchar(64) NOT NULL,
	MAILSERVERPORT varchar(5) NOT NULL,
	VALIDATE tinyint(1),
	IFBACK tinyint(1),
	USERNAME varchar(64) NOT NULL,
	PASSWORD varchar(64) NOT NULL,
	SEND_FROM varchar(64) NOT NULL,
	SEND_TO varchar(512) NOT NULL,
	SEND_CC varchar(512),
	SUBJECT varchar(64),
	CONTENTTYPE varchar(8),
	CONTENT text,
	ATTACHMENTSID varchar(512),
	CREATEDATE timestamp NULL,
	STATUS varchar(2) NOT NULL,
	EXCEPTIONCODE varchar(8),
	EXCEPTIONMESSAGE varchar(512),
	PRIMARY KEY (MAILID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE
    pas_mail_attachment
    (
	ATTACHID varchar(36) NOT NULL,
	MAILID varchar(36) NOT NULL,
	PATH varchar(512),
	NAME varchar(64),
	PRIMARY KEY (ATTACHID)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8; 

CREATE TABLE 
	pas_service_eps_rel 
	(
	cluster_id varchar(64) NOT NULL, 
	avg_inst_id varchar(20) DEFAULT '0', 
	inc_inst_id varchar(20) DEFAULT '0', 
	dec_inst_id varchar(20) DEFAULT '0', 
	PRIMARY KEY (cluster_id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 
	pas_servicewarn_strategy 
	(
	strategy_id varchar(64) NOT NULL,
	isEnable tinyint(1) NOT NULL,
	continued_time bigint(13) DEFAULT NULL,
	ignore_time bigint(13) DEFAULT NULL,
	alarm_type varchar(64) NOT NULL,
	alarm_address varchar(64) DEFAULT NULL,
	PRIMARY KEY (strategy_id)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 
	`pas_servicewarn_strategy_item`
	(
	`strategy_id` varchar(64) NOT NULL,
	`item_type` varchar(64) NOT NULL,
	`threshold` varchar(8) DEFAULT NULL,
	PRIMARY KEY (`strategy_id`,`item_type`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 
	`pas_service_alarm_strategy` 
	(
	`clusterId` varchar(64) NOT NULL,
	`strategy_id` varchar(64) NOT NULL,
	PRIMARY KEY (`clusterId`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
