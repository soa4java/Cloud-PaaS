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

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./stop.sh -reqId 10001 -srvDefName Tomcat -srvInstId 20001 -clusterName 20001"
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
	# check params
	if [ -z ${INSTANCE_ID} ]; then
		_error "INSTANCE_ID is null or blank."
		exit 0
	fi
	
	# Tomcat
	TOMCAT_HOME=${PROGRAME_HOME_PATH}/Tomcat/${INSTANCE_ID}/apache-tomcat-7.0.62
	if [ ! -d ${TOMCAT_HOME} ]; then
		_error "${TOMCAT_HOME} not exists."
		_fail
		exit 0
	fi
	CATALINA_PID=${TOMCAT_HOME}/pid
	export CATALINA_PID
    
	# check process
	if [ -f ${CATALINA_PID} ]; then
		PID=`cat ${CATALINA_PID}`
		if [ ! -z ${PID} ] && [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
			_success
			_return "Tomcat ${INSTANCE_ID} has been shut down."
			exit 0
		fi
	fi
	
	if [ ! -f ${TOMCAT_HOME}/bin/catalina.sh ]; then
		_error "${TOMCAT_HOME}/bin/catalina.sh not exists."
		_fail
		exit 0
	fi
	
	# execute shut down tomcat script
	${TOMCAT_HOME}/bin/catalina.sh stop "$@"
	
	if [ ! -z ${PID} ]; then
		# find process
		TIMEOUT=60
		COUNT=0
		while [ ${COUNT} -lt ${TIMEOUT} ]; do
			if [ `ps -p ${PID} | grep -v PID | wc -l` -eq 0 ]; then
				_return "Tomcat ${INSTANCE_ID} shut down at ${COUNT} seconds."
				_success
				exit 0
			else
				COUNT=`expr ${COUNT} + 1`
				echo -n "."
				sleep 1
			fi
		done
		kill -9 ${PID}
		_return "Tomcat ${INSTANCE_ID} force shut down by kill -9."
	fi
			
	_return "${CATALINA_PID} not found, execute [${TOMCAT_HOME}/bin/catalina.sh stop] to shut down it."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh