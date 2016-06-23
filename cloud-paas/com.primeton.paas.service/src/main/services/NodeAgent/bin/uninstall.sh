#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Uninstall nodeagent
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
bin_home=${BIN_HOME_PATH}/NodeAgent
program_home=${PROGRAME_HOME_PATH}/NodeAgent

echo "[`date`] Begin uninstall nodeagent."

if [ -d ${bin_home} ]; then
	echo "[`date`] Remove ${bin_home}."
	rm -rf ${bin_home}
fi

if [ -d ${program_home} ]; then
	echo "[`date`] Remove ${program_home}."
	rm -rf ${program_home}
fi

echo "[`date`] End uninstall nodeagent."
