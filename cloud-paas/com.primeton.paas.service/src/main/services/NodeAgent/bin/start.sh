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

# Agent env
AGENT_HOME=${PROGRAME_HOME_PATH}/NodeAgent/agent-java
AGENT_IP=${PAAS_LOCAL_IP}

# copy zkConfig.xml to conf
cp -f ${BIN_HOME_PATH}/Common/conf/zkConfig.xml ${AGENT_HOME}/conf

if [ ! -z ${JAVA_HOME} ]; then
	export JAVA_HOME
fi

# export environment variables
export AGENT_HOME
export AGENT_IP

# execute agent-java start script
${AGENT_HOME}/bin/start.sh