create table CLD_HOST
(
   HOST_IP              VARCHAR(15) not null,
   HOST_NAME            varchar(64),
   HOST_TYPE            varchar(512),
   primary key (HOST_IP)
);

create table CLD_HOST_ATTR_REL
(
   HOST_IP              VARCHAR(15) not null,
   ATTR_NAME            varchar(64) not null,
   ATTR_VALUE           varchar(256),
   primary key (HOST_IP, ATTR_NAME)
);

create table CLD_HOST_USED
(
   HOST_USED_ID         INTEGER(12) not null,
   HOST_IP              VARCHAR(15),
   PORT                 INTEGER(5),
   primary key (HOST_USED_ID)
);

create table CLD_SERVICE
(
   SERVICE_NAME         varchar(64) not null,
   SERVICE_DISPLAY_NAME varchar(128),
   SERVICE_DESC         varchar(256),
   SERVICE_MODE         varchar(64),
   primary key (SERVICE_NAME)
);

create table CLD_SERVICE_ATTR_REL
(
   SERVICE_NAME         varchar(64) not null,
   ATTR_NAME            varchar(64) not null,
   DEFAULT_VALUE		varchar(64),
   OVERRIDABLE			char(1),
   primary key (SERVICE_NAME, ATTR_NAME)
);

create table CLD_ATTR
(
   ATTR_TYPE            varchar(64) not null,
   ATTR_NAME            varchar(64) not null,
   ATTR_DISPLAY_NAME    varchar(64),
   DATA_TYPE            varchar(64),
   ATTR_DESC            varchar(64),
   primary key (ATTR_TYPE, ATTR_NAME)
);

create table CLD_APP
(
   APP_NAME             VARCHAR(64) not null,
   APP_DISPLAY_NAME     VARCHAR(64),
   OWNER                VARCHAR(64),
   APP_DESC             VARCHAR(256),
   primary key (APP_NAME)
);

create table CLD_CLUSTER
(
   CLUSTER_NAME         VARCHAR(64) not null,
   CLUSTER_DISPLAY_NAME VARCHAR(128),
   CLUSTER_DESC         VARCHAR(256),
   CLUSTER_TYPE         VARCHAR(64),
   OWNER                VARCHAR(64),
   primary key (CLUSTER_NAME)
);

create table CLD_INST_CLUSTER_ATTR
(
   CLUSTER_NAME         VARCHAR(64) not null,
   ATTR_NAME            VARCHAR(128) not null,
   ATTR_VALUE           VARCHAR(256),
   primary key (CLUSTER_NAME, ATTR_NAME)
);

create table CLD_CLUSTER_REL
(
   CLUSTER_NAME         VARCHAR(64) not null,
   REL_CLUSTER_NAME     VARCHAR(64) not null,
   primary key (CLUSTER_NAME, REL_CLUSTER_NAME)
);

create table CLD_INSTANCE_SERVICE
(
   INSTANCE_SERVICE_ID  INTEGER(12) not null,
   INSTANCE_NAME        VARCHAR(64),
   SERVICE_NAME         VARCHAR(64),
   PARENT_ID            INTEGER(12),
   PID                  INTEGER(12),
   IP                   VARCHAR(15),
   PORT                 INTEGER(5),
   OWNER                VARCHAR(64),
   primary key (INSTANCE_SERVICE_ID)
);

create table CLD_INST_APP_ATTR
(
   APP_NAME             varchar(64) not null,
   ATTR_NAME            varchar(64) not null,
   ATTR_VALUE           varchar(256),
   primary key (APP_NAME, ATTR_NAME)
);

create table CLD_INST_CLUSTER_APP
(
   APP_NAME             VARCHAR(64) not null,
   CLUSTER_NAME         VARCHAR(64) not null,
   primary key (APP_NAME, CLUSTER_NAME)
);

create table CLD_INST_CLUSTER_SRV
(
   CLUSTER_NAME         VARCHAR(64) not null,
   INSTANCE_SERVICE_ID  INTEGER(12) not null,
   primary key (CLUSTER_NAME, INSTANCE_SERVICE_ID)
);

create table CLD_INST_SERVICE_ATTR
(
   INSTANCE_SERVICE_ID  INTEGER(12) not null,
   ATTR_NAME            VARCHAR(64) not null,
   ATTR_VALUE           varchar(256),
   primary key (INSTANCE_SERVICE_ID, ATTR_NAME)
);

create table CLD_VARIABLE
(
   VARIABLE_TYPE        varchar(64) not null,
   VARIABLE_NAME        varchar(64) not null,
   VARIABLE_VALUE       varchar(1024),
   VARIABLE_DESC        varchar(512),
   primary key (VARIABLE_NAME)
);

create table CLD_CONFIG_MODULE(
    MODULE_ID INTEGER(12) NOT NULL,
    MODULE_NAME VARCHAR(128),
    APP_TYPE VARCHAR(64),
    IS_GLOBAL INTEGER(2),
    primary key (MODULE_ID)
);

create table CLD_CONFIG_ITEM(
    ITEM_ID INTEGER(12),
    MODULE_ID INTEGER(12),
    CONFIG_KEY VARCHAR(128),
    CONFIG_VALUE VARCHAR(2000),
    primary key(ITEM_ID)
);

create table CLD_ID_TABLE(
    TABLE_NAME varchar(64),
    START_ID integer(12),
    INCREMENT integer(4),
    NEXT_ID integer(12),
    primary key(TABLE_NAME)
);
