#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install collector service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/Collector/packages/java
collector_package=${package_home}/collector.zip

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/Collector
workspace_path=${WORKSPACE_HOME_PATH}/logs

# Help, print help information to terminal.
function _dohelp() {
    echo "Usage: ./install.sh"
    echo "-h) Print help"
    echo "-reqId) Request id"
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
	# print variables
	echo "package_home = ${package_home}"
	echo "collector_package = ${collector_package}"
	
	echo "program_home = ${program_home}"
	
	_return "[`date`] Begin install collector service."
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	#
	# install collector
	#
	_return "[`date`] Begin install collector."
	
	# check package
	if [ ! -f ${collector_package} ]; then
		_fail
		_return "[`date`] Can not install collector, ${collector_package} not found."
		exit 0
	fi
	
	# unzip
	_return "[`date`] Begin unzip ${collector_package}."
	cd ${package_home}
	unzip ${collector_package} -d ${program_home}
	_return "[`date`] End unzip ${collector_package}."
	
	_return "[`date`] End install collector."
	
	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}
			
	_success
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh