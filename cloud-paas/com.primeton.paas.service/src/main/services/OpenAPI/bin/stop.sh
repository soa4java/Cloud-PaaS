#!/bin/bash

# author tenghao(mailto:tenghao@primeton.com)

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./stop.sh -reqId 4001 -srvDefName OpenAPI -srvInstId 20001"
    
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
}

# Parse execute arguments if has
function _doparse() {
	echo "None parameter need to parse."
}

# Write your core/main code
function _doexecute() {
	# check params
	if [ -z ${INSTANCE_ID} ]; then
		_return "Stop OpenAPI error : -srvInstId xxx parameter not found."
		exit 0
	fi
	
	if [ -z ${SERVICE_TYPE} ]; then
		_return "Stop OpenAPI error : -srvDefName xxx parameter not found."
		exit 0
	fi
	
	# check pid
	PID_FILE=${BIN_HOME_PATH}/OpenAPI/instances/${INSTANCE_ID}/pid.txt
	if [ ! -f ${PID_FILE} ]; then
		_fail
		_error "OpenAPI srvInstId ${INSTANCE_ID} pid file ${PID_FILE} is not exist."
		exit 0
	fi
	
	# check process
	pid=`cat ${PID_FILE}`
	if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 0 ]; then
		_success
		_return "OpenAPI srvInstId ${INSTANCE_ID} is not running."
		exit 0
	else
		kill `cat ${PID_FILE}`
	fi
	
	#last check
	i=0
	while test $i -ne 10 ; do
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 0 ]; then
			_success
			_return "OpenAPI srvInstId ${INSTANCE_ID}  succeeded to stop."
			exit 0
		fi
		echo -n "-"
		i=`expr $i + 1`
		sleep 1
	done
	
	#term stop
	kill -9 `cat ${PID_FILE}`
	_success
	_return "OpenAPI srvInstId ${INSTANCE_ID}  succeeded to force stop."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh