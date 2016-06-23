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
appName=default
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787
# runMode=develop
runMode=product
enableSession=false

# server.xml
CONF_TEMPLATE=${BIN_HOME_PATH}/Tomcat/packages/conf/server.xml

# zkConfig.xml
ZK_CONFIG_XML=${BIN_HOME_PATH}/Common/conf/zkConfig.xml

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName Tomcat -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022 -appName app1"
	echo "./start.sh -reqId 10002 -srvDefName Tomcat -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022 -appName app1 -ip ${ip} -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
    
	echo "-ip) host ip"
	echo "-port) http port"
	echo "-shutdownPort) shut down port"
	echo "-ajpPort) AJP port"
	echo "-appName) application name"
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
		-appName) appName=$2;shift 2;;
		-minMemory) minMemory=$2;shift 2;;
		-maxMemory) maxMemory=$2;shift 2;;
		-minPermSize) minPermSize=$2;shift 2;;
		-maxPermSize) maxPermSize=$2;shift 2;;
		-enableSession) enableSession=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${INSTANCE_ID} ]; then
		_error "INSTANCE_ID is null or blank."
		exit 0
	fi
	
	if [ -z ${ip} ]; then
		ip=${PAAS_LOCAL_IP}}
	fi
	
	# Tomcat
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/Tomcat/${INSTANCE_ID}/apache-tomcat-7.0.62
	CONFIG_DIR=${PROGRAME_HOME_PATH}/Tomcat/${INSTANCE_ID}/app_configs
	
	if [ ! -d ${CONFIG_DIR}/default ]; then
		mkdir -p ${CONFIG_DIR}/default
	fi
	STARTUP_CONF=${CONFIG_DIR}/default/startup.conf
	if [ -f ${STARTUP_CONF} ]; then
		rm -f ${STARTUP_CONF}
	fi
	echo "# Auto generate by Tomcat/bin/start.sh" > ${STARTUP_CONF}
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
			_return "Tomcat ${INSTANCE_ID} has been started."
			exit 0
		fi
	fi
	
	WORK_DIR=${TOMCAT_HOME}/workspace
	FILE_ROOT_DIR=${TOMCAT_HOME}/files
	
	# configure server.xml
	if [ -f ${CONF_TEMPLATE} ]; then
		cp -f ${CONF_TEMPLATE} ${TOMCAT_HOME}/conf
		# modify port
		sed -i -e "s/_SHUTDOWN_PORT_/${shutdownPort}/g" ${TOMCAT_HOME}/conf/server.xml
		sed -i -e "s/_HTTP_PORT_/${port}/g" ${TOMCAT_HOME}/conf/server.xml
		sed -i -e "s/_AJP_PORT_/${ajpPort}/g" ${TOMCAT_HOME}/conf/server.xml
		_return "Modify server port [${port}, ${shutdownPort}, ${ajpPort}] success "
	else
		_return "[WARN] ${CONF_TEMPLATE} not found."
	fi
	
	# override zkConfig.xml
	if [ -d ${TOMCAT_HOME}/webapps/default/WEB-INF/config ]; then
		cp -f ${ZK_CONFIG_XML} ${TOMCAT_HOME}/webapps/default/WEB-INF/config/
	fi
	
	# if use msm
	if [ "${enableSession}" = "true" ]; then
		cp -f ${BIN_HOME_PATH}/Tomcat/packages/lib/*.jar ${TOMCAT_HOME}/lib
	fi
	
	# JAVA OPTS
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi
	
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.srvType=${SERVICE_TYPE} -Dpaas.instId=${INSTANCE_ID} -Dpaas.clusterName=${CLUSTER_NAME} -Dpaas.appName=${appName}"
	JAVA_OPTS="${JAVA_OPTS} -Dpaas.ip=${ip} -Dpaas.httpPort=${port} -Dpaas.workDir=${WORK_DIR} -Dpaas.runMode=${runMode} -Dpaas.fileRootDir=${FILE_ROOT_DIR} -Dtomcat.home=${TOMCAT_HOME}"
	# for EOS application
	JAVA_OPTS="${JAVA_OPTS} -DEXTERNAL_CONFIG_DIR=${CONFIG_DIR}"
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
		_return "Tomcat ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "Tomcat ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh