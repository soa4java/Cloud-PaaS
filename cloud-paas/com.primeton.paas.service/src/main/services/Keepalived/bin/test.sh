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

if [ ! -d /tmp/keepalived ]; then
	mkdir -p /tmp/keepalived
fi

keepalived_log=/tmp/keepalived/keepalived.out

if [ ! -f ${keepalived_log} ]; then
	touch ${keepalived_log}
fi

echo "[`date`] [${USER}] [${HOSTNAME}] [RUNNING]." >> ${keepalived_log}