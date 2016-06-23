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
mysql_program=${PROGRAME_HOME_PATH}/MySQL/mysql-5.5.25
mysqlPid=${mysql_program}/data/${HOSTNAME}.pid

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./stop.sh"
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
	if [ -f ${mysqlPid} ]; then
		pid="`cat ${mysqlPid}`"
		if [ `ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
			_return "MySQL server has stopped."
			_success
			exit 0
		fi
	fi
	
	# stop mysql
	service mysql stop
	
	# validate
	if [ `ps -p ${pid} | grep -v PID | wc -l` -ge 1 ]; then
		kill -9 ${pid}
	fi
	
	_return "MySQL server successfully stopped."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh