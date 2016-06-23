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

rabbitmq_server=${PROGRAME_HOME_PATH}/MsgQueue/rabbitmq_server-3.5.4

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage: ./stop.sh"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-srvInstId) Service instance id"
	echo "-srvDefName) Service type"
	echo "-clusterName) Cluster name"	
	echo "-port) Cluster port"
	echo "./stop.sh -reqId 0 -srvInstId 10001 -srvDefName MsgQueue -clusterName 10001 -port 5672"
}

# Parse execute arguments if has
function _doparse() {
    while [ -n "$1" ]; do
        case $1 in
        -ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
        *) break;;
        esac
    done
}

# Write your core/main code
function _doexecute() {
	pid_file=${rabbitmq_server}/${INSTANCE_ID}.pid

	# is running now
	if [ -f ${pid_file} ]; then
        pid=`cat ${pid_file}`        
        if [ "${pid}x" = "x" ]; then
        	rm -f ${pid_file}
    	elif [ ` ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
    		rm -f ${pid_file}
			_return "MsgQueue instance [${INSTANCE_ID}] has been shutdown."
			_success
			exit 0
    	else
        	${rabbitmq_server}/sbin/rabbitmqctl stop
			rm -f ${pid_file}
			# Validate is running    
			for ((count=0; count<30; count++)); do
				if [ `ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
					_return "MsgQueue ${INSTANCE_ID} successfully shutdown."
					_success
					exit 0
				fi
				sleep 1
			done
			kill -9 ${pid}
    	fi
    fi
       
    _success
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh