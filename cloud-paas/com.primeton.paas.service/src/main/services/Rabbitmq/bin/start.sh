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

rabbitmq_program=${PROGRAME_HOME_PATH}/Rabbitmq/rabbitmq_server-3.5.4

if [ `ps -ef | grep ${rabbitmq_program}/sbin | grep -v grep | wc -l` -gt 0 ]; then
	echo "[INFO] rabbitmq_server has been started."
	exit 0
fi

# write pid
${rabbitmq_program}/sbin/rabbitmq-server -detached