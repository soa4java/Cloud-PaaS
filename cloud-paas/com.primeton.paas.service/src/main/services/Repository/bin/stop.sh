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
	echo "[`date`] Repository never installed!"
	exit 1
fi
if [ ! -f ${server_pid} ]; then
	echo "[`date`] Repository already stopped!"
	exit 1
fi

# kill to stop
cmd="kill `cat ${server_pid}`"
echo "${cmd}"
${cmd}

# clean
rm ${server_pid}