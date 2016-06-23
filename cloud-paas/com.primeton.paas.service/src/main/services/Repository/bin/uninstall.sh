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
bin_home=${BIN_HOME_PATH}/Repository
program_home=${PROGRAME_HOME_PATH}/Repository

echo "[`date`] Begin uninstall Repository."

# stop
if [ -f ${bin_home}/bin/stop.sh ]; then
	${bin_home}/bin/stop.sh
fi

if [ -d ${bin_home} ]; then
	echo "[`date`] Remove ${bin_home}."
	rm -rf ${bin_home}
fi

if [ -d ${program_home} ]; then
	echo "[`date`] Remove ${program_home}."
	rm -rf ${program_home}
fi

echo "[`date`] End uninstall Repository."
