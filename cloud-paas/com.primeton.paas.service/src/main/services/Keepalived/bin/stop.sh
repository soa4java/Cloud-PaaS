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
program_home=${PROGRAME_HOME_PATH}/Keepalived/keepalived-1.2.8
keepalived=${program_home}/sbin/keepalived
fpid=${program_home}/sbin/keepalived.pid
fcfg=${program_home}/conf/keepalived.conf
cmd="${keepalived} -f ${fcfg} -p ${fpid}"

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./stop.sh"
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
	# check pid
	if [ -f ${fpid} ]; then
		pid=`cat ${fpid}`
		if [ ` ps -ef |grep ${pid} |grep -v "grep" | wc -l` -eq 0 ]; then
			rm ${fpid}
			_success
			_return "keepalived has stoped."
			exit 0
		else
			kill ${pid}
			sleep 1
			if [ ` ps -ef |grep ${pid} |grep -v "grep" | wc -l` -gt 0 ]; then
				kill -9 ${pid}
			fi
			_success
			_return "keepalived successfully stoped."
			exit 0
		fi
	else
		_success
		_return "keepalived has stoped."
	fi
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh