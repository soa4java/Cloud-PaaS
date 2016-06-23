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

redis_program=${PROGRAME_HOME_PATH}/Redis/redis-3.0.2

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./stop.sh -reqId 0 -srvInstId 10000 -srvDefName Redis -clusterName 10000"   
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-arg0) arg0=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	echo "redis_program = ${redis_program}"
	
	# redis pid file
	pid_file=${redis_program}/pids/${INSTANCE_ID}.pid
	echo "pid_file = ${pid_file}"
	if [ -f ${pid_file} ]; then
		pid="`cat ${pid_file}`"
		if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
			_success
			_return "Redis ${INSTANCE_ID} has been shut down."
			exit 0
		fi
	fi
	
	if [ ! -z ${pid} ]; then
		kill ${pid}
		# ${redis_program}/bin/redis-cli shutdown
		
		# find process
		TIMEOUT=60
		COUNT=0
		while [ ${COUNT} -lt ${TIMEOUT} ]; do
			if [ `ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
				_return "Redis ${INSTANCE_ID} shut down at ${COUNT} seconds."
				_success
				exit 0
			else
				COUNT=`expr ${COUNT} + 1`
				echo -n "."
				sleep 1
			fi
		done
		kill -9 ${pid}
		_return "Redis ${INSTANCE_ID} force shut down by kill -9."
	fi
	
	_success "Redis ${INSTANCE_ID} shut down."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh