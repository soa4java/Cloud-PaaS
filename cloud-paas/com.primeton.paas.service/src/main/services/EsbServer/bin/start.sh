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
port=61616
adminPort=6200
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787


# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName EsbServer -srvInstId 20001 -clusterName 20001 -port 61616 -adminPort 6200"
	echo "./start.sh -reqId 10002 -srvDefName EsbServer -srvInstId 20001 -clusterName 20001 -port 61616 -adminPort 6200 -ip ${ip} -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
    
	echo "-ip) host ip"
	echo "-port) http port"
	echo "-adminPort) EOS APP admin port"
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

	SERVER_HOME=${PROGRAME_HOME_PATH}/EsbServer/server
	if [ ! -d ${SERVER_HOME} ]; then
		_fail
		_return "EsbServer is not installed."
		exit 0
	fi
	# pid file
	PID_FILE=${SERVER_HOME}/pid
    
	# check process
	if [ -f ${PID_FILE} ]; then
		PID=`cat ${PID_FILE}`
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${PID}
			_return "EsbServer ${INSTANCE_ID} has been started."
			exit 0
		else
			rm -f ${PID_FILE}
		fi
	fi
	
	# startup.conf
	START_CONF=${SERVER_HOME}/EOS/_srv/startup.conf
	if [ -f ${START_CONF} ]; then
		rm -f ${START_CONF}
	fi
	echo "# Generate by agent execute start.sh" > ${START_CONF}
	echo "AdminPort=${adminPort}" >> ${START_CONF}
	echo "LocalIP=${ip}" >> ${START_CONF}
	echo "ESB_SERVER_ID=ESBServer_${ip}_${adminPort}" >> ${START_CONF}
	
	# start
	TIP_HOME=${SERVER_HOME}
	export TIP_HOME
	export JAVA_HOME
	
	CLASSPATH=${SERVER_HOME}/plugins/com.primeton.esb.tip.bootstrap-6.5.1.0.jar
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	# JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
	
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} -Dorg.mortbay.io.nio.BUSY_PAUSE=1 -DTIP_HOME=${TIP_HOME} com.primeton.tip.bootstrap.TIPServer start"
	echo ${CMD}
	
	# run
	cd ${SERVER_HOME}/EOS
	# nohup ${CMD} > /dev/null &
	nohup ${CMD} > ${SERVER_HOME}/console.out &
	PID=$!
	
	# find process
	if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
		_success
		_pid ${PID}
		echo -n ${PID} > ${PID_FILE}
		_return "EsbServer ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "EsbServer ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh