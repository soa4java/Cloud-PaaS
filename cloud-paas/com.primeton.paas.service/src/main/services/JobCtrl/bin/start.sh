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
port=8080
shutdownPort=8083
ajpPort=8443
adminPort=6200
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787

# server.xml
CONF_SERVER=${BIN_HOME_PATH}/JobCtrl/packages/conf/server.xml
CONF_WEB=${BIN_HOME_PATH}/JobCtrl/packages/conf/web.xml

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName JobCtrl -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022"
	echo "./start.sh -reqId 10002 -srvDefName JobCtrl -srvInstId 20001 -clusterName 20001 -port 8020 -shutdownPort 8021 -ajpPort 8022 -ip ${ip} -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
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
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/JobCtrl/apache-tomcat-6.0.44
	
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
	
	# configure server.xml
	if [ -f ${CONF_SERVER} ]; then
		cp -f ${CONF_SERVER} ${TOMCAT_HOME}/conf
		# modify port
		sed -i -e "s/_SHUTDOWN_PORT_/${shutdownPort}/g" ${TOMCAT_HOME}/conf/server.xml
		sed -i -e "s/_HTTP_PORT_/${port}/g" ${TOMCAT_HOME}/conf/server.xml
		sed -i -e "s/_AJP_PORT_/${ajpPort}/g" ${TOMCAT_HOME}/conf/server.xml
		_return "Modify server port [${port}, ${shutdownPort}, ${ajpPort}] success "
	else
		_return "[WARN] ${CONF_SERVER} not found."
	fi
	
	# configure web.xml, modify EOS admin port
	if [ -f ${CONF_WEB} ]; then
		cp -f ${CONF_WEB} ${TOMCAT_HOME}/webapps/primetonJobCtrl/WEB-INF
		# modify port
		sed -i -e "s/_ADMIN_PORT_/${adminPort}/g" ${TOMCAT_HOME}/webapps/primetonJobCtrl/WEB-INF/web.xml
		_return "Modify admin port ${adminPort} success "
	else
		_return "[WARN] ${CONF_WEB} not found."
	fi	
	
	# JAVA OPTS
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	
	if [[ ${debugMode} = "true" ]] && [ ${debugPort} -gt 0 ]; then
		JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${debugPort}"
	fi

	JAVA_OPTS="${JAVA_OPTS} -Dtomcat.home=${TOMCAT_HOME}"
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
	
	# Generate _work_primetonJobCtrl_${adminPort} in current path
	cd ${PROGRAME_HOME_PATH}/JobCtrl
	
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