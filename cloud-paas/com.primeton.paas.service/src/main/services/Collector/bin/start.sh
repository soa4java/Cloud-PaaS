#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
server_home=${PROGRAME_HOME_PATH}/Collector/collector
group_name=default
server_name=default
server_ip=${PAAS_LOCAL_IP}
log_root=${WORKSPACE_HOME_PATH}/application
# 1000KB = 1000 * 1024 B
appender_buffer=1024000

# JVM
minMemory=128
maxMemory=256
maxPermSize=128
debugMode=false
debugPort=8787
debugSuspend=y


# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./start.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-srvInstId) Service instance id"
    echo "-srvDefName) Service type"
    echo "-clusterName) Cluster name"
    echo "-groupName) example default"
    echo "-mqServer) name for rabbitmq_server"
    echo "-mqDests) exchange or queue name"
    echo "-mqTypes) queue | exchange"
    echo "-logRoot) log root directory"
    echo "-appenderBuffer) log4j appender buffer size (B), default 1024000B(1000KB)"
    echo "-ip) ip"
    echo "--------------------------------"
    echo "-minMemory) default 128 / greater than 128"
    echo "-maxMemory) default 256"
    echo "-maxPermSize) default 128 / greater than 128"
    echo "-debugMode) default false / true | false"
    echo "-debugPort) default 8787"
    echo "-debugSuspend) default y / y | n"
}

# Parse execute arguments if has
function _doparse() {
    while [ -n "$1" ]; do
        case $1 in
        -groupName) group_name=$2;shift 2;;
        -serverName) server_name=$2;shift 2;;
        -mqServer) mq_server=$2;shift 2;;
        -mqDests) mq_dests=$2;shift 2;;
        -mqTypes) mq_types=$2;shift 2;;
        -logRoot) log_root=$2;shift 2;;
        -appenderBuffer) appender_buffer=$2;shift 2;;
        -minMemory) minMemory=$2;shift 2;;
        -maxMemory) maxMemory=$2;shift 2;;
        -maxPermSize) maxPermSize=$2;shift 2;;
        -debugMode) debugMode=$2;shift 2;;
        -debugPort) debugPort=$2;shift 2;;
        -debugSuspend) debugSuspend=$2;shift 2;;
        -ip) server_ip=$2;shift 2;;
        *) break;;
        esac
    done
}

# Write your core/main code
function _doexecute() {
	server_name=${INSTANCE_ID}
	if [ ! -d ${server_home} ]; then
		_fail
		_return "${server_home} not found."
		exit 0
	fi
	
	echo "server_home=${server_home}"
	echo "group_name=${group_name}"
	echo "mq_server=${mq_server}"
	echo "mq_dests=${mq_dests}"
	echo "mq_types=${mq_types}"
	echo "log_root=${log_root}"
	echo "appender_buffer=${appender_buffer}"
	
    
	# copy zkConfig.xml to conf
	cp -f ${BIN_HOME_PATH}/Common/conf/zkConfig.xml ${server_home}/conf
	
	# Java env
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError"
	# If you need debug
	if ${debugMode} ; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=${debugSuspend},address=${debugPort}"
	fi
	JAVA_OPTS="${JAVA_OPTS} -Dserver_home=${server_home} -Dgroup_name=${group_name} -Dserver_name=${server_name} -Dmq_server=${mq_server} -Dmq_dests=${mq_dests} -Dmq_types=${mq_types} -Dserver_ip=${server_ip} -Dlog_root=${log_root} -Dappender_buffer=${appender_buffer}"
	export JAVA_OPTS

	RUNJAR=
	for f in `find ${server_home}/lib -name "*.jar"`
	do
		RUNJAR=${RUNJAR}:$f
	done
	export RUNJAR

	Main_Class="com.primeton.paas.collect.server.Bootstrap"

	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${RUNJAR} ${Main_Class}"
	echo "Main class is ${Main_Class}."
	
	# keywords
	keyword1="Dserver_name=${server_name}"
	keyword2="Dgroup_name=${group_name}"
	
	if [ ` ps -ef | grep ${Main_Class} | grep ${keyword1} | grep ${keyword2} | grep -v "grep" | wc -l` -eq 0 ]; then
		echo CMD=${CMD}
		
		# for write log to ${server_home}/logs/..., cd to ${server_home}
		cd ${server_home}
		
		# Execute java command to start JVM
		nohup ${CMD} > /dev/null &
	
		PID=$!
		_pid ${PID}
		echo -n ${PID} > ${server_home}/bin/${server_name}.pid
		
		_success
		_return "Server successfully started, pid is ${PID}."
		exit 0
		
	else
		_success
		_return "Server is already started."
		exit 0
	fi
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh