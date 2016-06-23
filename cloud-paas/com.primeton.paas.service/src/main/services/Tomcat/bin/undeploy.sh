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

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./undeploy.sh -reqId 0 -srvId 20001"
    echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvId) tomcat service id"
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
	
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/Tomcat/${srvId}/apache-tomcat-7.0.62
	
	if [ ! -d ${TOMCAT_HOME} ]; then
		_error "${TOMCAT_HOME} not exists."
		exit 0
	fi
	
	# clean
	rm -rf ${TOMCAT_HOME}/webapps/default
	
	# deploy templates
	if [ -d ${BIN_HOME_PATH}/Tomcat/packages/java/default ]; then
		cp -rf ${BIN_HOME_PATH}/Tomcat/packages/java/default ${TOMCAT_HOME}/webapps
	fi
	
	_success
	_return "Tomcat ${srvId} undeploy war successfully."
	
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh