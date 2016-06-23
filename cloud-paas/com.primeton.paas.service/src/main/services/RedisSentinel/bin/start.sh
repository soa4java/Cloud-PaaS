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

redis_program=${PROGRAME_HOME_PATH}/RedisSentinel/redis-3.0.2

# master name name1,name2,name3...
redis_clusters=
# master redis ip1,ip2,ip3...
redis_ips=
# master redis port1,port2,port3...
redis_ports=
# down-after-milliseconds (ms)
down_time=60000
# failover-timeout (ms)
failover_timeout=180000
# parallel-syncs
parallel_syncs=1
# Min sentinels agree
min_agrees=2
# port
port=26379

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:" 
	echo -n "./start.sh -reqId 0 -srvInstId 10000 -srvDefName RedisSentinel -clusterName 10000 -port 6379" 
	echo -n " -redis_clusters 20001,20002,2003"
	echo -n " -redis_ips 192.168.2.22,192.168.2.23,192.168.2.24"
	echo " -redis_ports 8200,8200,8200"
	
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvInstId) service instance id"
	echo "-srvDefName) service type"
	echo "-clusterName) cluster name"
	echo "-port) port, default 26379"
	echo "-redis_clusters) 20001,20002,2003..."
	echo "-redis_ips) Master redis servers, example: 192.168.2.22,192.168.2.23,192.168.2.24"
	echo "-redis_ports) Master redis port, example: 8200,8200,8200"
	echo "-down_time) Redis Sentinel down-after-milliseconds (ms), default 60000"
	echo "-failover_timeout) Redis Sentinel failover-timeout (ms), default 180000"
	echo "-parallel_syncs) Redis Sentinel failover-timeout (ms), default 1"
	echo "-min_agrees) Redis Sentinel min agrees, default 2"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-port) port=$2;shift 2;;
		-redis_clusters) redis_clusters=$2;shift 2;;
		-redis_ips) redis_ips=$2;shift 2;;
		-redis_ports) redis_ports=$2;shift 2;;
		-down_time) down_time=$2;shift 2;;
		-failover_timeout) failover_timeout=$2;shift 2;;
		-parallel_syncs) parallel_syncs=$2;shift 2;;
		-min_agrees) min_agrees=$2;shift 2;;
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
			_return "Redis Sentinel ${INSTANCE_ID} has been started. pid=${pid}"
			exit 0
		fi
	fi
	
	if [ ! -d ${redis_program}/conf ]; then 
		mkdir -p ${redis_program}/conf
	fi
	
	# redis sentinel.conf file
	sentinel_conf=${redis_program}/conf/sentinel-${INSTANCE_ID}.conf
	sentinel_nohup=${redis_program}/conf/sentinel-${INSTANCE_ID}.nohup
	echo "sentinel_conf = ${sentinel_conf}"
	
	redis_clusters=(`echo ${redis_clusters} | tr ";" ","`)
	clusters_array=(`echo ${redis_clusters} | tr "," "\n"`)
	
	redis_ips=(`echo ${redis_ips} | tr ";" ","`)
	ips_array=(`echo ${redis_ips} | tr "," "\n"`)
	
	redis_ports=(`echo ${redis_ports} | tr ";" ","`)
	ports_array=(`echo ${redis_ports} | tr "," "\n"`)
	
	# Generate sentinel.conf file
	if [ -f ${sentinel_conf} ]; then
		rm -f ${sentinel_conf}
	fi
	# New sentinel.conf file
	touch ${sentinel_conf}
	echo "port ${port}" >> ${sentinel_conf}
	
	length=${#clusters_array[@]}
	for ((i = 0; i < ${length}; i++)); do
	 	echo "" >> ${sentinel_conf}
		echo "sentinel monitor ${clusters_array[$i]} ${ips_array[$i]} ${ports_array[$i]} ${min_agrees}" >> ${sentinel_conf}
		echo "sentinel down-after-milliseconds ${clusters_array[$i]} ${down_time}" >> ${sentinel_conf}
		echo "sentinel failover-timeout ${clusters_array[$i]} ${failover_timeout}" >> ${sentinel_conf}
		echo "sentinel parallel-syncs ${clusters_array[$i]} ${parallel_syncs}" >> ${sentinel_conf}
	done	
		
	# clean
	if [ -f ${pid_file} ]; then
		rm -f ${pid_file}
	fi
	
	# execute start
	cmd="${redis_program}/bin/redis-server ${sentinel_conf} --sentinel"
	_return "${cmd}"
	nohup ${cmd} > ${sentinel_nohup} &
	pid=$!
	echo ${pid} > ${pid_file}
	
	TIMEOUT=60
	COUNT=0
	while [ ${COUNT} -lt ${TIMEOUT} ]; do
		if [ ! -z ${pid} ] && [ `ps -p ${pid} | grep -v PID | wc -l` -gt 0 ]; then
			_success
			_pid ${pid}
			_return "Redis Sentinel ${INSTANCE_ID} success started. pid=${pid}"
			exit 0
		fi
	done
	
	_error "Redis Sentinel ${INSTANCE_ID} start error."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh