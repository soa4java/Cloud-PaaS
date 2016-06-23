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
host=${PAAS_LOCAL_IP}
port=
memorySize=32
maxConnectionSize=1024
basePath=${PROGRAME_HOME_PATH}/Memcached/memcached-1.4.15
pidDir=${basePath}/bin/pids
pidFile=

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./stop.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-srvInstId) Service instance id"
    echo "-srvDefName) Service type"
    echo "-clusterName) Cluster name"
    echo "-port) memcached service port"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-port) port=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	
	if [[ "${port}x" = "x" ]] ; then
		_return "unknown port."
		_fail
		exit 0
	fi
	
	pidFile=${pidDir}/${port}.pid
	if [ -f ${pidFile} ]; then
		cachePid=`cat ${pidFile}`
		if [ ` ps -ef |grep ${cachePid} |grep -v "grep" | wc -l` -eq 1 ]; then
			kill -9 ${cachePid}
			_pid ${cachePid}
			_return "Memcached ip:${host} port:${port} , kill process [${cachePid}] success."
		fi
		rm ${pidFile}
		sleep 1
	fi
	if pgrep -f 'p '${port}; then
		_return "Memcached ip:${host} port:${port}, fail stop."
		_fail
		exit 0
	fi
	_return "Memcached ip:${host} port:${port} stopped."
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh