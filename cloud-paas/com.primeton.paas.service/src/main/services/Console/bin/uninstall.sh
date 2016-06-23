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
bin_home=${BIN_HOME_PATH}/Console
program_home=${PROGRAME_HOME_PATH}/Console

# stop
if [ -f ${bin_home}/bin/stop-app.sh ]; then
	${bin_home}/bin/stop-app.sh
fi
if [ -f ${bin_home}/bin/stop-platform.sh ]; then
	${bin_home}/bin/stop-platform.sh
fi

# clear
if [ -d ${bin_home} ]; then
	echo "[`date`] Remove ${bin_home}."
	rm -rf ${bin_home}
fi

if [ -d ${program_home} ]; then
	echo "[`date`] Remove ${program_home}."
	rm -rf ${program_home}
fi

# print
echo "#############################################################################################"
echo "[INFO] Remove Primeton PAAS console-app and console-platform from your system success."
echo "#############################################################################################"