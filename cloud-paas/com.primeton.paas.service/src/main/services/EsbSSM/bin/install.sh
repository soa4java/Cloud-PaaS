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

package_home=${BIN_HOME_PATH}/EsbSSM/packages/java
ssm_package=${package_home}/ssm.zip

program_home=${PROGRAME_HOME_PATH}/EsbSSM

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./install.sh -reqId uuid"   
	echo "-h) print help"
	echo "-reqId) request id"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-arg0) arg0=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] [WARN] Delete directory ${program_home}."
		rm -rf ${program_home}
	fi
	mkdir -p ${program_home}
	
	# unpack
	unzip ${ssm_package} -d ${program_home}
	
	if [ ! -d ${program_home}/ssm ]; then
		_fail
		_return "[`date`] [ERROR] Install EsbSSM service error."
		exit 0
	fi
	
	# clean stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
	
	_success
	_return "[`date`] [INFO] Install EsbSSM service success."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh