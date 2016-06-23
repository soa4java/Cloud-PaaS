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

package_home=${BIN_HOME_PATH}/Tomcat/packages/java
svn_package=${package_home}/svnkit_1.7.4.zip

program_home=${PROGRAME_HOME_PATH}/Tomcat
svn_program=${program_home}/svnkit-1.7.4-v1

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
	echo "svn_program=${svn_program}"
	echo "svn_package=${svn_package}"
	
	# clean
	if [ -d ${program_home} ]; then
		_return "[`date`] ${program_home} already exist, then remove it."
		rm -rf ${program_home}
	fi
	mkdir -p ${program_home}
	
	if [ ! -f ${svn_package} ]; then
		_error "${svn_package} not exists."
		_fail
		exit 0
	fi
	
	# unzip
	unzip ${svn_package} -d ${program_home}
	
	if [ -d ${svn_program} ]; then
		# clean stdout.txt
		echo "OK" > ${FILE_PATH_STDOUT}
		_success
		_return "Install Tomcat service finish."
		exit 0
	fi
	
	_fail
	_return "Install Tomcat service error."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh