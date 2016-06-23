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

zk_program=${PROGRAME_HOME_PATH}/Zookeeper/zookeeper-3.4.6

echo "JAVA_HOME=${JAVA_HOME}"
export JAVA_HOME

if [ ! -d ${zk_program} ]; then
	echo "[ERROR] ${zk_program} not exists, please run install.sh script first."
	exit 0
fi

cd ${zk_program}/bin
${zk_program}/bin/zkServer.sh start