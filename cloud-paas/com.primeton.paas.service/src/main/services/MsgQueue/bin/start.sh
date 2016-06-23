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

port=5672
user=paas
password=paas

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage: ./start.sh"
	echo "-h) Print help"
	echo "-reqId) Request id"
	echo "-srvInstId) Service instance id"
	echo "-srvDefName) Service type"
	echo "-clusterName) Cluster name"
	echo "-port) port number"
	echo "-user) user name"
	echo "-password) password"
	echo "./start.sh -reqId 0 -srvInstId 10001 -srvDefName MsgQueue -clusterName 10001 -port 5672 -user paas -password paas"
}

# Parse execute arguments if has
function _doparse() {
    while [ -n "$1" ]; do
        case $1 in
        -ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-user) user=$2;shift 2;;
		-password) password=$2;shift 2;;
        *) break;;
        esac
    done
}

# Write your core/main code
function _doexecute() {
	pid_file=${rabbitmq_server}/${INSTANCE_ID}.pid
	flag_file=${rabbitmq_server}/${INSTANCE_ID}.flag

	# is running now
	if [ -f ${pid_file} ]; then
        pid=`cat ${pid_file}`        
        if [ "${pid}x" = "x" ]; then
        	rm ${pid_file}
    	elif [ ` ps -p ${pid} | grep -v PID | wc -l` -eq 0 ]; then
    		rm ${pid_file}
    	else
        	_return "MsgQueue instance [${INSTANCE_ID}] has already started."
			_pid ${pid}
        	_success
        	exit 0
    	fi
    fi
	
	cd ${rabbitmq_server}/sbin
	# make sure write pid to here
	export RABBITMQ_PID_FILE=${pid_file}
	# CentOS
	export HOME=${PROGRAME_HOME_PATH}/MsgQueue/otp_R16B03/bin/
	nohup ./rabbitmq-server &
	
	for ((count=0; count<30; count++)); do
		if [ -f ${pid_file} ]; then
			pid=`cat ${pid_file}`
			_pid ${pid}
			break
		fi
		echo -n "-"
		sleep 1
	done
    # Validate is running    
    for ((count=0; count<30; count++)); do
		if [ `ps -p ${pid} | grep -v PID | wc -l` -eq 1 ]; then
			_return "MsgQueue ${INSTANCE_ID} successfully started."
			_success
			break
		fi
		echo -n "#"
		sleep 1
    done
	
	# erlang.cookie
	if [ -f /.erlang.cookie ]; then
		cp -f /.erlang.cookie /root
	fi
	
	# init user
	if [ ! -f ${flag_file} ]; then
		sleep 10
		${rabbitmq_server}/sbin/rabbitmqctl add_user ${user} ${password}
		${rabbitmq_server}/sbin/rabbitmqctl set_user_tags ${user} administrator
		${rabbitmq_server}/sbin/rabbitmqctl set_permissions -p / ${user} ".*" ".*" ".*"
		echo -n 'true' > ${flag_file}
	fi
    
    _success
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh