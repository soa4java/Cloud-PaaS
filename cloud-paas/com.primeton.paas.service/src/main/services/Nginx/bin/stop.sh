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
    echo "Usage: ./stop.sh"
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
    accessLog=${logDir}/${nginxId}.access.log
    errorLog=${logDir}/${nginxId}.error.log

    echo "pidFile=${pidFile}"
    echo "accessLog=${accessLog}"
    echo "errorLog=${errorLog}"

    pid=
    if [ -f ${pidFile} ]; then
        pid=`cat ${pidFile}`
        echo pid=${pid}
        # kill ${pid}
		# nginx -s singal [stop/quit]
		${nginxHome}/sbin/nginx -c ${confFile} -s stop
		
        rm ${accessLog}
        rm ${errorLog}
    fi

    if [[ "${pid}"x != x ]]; then
        for ((count=0; count<10; count++));do
            isRunning=`ps -ef | grep ${pid} | awk {'print $2'} | grep ${pid}`;
            if [[ ${isRunning} != ${pid} ]];then
            	_return "Nginx service instance [${nginxId}] successfully stopped."
                _success
                exit 0
            fi
            sleep 1;
        done
        kill -9 ${pid}
        _return "Nginx service instance [${nginxId}] successfully force stopped."
    fi

    _success
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh