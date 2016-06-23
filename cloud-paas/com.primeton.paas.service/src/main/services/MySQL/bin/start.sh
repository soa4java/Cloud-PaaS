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
    echo "Usage: ./start.sh"
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
		pid=`cat ${mysqlPid}`
		if [ `ps -p ${pid} | grep -v PID | wc -l` -gt 1 ]; then
			_pid ${pid}
			_return "MySQL instance{${INSTANCE_ID}}, pid{${pid}} is already started."
			_success
			exit 0
		fi
	fi
	
	# start mysql
	service mysql start

	# validate
	i=0
	while test $i -ne 60 ; do
		if [ -f ${mysqlPid} ]; then
			pid=`cat ${mysqlPid}`
			_return "MySQL server success started!! pid=${pid}"
			_pid ${pid}
			break;
		fi
		echo -n "#"
		i=`expr $i + 1`
		sleep 1
	done
	
	if test $i -ge 60 ; then
		_return "Start MySQL server timeout or failed time spend [$i]"
		_return "MySQL instance{${INSTANCE_ID}} failure started."
		_fail
		exit 1
	fi
	
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh