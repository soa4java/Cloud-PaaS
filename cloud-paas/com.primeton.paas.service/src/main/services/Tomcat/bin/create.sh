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
tomcat_package=${package_home}/apache-tomcat-7.0.62.tar.gz

program_home=${PROGRAME_HOME_PATH}/Tomcat

# Help, print help information to terminal.
function _dohelp() {
	echo "Usage:"
	echo "./create.sh -reqId uuid -srvId 20001"   
	echo "-h) print help"
	echo "-reqId) request id"
	echo "-srvId) service id"
}

# Parse execute arguments
function _doparse() {
	while [ -n "$1" ]; do
		case $1 in
		-srvId) srvId=$2;shift 2;;
		*) break;;
		esac
	done
}

# Write your core/main code
function _doexecute() {
	if [ -z ${srvId} ]; then
		_error "srvId is null or blank."
		exit 0
	fi
	tomcat_program=${program_home}/${srvId}/apache-tomcat-7.0.62
	
	echo "tomcat_program=${tomcat_program}"
	echo "tomcat_package=${tomcat_package}"
	
	if [ -d ${program_home}/${srvId} ]; then
		_return "Tomcat ${srvId} has been created."
		_success
		exit 0
	fi
	mkdir -p ${program_home}/${srvId}
	
	if [ ! -f ${tomcat_package} ]; then
		_error "${tomcat_package} not exists."
		_fail
		exit 0
	fi
	
	# unpack
	# cd ${program_home}/${srvId}
	tar -zvxf ${tomcat_package} -C ${program_home}/${srvId}
	
	# chmod
	chmod +x ${tomcat_program}/bin/*.sh
	
	if [ -d ${tomcat_program} ]; then
		if [ -d ${package_home}/default ]; then
			cp -rf ${package_home}/default ${tomcat_program}/webapps
		fi
		
		# clean stdout.txt
		echo "OK" > ${FILE_PATH_STDOUT}
		_success
		_return "Create Tomcat ${srvId} success."
		exit 0
	fi
	
	_fail
	_return "Create Tomcat ${srvId} error."
	exit 0
}

# import template.sh
source ${BIN_HOME_PATH}/Common/bin/template.sh