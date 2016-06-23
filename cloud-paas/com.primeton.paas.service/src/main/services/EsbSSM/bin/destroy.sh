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
SSM_HOME=${PROGRAME_HOME_PATH}/EsbSSM/ssm

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./destroy.sh -reqId uuid -serviceId 20001"
    echo "-h) Print help"
    echo "-reqId) Request id"
	echo "-serviceId) Service instance id"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-serviceId) serviceId=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ ! -d ${SSM_HOME} ]; then
		_fail
		_return "SSH_HOME=${SSM_HOME} not found, not installed."
	fi
	
	if [ -z ${serviceId} ]; then	
		_fail
		_return "serviceId is empty."
	fi
	
	# instance directory
	INSTANCE_ROOT=${SSM_HOME}/instances/${serviceId}
	if [ -d ${INSTANCE_ROOT} ]; then
		rm -rf ${INSTANCE_ROOT}
	fi
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh