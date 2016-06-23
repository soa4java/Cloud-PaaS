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
	echo "./stop.sh -reqId 10001 -srvDefName JobCtrl -srvInstId 20001 -clusterName 20001"
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
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
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
			_success
			_pid ${PID}
			_return "EsbServer ${INSTANCE_ID} has been shut down."
			rm -f ${PID_FILE}
			exit 0
		fi		
	fi
	
	# stop
	TIP_HOME=${SERVER_HOME}
	export TIP_HOME
	export JAVA_HOME
	
	CLASSPATH=${SERVER_HOME}/plugins/com.primeton.esb.tip.bootstrap-6.5.1.0.jar
	# JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	# JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
	
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} -DTIP_HOME=${TIP_HOME} com.primeton.tip.bootstrap.TIPServerAgent stop"
	echo ${CMD}
	
	# run
	cd ${SERVER_HOME}/EOS
	# nohup ${CMD} >> /dev/null &
	nohup ${CMD} >> ${SERVER_HOME}/console.out &
	
	if [ ! -z ${PID} ]; then
		# find process
		TIMEOUT=60
		COUNT=0
		while [ ${COUNT} -lt ${TIMEOUT} ]; do
			if [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
				_return "EsbServer ${INSTANCE_ID} shut down at ${COUNT} seconds."
				_success
				exit 0
			else
				COUNT=`expr ${COUNT} + 1`
				echo -n "."
				sleep 1
			fi
		done
		kill -9 ${PID}
		rm -f ${PID_FILE}
		_return "EsbServer ${INSTANCE_ID} force shut down by kill -9 ${PID}."
	fi
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh