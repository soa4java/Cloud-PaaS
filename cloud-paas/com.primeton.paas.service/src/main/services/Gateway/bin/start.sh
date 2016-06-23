#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ---------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

easn_program=${PROGRAME_HOME_PATH}/Gateway/easn/fes_698tcps

port=9999
# ip1:port1,ip2:port2...
preServers=127.0.0.1:8888
gatewayId=1
maxConnection=350000

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 0 -srvInstId 10000 -srvDefName Gateway -clusterName 10000 -port 9999"  
	echo "./start.sh -reqId 0 -srvInstId 10000 -srvDefName Gateway -clusterName 10000 -port 9999 -preServers 192.168.1.11:8888,192.168.1.12:8888 -maxConnection 300000" 
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
	echo "-port) default 6379"
	echo "-preServers) ip1:port1,ip2:port2..."
	echo "-maxConnection) max connection size, default 350000"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-preServers) preServers=$2;shift 2;;
		-maxConnection) maxConnection=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	echo "easn_program = ${easn_program}"
	
	gatewayId=${INSTANCE_ID}
	
	if [ ! -d ${easn_program} ]; then
		_error "${easn_program} not exists."
		exit 0
	fi
	
	# Gateway pid file
	pid_file=${easn_program}/${gatewayId}.pid
	echo "pid_file = ${pid_file}"
	if [ -f ${pid_file} ]; then
		pid="`cat ${pid_file}`"
		if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${pid}
			_return "Gateway ${INSTANCE_ID} has been started. pid=${pid}"
			exit 0
		fi
	fi
	
	preServers=(`echo ${preServers} | tr ";" ","`)
	url_array=(`echo ${preServers} | tr "," "\n"`)
	
	server_cmd=""
	for url in ${url_array[@]}; do
	 	server_cmd="${server_cmd} -d ${url}"
	done
	
	cmd="${easn_program}/fes_698tcps ${server_cmd} -P ${port} -n 1 -m ${maxConnection}"
	
	# start fes_698tcps
	nohup ${cmd} > ${easn_program}/${gatewayId}.nohup &
	# write pid
	pid=$!
	_pid ${pid}
	echo -n ${pid} > ${pid_file}
	
	if [ ! -z ${pid} ]; then
		if [ `ps -p ${pid} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_return "Gateway ${INSTANCE_ID} success started. pid=${pid}"
			exit 0
		fi
	fi
	
	_error "Gateway ${INSTANCE_ID} start error."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh