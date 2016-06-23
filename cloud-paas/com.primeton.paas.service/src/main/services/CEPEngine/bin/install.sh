#!/bin/bash

#
# ----------------------------------------------------------------
# Copyright (c) 2009 - 2015 Primeton. All Rights Reserved.
# ----------------------------------------------------------------
#
# author ZhongWen.Li (mailto:lizw@primeton.com)
#

#
# Install CEPEngine service
#

# import paas-env.sh
source $(cd $(dirname ${0})/../..; pwd)/Common/bin/paas-env.sh

# my variables
# software packages
package_home=${BIN_HOME_PATH}/CEPEngine/packages/java
engine_package=${package_home}/engine.zip

# Root directory for programs
program_home=${PROGRAME_HOME_PATH}/CEPEngine

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
	echo "engine_package = ${engine_package}"
	echo "program_home = ${program_home}"
	
	_return "[`date`] Begin install CEPEngine service."
	
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	
	# mkdir ${program_home}
	mkdir -p ${program_home}
	
	#
	# install collector
	#
	_return "[`date`] Begin install CEPEngine."
	
	# check package
	if [ ! -f ${engine_package} ]; then
		_fail
		_return "[`date`] Can not install CEPEngine, ${engine_package} not found."
		exit 0
	fi
	
	# unzip
	_return "[`date`] Begin unzip ${engine_package}."
	cd ${package_home}
	unzip ${engine_package} -d ${program_home}
	_return "[`date`] End unzip ${engine_package}."
	
	_return "[`date`] End install CEPEngine."
	
	# clear stdout.txt
	echo "OK" > ${FILE_PATH_STDOUT}	
	
	_success
	_return "Install CEPEngine success."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh