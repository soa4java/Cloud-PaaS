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

program_home=${PROGRAME_HOME_PATH}/Tomcat

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./destroy.sh -reqId uuid -srvId 20001"   
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvId) service id"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-srvId) srvId=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ -z ${srvId} ]; then
		_error "srvId is null or blank."
		exit 0
	fi
	if [ -d ${program_home}/${srvId} ]; then
		rm -rf ${program_home}/${srvId}
	fi
	if [ -d ${BIN_HOME_PATH}/Tomcat/${srvId} ]; then
		rm -rf ${BIN_HOME_PATH}/Tomcat/${srvId}
	fi
	_success
	_return "Destroy Tomcat ${srvId} success."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh