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

# generate bin/monitor_instance.sh
monitor_instance_sh=
monitor_template_sh=${BIN_HOME_PATH}/Nginx/bin/monitor_template.sh

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
	monitor_instance_sh=${BIN_HOME_PATH}/Nginx/bin/monitor_${INSTANCE_ID}.sh
	echo "nginxId=${nginxId}"
	echo "nginxHome=${nginxHome}"
	echo "monitor_instance_sh=${monitor_instance_sh}"
	
    confFile=${nginxHome}/config/${nginxId}.conf
    logDir=${nginxHome}/logs
    pidFile=${logDir}/${nginxId}.pid

	# Find configuration file
    if [[ ! -f "${confFile}" ]]; then
        _return "Nginx configuration file [${confFile}] not found !"
        _fail
        exit 0
    fi
    
    # Find sbin/nginx command
    if [ ! -f ${nginxHome}/sbin/nginx ]; then
    	_return "Nginx command ${nginxHome}/sbin/nginx not found."
    	_fail
    	exit 0
    fi
    
    # start nginx
    ${nginxHome}/sbin/nginx -c ${confFile}
    
	for ((i=0; i<30; i++));do
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

    _pid ${pid}
    
    # Generate monitor_instance.sh
	if [ ! -f ${monitor_template_sh} ]; then
		echo "File [${monitor_template_sh}] not found."
	fi
	if [ -f ${monitor_instance_sh} ]; then
		rm ${monitor_instance_sh}
	fi
	# copy template
	cp ${monitor_template_sh} ${monitor_instance_sh}
	# replace nginx pid
	sed -i -e "s/nginx_pid=1000/nginx_pid=${pid}/g" ${monitor_instance_sh}
	# chmod +x 
	chmod +x ${monitor_instance_sh}
    
    # Validate Nginx is Running
    for ((count=0; count<10; count++));do
        isRunning=`ps -ef | grep ${pid} | awk {'print $2'} | grep ${pid}`;
        if [[ ${isRunning} == ${pid} ]];then
            _return "Nginx successfully started."
            _success
            exit 0
        fi
    done
    
    _return "Command ${nginxHome}/sbin/nginx not found."
    _fail
    exit 0
}

# import template script
source ${BIN_HOME_PATH}/Common/bin/template.sh