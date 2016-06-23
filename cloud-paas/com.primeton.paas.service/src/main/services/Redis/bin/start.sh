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

redis_program=${PROGRAME_HOME_PATH}/Redis/redis-3.0.2

conf_template=${BIN_HOME_PATH}/Redis/packages/conf/redis.conf

port=6379
runMode=master
masterPort=6379

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./start.sh -reqId 0 -srvInstId 10000 -srvDefName Redis -clusterName 10000 -port 6379"  
	echo "./start.sh -reqId 0 -srvInstId 10000 -srvDefName Redis -clusterName 10000 -port 6379 -runMode slave -masterIp 127.0.0.1 -masterPort 8888" 
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
	echo "-port) default 6379"
	echo "-runMode) default master (master|slave)"
	echo "-masterIp) master redis ip"
	echo "-masterPort) master redis port"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-ip) ip=$2;shift 2;;
		-port) port=$2;shift 2;;
		-runMode) runMode=$2;shift 2;;
		-masterIp) masterIp=$2;shift 2;;
		-masterPort) masterPort=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	echo "redis_program = ${redis_program}"
	
	if [ ! -d ${redis_program} ]; then
		_error "${redis_program} not exists."
		exit 0
	fi
	
	if [ ! -d ${redis_program}/pids ]; then
		mkdir -p ${redis_program}/pids
	fi
	
	# redis pid file
	pid_file=${redis_program}/pids/${INSTANCE_ID}.pid
	echo "pid_file = ${pid_file}"
	if [ -f ${pid_file} ]; then
		pid="`cat ${pid_file}`"
		if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${pid}
			_return "Redis ${INSTANCE_ID} has been started. pid=${pid}"
			exit 0
		fi
	fi
	
	if [ ! -d ${redis_program}/conf ]; then 
		mkdir -p ${redis_program}/conf
	fi
	
	# redis conf file
	redis_conf=${redis_program}/conf/redis-${INSTANCE_ID}.conf
	echo "redis_conf = ${redis_conf}"
	
	# copy redis.conf template (override)
	cp -f ${conf_template} ${redis_conf}
	
	# workspace
	redis_workspace=${redis_program}/work/${INSTANCE_ID}
	echo "redis_workspace = ${redis_workspace}"
	if [ ! -d ${redis_workspace} ]; then
		mkdir -p ${redis_workspace}
	fi
	
	# modify redis.conf
	sed -i -e "s/#redis_instance_id#/${INSTANCE_ID}/g" ${redis_conf}
	sed -i -e "s/#redis_port#/${port}/g" ${redis_conf}
	
	if [ ! -z ${runMode} ] && [ "${runMode}" = "slave" ]; then
		sed -i -e "s/##slaveof#/slaveof/g" ${redis_conf}
		sed -i -e "s/#redis_master_ip#/${masterIp}/g" ${redis_conf}
		sed -i -e "s/#redis_master_port#/${masterPort}/g" ${redis_conf}
	fi
	
	# clean
	if [ -f ${pid_file} ]; then
		rm -f ${pid_file}
	fi
	
	# execute start
	_return "${redis_program}/bin/redis-server ${redis_conf}"
	${redis_program}/bin/redis-server ${redis_conf}
	
	TIMEOUT=60
	COUNT=0
	while [ ${COUNT} -lt ${TIMEOUT} ]; do
		if [ -f ${pid_file} ]; then
			pid="`cat ${pid_file}`"
			if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -gt 0 ]; then
				_success
				_pid ${pid}
				_return "Redis ${INSTANCE_ID} success started. pid=${pid}"
				exit 0
			fi
		else
			COUNT=`expr ${COUNT} + 1`
			echo -n "."
			sleep 1
		fi
	done
	
	_error "Redis ${INSTANCE_ID} start error."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh