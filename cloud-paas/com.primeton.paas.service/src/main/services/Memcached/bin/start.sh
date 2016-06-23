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
    echo "Usage: ./start.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-srvInstId) Service instance id"
    echo "-srvDefName) Service type"
    echo "-clusterName) Cluster name"
    echo "-ip) Memcached server ip"
    echo "-port) memcached service port"
    echo "-memorySize) Max memory size"
    echo "-maxConnectionSize) Max connection size"
}

# Parse execute arguments
function _doparse() {
    while [ -n "$1" ]; do
		case $1 in
		-ip) host=$2;shift 2;;
		-port) port=$2;shift 2;;
		-memorySize) memorySize=$2;shift 2;;
		-maxConnectionSize) maxConnectionSize=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ ! -d ${pidDir} ]; then
		mkdir -p ${pidDir}
	fi
		
	pidFile=${pidDir}/${port}.pid
	if [ -f ${pidFile} ]; then
		cachePid=`cat ${pidFile}`
		if [ ` ps -ef |grep ${cachePid} |grep -v "grep" | wc -l` -eq 1 ]; then
			_return "Memcached ip:${host} port:${port} is already start, pid:${cachePid}."
			_pid ${cachePid}
			_success
			exit 0
		fi
		rm ${pidFile}
	fi
	
	username=${USER}
	if [[ "${username}x" = "x" ]] ; then
		username=root
	fi
	
	if [[ "${port}x" = "x" ]] ; then
		_return "unknown port."
		_fail
		exit 0
	fi
	
	# start
	${basePath}/bin/memcached -d -u ${username} -p ${port} -l ${host} -m ${memorySize} -c ${maxConnectionSize} -P ${pidFile}
	
	# find pid file
    i=0
    while test $i -ne 100 ; do
    	if test -s "${pidFile}"
    	then
    		break
    	fi
    	echo -n "-"
    	i=`expr $i + 1`
    	sleep 0.5
    done
    
    # find process
	if [ -f ${pidFile} ]; then
		cachePid=`cat ${pidFile}`
		if [ `ps -ef |grep ${cachePid} |grep -v "grep" | wc -l` -eq 1 ]; then
			_pid ${cachePid}
			_return "Memcached ip:${host} port:${port} pid:${cachePid} successfully started."
			_success
			exit 0
    	fi
    else
		_return "Memcached ip:${host} port:${port} fail start."
    	_fail
    	exit 0
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh