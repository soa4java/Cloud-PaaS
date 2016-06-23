#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ---------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

easn_program=${PROGRAME_HOME_PATH}/Gateway/easn/fes_698tcps

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./stop.sh -reqId 0 -srvInstId 10000 -srvDefName Gateway -clusterName 10000"
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
		-arg0) arg0=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	echo "easn_program = ${easn_program}"
	
	gatewayId=${INSTANCE_ID}
	
	# Gateway pid file
	pid_file=${easn_program}/${gatewayId}.pid
	echo "pid_file = ${pid_file}"
	if [ -f ${pid_file} ]; then
		pid="`cat ${pid_file}`"
		if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
			_success
			rm -f ${pid_file}
			_return "Gateway ${INSTANCE_ID} has been shut down."
			exit 0
		fi
	fi
	
	# kill tcp
	kill -9 ${pid}
	rm -f ${pid_file}
	rm -f ${easn_program}/${gatewayId}.nohup
	
	_return "Gateway ${INSTANCE_ID} shut down success."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh