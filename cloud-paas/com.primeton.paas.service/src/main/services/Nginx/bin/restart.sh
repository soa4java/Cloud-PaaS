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
program_home=${PROGRAME_HOME_PATH}/Nginx
nginxHome=${program_home}/nginx-1.4.4
nginxId=

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./start.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
    echo "-srvInstId) Service instance id"
    echo "-srvDefName) Service type"
    echo "-clusterName) Cluster name"
}

# Parse execute arguments if has
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
	nginxId=${INSTANCE_ID}

	confFile=${nginxHome}/config/${nginxId}.conf
	logDir=${nginxHome}/logs
	pidFile=${logDir}/${nginxId}.pid

    if [[ ! -f "${confFile}" ]]; then
        _return "Nginx configuration file [${confFile}] not found."
        _fail
        exit 0
    fi

	if [ ! -f ${pidFile} ]; then
		_return "Nginx pid file [${pidFile}] not found."
		_fail
		exit 0	
	fi
	pid=`cat ${pidFile}`
	
	if [ ! -f ${nginxHome}/sbin/nginx ]; then
		_return "${nginxHome}/sbin/nginx not found."
		_fail
		exit 0
	fi
    
    # restart Nginx (send restart signal to master process)
    # kill -HUP ${pid} 
	${nginxHome}/sbin/nginx -c ${confFile} -s reload
    
    # find pid file
	for ((i=0; i<30; i++)); do
		if [ -f ${pidFile} ]; then
			pid=`cat ${pidFile}`;
			if [ "${pid}x" = "x" ]; then
				echo -n "-"
			else
				break
			fi
		fi
        sleep 1;
    done

    pid=`cat ${pidFile}`
    _pid ${pid}
    
    for ((count=0; count<10; count++)); do
        isRunning=`ps -ef | grep ${pid} | awk {'print $2'} | grep ${pid}`;
        if [[ ${isRunning} == ${pid} ]];then
        	_return "Nginx service instance [${nginxId}] successfully restarted."
            _success
            exit 0
        fi
    done
    
    _return "Nginx service instance [${nginxId}] restart failure."
    _fail
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh