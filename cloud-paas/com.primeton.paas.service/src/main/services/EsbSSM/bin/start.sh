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
port=6234
minMemory=128
maxMemory=256
minPermSize=64
maxPermSize=128
debugMode=false
debugPort=8787


# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 10001 -srvDefName EsbSSM -srvInstId 20001 -clusterName 20001 -port 6234"
	echo "./start.sh -reqId 10002 -srvDefName EsbSSM -srvInstId 20001 -clusterName 20001 -port 6234 -ip ${ip} -minMemory 128 -maxMemory 256 -minPermSize 64 -maxPermSize 128"
    
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

	SSM_HOME=${PROGRAME_HOME_PATH}/EsbSSM/ssm
	echo "SSM_HOME=${SSM_HOME}"
	if [ ! -d ${SSM_HOME} ]; then
		_fail
		_return "EsbSSM is not installed."
		exit 0
	fi
	# pid file
	PID_FILE=${SSM_HOME}/instances/${INSTANCE_ID}/pid
    
	# check process
	if [ -f ${PID_FILE} ]; then
		PID=`cat ${PID_FILE}`
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${PID}
			_return "EsbSSM ${INSTANCE_ID} has been started."
			exit 0
		else
			rm -f ${PID_FILE}
		fi
	fi
	
	CLASSPATH=""
	for file in ${SSM_HOME}/lib/*.jar
	do
		if [ -f ${file} ]; then 
			CLASSPATH=${CLASSPATH}:${file}
		fi
	done
	
	JAVA_OPTS="-Xms${minMemory}m -Xmx${maxMemory}m -XX:PermSize=${minPermSize}m -XX:MaxPermSize=${maxPermSize}m -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"
	JAVA_OPTS="${JAVA_OPTS} -DSSM_HOME=${SSM_HOME} -DSSM_INSTANCE=${INSTANCE_ID} -Dlog4j.configration=file:./log4j.agent.properties"
	# JAVA_OPTS="${JAVA_OPTS} -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
	
	CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} com.primeton.esb.ssm.agent.SsmAgentDaemon"
	echo ${CMD}
	
	# instance conf directory
	cd ${SSM_HOME}/instances/${INSTANCE_ID}/conf
	echo "pwd=`pwd`"
	# run
	# nohup ${CMD} > /dev/null &
	nohup ${CMD} > ${SSM_HOME}/instances/${INSTANCE_ID}/console.out &
	PID=$!
	
	# find process
	if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
		_success
		_pid ${PID}
		echo -n ${PID} > ${PID_FILE}
		_return "EsbSSM ${INSTANCE_ID} start successfully, pid=${PID}."
		exit 0
	else
		_fail
		_error "EsbSSM ${INSTANCE_ID} failed to start."
		exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh