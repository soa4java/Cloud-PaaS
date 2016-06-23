#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

JETTY_HOME=${PROGRAME_HOME_PATH}/Console/console-app
if [ -f ${JETTY_HOME}/pid ]; then
	pid="`cat ${JETTY_HOME}/pid`"
fi

if [ ! -z ${pid} ]; then
	if [ `ps -p ${pid} |grep -v PID | wc -l` -gt 0 ]; then
		kill ${pid}
		exit 0
	fi
fi
echo "###############################################################"
echo "[INFO] Primeton PAAS console-app has been shut down."
echo "###############################################################"