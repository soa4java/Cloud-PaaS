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

# generate bin/monitor_instance.sh for keepalived process invoke.
monitor_instance_sh=
monitor_template_sh=${BIN_HOME_PATH}/HaProxy/bin/monitor_template.sh

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
	haproxyId=${INSTANCE_ID}
	monitor_instance_sh=${BIN_HOME_PATH}/HaProxy/bin/monitor_${INSTANCE_ID}.sh
	
    pid_file=${haproxyHome}/conf/${haproxyId}.pid
    conf_file=${haproxyHome}/conf/${haproxyId}.haproxy
    nohup_file=${haproxyHome}/conf/${haproxyId}.nohup.log

    if [ ! -f ${conf_file} ]; then
        _return "HaProxy instance [${haproxyId}] configuration file [${conf_file}] not found."
        _fail
        exit 0
    fi

	# is running now
	if [ -f ${pid_file} ]; then
        pid=`cat ${pid_file}`
        
        if [ "${pid}x" = "x" ]; then
        	rm ${pid_file}
    	elif [ ` ps -ef |grep ${pid} |grep -v "grep" | wc -l` -eq 0 ]; then
    		rm ${pid_file}
    	else
        	_return "HaProxy instance [${haproxyId}] is already started."
        	_success
        	exit 0
    	fi
    fi

	# Command haproxy
	if [ ! -f ${haproxyHome}/sbin/haproxy ]; then
		_return "Command ${haproxyHome}/sbin/haproxy not found."
		_fail
		exit 0
	fi

	# start HaProxy
    nohup ${haproxyHome}/sbin/haproxy -f ${conf_file} -p ${pid_file} > ${nohup_file} &

	for ((count=0; count<20; count++)); do
		if [ -f ${pid_file} ]; then
			break
		fi
		sleep 1
	done

    pid=`cat ${pid_file}`;
    _pid ${pid}
    
    # Generate monitor_instance.sh
	if [ ! -f ${monitor_template_sh} ]; then
		echo "${monitor_template_sh} not found."
	fi
	if [ -f ${monitor_instance_sh} ]; then
		rm ${monitor_instance_sh}
	fi
	# copy template
	cp ${monitor_template_sh} ${monitor_instance_sh}
	# replace nginx pid
	sed -i -e "s/haproxy_pid=1000/haproxy_pid=${pid}/g" ${monitor_instance_sh}
	# chmod +x
	chmod +x ${monitor_instance_sh}

    # Validate HaProxy is running    
    for ((count=0; count<20; count++)); do
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 1 ]; then
			_return "HaProxy ${haproxyId} successfully started."
			_success
			exit 0
		fi
		sleep 1
    done
    
    # error
    _return "HaProxy failure to start."
    _fail
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh