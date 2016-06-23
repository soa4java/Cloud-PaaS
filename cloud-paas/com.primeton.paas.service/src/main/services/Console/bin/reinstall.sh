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

# my variables
# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Console

# clear
if [ -d ${program_home} ]; then
	${BIN_HOME_PATH}/Console/bin/stop-app.sh
	${BIN_HOME_PATH}/Console/bin/stop-platform.sh
	rm -rf ${program_home}
fi

# install
${BIN_HOME_PATH}/Console/bin/install.sh