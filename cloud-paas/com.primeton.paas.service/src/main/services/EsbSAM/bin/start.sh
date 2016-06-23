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
ip=${PAAS_LOCAL_IP}
port=8020
shutdownPort=8021
ajpPort=8022
adminPort=8023
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName EsbSAM -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022"
	echo "./start.sh -reqId 10002 -srvDefName EsbSAM -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022 -ip ${ip} -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
    
	echo "-ip) host ip"
	echo "-port) http port"
	echo "-shutdownPort) shut down port"
	echo "-ajpPort) AJP port"
	echo "-minMemory) jvm minMemory"
	echo "-maxMemory) jvm maxMemory"
	echo "-minPermSize) jvm minPermSize"
	echo "-maxPermSize) jvm maxPermSize"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-shutdownPort) shutdownPort=$2;shift 2;;
		-ajpPort) ajpPort=$2;shift 2;;
		-adminPort) adminPort=$2;shift 2;;
		-minMemory) minMemory=$2;shift 2;;
		-maxMemory) maxMemory=$2;shift 2;;
		-minPermSize) minPermSize=$2;shift 2;;
		-maxPermSize) maxPermSize=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ -z ${ip} ]; then
		ip=${PAAS_LOCAL_IP}}
	fi
	
	# Tomcat
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/EsbSAM/apache-tomcat-6.0.44
	CONFIG_DIR=${PROGRAME_HOME_PATH}/EsbSAM/app_configs
	
	if [ ! -d ${CONFIG_DIR}/sam ]; then
		mkdir -p ${CONFIG_DIR}/sam
	fi
	STARTUP_CONF=${CONFIG_DIR}/sam/startup.conf
	if [ -f ${STARTUP_CONF} ]; then
		rm -f ${STARTUP_CONF}
	fi
	echo "# Auto generate by EsbSAM/bin/start.sh" > ${STARTUP_CONF}
	echo "AdminPort=${adminPort}" >> ${STARTUP_CONF}
	echo "LocalIP=${ip}" >> ${STARTUP_CONF}
	
	if [ ! -d ${TOMCAT_HOME} ]; then
		_error "${TOMCAT_HOME} not exists."
		_fail
		exit 0
	fi
	CATALINA_PID=${TOMCAT_HOME}/pid
	export CATALINA_PID
    
	# check process
	if [ -f ${CATALINA_PID} ]; then
		PID=`cat ${CATALINA_PID}`
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_return "EsbSAM ${INSTANCE_ID} has been started."
			exit 0
		fi
	fi
		
	# JAVA OPTS
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi	
	JAVA_OPTS="${JAVA_OPTS} -Dtomcat.home=${TOMCAT_HOME} -DEXTERNAL_CONFIG_DIR=${CONFIG_DIR}"
	echo ${JAVA_OPTS}
	export JAVA_OPTS

	if [ -d ${JAVA_HOME} ]; then
		export JAVA_HOME
	fi
	
	if [ ! -f ${TOMCAT_HOME}/bin/catalina.sh ]; then
		_error "${TOMCAT_HOME}/bin/catalina.sh not exists."
		_fail
		exit 0
	fi
	
	# execute start tomcat script
	${TOMCAT_HOME}/bin/catalina.sh start "$@"
	
	if [ -f ${CATALINA_PID} ]; then
		PID=`cat ${CATALINA_PID}`
		_pid ${PID}
	fi
	
	# find process
	if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
		_success
		_return "EsbSAM ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "EsbSAM ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh