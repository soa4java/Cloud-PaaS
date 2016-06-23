#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

# Description
# The script for keepalived process call

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

program_home=${PROGRAME_HOME_PATH}/Keepalived/keepalived-1.2.8
f_keepalived_out=/tmp/keepalived/keepalived.out
f_keepalived_pid=${program_home}/sbin/keepalived.pid
haproxy_pid=1000

# write log
function logger() {
	if [ ! -f ${f_keepalived_out} ]; then
		if [ ! -d /tmp/keepalived ]; then
			mkdir -p /tmp/keepalived
		fi
		touch ${f_keepalived_out}
	fi
	echo "[`date`] [${HOSTNAME}] [${USER}] $1" >> ${f_keepalived_out}
}

# stop keepalived
function stop() {
	if [ -f ${f_keepalived_pid} ]; then
		pid=`cat ${f_keepalived_pid}`
		if [ `ps -p ${pid} |grep -v PID | wc -l` -eq 1 ]; then
			logger "Begin stop keepalived."
			kill ${pid}
			logger "Finish stop keepalived."
		fi
	fi
}

# monitor HaProxy process
if [ `ps -p ${haproxy_pid} |grep -v PID | wc -l` -eq 0 ]; then
	logger "[WARN] HaProxy process [${haproxy_pid}] not found."
	stop
else
	logger "[INFO] HaProxy process [${haproxy_pid}] OK."
fi
