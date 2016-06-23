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

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./stop.sh -reqId 10001 -srvDefName EsbSSM -srvInstId 20001 -clusterName 20001"
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
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
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
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
			_success
			_pid ${PID}
			_return "EsbSSM ${INSTANCE_ID} has been shut down."
			exit 0
		else
			rm -f ${PID_FILE}
		fi
	fi
		
	# stop
	if [ ! -z ${PID} ]; then
		kill ${PID}
		# find process
		TIMEOUT=60
		COUNT=0
		while [ ${COUNT} -lt ${TIMEOUT} ]; do
			if [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
				_return "EsbSSM ${INSTANCE_ID} shut down at ${COUNT} seconds."
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
		_return "EsbSSM ${INSTANCE_ID} force shut down by kill -9 ${PID}."
	fi
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh