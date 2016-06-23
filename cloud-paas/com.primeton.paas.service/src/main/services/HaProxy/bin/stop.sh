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
haproxyId=
haproxyHome=${PROGRAME_HOME_PATH}/HaProxy/haproxy-1.4.24
haproxyConf=${haproxyHome}/conf

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
	haproxyId=${INSTANCE_ID}
	haproxyConf=${haproxyHome}/conf
	
    pid_file="${haproxyConf}/${haproxyId}.pid"
    conf_file="${haproxyConf}/${haproxyId}.haproxy"
    log_file="${haproxyConf}/${haproxyId}.nohup.log"
    if [[ -f "$pid_file" ]]; then
            pid=`cat $pid_file`
            echo "Will execute kill ${pid}"
            kill $pid
            rm "$pid_file"
            rm "$log_file"
    fi
	
	#term stop
	kill -9 $pid
	
    _pid ${pid}
    _return "HaProxy ${haproxyId} successfully stopped."
    _success
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh