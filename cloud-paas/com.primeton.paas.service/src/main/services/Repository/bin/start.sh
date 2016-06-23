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

program_home=${PROGRAME_HOME_PATH}
repository_program=${program_home}/Repository/jetty

server_pid=${repository_program}/server.pid
if [ ! -d ${repository_program} ]; then
	echo "[WARN] Repository never installed!"
	exit 1
fi
if [ -f ${server_pid} ]; then
	PID="`cat ${server_pid}`"
	if [ `ps -p ${PID} | grep -v PID | wc -l` -gt 0 ]; then
		echo "######################################################################################"
		echo "[INFO] Repository has been started! pid is ${PID}."
		echo "######################################################################################"
		exit 0
	fi
fi

JAVA_OPTS="-Xms128m -Xmx256m -XX:MaxPermSize=128m"
cd ${repository_program}
cmd="${JAVA_HOME}/bin/java ${JAVA_OPTS} -jar start.jar jetty.port=7399"

echo ${cmd}

nohup ${cmd} > ${repository_program}/nohup.out 2>&1 &
PID=$!
echo ${PID} > ${server_pid}

echo "######################################################################################"
echo "[INFO] [`date`] Repository started success with pid : ${PID}."
echo "######################################################################################"